package cn.atsoft.dasheng.orCode.controller;

import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.model.result.CategoryResult;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.params.OrCodeBindParam;
import cn.atsoft.dasheng.orCode.model.params.OrCodeParam;
import cn.atsoft.dasheng.orCode.model.result.*;
import cn.atsoft.dasheng.orCode.model.result.InstockRequest;
import cn.atsoft.dasheng.orCode.model.result.StockRequest;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
    private SpuClassificationService spuClassificationService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private StockService stockService;
    @Autowired
    private InstockService instockService;
    @Autowired
    private OutstockService outstockService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private CategoryService categoryService;

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
                    Instock instock = instockService.query().eq("instock_id", codeBind.getFormId()).one();
                    if (ToolUtil.isEmpty(instock)) {
                        throw new ServiceException(500, "当前数据不存在");
                    }
                    InstockResult instockResult = new InstockResult();
                    ToolUtil.copyProperties(instock, instockResult);
                    InstockRequest instockRequest = new InstockRequest();
                    instockRequest.setType("instock");
                    instockRequest.setResult(instockResult);
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

}


