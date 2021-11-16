package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.Data;
import cn.atsoft.dasheng.form.model.params.DataParam;
import cn.atsoft.dasheng.form.model.result.DataResult;
import cn.atsoft.dasheng.form.service.DataService;
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
 * 自定义表单主表控制器
 *
 * @author song
 * @Date 2021-11-16 16:11:41
 */
@RestController
@RequestMapping("/data")
@Api(tags = "自定义表单主表")
public class DataController extends BaseController {

    @Autowired
    private DataService dataService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody DataParam dataParam) {
        this.dataService.add(dataParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody DataParam dataParam) {

        this.dataService.update(dataParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody DataParam dataParam)  {
        this.dataService.delete(dataParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<DataResult> detail(@RequestBody DataParam dataParam) {
        Data detail = this.dataService.getById(dataParam.getDataId());
        DataResult result = new DataResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<DataResult> list(@RequestBody(required = false) DataParam dataParam) {
        if(ToolUtil.isEmpty(dataParam)){
            dataParam = new DataParam();
        }
        return this.dataService.findPageBySpec(dataParam);
    }




}


