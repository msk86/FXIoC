package fxioc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IoCContainer {
    Map<Class<?>, Class<?>> classMap = new HashMap<Class<?>, Class<?>>();

    public <T> void register(Class<T> clazz) {
        registerInterface(clazz);
        registerSuperClass(clazz);
        classMap.put(clazz, clazz);
    }

    private <T> void registerSuperClass(Class<T> clazz) {
        Class<?> iterClass = clazz;
        while (iterClass.getSuperclass() != Object.class) {
            classMap.put(iterClass.getSuperclass(), clazz);
            iterClass = iterClass.getSuperclass();
        }
    }

    private <T> void registerInterface(Class<T> clazz) {
        Class<?>[] clazzInterfaces = clazz.getInterfaces();
        for (Class<?> clazzInterface : clazzInterfaces) {
            classMap.put(clazzInterface, clazz);
        }
    }

    public <T> T getBean(Class<T> clazz) {
        Class<?> beanClass = classMap.get(clazz);
        if (beanClass == null) return null;

        try {
            T t = createByConstructor(beanClass);
            injectBySetter(beanClass, t);
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T> void injectBySetter(Class<?> beanClass, T t) throws IllegalAccessException, InvocationTargetException {
        Method[] methods = beanClass.getMethods();
        for (Method method : methods) {
            if(method.getName().startsWith("set") && method.getReturnType() == Void.TYPE && method.getParameterTypes().length == 1) {
                Class<?> type = method.getParameterTypes()[0];
                method.invoke(t, getBean(type));
            }
        }
    }

    private <T> T createByConstructor(Class<?> beanClass) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<T>[] constructors = (Constructor<T>[]) beanClass.getConstructors();
        Class<?>[] parameterTypes = constructors[0].getParameterTypes();
        ArrayList<Object> list = new ArrayList<Object>();
        for (Class<?> parameterType : parameterTypes) {
            list.add(getBean(parameterType));
        }
        return constructors[0].newInstance(list.toArray());
    }
}
