package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.RestGeneralFormData;
import cn.atsoft.dasheng.form.model.params.RestGeneralFormDataParam;
import cn.atsoft.dasheng.form.model.result.RestGeneralFormDataResult;
import cn.atsoft.dasheng.form.service.RestGeneralFormDataService;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 控制器
 *
 * @author Captain_Jazz
 * @Date 2022-09-08 09:50:07
 */
@RestController
@RequestMapping("/generalFormData/{version}")
@ApiVersion("2.0")
@Api(tags = "表单数据管理")
public class RestGeneralFormDataController extends BaseController {

    @Autowired
    private RestGeneralFormDataService generalFormDataService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestGeneralFormDataParam generalFormDataParam) {
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
    public ResponseData update(@RequestBody RestGeneralFormDataParam generalFormDataParam) {

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
    public ResponseData delete(@RequestBody RestGeneralFormDataParam generalFormDataParam)  {
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
    public ResponseData<RestGeneralFormDataResult> detail(@RequestBody RestGeneralFormDataParam generalFormDataParam) {
        RestGeneralFormData detail = this.generalFormDataService.getById(generalFormDataParam.getId());
        RestGeneralFormDataResult result = new RestGeneralFormDataResult();
        ToolUtil.copyProperties(detail, result);

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
    public PageInfo<RestGeneralFormDataResult> list(@RequestBody(required = false) RestGeneralFormDataParam generalFormDataParam) {
        if(ToolUtil.isEmpty(generalFormDataParam)){
            generalFormDataParam = new RestGeneralFormDataParam();
        }
        return this.generalFormDataService.findPageBySpec(generalFormDataParam);
    }




}


