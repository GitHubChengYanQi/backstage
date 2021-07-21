package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ContractMachine;
import cn.atsoft.dasheng.app.model.params.ContractMachineParam;
import cn.atsoft.dasheng.app.model.result.ContractMachineResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 机床合同表 服务类
 * </p>
 *
 * @author 
 * @since 2021-07-20
 */
public interface ContractMachineService extends IService<ContractMachine> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-07-20
     */
    void add(ContractMachineParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-07-20
     */
    void delete(ContractMachineParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-07-20
     */
    void update(ContractMachineParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-07-20
     */
    ContractMachineResult findBySpec(ContractMachineParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-07-20
     */
    List<ContractMachineResult> findListBySpec(ContractMachineParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-07-20
     */
     PageInfo<ContractMachineResult> findPageBySpec(ContractMachineParam param);

}
