package com.crs.utils;

import java.util.Collection;
import java.util.Map;

public class CheckNull {

    public boolean isAllNotNull(Object... args) {
        for (Object arg : args) {
            if (!isNotNull(arg)) {
                return false;
            }
        }
        return false;
    }

    /**
     * 检查对象是否为 null
     *
     * @param obj 对象
     * @return {@code true} null, {@code false} not null
     */
    public static boolean isNull(Object obj) {
        if (obj instanceof Collection) {
            // 检查 Collection 是否为 null
            return ((Collection) obj).isEmpty();
        } else if (obj instanceof Map) {
            // 检查 Map 是否为 null
            return ((Map) obj).isEmpty();
        } else if (obj instanceof CharSequence) {
            // 检查 CharSequence 是否为 null
            return ((CharSequence) obj).length() == 0;
        } else if (obj instanceof Object[]) {
            // 检查 Object 数组是否为 null
            return ((Object[]) obj).length == 0;
        } else if (obj instanceof boolean[]) {
            // 检查 boolean 数组是否为 null
            return ((boolean[]) obj).length == 0;
        } else if (obj instanceof byte[]) {
            // 检查 byte 数组是否为 null
            return ((byte[]) obj).length == 0;
        } else if (obj instanceof char[]) {
            // 检查 char 数组是否为 null
            return ((char[]) obj).length == 0;
        } else if (obj instanceof double[]) {
            // 检查 double 数组是否为 null
            return ((double[]) obj).length == 0;
        } else if (obj instanceof float[]) {
            // 检查 float 数组是否为 null
            return ((float[]) obj).length == 0;
        } else if (obj instanceof int[]) {
            // 检查 int 数组是否为 null
            return ((int[]) obj).length == 0;
        } else if (obj instanceof long[]) {
            // 检查 long 数组是否为 null
            return ((long[]) obj).length == 0;
        } else if (obj instanceof short[]) {
            // 检查 short 数组是否为 null
            return ((short[]) obj).length == 0;
        } else {
            // 检查对象是否为 null
            return obj == null;
        }
    }

    /**
     * 检查对象是否非 null
     *
     * @param obj 对象
     * @return {@code true} not null, {@code false} null
     */
    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }
}
