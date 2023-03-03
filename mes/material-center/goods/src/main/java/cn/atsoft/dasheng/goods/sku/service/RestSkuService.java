package cn.atsoft.dasheng.goods.sku.service;


import cn.atsoft.dasheng.goods.sku.entity.RestSku;
import cn.atsoft.dasheng.goods.sku.model.params.RestSkuParam;
import cn.atsoft.dasheng.goods.sku.model.result.RestSkuResult;
import cn.atsoft.dasheng.goods.sku.model.result.SkuListResult;
import cn.atsoft.dasheng.goods.spu.model.result.RestSpuResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * sku表	 服务类
 * </p>
 *
 * @author
 * @since 2021-10-18
 */
public interface RestSkuService extends IService<RestSku> {

    /**
     * 新增
     *
     * @author
     * @Date 2021-10-18
     */
    Map<String, RestSku> add(RestSkuParam param);

//    void editEnclosure(RestSkuParam param);
//
//    List<RestSkuResult> getSkuByMd5(RestSkuParam param);
//
//    @Transactional
//    void directAdd(RestSkuParam param);
//
//    void mirageSku(RestSkuParam param);
//
//    /**
//     * 删除
//     *
//     * @author
//     * @Date 2021-10-18
//     */
//    void delete(RestSkuParam param);
//
//    /**
//     * 批量删除
//     */
//    void deleteBatch(RestSkuParam param);
//
//    void batchAddSku(BatchSkuParam batchSkuParam);
//
//    List<SkuBind> skuBindList(SkuBindParam skuBindParam);
//
//
//    /**
//     * 更新
//     *
//     * @author
//     * @Date 2021-10-18
//     */
//    void update(RestSkuParam param);
//
//    /**
//     * 查询单条数据，Specification模式
//     *
//     * @author
//     * @Date 2021-10-18
//     */
//    RestSkuResult findBySpec(RestSkuParam param);
//
//    /**
//     * 查询列表，Specification模式
//     *
//     * @author
//     * @Date 2021-10-18
//     */
//    List<RestSkuResult> findListBySpec(RestSkuParam param);
//
//    /**
//     * 查询分页数据，Specification模式
//     *
//     * @author
//     * @Date 2021-10-18
//     */
//    PageInfo<RestSkuResult> findPageBySpec(RestSkuParam param);
//
//
//    List<SkuSimpleResult> simpleFormatSkuResult(List<Long> skuIds);
//
//    List<BackSku> backSku(Long ids);
//
//    List<RestSkuResult> backSkuList(List<Long> skuIds);
//
//    RestSpuResult backSpu(Long spuId);
//
//    /**
//     * @param skuiIds
//     * @return
//     */
//    Map<Long, List<BackSku>> sendSku(List<Long> skuiIds);
//
//    Page skuPage(RestSkuParam param);
//
//    PageInfo<RestSkuResult> changePageBySpec(RestSkuParam param);
//
//
//    List<RestSkuResult> AllSku();
//
//    void formatSkuMedias(RestSkuResult skuResult);
//
//    List<MediaResult> strToMediaResults(String param);
//
    void format(List<RestSkuResult> param);
//
//
//    RestSkuResult getSku(Long id);
//
//    String skuMessage(Long skuId);
//
    List<RestSkuResult> formatSkuResult(List<Long> skuIds);
//
//    RestSkuResult getDetail(Long skuId);
//
//    Long addSkuFromSpu(PartsParam partsParam);
    List<RestSku> skuResultBySpuId(Long spuId);

    List<RestSku> getByIds(List<Long> skuIds);

    List<SkuListResult> viewResultsByIds(List<Long> ids);
}
