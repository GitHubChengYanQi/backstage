package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.CompanyRole;
import cn.atsoft.dasheng.crm.model.params.CompanyRoleParam;
import cn.atsoft.dasheng.crm.model.result.CompanyRoleResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 公司角色表 服务类
 * </p>
 *
 * @author 
 * @since 2021-09-06
 */
public interface CompanyRoleService extends IService<CompanyRole> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-09-06
     */
    CompanyRole add(CompanyRoleParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-09-06
     */
    void delete(CompanyRoleParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-09-06
     */
    void update(CompanyRoleParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-09-06
     */
    CompanyRoleResult findBySpec(CompanyRoleParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-09-06
     */
    List<CompanyRoleResult> findListBySpec(CompanyRoleParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-09-06
     */
     PageInfo<CompanyRoleResult> findPageBySpec(CompanyRoleParam param);

    void batchDelete(List<Long> ids);

}
