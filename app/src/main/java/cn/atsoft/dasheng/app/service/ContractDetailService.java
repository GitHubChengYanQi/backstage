package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.app.model.params.BusinessDetailedParam;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ContractDetail;
import cn.atsoft.dasheng.app.model.params.ContractDetailParam;
import cn.atsoft.dasheng.app.model.result.ContractDetailResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 合同产品明细 服务类
 * </p>
 *
 * @author sb
 * @since 2021-09-18
 */
public interface ContractDetailService extends IService<ContractDetail> {

    /**
     * 新增
     *
     * @author sb
     * @Date 2021-09-18
     */
    void add(ContractDetailParam param);

    void addAll(BusinessDetailedParam param);

    /**
     * 删除
     *
     * @author sb
     * @Date 2021-09-18
     */
    void delete(ContractDetailParam param);

    /**
     * 更新
     *
     * @author sb
     * @Date 2021-09-18
     */
    void update(ContractDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author sb
     * @Date 2021-09-18
     */
    ContractDetailResult findBySpec(ContractDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author sb
     * @Date 2021-09-18
     */
    List<ContractDetailResult> findListBySpec(ContractDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author sb
     * @Date 2021-09-18
     */
     PageInfo<ContractDetailResult> findPageBySpec(ContractDetailParam param, DataScope dataScope );

    void format(List<ContractDetailResult> data);
}
