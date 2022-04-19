package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.ContractTempleteDetail;
import cn.atsoft.dasheng.crm.model.params.ContractTempleteDetailParam;
import cn.atsoft.dasheng.crm.model.result.ContractTempleteDetailResult;
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
public interface ContractTempleteDetailService extends IService<ContractTempleteDetail> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    void add(ContractTempleteDetailParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    void delete(ContractTempleteDetailParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    void update(ContractTempleteDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    ContractTempleteDetailResult findBySpec(ContractTempleteDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    List<ContractTempleteDetailResult> findListBySpec(ContractTempleteDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
     PageInfo<ContractTempleteDetailResult> findPageBySpec(ContractTempleteDetailParam param);

}
