package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.RestFormStyle;
import cn.atsoft.dasheng.form.model.params.RestFormStyleParam;
import cn.atsoft.dasheng.form.model.result.RestFormStyleResult;
import cn.atsoft.dasheng.form.service.RestFormStyleService;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 表单风格控制器
 *
 * @author
 * @Date 2022-09-23 09:20:36
 */
@RestController
@RequestMapping("/formStyle/{version}")
@ApiVersion("2.0")
@Api(tags = "表单风格 管理")
public class RestFormStyleController extends BaseController {

    @Autowired
    private RestFormStyleService formStyleService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2022-09-23
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestFormStyleParam formStyleParam) {
        this.formStyleService.add(formStyleParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2022-09-23
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody RestFormStyleParam formStyleParam) {

        this.formStyleService.update(formStyleParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2022-09-23
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody RestFormStyleParam formStyleParam) {
        this.formStyleService.delete(formStyleParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2022-09-23
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<RestFormStyleResult> detail(@RequestBody RestFormStyleParam formStyleParam) {
        RestFormStyle detail = null;
        try {
            if (ToolUtil.isNotEmpty(formStyleParam.getStyleId())) {
                detail = this.formStyleService.getById(formStyleParam.getStyleId());
            } else if (ToolUtil.isNotEmpty(formStyleParam.getFormType())) {
                detail = this.formStyleService.getOne(new QueryWrapper<RestFormStyle>() {{
                    eq("form_type", formStyleParam.getFormType());
                }});
            }
        } catch (Exception e) {

        }

        if (ToolUtil.isEmpty(detail)) {
            return ResponseData.success(null);
        }

        RestFormStyleResult result = new RestFormStyleResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2022-09-23
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<RestFormStyleResult> list(@RequestBody(required = false) RestFormStyleParam formStyleParam) {
        if (ToolUtil.isEmpty(formStyleParam)) {
            formStyleParam = new RestFormStyleParam();
        }
        return this.formStyleService.findPageBySpec(formStyleParam);
    }


}


