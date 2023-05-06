package cn.atsoft.dasheng.sys.modular.system.service;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.enmu.MicroServiceType;
import cn.atsoft.dasheng.enmu.OperationType;
import cn.atsoft.dasheng.entity.MessageEntity;
import cn.atsoft.dasheng.entity.MicroServiceEntity;
import cn.atsoft.dasheng.entity.RestMessage;
import cn.atsoft.dasheng.producer.RestMessageProducer;
import cn.atsoft.dasheng.sys.modular.system.entity.Dept;
import cn.atsoft.dasheng.sys.modular.system.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cn.atsoft.dasheng.enmu.MessageType.MICROSERVICE;

@Service
public class RestInitTenantService {
    @Autowired
    private RoleService roleService;

    @Autowired
    private DeptService deptService;
    @Autowired
    private RestMessageProducer restMessageProducer;


    public void init(Long tenantId) {
        System.out.println("init tenant");
        //初始化租户 创建一些基础信息

//        创建角色以及部门
        deptService.addDept(new Dept(){{
            setSimpleName("生产部");
            setFullName("生产部");
            setTenantId(getTenantId());
            setPid(0L);
        }});
        deptService.addDept(new Dept(){{
            setSimpleName("采购部");
            setFullName("采购部");
            setTenantId(getTenantId());
            setPid(0L);
        }});
        deptService.addDept(new Dept(){{
            setSimpleName("营销部");
            setTenantId(getTenantId());
            setFullName("营销部");
            setPid(0L);
        }});


        roleService.save(new Role(){{
            setName("经理");
            setDescription("经理");
            setPid(0L);
            setSort(1);
        }});
        roleService.save(new Role(){{
            setName("主管");
            setDescription("主管");
            setPid(0L);
            setSort(2);
        }});
        roleService.save(new Role(){{
            setName("员工");
            setDescription("员工");
            setPid(0L);
            setSort(2);
        }});
//        创建仓库
        restMessageProducer.microService(new MicroServiceEntity(){{
            setType(MicroServiceType.INIT);
            setOperationType(OperationType.DEFAULT);
            setObject(tenantId);
            setTimes(1);
            setMaxTimes(3);
        }});
//        创建库位
//        创建供应商以及自己企业

    }

}
