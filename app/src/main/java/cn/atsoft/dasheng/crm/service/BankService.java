package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Bank;
import cn.atsoft.dasheng.crm.model.params.BankParam;
import cn.atsoft.dasheng.crm.model.result.BankResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author song
 * @since 2022-02-24
 */
public interface BankService extends IService<Bank> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-02-24
     */
    void add(BankParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-02-24
     */
    void delete(BankParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-02-24
     */
    void update(BankParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-02-24
     */
    BankResult findBySpec(BankParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-02-24
     */
    List<BankResult> findListBySpec(BankParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-02-24
     */
     PageInfo<BankResult> findPageBySpec(BankParam param);

}
