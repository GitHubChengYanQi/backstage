package cn.atsoft.dasheng.task.controller;

import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import cn.atsoft.dasheng.app.pojo.AllBomResult;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.task.entity.AsynTask;
import cn.atsoft.dasheng.task.model.params.AsynTaskParam;
import cn.atsoft.dasheng.task.model.result.AsynTaskResult;
import cn.atsoft.dasheng.task.service.AsynTaskDetailService;
import cn.atsoft.dasheng.task.service.AsynTaskService;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 等待任务表控制器
 *
 * @author song
 * @Date 2022-04-01 10:57:49
 */
@RestController
@RequestMapping("/asynTask/{v1}")
@Api(tags = "等待任务表")
public class AsynTaskVersionController extends BaseController {

    @Autowired
    private AsynTaskService asynTaskService;
    @Autowired
    private ErpPartsDetailService partsDetailService;


    /**
     * 物料分析详情明细
     *
     * @return
     */
    @ApiVersion("1.1")
    @RequestMapping(value = "/BomDetailed", method = RequestMethod.POST)
    public ResponseData BomDetailed() {
        Object spectaculars = asynTaskService.BomDetailedVersion();
        return ResponseData.success(spectaculars);
    }


    /**
     * 物料分析轮播
     *
     * @return
     */
    @ApiVersion("1.1")
    @RequestMapping(value = "/spectaculars", method = RequestMethod.POST)
    public ResponseData spectaculars() {
        Object spectaculars = asynTaskService.spectacularsVersion();
        return ResponseData.success(spectaculars);
    }


    /**
     * 生产当前bom 所需的物料
     *
     * @param skuId
     * @return
     */
    @ApiVersion("1.1")
    @RequestMapping(value = "/bomResult", method = RequestMethod.GET)
    public ResponseData bomResult(@RequestParam("skuId") Long skuId) {
        List<ErpPartsDetail> details = asynTaskService.bomResult(skuId, 1);
        List<ErpPartsDetailResult> detailResults = BeanUtil.copyToList(details, ErpPartsDetailResult.class);
        partsDetailService.format(detailResults);
        return ResponseData.success(detailResults);
    }

}


