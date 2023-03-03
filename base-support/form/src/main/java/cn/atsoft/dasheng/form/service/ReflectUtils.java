package cn.atsoft.dasheng.form.service;

import java.lang.reflect.Field;

import java.util.ArrayList;

import java.util.Arrays;

public class ReflectUtils {
    /**
     * 获取类clazz的所有Field，包括其父类的Field，如果重名，以子类Field为准。
     *
     * @param clazz
     * @return Field数组
     */

    public static Field[] getAllField(Class clazz) {

        ArrayList<Field> fieldList = new ArrayList<>();

        Field[] dFields = clazz.getDeclaredFields();

        if (dFields.length > 0) {

            fieldList.addAll(Arrays.asList(dFields));

        }

        Class  superClass = clazz.getSuperclass();

        if (superClass != Object.class) {

            Field[] superFields = getAllField(superClass);

            if (superFields.length > 0) {

                for (Field field : superFields) {

                    if (!isContain(fieldList, field)) {

                        fieldList.add(field);

                    }

                }

            }

        }

        Field[] result = new Field[fieldList.size()];

        fieldList.toArray(result);

        return result;

    }

    /**
     * 检测Field List中是否已经包含了目标field
     *
     * @param fieldList
     * @param field     带检测field
     * @return
     */

    public static boolean isContain(ArrayList<Field> fieldList, Field field) {

        for (Field temp : fieldList) {

            if (temp.getName().equals(field.getName())) {

                return true;

            }

        }

        return false;

    }

}
