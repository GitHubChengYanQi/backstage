package cn.atsoft.dasheng.config.datasource;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.core.metadata.CustomMetaObjectHandler;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.dynamic.model.params.DynamicParam;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.MicroServiceEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.dynamic.entity.Dynamic;
import cn.atsoft.dasheng.sys.core.constant.factory.ConstantFactory;
import cn.atsoft.dasheng.sys.core.constant.factory.IConstantFactory;
import cn.atsoft.dasheng.sys.modular.rest.entity.RestUser;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import com.alibaba.fastjson.JSON;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

import static cn.atsoft.dasheng.message.enmu.MicroServiceType.DYNAMIC;

/**
 * mp的插件拓展和资源扫描
 */
@Configuration
@MapperScan(basePackages = {"cn.atsoft.**.mapper"})
public class  PluginsConfig {
    @Autowired
    private MessageProducer messageProducer;

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
            protected Long getUserTenantId() {
                try {
                    return LoginContextHolder.getContext().getUser().getTenantId();
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
                Object userId = null;
                try {
                    userId = getFieldValByName(getUserIdFieldName(), metaObject);
                    IConstantFactory iConstantFactory = new ConstantFactory();
                    Long deptId1 = iConstantFactory.getDeptId((Long) userId);
                    if (userId != null && getFieldValByName(getDeptIdFieldName(), metaObject) == null) {
                        setFieldValByName(getDeptIdFieldName(), deptId1, metaObject);
                    }
                } catch (ReflectionException e) {
                    //没有此字段，则不处理
                }
                try {
                    //获取当前登录用户
                    Object accountId = getUserUniqueId();
                    Object fieldValByName = getFieldValByName(getCreateUserFieldName(), metaObject);
                    if(ToolUtil.isEmpty(fieldValByName)){
                        setFieldValByName(getCreateUserFieldName(), accountId, metaObject);
                    }
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


                try {
                    setFieldValByName(getUpdateTimeFieldName(), null, metaObject);
                } catch (ReflectionException e) {
                    //没有此字段，则不处理
                }

                Object tenantId = null;
                try {
                    tenantId = getFieldValByName(getTenantFieldName(), metaObject);
                    if (tenantId == null) {

                        //部门
                        Object userTenantId = getUserTenantId();

                        setFieldValByName(getTenantFieldName(), userTenantId, metaObject);
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
            }
        };
    }

}
