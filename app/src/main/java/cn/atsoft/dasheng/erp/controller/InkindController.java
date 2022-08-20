package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.model.params.CodingRulesParam;
import cn.atsoft.dasheng.erp.model.params.InkindParam;
import cn.atsoft.dasheng.erp.model.result.InkindResult;
import cn.atsoft.dasheng.erp.service.InkindService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 实物表控制器
 *
 * @author song
 * @Date 2021-11-01 14:31:45
 */
@RestController
@RequestMapping("/inkind")
@Api(tags = "实物表")
public class InkindController extends BaseController {

    @Autowired
    private InkindService inkindService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-11-01
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody InkindParam inkindParam) {
        this.inkindService.add(inkindParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-11-01
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改实物", key = "name", dict = InkindParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody InkindParam inkindParam) {

        this.inkindService.update(inkindParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-11-01
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除实物", key = "name", dict = InkindParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody InkindParam inkindParam) {
        this.inkindService.delete(inkindParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-11-01
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody InkindParam inkindParam) {
        InkindResult result = this.inkindService.inkindDetail(inkindParam);
        return ResponseData.success(result);
    }


    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-11-01
     */
    @RequestMapping(value = "/details", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData details(@RequestBody InkindParam inkindParam) {
        List<InkindResult> inkindResults = this.inkindService.details(inkindParam);
        return ResponseData.success(inkindResults);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-11-01
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<InkindResult> list(@RequestBody(required = false) InkindParam inkindParam) {
        if (ToolUtil.isEmpty(inkindParam)) {
            inkindParam = new InkindParam();
        }
        return this.inkindService.findPageBySpec(inkindParam);
    }


}


