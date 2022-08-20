package cn.atsoft.dasheng.daoxin.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.daoxin.entity.DaoxinPosition;
import cn.atsoft.dasheng.daoxin.model.params.DaoxinPositionParam;
import cn.atsoft.dasheng.daoxin.model.result.DaoxinPositionResult;
import cn.atsoft.dasheng.daoxin.service.DaoxinPositionService;
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
 * daoxin职位表控制器
 *
 * @author Captain_Jazz
 * @Date 2022-02-17 15:10:45
 */
@RestController
@RequestMapping("/daoxinPosition")
@Api(tags = "daoxin职位表")
public class DaoxinPositionController extends BaseController {

    @Autowired
    private DaoxinPositionService daoxinPositionService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody DaoxinPositionParam daoxinPositionParam) {
        this.daoxinPositionService.add(daoxinPositionParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody DaoxinPositionParam daoxinPositionParam) {

        this.daoxinPositionService.update(daoxinPositionParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody DaoxinPositionParam daoxinPositionParam)  {
        this.daoxinPositionService.delete(daoxinPositionParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody DaoxinPositionParam daoxinPositionParam) {
        DaoxinPosition detail = this.daoxinPositionService.getById(daoxinPositionParam.getPositionId());
        DaoxinPositionResult result = new DaoxinPositionResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<DaoxinPositionResult> list(@RequestBody(required = false) DaoxinPositionParam daoxinPositionParam) {
        if(ToolUtil.isEmpty(daoxinPositionParam)){
            daoxinPositionParam = new DaoxinPositionParam();
        }
        return this.daoxinPositionService.findPageBySpec(daoxinPositionParam);
    }




}


