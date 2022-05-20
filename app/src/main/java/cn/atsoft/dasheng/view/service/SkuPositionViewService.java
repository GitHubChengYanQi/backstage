package cn.atsoft.dasheng.view.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.view.entity.SkuPositionView;
import cn.atsoft.dasheng.view.model.params.SkuPositionViewParam;
import cn.atsoft.dasheng.view.model.result.SkuPositionViewResult;
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
public interface SkuPositionViewService extends IService<SkuPositionView> {

    /**
     * 新增
     *
     * @author 
     * @Date 2022-05-20
     */
    void add(SkuPositionViewParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2022-05-20
     */
    void delete(SkuPositionViewParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2022-05-20
     */
    void update(SkuPositionViewParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2022-05-20
     */
    SkuPositionViewResult findBySpec(SkuPositionViewParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2022-05-20
     */
    List<SkuPositionViewResult> findListBySpec(SkuPositionViewParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2022-05-20
     */
     PageInfo<SkuPositionViewResult> findPageBySpec(SkuPositionViewParam param);

}
