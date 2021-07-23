package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.PartsList;
import cn.atsoft.dasheng.app.model.params.PartsListParam;
import cn.atsoft.dasheng.app.model.result.PartsListResult;
import cn.atsoft.dasheng.app.service.PartsListService;
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
 * 零件表控制器
 *
 * @author 
 * @Date 2021-07-23 16:48:11
 */
@RestController
@RequestMapping("/partsList")
@Api(tags = "零件表")
public class PartsListController extends BaseController {

    @Autowired
    private PartsListService partsListService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody PartsListParam partsListParam) {
        this.partsListService.add(partsListParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody PartsListParam partsListParam) {

        this.partsListService.update(partsListParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody PartsListParam partsListParam)  {
        this.partsListService.delete(partsListParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<PartsListResult> detail(@RequestBody PartsListParam partsListParam) {
        PartsList detail = this.partsListService.getById(partsListParam.getPartsListId());
        PartsListResult result = new PartsListResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<PartsListResult> list(@RequestBody(required = false) PartsListParam partsListParam) {
        if(ToolUtil.isEmpty(partsListParam)){
            partsListParam = new PartsListParam();
        }
        return this.partsListService.findPageBySpec(partsListParam);
    }




}


