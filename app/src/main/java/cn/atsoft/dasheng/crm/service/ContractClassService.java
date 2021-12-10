package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.ContractClass;
import cn.atsoft.dasheng.crm.model.params.ContractClassParam;
import cn.atsoft.dasheng.crm.model.result.ContractClassResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 合同分类 服务类
 * </p>
 *
 * @author song
 * @since 2021-12-09
 */
public interface ContractClassService extends IService<ContractClass> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-12-09
     */
    void add(ContractClassParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-12-09
     */
    void delete(ContractClassParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-12-09
     */
    void update(ContractClassParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-12-09
     */
    ContractClassResult findBySpec(ContractClassParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-12-09
     */
    List<ContractClassResult> findListBySpec(ContractClassParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-12-09
     */
     PageInfo<ContractClassResult> findPageBySpec(ContractClassParam param);

}
