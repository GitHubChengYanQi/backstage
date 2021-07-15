package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Place;
import cn.atsoft.dasheng.app.model.params.PlaceParam;
import cn.atsoft.dasheng.app.model.result.PlaceResult;
import cn.atsoft.dasheng.app.service.PlaceService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import cn.atsoft.dasheng.app.wrapper.PlaceSelectWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 地点表控制器
 *
 * @author 
 * @Date 2021-07-15 11:13:02
 */
@RestController
@RequestMapping("/place")
@Api(tags = "地点表")
public class PlaceController extends BaseController {

    @Autowired
    private PlaceService placeService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody PlaceParam placeParam) {
        this.placeService.add(placeParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody PlaceParam placeParam) {

        this.placeService.update(placeParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody PlaceParam placeParam)  {
        this.placeService.delete(placeParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<PlaceResult> detail(@RequestBody PlaceParam placeParam) {
        Place detail = this.placeService.getById(placeParam.getPalceId());
        PlaceResult result = new PlaceResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<PlaceResult> list(@RequestBody(required = false) PlaceParam placeParam) {
        if(ToolUtil.isEmpty(placeParam)){
            placeParam = new PlaceParam();
        }
        return this.placeService.findPageBySpec(placeParam);
    }

    /**
    * 选择列表
    *
    * @author 
    * @Date 2021-07-15
    */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String,Object>>> listSelect() {
        List<Map<String,Object>> list = this.placeService.listMaps();

        PlaceSelectWrapper factory = new PlaceSelectWrapper(list);
        List<Map<String,Object>> result = factory.wrap();
        return ResponseData.success(result);
    }



}


