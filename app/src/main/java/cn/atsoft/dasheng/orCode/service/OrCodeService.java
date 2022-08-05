package cn.atsoft.dasheng.orCode.service;

import cn.atsoft.dasheng.app.model.result.StockResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.model.params.OrCodeParam;
import cn.atsoft.dasheng.orCode.model.result.BackCodeRequest;
import cn.atsoft.dasheng.orCode.model.result.InKindRequest;
import cn.atsoft.dasheng.orCode.model.result.OrCodeResult;
import cn.atsoft.dasheng.orCode.pojo.AutomaticBindResult;
import cn.atsoft.dasheng.orCode.pojo.BatchAutomatic;
import cn.atsoft.dasheng.orCode.pojo.InkindQrcode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 二维码 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
public interface OrCodeService extends IService<OrCode> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-29
     */
    Long add(OrCodeParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-29
     */
    void delete(OrCodeParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-29
     */
    void update(OrCodeParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    OrCodeResult findBySpec(OrCodeParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    List<OrCodeResult> findListBySpec(OrCodeParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    PageInfo<OrCodeResult> findPageBySpec(OrCodeParam param);

    /**
     * spu详情
     *
     * @param spuResult
     */
    void spuFormat(SpuResult spuResult);

    /**
     * 库位详情接口
     *
     * @param storehousePositionsResult
     */
    void storehousePositionsFormat(StorehousePositionsResult storehousePositionsResult);

    /**
     * 库存详情
     *
     * @param stockResult
     */
    void stockFormat(StockResult stockResult);

    /**
     * 仓库详情
     *
     * @param storehouseResult
     */
    void storehouseFormat(StorehouseResult storehouseResult);


    List<Long> backBatchCode(List<Long> ids, String type);

    /**
     * 返回二维码
     *
     * @param backCodeRequest
     * @return
     */
    Long backCode(BackCodeRequest backCodeRequest);

    /**
     * 判断
     *
     * @param inKindRequest
     * @return
     */
    Boolean isNotBind(InKindRequest inKindRequest);

    /**
     * 判断绑定合法性
     *
     * @param inKindRequest
     * @return
     */
    Inkind judgeBind(InKindRequest inKindRequest);

    /**
     * 扫码入库
     *
     * @param inKindRequest
     * @return
     */
    Long instockByCode(InKindRequest inKindRequest);

    /**
     * 批量扫码入库
     *
     * @param inKindRequest
     * @return
     */

    void batchInstockByCode(InKindRequest inKindRequest);


    /**
     * 批量扫码出库
     *
     * @param inKindRequest
     * @return
     */

    void batchOutStockByCode(InKindRequest inKindRequest);

    /**
     * 批量增加二维码
     *
     * @param param
     */
    void batchAdd(OrCodeParam param);


    Map<String, Object> inkindDetail(Long qrcodeId);

    /**
     * @return
     */
    Object backInkindByCode(InKindRequest inKindRequest);

    /**
     * 扫码出库
     *
     * @return
     */
    Long outStockByCode(InKindRequest inKindRequest);


    /**
     * 自动生成二维码绑定
     */
    AutomaticBindResult automaticBinding(BackCodeRequest codeRequest);

    /**
     * 批量自动生成二维码绑定
     *
     * @param batchAutomatic
     * @return
     */
    List<AutomaticBindResult> batchAutomaticBinding(BatchAutomatic batchAutomatic);


    InkindQrcode ExcelBind(Long skuId, Long number, Long brandId, Long customerId, Long positionId);
}
