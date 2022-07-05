package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.app.model.params.PartsParam;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.model.params.BatchSkuParam;
import cn.atsoft.dasheng.erp.model.params.SkuParam;
import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
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
public interface SkuService extends IService<Sku> {

    /**
     * 新增
     *
     * @author
     * @Date 2021-10-18
     */
    Map<String,Sku> add(SkuParam param);

    List<SkuResult> getSkuByMd5(SkuParam param);

    @Transactional
    void directAdd(SkuParam param);

    void mirageSku(SkuParam param);

    /**
     * 删除
     *
     * @author
     * @Date 2021-10-18
     */
    void delete(SkuParam param);

    /**
     * 批量删除
     */
    void deleteBatch(SkuParam param);

    void batchAddSku(BatchSkuParam batchSkuParam);

    /**
     * 更新
     *
     * @author
     * @Date 2021-10-18
     */
    void update(SkuParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author
     * @Date 2021-10-18
     */
    SkuResult findBySpec(SkuParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author
     * @Date 2021-10-18
     */
    List<SkuResult> findListBySpec(SkuParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author
     * @Date 2021-10-18
     */
    PageInfo<SkuResult> findPageBySpec(SkuParam param);


    List<SkuSimpleResult> simpleFormatSkuResult(List<Long> skuIds);

    List<BackSku> backSku(Long ids);

    List<SkuResult> backSkuList(List<Long> skuIds);

    SpuResult backSpu(Long spuId);

    /**
     * @param skuiIds
     * @return
     */
    Map<Long, List<BackSku>> sendSku(List<Long> skuiIds);

    PageInfo<SkuResult> changePageBySpec(SkuParam param);



    List<SkuResult> AllSku();

    void format(List<SkuResult> param);


    SkuResult getSku(Long id);

    String skuMessage(Long skuId);

    List<SkuResult> formatSkuResult (List<Long> skuIds);

    SkuResult getDetail(Long skuId);

    //    /**
//     * 查询产品 新建或返回已有产品id
//     *
//     * @param param
//     * @return
//     */
//    private Long getOrSaveSpuClass(SkuParam param) {
//        Long spuClassificationId = 0L;
//        SpuClassification spuClassification = new SpuClassification();
//
//        if (ToolUtil.isNotEmpty(param.getSpuClassification().getSpuClassificationId())){
//            spuClassification = spuClassificationService.lambdaQuery().eq(SpuClassification::getSpuClassificationId, param.getSpuClassification().getSpuClassificationId()).and(i -> i.eq(SpuClassification::getDisplay, 1)).and(i -> i.eq(SpuClassification::getType, 2)).one();
//        }else{
//            spuClassification = spuClassificationService.lambdaQuery().eq(SpuClassification::getName, param.getSpuClassification().getName()).and(i -> i.eq(SpuClassification::getDisplay, 1)).and(i -> i.eq(SpuClassification::getType, 2)).one();
//        }
//
//        if (ToolUtil.isEmpty(spuClassification)) {
//            SpuClassification spuClassificationEntity = new SpuClassification();
//            spuClassificationEntity.setName(param.getSpuClassification().getName());
//            spuClassificationEntity.setType(1L);
//            spuClassificationEntity.setPid(param.getSpuClass());
//            spuClassificationService.save(spuClassificationEntity);
//            spuClassificationId = spuClassificationEntity.getSpuClassificationId();
//        } else {
//            spuClassificationId = spuClassification.getSpuClassificationId();
//        }
//        param.setSpuClass(spuClassificationId);
//        return spuClassificationId;
//    }
    Long addSkuFromSpu(PartsParam partsParam);
}
