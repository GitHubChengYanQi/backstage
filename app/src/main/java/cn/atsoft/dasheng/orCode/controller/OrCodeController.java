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
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.params.OrCodeBindParam;
import cn.atsoft.dasheng.orCode.model.params.OrCodeExcel;
import cn.atsoft.dasheng.orCode.model.params.OrCodeParam;
import cn.atsoft.dasheng.orCode.model.result.*;
import cn.atsoft.dasheng.orCode.model.result.InstockRequest;
import cn.atsoft.dasheng.orCode.model.result.StockRequest;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.convert.Convert;
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

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody OrCodeParam orCodeParam) {
        this.orCodeService.add(orCodeParam);
        return ResponseData.success();
    }

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
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody OrCodeParam orCodeParam) {

        this.orCodeService.update(orCodeParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody OrCodeParam orCodeParam) {
        this.orCodeService.delete(orCodeParam);
        return ResponseData.success();
    }


    /**
     * 返回二维码
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/instockByCode", method = RequestMethod.POST)
    @ApiOperation("二维码")
    @Transactional
    public ResponseData instockByCode(@RequestBody InKindRequest inKindRequest) {
        Long number = orCodeService.instockByCode(inKindRequest);
        return ResponseData.success(number);

    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<OrCodeResult> detail(@RequestBody OrCodeParam orCodeParam) {
        OrCode detail = this.orCodeService.getById(orCodeParam.getOrCodeId());
        OrCodeResult result = new OrCodeResult();
        ToolUtil.copyProperties(detail, result);
        return ResponseData.success(result);
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
    public ResponseData backObject(@RequestParam Long id) {
        OrCodeBind codeBind = orCodeBindService.query().in("qr_code_id", id).one();
        if (ToolUtil.isEmpty(codeBind)) {
            throw new ServiceException(500, "当前二维码不合法");
        } else {
            String source = codeBind.getSource();
            switch (source) {
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

                    OutstockListingParam outstockListingParams = new OutstockListingParam();
                    outstockListingParams.setOutstockOrderId(outstockOrder.getOutstockOrderId());
                    PageInfo<OutstockListingResult> listingResultPageInfo = outstockListingService.findPageBySpec(outstockListingParams);
                    outstockResult.setOutstockListing(listingResultPageInfo.getData());

                    OutstockParam outstockParam = new OutstockParam();
                    outstockParam.setOutstockOrderId(outstockOrder.getOutstockOrderId());
                    PageInfo<OutstockResult> outstockResultPageInfo = outstockService.findPageBySpec(outstockParam, null);
                    outstockResult.setOutstockResults(outstockResultPageInfo.getData());

                    OutStockOrderRequest outStockOrderRequest = new OutStockOrderRequest();
                    outStockOrderRequest.setType("outstock");
                    outStockOrderRequest.setResult(outstockResult);
                    return ResponseData.success(outStockOrderRequest);
            }
        }
        return ResponseData.success();
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
        Boolean t = orCodeService.judgeBind(inKindRequest);
        return ResponseData.success(t);
    }


    /**
     * 判断出库
     *
     * @param inKindRequest
     * @return
     */
    @RequestMapping(value = "/backInkindByCode", method = RequestMethod.POST)
    @ApiOperation("判断是否绑定")
    public ResponseData backInkindByCode(@RequestBody InKindRequest inKindRequest) {
        Object o = orCodeService.backInkindByCode(inKindRequest);
        return ResponseData.success(o);
    }

//    /**
//     * 二维码导出
//     *
//     * @return
//     */
//    @RequestMapping(value = "/codeingExcelExport", method = RequestMethod.POST)
//    public void codeingExcelExport(@RequestBody OrCodeParam orCodeParam) {
//        Map<Long, String> codeMap = new HashMap<>();
//        List<Long> codeIds = new ArrayList<>();
//        //所有二维码
//        if (orCodeParam.getCodeType() == 0) {
//            List<OrCode> orCodes = orCodeService.query().list();
//            for (OrCode orCode : orCodes) {
//                Long orCodeId = orCode.getOrCodeId();
//                String replace = orCodeParam.getUrl().replace("codeId", orCode.getOrCodeId().toString());
//                codeIds.add(orCodeId);
//                codeMap.put(orCodeId, replace);
//            }
//        }
//        //未绑定的二维码
//        if (orCodeParam.getCodeType() == 1) {
//            List<OrCodeBind> codeBinds = orCodeBindService.query().list();
//            List<Long> ids = new ArrayList<>();
//            for (OrCodeBind codeBind : codeBinds) {
//                ids.add(codeBind.getOrCodeId());
//            }
//            List<OrCode> codes = orCodeService.query().notIn("qr_code_id", ids).list();
//            for (OrCode code : codes) {
//                Long orCodeId = code.getOrCodeId();
//                codeIds.add(orCodeId);
//                String replace = orCodeParam.getUrl().replace("codeId", orCodeId.toString());
//                codeMap.put(orCodeId, replace);
//            }
//        }
//        //绑定的二维码
//        if (orCodeParam.getCodeType() == 2) {
//            List<OrCodeBind> binds = orCodeBindService.list();
//            for (OrCodeBind bind : binds) {
//                Long orCodeId = bind.getOrCodeId();
//                codeIds.add(orCodeId);
//                String replace = orCodeParam.getUrl().replace("codeId", orCodeId.toString());
//                codeMap.put(orCodeId, replace);
//            }
//        }
//        List<OrCodeExcel> orCodeExcels = new ArrayList<>();
//        for (Long codeId : codeIds) {
//            String s = codeMap.get(codeId);
//            OrCodeExcel orCodeExcel = new OrCodeExcel();
//            orCodeExcel.setCode(codeId);
//            orCodeExcel.setUrl(s);
//            orCodeExcels.add(orCodeExcel);
//        }
//        ExportParams exportParams = new ExportParams();
//        exportParams.setTitle("二维码");
//        exportParams.setSecondTitle("路径");
//
//
//        Workbook sheets = ExcelExportUtil.exportExcel(exportParams, OrCodeExcel.class, orCodeExcels);
//
//    }
}


