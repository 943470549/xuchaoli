package com.platform.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Type是 Java 编程语言中所有类型的公共高级接口。
 * 它们包括原始类型、参数化类型、数组类型、类型变量和基本类型  Class implements Type。
 *
 * @author zhaozhongchao
 */
public class ReflectionUtil {

    private static final String TYPE_CLASS_NAME_PREFIX = "class ";
    private static final String TYPE_INTERFACE_NAME_PREFIX = "interface ";

    /**
     * Modifier类定义的修饰符值,含有多个修饰符的属性或方法获取修饰符时返回修饰符对应int值得和
     */
    private static final int PUBLIC_MODIFIER_VALUE = 1;
    private static final int PRIVATE_MODIFIER_VALUE = 2;
    private static final int PROTECT_MODIFIER_VALUE = 4;
    private static final int FINAL_MODIFIER_VALUE = 16;
    private static final int STATIC_MODIFIER_VALUE = 8;

    private ReflectionUtil() {
    }

    /**
     * 通过Type获取类名
     *
     * @param type
     * @return
     */
    public static String getClassName(Type type) {
        if (type == null) {
            return "";
        }
        String className = type.toString();
        if (className.startsWith(TYPE_CLASS_NAME_PREFIX)) {
            className = className.substring(TYPE_CLASS_NAME_PREFIX.length());
        } else if (className.startsWith(TYPE_INTERFACE_NAME_PREFIX)) {
            className = className
                    .substring(TYPE_INTERFACE_NAME_PREFIX.length());
        }
        return className;
    }

    /**
     * 通过Type获取Class<?>
     *
     * @param type
     * @return
     * @throws ClassNotFoundException
     */
    public static Class<?> getClass(Type type) throws ClassNotFoundException {
        String className = getClassName(type);
        if (className == null || className.isEmpty()) {
            return null;
        }
        return Class.forName(className);
    }

    /**
     * 通过Type创建对象 Type不能是抽象类、接口、数组类型、以及基础类型、Void否则会发生InstantiationException异常
     *
     * @param type
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static Object newInstance(Type type) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> clazz = getClass(type);
        if (clazz == null) {
            return null;
        }
        return clazz.newInstance();
    }

    /**
     * 用来返回表示当前Class 所表示的实体（类、接口、基本类型或 void）的直接超类的Type。
     * 如果这个直接超类是参数化类型的，则返回的Type对象必须明确反映在源代码中声明时使用的类型.
     * eg: com.mot.hyena.test.GT2<java.lang.Integer>
     */
    private static Type getGenericSuperclass() {
        return null;
    }

    /**
     * 与上面那个方法类似，只不过Java的类可以实现多个接口，所以返回的Type必须用数组来存储。
     */
    private static Type[] getGenericInterfaces() {
        return null;
    }

    /**
     * 以上两个方法返回的都是Type对象或数组，在我们的这个话题中，Class都是代表的参数化类型，
     * 因此可以将Type对象Cast成ParameterizedType对象。
     * 而 ParameterizedType对象有一个方法， getActualTypeArguments()
     * 用来返回一个Type对象数组，这个数组代表着这个Type声明中实际使用的类型。
     *
     * @return
     */
    private static Type[] getActualTypeArguments() {
        return null;
    }

    /**
     * 获取类级别泛型参数 <br>
     * 适用于 child extends parent<class2> 返回class2
     * Map 返回K,V<br>
     * List返回 E<br>
     * 获取运行时泛型参数的类型，并数组的方式返回<br>
     * 当传入的对象为非泛型类型，则会返回空数组形式<br>
     * ParameterizedType参数化类型，即泛型<br>
     * getActualTypeArguments获取参数化类型的数组，泛型可能有多个<br>
     * getGenericSuperclass()获得带有泛型的父类<br>
     * getSuperclass()获得该类的父类 <br>
     *
     * @param object
     * @return
     */
    public static Type[] getParentParameterizedTypes(Object object) {
        Type superclassType = object.getClass().getGenericSuperclass();
        if (!ParameterizedType.class.isAssignableFrom(superclassType.getClass())) {
            return null;
        }
        return ((ParameterizedType) superclassType).getActualTypeArguments();
    }

    /**
     * 获取属性的泛型参数,无需初始化
     *
     * @param field
     * @return
     */
    public static Class<?>[] getFieldParameterizedTypes(Field field) {

        System.out.println(field.getType().getName());

        Type type = field.getGenericType();

        ParameterizedType parameterizedType = (ParameterizedType) type;

        return (Class<?>[]) parameterizedType.getActualTypeArguments();

    }

    /**
     * 判断类是否有默认构造函数
     *
     * @param clazz
     * @return
     * @throws SecurityException
     */
    public static boolean hasDefaultConstructor(Class<?> clazz)
            throws SecurityException {
        Class<?>[] empty = {};
        try {
            clazz.getConstructor(empty);
        } catch (NoSuchMethodException e) {
            return false;
        }
        return true;
    }

    /**
     * 获取指定类型中的特定field类型
     *
     * @param clazz
     * @param fieldName 字段名
     * @return
     */
    public static Class<?> getFieldClass(Class<?> clazz, String fieldName) {
        if (clazz == null || fieldName == null || fieldName.isEmpty()) {
            return null;
        }

        Class<?> propertyClass = null;

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getName().equalsIgnoreCase(fieldName)) {
                propertyClass = field.getType();
                break;
            }
        }

        return propertyClass;
    }

    /**
     * 获取指定类型中的特定method返回类型
     *
     * @param clazz
     * @param methodName 方法名
     * @return
     */
    public static Class<?> getMethodReturnType(Class<?> clazz, String methodName) {
        if (clazz == null || methodName == null || methodName.isEmpty()) {
            return null;
        }
        methodName = methodName.toLowerCase();
        Class<?> returnType = null;

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                returnType = method.getReturnType();
                break;
            }
        }
        return returnType;
    }

    /**
     * 根据字符串标示获取枚举常量
     *
     * @param clazz
     * @param enumField
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Object getEnumConstant(Class<?> clazz, String enumField) {
        if (clazz == null || enumField == null || enumField.isEmpty()) {
            return null;
        }
        return Enum.valueOf((Class<Enum>) clazz, enumField);
    }

    /**
     * 属性赋值
     *
     * @param object
     * @param value
     */
    public static void setValue(Object object, String fieldName, Object value) {
        try {
            Class<?> clazz = object.getClass();

            Field field;

            field = clazz.getDeclaredField(fieldName);

            if (field.isAccessible()) {

                field.set(object, value);

            } else {
                // private 私有变量
                // 设置允许访问
                field.setAccessible(true);

                field.set(object, value);

            }
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 对象转换为map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> toMap(Object obj) {

        Map<String, Object> map = new HashMap<String, Object>();
        // 获取对象对应类中的所有属性域
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            String varName = fields[i].getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o = fields[i].get(obj);
                if (o != null) {
                    map.put(varName, o);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 获取指定类指定方法参数类型
     *
     * @param clazz
     * @param methodName
     * @return
     */
    public static Class<?>[] getMethodParameterType(Class<?> clazz,
                                                    String methodName) {

        Class<?>[] parameterTypes = null;
        Method[] methods = clazz.getMethods();

        for (Method method : methods) {

            String name = method.getName();
            if (name.equalsIgnoreCase(methodName)) {
                parameterTypes = method.getParameterTypes();
                break;
            }
        }
        return parameterTypes;
    }

    /**
     * 获取指定类指定属性的修饰符(与方法修饰符类似)
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    public static String getFieldModifiers(Class<?> clazz, String fieldName) {

        int typeValue = 0;
        Field field;
        try {
            field = clazz.getDeclaredField(fieldName);
            typeValue = field.getModifiers();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Modifier.toString(typeValue);

    }

    /**
     * 执行指定类指定方法(不能使用包装类替换基本类型 Integer.class不能代替int.class)
     *
     * @param object     类
     * @param methodName 方法名称
     * @param paramClass 参数Class类型
     * @param paramValue 参数列表
     * @return
     */
    public static Object excuteMethod(Object object, String methodName,
                                      Class<?>[] paramClass, Object[] paramValue) {

        Class<?> clazz = object.getClass();

        Object resultObject = null;
        try {

            Method method = clazz.getDeclaredMethod(methodName, paramClass);

            resultObject = method.invoke(object, paramValue);

            // 执行static函数无需newInstance
            // resultObject = method.invoke(clazz, paramValue);

        } catch (NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            return resultObject;
        }
    }

    /**
     * 判断一个类是否是另一个类的子类
     *
     * @param clazz
     * @param target
     * @return
     */
    public static boolean isChild(Class<?> clazz, Class<?> target) {

        return clazz.isAssignableFrom(target);

    }

    /**
     * 判断类型是否是基本类型数据
     *
     * @param clazz
     * @return
     */
    public static boolean isBaseData(Class<?> clazz) {

        return clazz.isPrimitive();
    }

    /**
     * 获取数组的类型 如String[]与String[][]返回java.lang.String
     *
     * @param clazz
     * @return
     */
    public static Class<?> getArrayItemType(Class<?> clazz) {

        Class<?> componentClass = null;

        if (clazz.isArray()) {

            componentClass = clazz.getComponentType();

            return getArrayItemType(componentClass);

        } else {
            return clazz;
        }

    }

    class T {
        List<A> a = new ArrayList<A>();
        List<B> b;
        List<String> d;
        Map<Integer, String> map;
        int c;
    }

    class A {
    }

    class B {
    }

    public static void main(String[] args) {
        // Map<String, Integer> map = new HashMap<String, Integer>();

        List<A> ab = new ArrayList<A>();

        for (Type type : getParentParameterizedTypes(ab)) {
            System.out.println(getClassName(type));
        }

         int[] ints = new int[2];
//         Button[] buttons = new Button[6];
//         String[][] twoDim = new String[4][5];
//         String[] two = new String[4];
//
//         System.out.println(getArrayType(ints.getClass()).getName());
        Class tc = T.class;
        Field[] fields = tc.getDeclaredFields();
        for (Field f : fields) {
            Class fc = f.getType();
            if (fc.isPrimitive()) {
                System.out.println("基本数据类型： " + f.getName() + "  "
                        + fc.getName());
            } else {
                if (fc.isAssignableFrom(List.class)) { // 判断是否为List
                    System.out.println("List类型：" + f.getName());
                    Type gt = f.getGenericType(); // 得到泛型类型
                    ParameterizedType pt = (ParameterizedType) gt;
                    Class lll = (Class) pt.getActualTypeArguments()[0];
                    System.out.println("\t\t" + lll.getName());
                }
            }
        }
    }
}