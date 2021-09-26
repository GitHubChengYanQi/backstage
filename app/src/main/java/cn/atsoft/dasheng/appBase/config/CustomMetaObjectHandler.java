package cn.atsoft.dasheng.appBase.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;
import org.springframework.stereotype.Component;

import java.util.Date;

//@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {

        Object delFlag = null;
        try {
            delFlag = getFieldValByName(getDeleteFlagFieldName(), metaObject);
            if (delFlag == null) {
                setFieldValByName(getDeleteFlagFieldName(), getDefaultDelFlagValue(), metaObject);
            }
        } catch (ReflectionException e) {
            //没有此字段，则不处理
        }

        Object createTime = null;
        try {
            createTime = getFieldValByName(getCreateTimeFieldName(), metaObject);
            if (createTime == null) {
                setFieldValByName(getCreateTimeFieldName(), new Date(), metaObject);
            }
        } catch (ReflectionException e) {
            //没有此字段，则不处理
        }

        Object createUser = null;
        try {
            createUser = getFieldValByName(getCreateUserFieldName(), metaObject);
            if (createUser == null) {

                //获取当前登录用户
                Object accountId = getUserUniqueId();

                setFieldValByName(getCreateUserFieldName(), accountId, metaObject);
            }
        } catch (ReflectionException e) {
            //没有此字段，则不处理
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            setFieldValByName(getUpdateTimeFieldName(), new Date(), metaObject);
        } catch (ReflectionException e) {
            //没有此字段，则不处理
        }

        Object updateUser = null;
        try {
            updateUser = getFieldValByName(getUpdateUserFieldName(), metaObject);
            if (updateUser == null) {
                Object accountId = getUserUniqueId();
                setFieldValByName(getUpdateUserFieldName(), accountId, metaObject);
            }
        } catch (ReflectionException e) {
            //没有此字段，则不处理
        }
    }

    /**
     * 获取逻辑删除字段的名称（非数据库中字段名称）
     */
    protected String getDeleteFlagFieldName() {
        return "delFlag";
    }

    /**
     * 获取逻辑删除字段的默认值
     */
    protected Object getDefaultDelFlagValue() {
        return "N";
    }

    /**
     * 获取创建时间字段的名称（非数据库中字段名称）
     */
    protected String getCreateTimeFieldName() {
        return "createTime";
    }

    /**
     * 获取创建用户字段的名称（非数据库中字段名称）
     */
    protected String getCreateUserFieldName() {
        return "createUser";
    }

    /**
     * 获取更新时间字段的名称（非数据库中字段名称）
     */
    protected String getUpdateTimeFieldName() {
        return "updateTime";
    }

    /**
     * 获取更新用户字段的名称（非数据库中字段名称）
     */
    protected String getUpdateUserFieldName() {
        return "updateUser";
    }

    /**
     * 获取用户唯一id（注意默认获取的用户唯一id为空，如果想填写则需要继承本类）
     */
    protected Object getUserUniqueId() {
        return "";
    }
}
