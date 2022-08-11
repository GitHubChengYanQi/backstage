package cn.atsoft.dasheng.dynamic.controller;

import cn.atsoft.dasheng.dynamic.model.result.DynamicResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.dynamic.entity.Dynamic;
import cn.atsoft.dasheng.dynamic.model.params.DynamicParam;
import cn.atsoft.dasheng.dynamic.service.DynamicService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 控制器
 *
 * @author Captain_Jazz
 * @Date 2022-08-10 14:38:56
 */
@RestController
@RequestMapping("/dynamic")
@Api(tags = "")
public class DynamicController extends BaseController {

    @Autowired
    private DynamicService dynamicService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody DynamicParam dynamicParam) {
        this.dynamicService.add(dynamicParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody DynamicParam dynamicParam) {

        this.dynamicService.update(dynamicParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody DynamicParam dynamicParam)  {
        this.dynamicService.delete(dynamicParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<DynamicResult> detail(@RequestBody DynamicParam dynamicParam) {
        Dynamic detail = this.dynamicService.getById(dynamicParam.getDynamicId());
        DynamicResult result = new DynamicResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<DynamicResult> list(@RequestBody(required = false) DynamicParam dynamicParam) {
        if(ToolUtil.isEmpty(dynamicParam)){
            dynamicParam = new DynamicParam();
        }
        return this.dynamicService.findPageBySpec(dynamicParam);
    }




}


