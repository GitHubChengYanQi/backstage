package cn.atsoft.dasheng.goods.spu.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.goods.sku.model.result.RestSkuResult;
import cn.atsoft.dasheng.goods.spu.entity.RestSpu;
import cn.atsoft.dasheng.goods.spu.model.params.RestSpuParam;
import cn.atsoft.dasheng.goods.spu.model.result.RestSpuResult;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author
 * @since 2021-10-18
 */
public interface RestSpuService extends IService<RestSpu> {

    /**
     * 新增
     *
     * @author
     * @Date 2021-10-18
     */
    Long add(RestSpuParam param);

    /**
     * 删除
     *
     * @author
     * @Date 2021-10-18
     */
    void delete(RestSpuParam param);

    /**
     * 更新
     *
     * @author
     * @Date 2021-10-18
     */
    void update(RestSpuParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author
     * @Date 2021-10-18
     */
    RestSpuResult findBySpec(RestSpuParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author
     * @Date 2021-10-18
     */
    List<RestSpuResult> findListBySpec(RestSpuParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author
     * @Date 2021-10-18
     */
    PageInfo<RestSpuResult> findPageBySpec(RestSpuParam param);

    ResponseData detail (RestSpuParam spuParam);


    void skuFormat(List<RestSkuResult> skuResults);
}
