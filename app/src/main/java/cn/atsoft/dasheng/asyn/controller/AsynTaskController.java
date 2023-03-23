package cn.atsoft.dasheng.asyn.controller;

import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import cn.atsoft.dasheng.app.pojo.AllBomResult;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.asyn.entity.AsynTask;
import cn.atsoft.dasheng.asyn.model.params.AsynTaskParam;
import cn.atsoft.dasheng.asyn.model.result.AsynTaskResult;
import cn.atsoft.dasheng.asyn.service.AsynTaskDetailService;
import cn.atsoft.dasheng.asyn.service.AsynTaskService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;


/**
 * 等待任务表控制器
 *
 * @author song
 * @Date 2022-04-01 10:57:49
 */
@RestController
@RequestMapping("/asynTask")
@Api(tags = "等待任务表")
public class AsynTaskController extends BaseController {

    @Autowired
    private AsynTaskService asynTaskService;
    @Autowired
    private AsynTaskDetailService detailService;
    @Autowired
    private ErpPartsDetailService partsDetailService;


    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-04-01
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody AsynTaskParam asynTaskParam) {
        AsynTask detail = this.asynTaskService.getById(asynTaskParam.getTaskId());
        AsynTaskResult result = new AsynTaskResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
        switch (result.getType()) {
            case "物料分析":
                AllBomResult allBomResult = JSON.parseObject(result.getContent(), AllBomResult.class);
                result.setAllBomResult(allBomResult);
                break;
            case "物料导入":
            case "库存导入":
            case "库位导入":
                Map<String, Integer> num = detailService.getNum(result.getTaskId());
                result.setSuccessNum(num.get("successNum"));
                result.setErrorNum(num.get("errorNum"));
                break;
        }
        result.setContent(null);
        return ResponseData.success(result);
    }



    
    /**
     * 物料分析详情明细
     *
     * @return
     */
    @RequestMapping(value = "/BomDetailed", method = RequestMethod.POST)
    public ResponseData BomDetailed() {
        Object spectaculars = asynTaskService.BomDetailed();
        return ResponseData.success(spectaculars);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-04-01
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<AsynTaskResult> list(@RequestBody(required = false) AsynTaskParam asynTaskParam) {
        if (ToolUtil.isEmpty(asynTaskParam)) {
            asynTaskParam = new AsynTaskParam();
        }
        return this.asynTaskService.findPageBySpec(asynTaskParam);
    }

    /**
     * 物料分析轮播
     *
     * @return
     */
    @RequestMapping(value = "/spectaculars", method = RequestMethod.POST)
    public ResponseData spectaculars() {
        Object spectaculars = asynTaskService.spectaculars();
        return ResponseData.success(spectaculars);
    }

    /**
     * 库存轮播
     *
     * @return
     */
    @RequestMapping(value = "/stockSpectaculars", method = RequestMethod.POST)
    public ResponseData stockSpectaculars() {
        Object spectaculars = asynTaskService.stockSpectaculars();
        return ResponseData.success(spectaculars);
    }

    /**
     * 生产当前bom 所需的物料
     *
     * @param skuId
     * @return
     */
    @RequestMapping(value = "/bomResult", method = RequestMethod.GET)
    public ResponseData bomResult(@RequestParam("skuId") Long skuId) {

        List<ErpPartsDetail> details = asynTaskService.bomResult(skuId, 1);
        List<ErpPartsDetailResult> detailResults = BeanUtil.copyToList(details, ErpPartsDetailResult.class);
        partsDetailService.format(detailResults);
        return ResponseData.success(detailResults);
    }

}


