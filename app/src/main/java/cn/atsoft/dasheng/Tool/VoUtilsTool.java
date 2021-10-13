package cn.atsoft.dasheng.Tool;


import com.baomidou.mybatisplus.extension.api.R;

import java.lang.reflect.Field;


public class VoUtilsTool {
    /****
     * @author song
     * @param object 对象
     * @return 如果对象不为空，且没有空值。返回false，对象为空或者有空值，返回true
     * */
    public static boolean checkObjFieldIsNull(Object object) throws IllegalAccessException {
        boolean flag = false;
        if (null != object) {
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);//在用反射时访问私有变量（private修饰变量）
                //对象且不全是空值 返回false
                flag = false;
                if (field.get(object) == null || field.get(object).equals("")) {
                    flag = true;
                }
                if (!flag) {
                    return flag;
                }
            }
        }
        return flag;
    }

}