package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.app.model.params.PartsParam;
import cn.atsoft.dasheng.appBase.model.result.MediaResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.model.params.BatchSkuParam;
import cn.atsoft.dasheng.erp.model.params.SkuParam;
import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.pojo.SkuBind;
import cn.atsoft.dasheng.erp.pojo.SkuBindParam;
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
    Map<String, Sku> add(SkuParam param);

    void editEnclosure(SkuParam param);

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

    List<SkuBind> skuBindList(SkuBindParam skuBindParam);

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

    void formatSkuMedias(SkuResult skuResult);

    List<MediaResult> strToMediaResults(String param);

    void format(List<SkuResult> param);


    SkuResult getSku(Long id);

    String skuMessage(Long skuId);

    List<SkuResult> formatSkuResult(List<Long> skuIds);

    SkuResult getDetail(Long skuId);

    Long addSkuFromSpu(PartsParam partsParam);
}
