package cn.atsoft.dasheng.orCode.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.InstockParam;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.InstockListParam;
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
    private OutstockService outstockService;
    @Autowired
    private UserService userService;
    @Autowired
    private InstockListService instockListService;
    @Autowired
    private InstockService instockService;

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
        Boolean aBoolean = orCodeService.instockByCode(inKindRequest);
        return ResponseData.success(aBoolean);

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


    @RequestMapping(value = "/qrCodetoExcel", method = RequestMethod.POST)
    @ApiOperation("导出")
    public void qrCodetoExcel(HttpServletResponse response, Long type, String url) throws IOException {
        String title = "二维码导出表单";
        String[] header = {"序号", "Id", "地址","请扫描"};


        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("二维码导出");
        sheet.setDefaultColumnWidth(40);
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 3);
        sheet.addMergedRegion(region);
//        sheet.setColumnWidth(0, 10);
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell ti = titleRow.createCell(0);
        ti.setCellValue(title);
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        ti.setCellStyle(titleStyle);



        HSSFRow headrow = sheet.createRow(1);

        for (int i = 0; i < header.length; i++) {
            //创建一个单元格
            HSSFCell cell = headrow.createCell(i);

            //创建一个内容对象
            HSSFRichTextString text = new HSSFRichTextString(header[i]);


            HSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            //将内容对象的文字内容写入到单元格中
            cell.setCellValue(text);
            cell.setCellStyle(headerStyle);
        }


        List<Map<String, String>> list = new ArrayList<>();
        //所有二维码
        if (type == 0) {
            List<OrCode> orCodes = orCodeService.query().list();
            for (OrCode orCode : orCodes) {
                Map<String, String> codeMap = new HashMap<>();
                Long orCodeId = orCode.getOrCodeId();
                String replace = url.replace("codeId", orCode.getOrCodeId().toString());
                codeMap.put("id", orCodeId.toString());
                codeMap.put("url", replace);
                list.add(codeMap);
            }
        }
        //未绑定的二维码
        if ((type == 1)) {
            List<OrCodeBind> codeBinds = orCodeBindService.query().list();
            List<Long> ids = new ArrayList<>();
            for (OrCodeBind codeBind : codeBinds) {
                ids.add(codeBind.getOrCodeId());
            }
            List<OrCode> codes = orCodeService.query().notIn("qr_code_id", ids).list();
            for (OrCode code : codes) {
                Map<String, String> codeMap = new HashMap<>();
                Long orCodeId = code.getOrCodeId();
                String replace = url.replace("codeId", orCodeId.toString());
                codeMap.put("id", orCodeId.toString());
                codeMap.put("url", replace);
                list.add(codeMap);
            }
        }
        //绑定的二维码
        if (type == 2) {
            List<OrCodeBind> binds = orCodeBindService.list();
            for (OrCodeBind bind : binds) {
                Map<String, String> codeMap = new HashMap<>();
                Long orCodeId = bind.getOrCodeId();
                String replace = url.replace("codeId", orCodeId.toString());
                codeMap.put("id", orCodeId.toString());
                codeMap.put("url", replace);
                list.add(codeMap);
            }
        }

        int i = 2;
        for (Map<String, String> longStringMap : list) {

            HSSFRow row1 = sheet.createRow(i);
            HSSFCell id = row1.createCell(1);
            HSSFCell url1 = row1.createCell(2);
            HSSFRichTextString idValue = new HSSFRichTextString(longStringMap.get("id"));
            HSSFRichTextString urlValue = new HSSFRichTextString(longStringMap.get("url"));
            id.setCellValue(idValue);
            url1.setCellValue(urlValue);
            i++;

        }


//        int i =1;
//        for (Long orCode : codeIds) {
//            if (ToolUtil.isEmpty(orCode)) {
//                HSSFRow row1 = sheet.createRow(i);
//                HSSFCell id = row1.createCell(1);
//                HSSFCell url = row1.createCell(2);
//                HSSFRichTextString text = new HSSFRichTextString(orCode.getOrCodeId().toString());
//                id.setCellValue(text);
//                url.setCellValue("www.baidu.com");
//                i++;
//            }
//        }

        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");

        //这后面可以设置导出Excel的名称
        response.setHeader("Content-disposition", "attachment;filename=qrCode.xls");

        //刷新缓冲
        response.flushBuffer();

        //workbook将Excel写入到response的输出流中，供页面下载
        workbook.write(response.getOutputStream());
//        System.out.println(workbook.write(response.getOutputStream()));
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
                    Outstock outstock = outstockService.query().eq("outstock_id", codeBind.getFormId()).one();
                    if (ToolUtil.isEmpty(outstock)) {
                        throw new ServiceException(500, "当前数据不存在");
                    }
                    OutstockResult outstockResult = new OutstockResult();
                    ToolUtil.copyProperties(outstock, outstockResult);
                    OutStockRequest outStockRequest = new OutStockRequest();
                    outStockRequest.setType("outstock");
                    outStockRequest.setResult(outstockResult);
                    return ResponseData.success(outStockRequest);
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
     * 二维码导出
     *
     * @return
     */
    @RequestMapping(value = "/codeingExcelExport", method = RequestMethod.POST)
    public void codeingExcelExport(@RequestBody OrCodeParam orCodeParam) {
        Map<Long, String> codeMap = new HashMap<>();
        List<Long> codeIds = new ArrayList<>();
        //所有二维码
        if (orCodeParam.getCodeType() == 0) {
            List<OrCode> orCodes = orCodeService.query().list();
            for (OrCode orCode : orCodes) {
                Long orCodeId = orCode.getOrCodeId();
                String replace = orCodeParam.getUrl().replace("codeId", orCode.getOrCodeId().toString());
                codeIds.add(orCodeId);
                codeMap.put(orCodeId, replace);
            }
        }
        //未绑定的二维码
        if (orCodeParam.getCodeType() == 1) {
            List<OrCodeBind> codeBinds = orCodeBindService.query().list();
            List<Long> ids = new ArrayList<>();
            for (OrCodeBind codeBind : codeBinds) {
                ids.add(codeBind.getOrCodeId());
            }
            List<OrCode> codes = orCodeService.query().notIn("qr_code_id", ids).list();
            for (OrCode code : codes) {
                Long orCodeId = code.getOrCodeId();
                codeIds.add(orCodeId);
                String replace = orCodeParam.getUrl().replace("codeId", orCodeId.toString());
                codeMap.put(orCodeId, replace);
            }
        }
        //绑定的二维码
        if (orCodeParam.getCodeType() == 2) {
            List<OrCodeBind> binds = orCodeBindService.list();
            for (OrCodeBind bind : binds) {
                Long orCodeId = bind.getOrCodeId();
                codeIds.add(orCodeId);
                String replace = orCodeParam.getUrl().replace("codeId", orCodeId.toString());
                codeMap.put(orCodeId, replace);
            }
        }
        List<OrCodeExcel> orCodeExcels = new ArrayList<>();
        for (Long codeId : codeIds) {
            String s = codeMap.get(codeId);
            OrCodeExcel orCodeExcel = new OrCodeExcel();
            orCodeExcel.setCode(codeId);
            orCodeExcel.setUrl(s);
            orCodeExcels.add(orCodeExcel);
        }
        ExportParams exportParams = new ExportParams();
        exportParams.setTitle("二维码");
        exportParams.setSecondTitle("路径");


        Workbook sheets = ExcelExportUtil.exportExcel(exportParams, OrCodeExcel.class, orCodeExcels);

    }
}


