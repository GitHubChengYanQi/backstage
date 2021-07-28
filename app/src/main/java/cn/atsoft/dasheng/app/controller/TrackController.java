package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Track;
import cn.atsoft.dasheng.app.model.params.TrackParam;
import cn.atsoft.dasheng.app.model.result.TrackResult;
import cn.atsoft.dasheng.app.service.TrackService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 报价表控制器
 *
 * @author cheng
 * @Date 2021-07-19 15:13:58
 */
@RestController
@RequestMapping("/track")
@Api(tags = "报价表")
public class TrackController extends BaseController {

    @Autowired
    private TrackService trackService;

    /**
     * 新增接口
     *
     * @author cheng
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody TrackParam trackParam) {
        Long add = this.trackService.add(trackParam);
        return ResponseData.success(add);
    }

    /**
     * 编辑接口
     *
     * @author cheng
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody TrackParam trackParam) {

        this.trackService.update(trackParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author cheng
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody TrackParam trackParam) {
        this.trackService.delete(trackParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author cheng
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<TrackResult> detail(@RequestBody TrackParam trackParam) {
        Track detail = this.trackService.getById(trackParam.getTrackId());
        TrackResult result = new TrackResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author cheng
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<TrackResult> list(@RequestBody(required = false) TrackParam trackParam) {
        if (ToolUtil.isEmpty(trackParam)) {
            trackParam = new TrackParam();
        }
        return this.trackService.findPageBySpec(trackParam);
    }


}


