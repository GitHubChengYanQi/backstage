package cn.atsoft.dasheng.sys.modular.system.service;

import cn.atsoft.dasheng.sys.modular.system.entity.Dept;
import cn.atsoft.dasheng.sys.modular.system.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitTenantService {
    @Autowired
    private RoleService roleService;

    @Autowired
    private DeptService deptService;


    public void init(Long tenantId) {
        System.out.println("init tenant");
        //初始化租户 创建一些基础信息

//        创建角色以及部门
        deptService.addDept(new Dept(){{
            setSimpleName("生产部");
            setFullName("生产部");
            setPid(0L);
        }});
        deptService.addDept(new Dept(){{
            setSimpleName("采购部");
            setFullName("采购部");
            setPid(0L);
        }});
        deptService.addDept(new Dept(){{
            setSimpleName("营销部");
            setFullName("营销部");
            setPid(0L);
        }});


        roleService.save(new Role(){{
            setName("经理");
            setSort(1);
        }});
        roleService.save(new Role(){{
            setName("主管");
            setSort(2);
        }});
        roleService.save(new Role(){{
            setName("员工");
            setSort(2);
        }});
//        创建仓库


//        创建库位
//        创建供应商以及自己企业

    }

}
