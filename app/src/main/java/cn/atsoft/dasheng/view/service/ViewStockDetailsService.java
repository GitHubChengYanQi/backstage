package cn.atsoft.dasheng.view.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.view.entity.ViewStockDetails;
import cn.atsoft.dasheng.view.model.params.ViewStockDetailsParam;
import cn.atsoft.dasheng.view.model.result.ViewStockDetailsResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author 
 * @since 2022-01-27
 */
public interface ViewStockDetailsService extends IService<ViewStockDetails> {

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2022-01-27
     */
    ViewStockDetailsResult findBySpec(ViewStockDetailsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2022-01-27
     */
    PageInfo<ViewStockDetailsResult> findListBySpec(ViewStockDetailsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2022-01-27
     */
     PageInfo<ViewStockDetailsResult> findPageBySpec(ViewStockDetailsParam param);

}
