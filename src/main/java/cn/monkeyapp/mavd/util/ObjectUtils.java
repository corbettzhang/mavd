package cn.monkeyapp.mavd.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Corbett Zhang
 */
public class ObjectUtils {

    /**
     * 以destination对象为主
     */
    public static <T> void mergeObject(T origin, T destination) {
        if (origin == null || destination == null) {
            return;
        }
        if (!origin.getClass().equals(destination.getClass())) {
            return;
        }

        Field[] fields = getAllFields(destination.getClass());
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object valueD = field.get(origin);
                Object valueO = field.get(destination);
                if (null == valueO) {
                    field.set(destination, valueD);
                }
                field.setAccessible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取本类及其父类的属性的方法
     *
     * @param clazz 当前类对象
     * @return 字段数组
     */
    private static Field[] getAllFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        return fieldList.toArray(fields);
    }

    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

}
