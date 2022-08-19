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
public class PluginsConfig {
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
                try {
                    printDynamic(metaObject, (Long) userId, 1);
                } catch (Exception e) {
                    e.printStackTrace();
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
                try {
                    printDynamic(metaObject, (Long) userId, 2);
                } catch (Exception e) {


                }
            }
        };
    }

    public void printDynamic(MetaObject object, Long userId, Integer type) {
        for (String simpleName : WHITE_LIST) {
            if (simpleName.equals(object.getOriginalObject().getClass().getSimpleName()) && (!userId.equals(-100L) || ToolUtil.isNotEmpty(userId))) {
                DynamicParam dynamic = new DynamicParam();
                dynamic.setAfter(JSON.toJSONString(object.getOriginalObject()));
                dynamic.setType(type.toString());
                dynamic.setUserId(userId);
                this.formatDynamic(object, dynamic);
                messageProducer.microService(new MicroServiceEntity() {{
                    setType(DYNAMIC);
                    setObject(dynamic);
                    setOperationType(OperationType.SAVE);
                    setMaxTimes(2);
                    setTimes(1);
                    ;
                }});
            }
        }

    }

    static final String[] WHITE_LIST = {
            "Sku",
            "ActivitiProcessTask",
            "ActivitiProcessLog"
    };
    public enum WHITE_LIST {
            Sku,
            ActivitiProcessTask,
            ActivitiProcessLog
    };

    private void formatDynamic(MetaObject object, DynamicParam dynamic) {

        if (ToolUtil.isNotEmpty(object.getValue("skuId"))) {
            dynamic.setSkuId((Long) object.getValue("skuId"));
        }
        if (ToolUtil.isNotEmpty(object.getValue("spuId"))) {
            dynamic.setSkuId((Long) object.getValue("spuId"));
        }
        dynamic.setSourceId((Long) object.getValue("skuId"));

        if (ToolUtil.isNotEmpty(object.getValue("processTaskId"))) {
            dynamic.setTaskId((Long) object.getValue("processTaskId"));
        }
        dynamic.setSourceId((Long) object.getValue("processTaskId"));

        if (ToolUtil.isNotEmpty(object.getValue("taskId"))) {
            dynamic.setTaskId((Long) object.getValue("taskId"));
        }
        if (ToolUtil.isNotEmpty(object.getValue("spuId"))) {
            dynamic.setSpuId((Long) object.getValue("spuId"));
        }
        if (ToolUtil.isNotEmpty(object.getValue("storehouseId"))) {
            dynamic.setStorehouseId((Long) object.getValue("storehouseId"));
        }
        if (ToolUtil.isNotEmpty(object.getValue("storeHouseId"))) {
            dynamic.setStorehouseId((Long) object.getValue("storeHouseId"));
        }
        if (ToolUtil.isNotEmpty(object.getValue("positionsId"))) {
            dynamic.setStorehousePositionsId((Long) object.getValue("positionsId"));
        }
        if (ToolUtil.isNotEmpty(object.getValue("storehousePositionsId"))) {
            dynamic.setStorehousePositionsId((Long) object.getValue("storehousePositionsId"));
        }
        switch (object.getOriginalObject().getClass().getSimpleName()) {
            case "ActivitiProcessTask":
                dynamic.setSourceId((Long) object.getValue("ProcessTask"));
                dynamic.setSource(object.getOriginalObject().getClass().getSimpleName());
                break;
            case "ActivitiProcessLog":
                dynamic.setSourceId((Long) object.getValue("ActivitiProcessLog"));
                dynamic.setSource(object.getOriginalObject().getClass().getSimpleName());
                break;
            case "InstockOrder":
                dynamic.setSourceId((Long) object.getValue("InstockOrder"));
                dynamic.setSource(object.getOriginalObject().getClass().getSimpleName());
                break;
            case "ProductionPickLists":
                dynamic.setSourceId((Long) object.getValue("ProductionPickLists"));
                break;
             case "ProductionPickListsCart":
                dynamic.setSourceId((Long) object.getValue("ProductionPickListsCart"));
                break;
            case "Maintenance":
                dynamic.setSourceId((Long) object.getValue("Maintenance"));
                break;
            case "Allocation":
                dynamic.setSourceId((Long) object.getValue("Allocation"));
                break;
        }
        dynamic.setSource(object.getOriginalObject().getClass().getSimpleName());

    }
}
