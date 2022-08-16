package cn.atsoft.dasheng.portal.repairDynamic.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.repairDynamic.entity.RepairDynamic;
import cn.atsoft.dasheng.portal.repairDynamic.model.params.RepairDynamicParam;
import cn.atsoft.dasheng.portal.repairDynamic.model.result.RepairDynamicResult;
import cn.atsoft.dasheng.portal.repairDynamic.service.RepairDynamicService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 售后动态表控制器
 *
 * @author 
 * @Date 2021-08-24 08:51:32
 */
@RestController
@RequestMapping("/repairDynamic")
@Api(tags = "售后动态表")
public class RepairDynamicController extends BaseController {

    @Autowired
    private RepairDynamicService repairDynamicService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RepairDynamicParam repairDynamicParam) {
        this.repairDynamicService.add(repairDynamicParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody RepairDynamicParam repairDynamicParam) {

        this.repairDynamicService.update(repairDynamicParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody RepairDynamicParam repairDynamicParam)  {
        this.repairDynamicService.delete(repairDynamicParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody RepairDynamicParam repairDynamicParam) {
        RepairDynamic detail = this.repairDynamicService.getById(repairDynamicParam.getDynamicId());
        RepairDynamicResult result = new RepairDynamicResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<RepairDynamicResult> list(@RequestBody(required = false) RepairDynamicParam repairDynamicParam) {
        if(ToolUtil.isEmpty(repairDynamicParam)){
            repairDynamicParam = new RepairDynamicParam();
        }
        return this.repairDynamicService.findPageBySpec(repairDynamicParam);
    }




}


