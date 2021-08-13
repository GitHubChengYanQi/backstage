package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.model.params.ContractParam;
import cn.atsoft.dasheng.app.model.result.ContractResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 合同表 服务类
 * </p>
 *
 * @author 
 * @since 2021-07-21
 */
public interface ContractService extends IService<Contract> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-07-21
     * @return
     */
    Contract add(ContractParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-07-21
     * @return
     */
    Contract delete(ContractParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-07-21
     * @return
     */
    Contract update(ContractParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-07-21
     */
    ContractResult findBySpec(ContractParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-07-21
     */
    List<ContractResult> findListBySpec(ContractParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-07-21
     */
     PageInfo<ContractResult> findPageBySpec(ContractParam param);

    void batchDelete( List<Long> id);

    ContractResult detail (Long id);


    ContractResult addResult (ContractParam param);
}
