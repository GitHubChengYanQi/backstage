package cn.atsoft.dasheng.fieldAuthority.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.fieldAuthority.entity.FieldAuthority;
import cn.atsoft.dasheng.fieldAuthority.model.params.FieldAuthorityParam;
import cn.atsoft.dasheng.fieldAuthority.model.result.FieldAuthorityResult;
import cn.atsoft.dasheng.fieldAuthority.service.FieldAuthorityService;
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
 * 字段操作权限表控制器
 *
 * @author Captain_Jazz
 * @Date 2022-05-17 11:11:35
 */
@RestController
@RequestMapping("/fieldAuthority")
@Api(tags = "字段操作权限表")
public class FieldAuthorityController extends BaseController {

    @Autowired
    private FieldAuthorityService fieldAuthorityService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-05-17
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody FieldAuthorityParam fieldAuthorityParam) {
        this.fieldAuthorityService.add(fieldAuthorityParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-05-17
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody FieldAuthorityParam fieldAuthorityParam) {

        this.fieldAuthorityService.update(fieldAuthorityParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-05-17
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody FieldAuthorityParam fieldAuthorityParam)  {
        this.fieldAuthorityService.delete(fieldAuthorityParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-05-17
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<FieldAuthorityResult> detail(@RequestBody FieldAuthorityParam fieldAuthorityParam) {
        FieldAuthority detail = this.fieldAuthorityService.getById(fieldAuthorityParam.getAuthorityId());
        FieldAuthorityResult result = new FieldAuthorityResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-05-17
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<FieldAuthorityResult> list(@RequestBody(required = false) FieldAuthorityParam fieldAuthorityParam) {
        if(ToolUtil.isEmpty(fieldAuthorityParam)){
            fieldAuthorityParam = new FieldAuthorityParam();
        }
        return this.fieldAuthorityService.findPageBySpec(fieldAuthorityParam);
    }




}


