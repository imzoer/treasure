package com.cookies.treasure;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 * <pre>
 *      SharedPreference包装类
 *      使用要求：
 *      1、接口要继承{@link Treasure}
 *      2、接口函数支持以get和set开头，例如getXXX,setXXX,其中getXXX表示读取xxx的值，setXXX表示设置xxx的值
 *      3、支持根据类名生成sp文件名。如果不使用{@link FileName}指定sp文件名，则根据接口的名称toLowerCase得到sp文件名
 * </pre>
 * <pre>
 *      key的生成规则：
 *      1、如果有{@link Key}，用key()的值
 *      2、对于GET函数，所有参数用于拼接key(临时方案)；对于SET函数，如果args长度大于1，那么除最后一个参数，前面所有的参数用于拼接key。每个参数之间以_分隔
 *      3、如果args长度为1，用methodName生成key，比如getXXX，则生成的key是xxx
 * </pre>
 * <pre>
 *      还未支持的功能：
 *      1、指定sp的读取模式 private / multi process
 *      2、GET函数传入参数作为default值(对应于key的生成规则部分，2的临时方案)
 * </pre>
 */
@SuppressWarnings("unchecked")
public class Treasures {
    public static final String GET = "get";
    public static final String SET = "set";

    private static Context context;
    private static HashMap<Class<? extends Treasure>, Treasure> container = new HashMap<>();

    public static void init(Context c) {
        context = c;
    }

    public static <T extends Treasure> T of(Class<T> clazz) {
        if (container.containsKey(clazz)) {
            return (T) container.get(clazz);
        } else {
            Object obj = Proxy.newProxyInstance(context.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return dispatch(method, args);
                }
            });
            T t = clazz.cast(obj);
            container.put(clazz, t);
            return t;
        }
    }

    private static Object dispatch(Method method, Object[] args) {
        String methodName = method.getName();
        if (methodName.length() < 3) {
            throw new RuntimeException("methodName not valid");
        }

        // 1.get sp
        String fileName;
        FileName fileNameAnnotation = method.getDeclaringClass().getAnnotation(FileName.class);
        if (fileNameAnnotation != null) {
            fileName = fileNameAnnotation.filename();
        } else {
            fileName = method.getDeclaringClass().getSimpleName().toLowerCase();
        }

        GetMode getModeAnnotation = method.getDeclaringClass().getAnnotation(GetMode.class);
        Get get = Get.MODE_PRIVATE;
        if (getModeAnnotation != null) {
            get = getModeAnnotation.mode();
        } else {
            getModeAnnotation = method.getAnnotation(GetMode.class);
            if (getModeAnnotation != null) {
                get = getModeAnnotation.mode();
            }
        }
        SharedPreferences sp = context.getSharedPreferences(fileName, get.get());

        // 2.get key for kv pair
        String key = "";
        Key keyAnnotation = method.getAnnotation(Key.class);
        boolean isGET = method.getName().startsWith(GET);
        if (keyAnnotation != null) {
            key = keyAnnotation.key();
        } else if (args != null && args.length > 1) {
            int len = isGET ? args.length : args.length - 1; // get函数所有参数用于生成key
            for (int i = 0; i < len; i++) {
                if (args[i].getClass() == String.class || args[i].getClass() == Integer.class || args[i].getClass() == Long.class) {
                    key += args[i];
                    if (i != len - 1) {
                        key += "_";
                    }
                    continue;
                }
                throw new RuntimeException("args for generate key must be string/int/long");
            }
            if (key.equals("")) {
                throw new RuntimeException("generated key not valid");
            }
        } else {
            key = methodName.substring(3).toLowerCase();
        }

        // 3.get value
        if (method.getName().startsWith(GET)) {
            Default defaultAnnotation = method.getAnnotation(Default.class);
            Class returnType = method.getReturnType();
            if (returnType == int.class || returnType == Integer.class) {
                return sp.getInt(key, defaultAnnotation == null ? 0 : defaultAnnotation.intValue());
            } else if (returnType == String.class) {
                return sp.getString(key, defaultAnnotation == null ? "" : defaultAnnotation.stringValue());
            } else if (returnType == long.class || returnType == Long.class) {
                return sp.getLong(key, defaultAnnotation == null ? 0 : defaultAnnotation.longValue());
            } else if (returnType == float.class || returnType == Float.class) {
                return sp.getFloat(key, defaultAnnotation == null ? 0 : defaultAnnotation.floatValue());
            } else if (returnType == boolean.class || returnType == Boolean.class) {
                return sp.getBoolean(key, defaultAnnotation != null && defaultAnnotation.boolValue());
            } else {
                throw new RuntimeException("unknown returnType:" + returnType);
            }
        }
        // 4.set value
        else if (method.getName().startsWith(SET)) {
            Class[] paramTypes = method.getParameterTypes();
            if (paramTypes.length == 0) {
                throw new RuntimeException("set " + key + " called but no value provided");
            }
            if (args == null || args.length == 0) {
                throw new RuntimeException("set func should not have 0 args.");
            }
            SaveMode saveModeAnnotation = method.getDeclaringClass().getAnnotation(SaveMode.class);
            Write mode = Write.APPLY;
            if (saveModeAnnotation != null) { // 检查接口是否有Mode注解
                mode = saveModeAnnotation.mode();
            } else {
                // 检查方法是否有Mode注解
                saveModeAnnotation = method.getAnnotation(SaveMode.class);
                if (saveModeAnnotation != null) {
                    mode = saveModeAnnotation.mode();
                }
            }
            int paramPos = args.length - 1;
            Class paramType = paramTypes[paramPos];
            SharedPreferences.Editor e;
            if (paramType == int.class || paramType == Integer.class) {
                e = sp.edit().putInt(key, (Integer) args[paramPos]);
            } else if (paramType == String.class) {
                e = sp.edit().putString(key, (String) args[paramPos]);
            } else if (paramType == long.class || paramType == Long.class) {
                e = sp.edit().putLong(key, (Long) args[paramPos]);
            } else if (paramType == float.class || paramType == Float.class) {
                e = sp.edit().putFloat(key, (Float) args[paramPos]);
            } else if (paramType == boolean.class || paramType == Boolean.class) {
                e = sp.edit().putBoolean(key, (Boolean) args[paramPos]);
            } else {
                throw new RuntimeException("unknown paramType:" + paramType);
            }
            if (mode == Write.APPLY) {
                e.apply();
            } else {
                e.commit();
            }
            return null;
        } else {
            throw new RuntimeException("method name should be start with get or set");
        }
    }
}