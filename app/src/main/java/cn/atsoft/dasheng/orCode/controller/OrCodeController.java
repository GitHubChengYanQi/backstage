package cn.atsoft.dasheng.orCode.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.InstockParam;
import cn.atsoft.dasheng.app.model.params.OutstockParam;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.InstockListParam;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.model.result.CategoryResult;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.form.entity.FormDataValue;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.params.OrCodeBindParam;
import cn.atsoft.dasheng.orCode.model.params.OrCodeExcel;
import cn.atsoft.dasheng.orCode.model.params.OrCodeParam;
import cn.atsoft.dasheng.orCode.model.result.*;
import cn.atsoft.dasheng.orCode.model.result.InstockRequest;
import cn.atsoft.dasheng.orCode.model.result.SkuRequest;
import cn.atsoft.dasheng.orCode.model.result.StockRequest;
import cn.atsoft.dasheng.orCode.pojo.AutomaticBindResult;
import cn.atsoft.dasheng.orCode.pojo.BatchAutomatic;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.orCode.service.impl.OrCodeServiceImpl;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.convert.Convert;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import javafx.scene.control.IndexedCell;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 二维码控制器
 *
 * @author song
 * @Date 2021-10-29 10:23:27
 */
@RestController
@RequestMapping("/orCode")
@Api(tags = "二维码")
public class OrCodeController extends BaseController {

    @Autowired
    private OrCodeService orCodeService;
    @Autowired
    private OrCodeBindService orCodeBindService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private SpuService spuService;
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private StorehousePositionsService storehousePositionsService;
    @Autowired
    private StockService stockService;
    @Autowired
    private InstockOrderService instockOrderService;
    @Autowired
    private OutstockOrderService outstockOrderService;
    @Autowired
    private UserService userService;
    @Autowired
    private InstockListService instockListService;
    @Autowired
    private InstockService instockService;
    @Autowired
    private OutstockListingService outstockListingService;
    @Autowired
    private OutstockService outstockService;
    @Autowired
    private QualityTaskService qualityTaskService;
    @Autowired
    private InkindService inkindService;
    @Autowired
    private QualityTaskDetailService detailService;


    /**
     * 批量增加二维码
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/batchAdd", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData batchAdd(@RequestBody OrCodeParam orCodeParam) {
        this.orCodeService.batchAdd(orCodeParam);
        return ResponseData.success();
    }


    /**
     * 批量自动生成二维码绑定
     */
    @RequestMapping(value = "/batchAutomaticBinding", method = RequestMethod.POST)
    public ResponseData batchAutomaticBinding(@Valid @RequestBody BatchAutomatic batchAutomatic) {
        List<AutomaticBindResult> bindResults = this.orCodeService.batchAutomaticBinding(batchAutomatic);
        return ResponseData.success(bindResults);
    }

    /**
     * 扫码入库
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/instockByCode", method = RequestMethod.POST)
    @ApiOperation("扫码入库")
    public ResponseData instockByCode(@RequestBody InKindRequest inKindRequest) {
        if (ToolUtil.isEmpty(inKindRequest.getCodeId())) {
            throw new ServiceException(500, "请扫描二维码");
        }
        Long number = orCodeService.instockByCode(inKindRequest);
        return ResponseData.success(number);

    }

    /**
     * 批量扫码入库
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/batchInstockByCode", method = RequestMethod.POST)
    @ApiOperation("批量扫码入库")
    public ResponseData batchInstockByCode(@RequestBody InKindRequest inKindRequest) {
        if (ToolUtil.isEmpty(inKindRequest.getCodeIds())) {
            throw new ServiceException(500, "请扫描二维码");
        }
        orCodeService.batchInstockByCode(inKindRequest);
        return ResponseData.success();
    }


    /**
     * 批量扫码出库
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/batchOutStockByCode", method = RequestMethod.POST)
    @ApiOperation("批量扫码入库")
    public ResponseData batchOutStockByCode(@RequestBody InKindRequest inKindRequest) {
        if (ToolUtil.isEmpty(inKindRequest.getBatchOutStockParams())) {
            throw new ServiceException(500, "请扫描二维码");
        }
        orCodeService.batchOutStockByCode(inKindRequest);
        return ResponseData.success();
    }


    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<OrCodeResult> list(@RequestBody(required = false) OrCodeParam orCodeParam) {
        if (ToolUtil.isEmpty(orCodeParam)) {
            orCodeParam = new OrCodeParam();
        }
        return this.orCodeService.findPageBySpec(orCodeParam);
    }


    /**
     * 返回二维码
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/backCode", method = RequestMethod.POST)
    @ApiOperation("二维码")
    @Transactional
    public ResponseData backCode(@RequestBody BackCodeRequest backCodeRequest) {
        Long aLong = orCodeService.backCode(backCodeRequest);
        return ResponseData.success(aLong);
    }


    /**
     * 返回批量二维码
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/batchBackCode", method = RequestMethod.POST)
    @Transactional
    public ResponseData batchBackCode(@RequestBody OrCodeRequest codeRequest) {
        List<Long> longs = orCodeService.backBatchCode(codeRequest.getIds(), codeRequest.getType());
        return ResponseData.success(longs);
    }


    /**
     * 通过二维码返回真是数据
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/backObject", method = RequestMethod.GET)
    @Transactional
    public ResponseData backObject(@Param("id") Long id) {
        if (ToolUtil.isEmpty(id)) {
            throw new ServiceException(500, "未提交参数");
        }
        OrCodeBind codeBind = orCodeBindService.query().eq("qr_code_id", id).one();
        if (ToolUtil.isEmpty(codeBind)) {
            OrCode orCode = orCodeService.getById(id);
            if (ToolUtil.isNotEmpty(orCode))
                return null;
            else
                throw new ServiceException(500, "当前二维码不合法");
        } else {
            String source = codeBind.getSource();
            switch (source) {
                case "sku":
                    Sku sku = skuService.query().eq("sku_id", codeBind.getFormId()).one();
                    if (ToolUtil.isEmpty(sku)) {
                        throw new ServiceException(500, "当前物料不存在");
                    }
                    List<Long> skuIds = new ArrayList<>();
                    skuIds.add(sku.getSkuId());
                    List<SkuResult> skuResultListAndFormat = skuService.formatSkuResult(skuIds);
                    SkuRequest skuRequest = new SkuRequest();
                    skuRequest.setType("sku");
                    skuRequest.setResult(skuResultListAndFormat.size() > 0 ? skuResultListAndFormat.get(0) : null);
                    return ResponseData.success(skuRequest);
                case "spu":
                    Spu spu = spuService.query().eq("spu_id", codeBind.getFormId()).one();
                    if (ToolUtil.isEmpty(spu)) {
                        throw new ServiceException(500, "当前物料不存在");
                    }
                    SpuResult spuResult = new SpuResult();
                    ToolUtil.copyProperties(spu, spuResult);
                    try {
                        orCodeService.spuFormat(spuResult);
                    } catch (Exception e) {
                    }
                    SpuRequest spuRequest = new SpuRequest();
                    spuRequest.setType("spu");
                    spuRequest.setResult(spuResult);
                    return ResponseData.success(spuRequest);

                case "storehouse":
                    Storehouse storehouse = storehouseService.query().eq("storehouse_id", codeBind.getFormId()).one();
                    if (ToolUtil.isEmpty(storehouse)) {
                        throw new ServiceException(500, "当前仓库不存在");
                    }
                    StorehouseResult storehouseResult = new StorehouseResult();
                    ToolUtil.copyProperties(storehouse, storehouseResult);
                    try {
                        orCodeService.storehouseFormat(storehouseResult);
                    } catch (Exception e) {
                    }
                    StoreHouseRequest storeHouseRequest = new StoreHouseRequest();
                    storeHouseRequest.setType("storehouse");
                    storeHouseRequest.setResult(storehouseResult);
                    return ResponseData.success(storeHouseRequest);

                case "storehousePositions":
                    StorehousePositions storehousePositions = storehousePositionsService.query().in("storehouse_positions_id", codeBind.getFormId()).one();
                    if (ToolUtil.isEmpty(storehousePositions)) {
                        throw new ServiceException(500, "当前库位不存在");
                    }
                    StorehousePositionsResult storehousePositionsResult = new StorehousePositionsResult();
                    ToolUtil.copyProperties(storehousePositions, storehousePositionsResult);
                    try {
                        orCodeService.storehousePositionsFormat(storehousePositionsResult);
                    } catch (Exception e) {
                    }
                    StoreHousePositionsRequest storeHousePositionsRequest = new StoreHousePositionsRequest();
                    storeHousePositionsRequest.setType("storehousePositions");
                    storeHousePositionsRequest.setResult(storehousePositionsResult);
                    return ResponseData.success(storeHousePositionsRequest);

                case "stock":
                    Stock stock = stockService.query().eq("stock_id", codeBind.getFormId()).one();
                    if (ToolUtil.isEmpty(stock)) {
                        throw new ServiceException(500, "当前库存不存在");
                    }
                    StockResult stockResult = new StockResult();
                    ToolUtil.copyProperties(stock, stockResult);
                    try {
                        orCodeService.stockFormat(stockResult);
                    } catch (Exception e) {
                    }
                    StockRequest stockRequest = new StockRequest();
                    stockRequest.setType("storehouse");
                    stockRequest.setResult(stockResult);
                    return ResponseData.success(stockRequest);

                case "instock":
                    InstockOrder instockOrder = instockOrderService.query().eq("instock_order_id", codeBind.getFormId()).one();
                    if (ToolUtil.isEmpty(instockOrder)) {
                        throw new ServiceException(500, "当前数据不存在");
                    }
                    InstockOrderResult instockOrderResult = new InstockOrderResult();
                    ToolUtil.copyProperties(instockOrder, instockOrderResult);
                    Storehouse storehouseDetail = storehouseService.getById(instockOrder.getStoreHouseId());
                    if (ToolUtil.isNotEmpty(storehouseDetail)) {
                        StorehouseResult storehouseResult1 = new StorehouseResult();
                        ToolUtil.copyProperties(storehouseDetail, storehouseResult1);
                        instockOrderResult.setStorehouseResult(storehouseResult1);
                    }
                    User user = userService.getById(instockOrder.getUserId());
                    if (ToolUtil.isNotEmpty(user)) {
                        UserResult userResult = new UserResult();
                        ToolUtil.copyProperties(user, userResult);
                        instockOrderResult.setUserResult(userResult);
                    }
                    InstockListParam instockListParam = new InstockListParam();
                    instockListParam.setInstockOrderId(instockOrder.getInstockOrderId());
                    PageInfo<InstockListResult> instockListResultPageInfo = instockListService.findPageBySpec(instockListParam);
                    List<InstockListResult> instockListResults = instockListResultPageInfo.getData();

                    instockOrderResult.setInstockListResults(instockListResults);

                    InstockParam instockParam = new InstockParam();
                    instockParam.setInstockOrderId(instockOrder.getInstockOrderId());
                    PageInfo<InstockResult> instockResultPageInfo = instockService.findPageBySpec(instockParam, null);
                    List<InstockResult> instockResults = instockResultPageInfo.getData();

                    instockOrderResult.setInstockResults(instockResults);

                    InstockRequest instockRequest = new InstockRequest();
                    instockRequest.setType("instock");
                    instockRequest.setResult(instockOrderResult);
                    return ResponseData.success(instockRequest);

                case "outstock":
                    OutstockOrder outstockOrder = outstockOrderService.query().eq("outstock_order_id", codeBind.getFormId()).one();
                    if (ToolUtil.isEmpty(outstockOrder)) {
                        throw new ServiceException(500, "当前数据不存在");
                    }
                    OutstockOrderResult outstockResult = new OutstockOrderResult();
                    ToolUtil.copyProperties(outstockOrder, outstockResult);

                    Storehouse outStockStorehouse = storehouseService.getById(outstockResult.getStorehouseId());
                    if (ToolUtil.isNotEmpty(outStockStorehouse)) {
                        StorehouseResult storehouseResult1 = new StorehouseResult();
                        ToolUtil.copyProperties(outStockStorehouse, storehouseResult1);
                        outstockResult.setStorehouseResult(storehouseResult1);
                    }
                    User outStockUser = userService.getById(outstockOrder.getUserId());
                    if (ToolUtil.isNotEmpty(outStockUser)) {
                        UserResult userResult = new UserResult();
                        ToolUtil.copyProperties(outStockUser, userResult);
                        outstockResult.setUserResult(userResult);
                    }

                    List<OutstockListingResult> details = outstockListingService.getDetailsByOrderId(outstockOrder.getOutstockOrderId());
                    outstockResult.setOutstockListing(details);

                    OutstockParam outstockParam = new OutstockParam();
                    outstockParam.setOutstockOrderId(outstockOrder.getOutstockOrderId());
                    PageInfo<OutstockResult> outstockResultPageInfo = outstockService.findPageBySpec(outstockParam, null);
                    outstockResult.setOutstockResults(outstockResultPageInfo.getData());

                    OutStockOrderRequest outStockOrderRequest = new OutStockOrderRequest();
                    outStockOrderRequest.setType("outstock");
                    outStockOrderRequest.setResult(outstockResult);
                    return ResponseData.success(outStockOrderRequest);
                case "quality":
                    QualityTask qualityTask = qualityTaskService.query().eq("quality_task_id", codeBind.getFormId()).one();
                    if (ToolUtil.isEmpty(qualityTask)) {
                        throw new ServiceException(500, "当前数据不存在");
                    }
                    QualityTaskResult qualityTaskResult = new QualityTaskResult();
                    ToolUtil.copyProperties(qualityTask, qualityTaskResult);
                    qualityTaskService.detailFormat(qualityTaskResult);

                    if (ToolUtil.isNotEmpty(qualityTaskResult.getUserId())) {
                        User user1 = userService.getById(qualityTaskResult.getUserId());
                        qualityTaskResult.setUserName(user1.getName());
                    }

                    QualityRequest qualityRequest = new QualityRequest();
                    qualityRequest.setType("quality");
                    qualityRequest.setResult(qualityTaskResult);
                    return ResponseData.success(qualityRequest);
                case "item":
                    InkindResult inkindResult = inkindService.getInkindResult(codeBind.getFormId());
                    InkindBack inkindBack = new InkindBack();
                    switch (inkindResult.getSource()) {
                        case "质检":
                            QualityTaskDetail detail = detailService.getById(inkindResult.getSourceId());
                            inkindResult.setTaskDetail(detail);
                            break;
                        case "入库":
                        case "自由入库":
                        case "盘点入库":
                            Map<String, Object> inkindDetail = orCodeService.inkindDetail(inkindResult.getInkindId());
                            inkindResult.setInkindDetail(inkindDetail);
                            break;
                    }
                    inkindBack.setInkindResult(inkindResult);
                    inkindBack.setType("item");
                    return ResponseData.success(inkindBack);
            }
        }
        return ResponseData.success();
    }


    /**
     * 判断是否绑定
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/inkindDetail", method = RequestMethod.GET)
    @ApiOperation("判断是否绑定")
    public ResponseData inkindDetail(@RequestParam Long id) {
        Long formId = orCodeBindService.getFormId(id);
        return ResponseData.success(orCodeService.inkindDetail(formId));
    }

    /**
     * 判断是否绑定
     *
     * @param inKindRequest
     * @return
     */
    @RequestMapping(value = "/isNotBind", method = RequestMethod.POST)
    @ApiOperation("判断是否绑定")
    public ResponseData isNotBind(@RequestBody InKindRequest inKindRequest) {
        Boolean t = orCodeService.isNotBind(inKindRequest);
        return ResponseData.success(t);
    }


    /**
     * 判断绑定合法性
     *
     * @param inKindRequest
     * @return
     */
    @RequestMapping(value = "/judgeBind", method = RequestMethod.POST)
    @ApiOperation("判断是否绑定")
    public ResponseData judgeBind(@RequestBody InKindRequest inKindRequest) {
        Inkind inkind = orCodeService.judgeBind(inKindRequest);
        return ResponseData.success(inkind);
    }


    /**
     * 判断出库
     *
     * @param inKindRequest
     * @return
     */
    @RequestMapping(value = "/backInkindByCode", method = RequestMethod.POST)
    public ResponseData backInkindByCode(@RequestBody InKindRequest inKindRequest) {
        Object o = orCodeService.backInkindByCode(inKindRequest);
        return ResponseData.success(o);
    }

    /**
     * 扫码出库
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/outStockByCode", method = RequestMethod.POST)
    @Transactional
    public ResponseData outStockByCode(@RequestBody InKindRequest inKindRequest) {
        Long aLong = orCodeService.outStockByCode(inKindRequest);
        return ResponseData.success(aLong);
    }

    /**
     * 自动生成并绑定
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/automaticBinding", method = RequestMethod.POST)
    @Transactional
    public ResponseData automaticBinding(@RequestBody BackCodeRequest codeRequest) {
        AutomaticBindResult automaticBindResult = orCodeService.automaticBinding(codeRequest);
        return ResponseData.success(automaticBindResult);
    }
}


