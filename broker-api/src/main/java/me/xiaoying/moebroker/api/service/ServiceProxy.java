package me.xiaoying.moebroker.api.service;

import javassist.*;
import me.xiaoying.moebroker.api.Protocol;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ServiceProxy {
    private static final Map<String, Class<?>> knownServices = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T proxy(Class<T> clazz, Protocol protocol) {
        if (ServiceProxy.knownServices.containsKey(clazz.getName()))
            return (T) ServiceProxy.knownServices.get(clazz.getName());

        ClassPool classPool = ClassPool.getDefault();

        String proxyClassName = clazz.getPackage().getName() + "." + clazz.getSimpleName() + "BrokerService";
        CtClass targetClass = classPool.makeClass(proxyClassName);

        try {
            // interface
            targetClass.addInterface(classPool.get(clazz.getName()));

            // field
            CtClass protocolClass = classPool.get(protocol.getClass().getName());
            CtField ctProtocol = new CtField(protocolClass, "protocol", targetClass);
            ctProtocol.setModifiers(Modifier.PRIVATE | Modifier.FINAL);
            targetClass.addField(ctProtocol);

            // constructor
            CtConstructor constructor = new CtConstructor(new CtClass[]{protocolClass}, targetClass);
            constructor.setBody("{ this.protocol = $1; }");
            targetClass.addConstructor(constructor);

            CtClass messageClass = classPool.get(InvokeMethodMessage.class.getName());

            // methods
            for (Method method : clazz.getDeclaredMethods()) {
                CtClass returnType = classPool.get(method.getReturnType().getName());

                CtClass[] paramTypes = new CtClass[method.getParameterCount()];
                for (int i = 0; i < method.getParameterCount(); i++)
                    paramTypes[i] = classPool.get(method.getParameterTypes()[i].getName());

                StringBuilder methodBody = new StringBuilder();
                methodBody.append("{");

                StringBuilder paramsBuilder = new StringBuilder();
                for (int i = 0; i < method.getParameterCount(); i++) {
                    if (i > 0)
                        paramsBuilder.append(", ");

                    paramsBuilder.append("$").append(i + 1);
                }

                methodBody.append(messageClass.getName() + " message = new " + messageClass.getName() + "(");
                methodBody.append(clazz.getName() + ".class, ");
                methodBody.append("\"" + method.getName() + "\", ");
                methodBody.append("new Object[]{" + paramsBuilder + "}");
                methodBody.append(");");

                if (method.getReturnType() == void.class)
                    methodBody.append("protocol.oneway(message);");
                else
                    methodBody.append("return (" + returnType.getName() + ") protocol.invokeSync(message);");

                methodBody.append("}");

                CtMethod ctMethod = new CtMethod(returnType, method.getName(), paramTypes, targetClass);
                ctMethod.setBody(methodBody.toString());
                ctMethod.setModifiers(Modifier.PUBLIC);
                targetClass.addMethod(ctMethod);
            }

            targetClass.writeFile();

            Class<T> resultClass = (Class<T>) targetClass.toClass();
            ServiceProxy.knownServices.put(clazz.getName(), resultClass);
            return resultClass.getConstructor(protocol.getClass()).newInstance(protocol);

        } catch (NotFoundException | CannotCompileException | IOException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to create proxy for " + clazz.getName(), e);
        } finally {
            if (targetClass != null) targetClass.detach();
        }
    }
}