package cn.atsoft.dasheng.generalForm.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.generalForm.entity.GeneralFormData;
import cn.atsoft.dasheng.generalForm.model.params.GeneralFormDataParam;
import cn.atsoft.dasheng.generalForm.model.result.GeneralFormDataResult;
import cn.atsoft.dasheng.generalForm.service.GeneralFormDataService;
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
 * 控制器
 *
 * @author Captain_Jazz
 * @Date 2022-09-08 09:50:07
 */
@RestController
@RequestMapping("/generalFormData")
@Api(tags = "")
public class GeneralFormDataController extends BaseController {

    @Autowired
    private GeneralFormDataService generalFormDataService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody GeneralFormDataParam generalFormDataParam) {
        this.generalFormDataService.add(generalFormDataParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody GeneralFormDataParam generalFormDataParam) {

        this.generalFormDataService.update(generalFormDataParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody GeneralFormDataParam generalFormDataParam)  {
        this.generalFormDataService.delete(generalFormDataParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<GeneralFormDataResult> detail(@RequestBody GeneralFormDataParam generalFormDataParam) {
        GeneralFormData detail = this.generalFormDataService.getById(generalFormDataParam.getId());
        GeneralFormDataResult result = new GeneralFormDataResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<GeneralFormDataResult> list(@RequestBody(required = false) GeneralFormDataParam generalFormDataParam) {
        if(ToolUtil.isEmpty(generalFormDataParam)){
            generalFormDataParam = new GeneralFormDataParam();
        }
        return this.generalFormDataService.findPageBySpec(generalFormDataParam,new DataScope(null, LoginContextHolder.getContext().getTenantId()));
    }




}


