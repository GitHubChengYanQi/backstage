package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InventoryDetail;
import cn.atsoft.dasheng.erp.model.params.InventoryDetailParam;
import cn.atsoft.dasheng.erp.model.result.InventoryDetailResult;
import cn.atsoft.dasheng.erp.service.InventoryDetailService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;


/**
 * 盘点任务详情控制器
 *
 * @author Captain_Jazz
 * @Date 2021-12-27 09:27:27
 */
@RestController
@RequestMapping("/inventoryDetail")
@Api(tags = "盘点任务详情")
public class InventoryDetailController extends BaseController {

    @Autowired
    private InventoryDetailService inventoryDetailService;

//    /**
//     * 新增接口
//     *
//     * @author Captain_Jazz
//     * @Date 2021-12-27
//     */
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    @ApiOperation("新增")
//    public ResponseData addItem(@RequestBody InventoryDetailParam inventoryDetailParam) {
//        this.inventoryDetailService.add(inventoryDetailParam);
//        return ResponseData.success();
//    }


    /**
     * 盘点入库
     *
     * @author
     * @Date 2021-12-27
     */
    @RequestMapping(value = "/inventoryInstock", method = RequestMethod.POST)
    @ApiOperation("盘点入库")
    public ResponseData inventoryInstock(@RequestBody InventoryDetailParam inventoryDetailParam) {
        this.inventoryDetailService.inventoryInstock(inventoryDetailParam);
        return ResponseData.success();
    }

    @RequestMapping(value = "/taskList", method = RequestMethod.POST)
    public ResponseData taskList(@RequestBody InventoryDetailParam inventoryDetailParam) {
        Object taskList = this.inventoryDetailService.taskList(inventoryDetailParam.getInventoryId());
        return ResponseData.success(taskList);
    }

    @RequestMapping(value = "/mergeList", method = RequestMethod.POST)
    public ResponseData mergeList() {
        Object mergeList = this.inventoryDetailService.mergeList();
        return ResponseData.success(mergeList);
    }


    @RequestMapping(value = "/addPhoto", method = RequestMethod.POST)
    public ResponseData addPhoto(@RequestBody InventoryDetailParam inventoryDetailParam) {
        this.inventoryDetailService.addPhoto(inventoryDetailParam);
        return ResponseData.success();
    }


    @RequestMapping(value = "/temporaryLock", method = RequestMethod.POST)
    public ResponseData temporaryLock(@RequestBody InventoryDetailParam inventoryDetailParam) {
        this.inventoryDetailService.temporaryLock(inventoryDetailParam);
        return ResponseData.success();
    }

    /**
     * 所有盘点任务合并
     *
     * @return
     */
    @RequestMapping(value = "/mergeDetail", method = RequestMethod.GET)
    public ResponseData mergeDetail() {
        Object detail = this.inventoryDetailService.mergeDetail();
        return ResponseData.success(detail);
    }


    /**
     * 盘点完成
     *
     * @param inventoryDetailParam
     * @return
     */
    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    public ResponseData complete(@RequestBody InventoryDetailParam inventoryDetailParam) {
        if (ToolUtil.isEmpty(inventoryDetailParam.getInventoryId())) {
            throw new ServiceException(500, "请确定盘点id");
        }
        this.inventoryDetailService.complete(inventoryDetailParam.getInventoryId());
        return ResponseData.success();
    }
//    /**
//     * 编辑接口
//     *
//     * @author Captain_Jazz
//     * @Date 2021-12-27
//     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody InventoryDetailParam inventoryDetailParam) {
//
//        this.inventoryDetailService.update(inventoryDetailParam);
//        return ResponseData.success();
//    }

//    /**
//     * 删除接口
//     *
//     * @author Captain_Jazz
//     * @Date 2021-12-27
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody InventoryDetailParam inventoryDetailParam) {
//        this.inventoryDetailService.delete(inventoryDetailParam);
//        return ResponseData.success();
//    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<InventoryDetailResult> detail(@RequestBody InventoryDetailParam inventoryDetailParam) {
        InventoryDetail detail = this.inventoryDetailService.getById(inventoryDetailParam.getDetailId());
        InventoryDetailResult result = new InventoryDetailResult();
        ToolUtil.copyProperties(detail, result);
        return ResponseData.success(result);
    }


    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<InventoryDetailResult> list(@RequestBody(required = false) InventoryDetailParam inventoryDetailParam) {
        if (ToolUtil.isEmpty(inventoryDetailParam)) {
            inventoryDetailParam = new InventoryDetailParam();
        }
        return this.inventoryDetailService.findPageBySpec(inventoryDetailParam);
    }


}


