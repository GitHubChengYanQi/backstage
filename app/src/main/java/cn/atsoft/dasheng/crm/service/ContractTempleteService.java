package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.ContractTemplete;
import cn.atsoft.dasheng.crm.model.params.ContractTempleteParam;
import cn.atsoft.dasheng.crm.model.result.ContractTempleteResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 自定义合同变量 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-18
 */
public interface ContractTempleteService extends IService<ContractTemplete> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    ContractTempleteResult add(ContractTempleteParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    void delete(ContractTempleteParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    void update(ContractTempleteParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    ContractTempleteResult findBySpec(ContractTempleteParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    List<ContractTempleteResult> findListBySpec(ContractTempleteParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
     PageInfo<ContractTempleteResult> findPageBySpec(ContractTempleteParam param);

}
