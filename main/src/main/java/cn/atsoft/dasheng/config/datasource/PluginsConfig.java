package cn.atsoft.dasheng.config.datasource;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.core.metadata.CustomMetaObjectHandler;
import cn.atsoft.dasheng.modular.dynamic.entity.Dynamic;
import cn.atsoft.dasheng.sys.core.constant.factory.ConstantFactory;
import cn.atsoft.dasheng.sys.core.constant.factory.IConstantFactory;
import cn.atsoft.dasheng.sys.modular.rest.entity.RestUser;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import com.alibaba.fastjson.JSON;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * mp的插件拓展和资源扫描
 */
@Configuration
@MapperScan(basePackages = {"cn.atsoft.**.mapper"})
public class PluginsConfig {

    /**
     * 拓展核心包中的字段包装器
     */
    @Bean
    public CustomMetaObjectHandler gunsMpFieldHandler() {
        return new CustomMetaObjectHandler() {
            @Override
            protected Long getUserDeptId() {
                try {
                    return LoginContextHolder.getContext().getUser().getDeptId();
                } catch (Exception e) {

                    //如果获取不到当前用户就存空id
                    return -100L;
                }
            }

            @Override
            protected Long getUserUniqueId() {
                try {
                    return LoginContextHolder.getContext().getUser().getId();
                } catch (Exception e) {

                    //如果获取不到当前用户就存空id
                    return -100L;
                }
            }


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

                try {
                    setFieldValByName(getCreateTimeFieldName(), new Date(), metaObject);
                } catch (ReflectionException e) {
                    //没有此字段，则不处理
                }

                try {
                    //获取当前登录用户
                    Object accountId = getUserUniqueId();
                    setFieldValByName(getCreateUserFieldName(), accountId, metaObject);
                } catch (ReflectionException e) {
                    //没有此字段，则不处理
                }
                Object deptId = null;
                try {
                    deptId = getFieldValByName(getDeptIdFieldName(), metaObject);
                    if (deptId == null) {
                        Long userDeptId = this.getUserDeptId();
                        setFieldValByName(getDeptIdFieldName(), userDeptId, metaObject);
                    }
                } catch (ReflectionException e) {
                    //没有此字段，则不处理
                }

                Object userId = null;
                try {
                    userId = getFieldValByName(getUserIdFieldName(), metaObject);
                    IConstantFactory iConstantFactory = new ConstantFactory();
                    Long deptId1 = iConstantFactory.getDeptId((Long) userId);
                    if (userId != null) {
                        setFieldValByName(getDeptIdFieldName(), deptId1, metaObject);
                    }
                } catch (ReflectionException e) {
                    //没有此字段，则不处理
                }
                try {
                    setFieldValByName(getUpdateTimeFieldName(), null, metaObject);
                } catch (ReflectionException e) {
                    //没有此字段，则不处理
                }
                try{
                    printDynamic(metaObject,LoginContextHolder.getContext().getUser(),1 );
                }catch (Exception e){

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

                Object userId = null;
                try {
                    userId = getFieldValByName(getUserIdFieldName(), metaObject);
                    if (!(metaObject.getOriginalObject() instanceof User) && !(metaObject.getOriginalObject() instanceof RestUser)) {
                        IConstantFactory iConstantFactory = new ConstantFactory();
                        Long deptId1 = iConstantFactory.getDeptId((Long) userId);
                        if (userId != null) {
                            setFieldValByName(getDeptIdFieldName(), deptId1, metaObject);
                        }
                    }

                } catch (ReflectionException e) {
                    //没有此字段，则不处理
                }
                try{
                    printDynamic(metaObject,LoginContextHolder.getContext().getUser(),2 );
                }catch (Exception e){

                }
            }
        };
    }

    public void printDynamic(MetaObject object, LoginUser loginUser, Integer type) {
        if (type.equals(1)){
            for (String simpleName : WHITE_LIST) {
                if (simpleName.equals(object.getOriginalObject().getClass().getSimpleName())){
                    System.err.println(loginUser.getName()+"_"+"添加了"+"_"+object.getOriginalObject().getClass().getSimpleName()+"_"+ JSON.toJSONString(object.getOriginalObject()));
                    Dynamic dynamic = new Dynamic();
                    dynamic.setAfter(JSON.toJSONString(object.getOriginalObject()));
                    dynamic.setType(type.toString());
                }
            }

        }else if (type.equals(2)){
            for (String simpleName : WHITE_LIST) {
                if (simpleName.equals(object.getOriginalObject().getClass().getSimpleName())){
                    System.err.println(loginUser.getName()+"_"+"操作了"+"_"+object.getOriginalObject().getClass().getSimpleName());
                }
            }

        }
    }

    static final String[] WHITE_LIST={
        "Sku", "ActivitiProcessTask","OperationLog"
    };


}
