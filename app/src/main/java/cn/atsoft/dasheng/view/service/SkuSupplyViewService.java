package cn.atsoft.dasheng.view.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.view.entity.SkuSupplyView;
import cn.atsoft.dasheng.view.model.params.SkuSupplyViewParam;
import cn.atsoft.dasheng.view.model.result.SkuSupplyViewResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author 
 * @since 2022-05-20
 */
public interface SkuSupplyViewService extends IService<SkuSupplyView> {

    /**
     * 新增
     *
     * @author 
     * @Date 2022-05-20
     */
    void add(SkuSupplyViewParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2022-05-20
     */
    void delete(SkuSupplyViewParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2022-05-20
     */
    void update(SkuSupplyViewParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2022-05-20
     */
    SkuSupplyViewResult findBySpec(SkuSupplyViewParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2022-05-20
     */
    List<SkuSupplyViewResult> findListBySpec(SkuSupplyViewParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2022-05-20
     */
     PageInfo<SkuSupplyViewResult> findPageBySpec(SkuSupplyViewParam param);

}
