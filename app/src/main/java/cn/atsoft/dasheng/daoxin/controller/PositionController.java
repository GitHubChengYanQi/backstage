package cn.atsoft.dasheng.daoxin.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.daoxin.entity.Position;
import cn.atsoft.dasheng.daoxin.model.params.PositionParam;
import cn.atsoft.dasheng.daoxin.model.result.PositionResult;
import cn.atsoft.dasheng.daoxin.service.PositionService;
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
 * @Date 2022-02-17 14:45:19
 */
@RestController
@RequestMapping("/position")
@Api(tags = "daoxin职位表")
public class PositionController extends BaseController {

//    @Autowired
//    private PositionService positionService;
//
//    /**
//     * 新增接口
//     *
//     * @author Captain_Jazz
//     * @Date 2022-02-17
//     */
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    @ApiOperation("新增")
//    public ResponseData addItem(@RequestBody PositionParam positionParam) {
//        this.positionService.add(positionParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 编辑接口
//     *
//     * @author Captain_Jazz
//     * @Date 2022-02-17
//     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody PositionParam positionParam) {
//
//        this.positionService.update(positionParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 删除接口
//     *
//     * @author Captain_Jazz
//     * @Date 2022-02-17
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody PositionParam positionParam)  {
//        this.positionService.delete(positionParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 查看详情接口
//     *
//     * @author Captain_Jazz
//     * @Date 2022-02-17
//     */
//    @RequestMapping(value = "/detail", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData<PositionResult> detail(@RequestBody PositionParam positionParam) {
//        Position detail = this.positionService.getById(positionParam.getPositionId());
//        PositionResult result = new PositionResult();
//        ToolUtil.copyProperties(detail, result);
//
//        result.setValue(parentValue);
//        return ResponseData.success(result);
//    }
//
//    /**
//     * 查询列表
//     *
//     * @author Captain_Jazz
//     * @Date 2022-02-17
//     */
//    @RequestMapping(value = "/list", method = RequestMethod.POST)
//    @ApiOperation("列表")
//    public PageInfo<PositionResult> list(@RequestBody(required = false) PositionParam positionParam) {
//        if(ToolUtil.isEmpty(positionParam)){
//            positionParam = new PositionParam();
//        }
//        return this.positionService.findPageBySpec(positionParam);
//    }
//
//


}


