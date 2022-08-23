package cn.atsoft.dasheng.uc.service;

import cn.atsoft.dasheng.uc.entity.UcCurrency;
import cn.atsoft.dasheng.uc.model.params.UcCurrencyParam;
import cn.atsoft.dasheng.uc.model.result.UcCurrencyResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Sing
 * @since 2021-03-22
 */
public interface UcCurrencyService extends IService<UcCurrency> {

    /**
     * 新增
     *
     * @author Sing
     * @Date 2021-03-22
     */
    void add(UcCurrencyParam param);

    /**
     * 删除
     *
     * @author Sing
     * @Date 2021-03-22
     */
    void delete(UcCurrencyParam param);

    /**
     * 更新
     *
     * @author Sing
     * @Date 2021-03-22
     */
    void update(UcCurrencyParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Sing
     * @Date 2021-03-22
     */
    UcCurrencyResult findBySpec(UcCurrencyParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Sing
     * @Date 2021-03-22
     */
    List<UcCurrencyResult> findListBySpec(UcCurrencyParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Sing
     * @Date 2021-03-22
     */
     PageInfo findPageBySpec(UcCurrencyParam param);

}
