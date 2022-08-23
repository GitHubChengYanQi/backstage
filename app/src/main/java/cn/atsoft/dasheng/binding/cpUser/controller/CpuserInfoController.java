package cn.atsoft.dasheng.binding.cpUser.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.binding.cpUser.entity.CpuserInfo;
import cn.atsoft.dasheng.binding.cpUser.model.params.CpuserInfoParam;
import cn.atsoft.dasheng.binding.cpUser.model.result.CpuserInfoResult;
import cn.atsoft.dasheng.binding.cpUser.service.CpuserInfoService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.rest.service.RestUserService;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 控制器
 *
 * @author 
 * @Date 2021-10-12 15:54:41
 */
@RestController
@RequestMapping("/cpuserInfo")
@Api(tags = "")
public class CpuserInfoController extends BaseController {

    @Autowired
    private CpuserInfoService cpuserInfoService;
    @Autowired
    private StepsService appStepService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-10-12
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CpuserInfoParam cpuserInfoParam) {
        this.cpuserInfoService.add(cpuserInfoParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-10-12
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody CpuserInfoParam cpuserInfoParam) {

        this.cpuserInfoService.update(cpuserInfoParam);
        return ResponseData.success();
    }


    @RequestMapping(value = "/backHeadPortrait", method = RequestMethod.GET)
    public ResponseData backHeadPortrait() {
        Long userId = LoginContextHolder.getContext().getUserId();
        String imgUrl = appStepService.imgUrl(userId.toString());
        return ResponseData.success(imgUrl);
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-10-12
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CpuserInfoParam cpuserInfoParam)  {
        this.cpuserInfoService.delete(cpuserInfoParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-10-12
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody CpuserInfoParam cpuserInfoParam) {
        CpuserInfo detail = this.cpuserInfoService.getById(cpuserInfoParam.getCpUserId());
        CpuserInfoResult result = new CpuserInfoResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-10-12
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<CpuserInfoResult> list(@RequestBody(required = false) CpuserInfoParam cpuserInfoParam) {
        if(ToolUtil.isEmpty(cpuserInfoParam)){
            cpuserInfoParam = new CpuserInfoParam();
        }
        return this.cpuserInfoService.findPageBySpec(cpuserInfoParam);
    }




}


