package cn.atsoft.dasheng.view.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.view.entity.SkuBasisView;
import cn.atsoft.dasheng.view.model.params.SkuBasisViewParam;
import cn.atsoft.dasheng.view.model.result.SkuBasisViewResult;
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
public interface SkuBasisViewService extends IService<SkuBasisView> {

    /**
     * 新增
     *
     * @author 
     * @Date 2022-05-20
     */
    void add(SkuBasisViewParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2022-05-20
     */
    void delete(SkuBasisViewParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2022-05-20
     */
    void update(SkuBasisViewParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2022-05-20
     */
    SkuBasisViewResult findBySpec(SkuBasisViewParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2022-05-20
     */
    List<SkuBasisViewResult> findListBySpec(SkuBasisViewParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2022-05-20
     */
     PageInfo<SkuBasisViewResult> findPageBySpec(SkuBasisViewParam param);

}
