package cn.atsoft.dasheng.uc.service;

import cn.atsoft.dasheng.uc.entity.UcPayOrder;
import cn.atsoft.dasheng.uc.model.params.UcPayOrderParam;
import cn.atsoft.dasheng.uc.model.result.UcPayOrderResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Sing
 * @since 2021-03-21
 */
public interface UcPayOrderService extends IService<UcPayOrder> {

    /**
     * 新增
     *
     * @author Sing
     * @Date 2021-03-21
     */
    void add(UcPayOrderParam param);

    /**
     * 删除
     *
     * @author Sing
     * @Date 2021-03-21
     */
    void delete(UcPayOrderParam param);

    /**
     * 更新
     *
     * @author Sing
     * @Date 2021-03-21
     */
    void update(UcPayOrderParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Sing
     * @Date 2021-03-21
     */
    UcPayOrderResult findBySpec(UcPayOrderParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Sing
     * @Date 2021-03-21
     */
    List<UcPayOrderResult> findListBySpec(UcPayOrderParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Sing
     * @Date 2021-03-21
     */
     PageInfo findPageBySpec(UcPayOrderParam param);

}
