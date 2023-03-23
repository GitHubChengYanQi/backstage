package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.TrackMessage;
import cn.atsoft.dasheng.crm.model.params.TrackMessageParam;
import cn.atsoft.dasheng.crm.model.result.TrackMessageResult;
import cn.atsoft.dasheng.crm.service.TrackMessageService;
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
 * 商机跟踪内容控制器
 *
 * @author 
 * @Date 2021-09-07 18:16:17
 */
@RestController
@RequestMapping("/trackMessage")
@Api(tags = "商机跟踪内容")
public class TrackMessageController extends BaseController {

    @Autowired
    private TrackMessageService trackMessageService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-09-07
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody TrackMessageParam trackMessageParam) {
        this.trackMessageService.add(trackMessageParam);
        return ResponseData.success();
    }

//    /**
//     * 编辑接口
//     *
//     * @author
//     * @Date 2021-09-07
//     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody TrackMessageParam trackMessageParam) {
//
//        this.trackMessageService.update(trackMessageParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 删除接口
//     *
//     * @author
//     * @Date 2021-09-07
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody TrackMessageParam trackMessageParam)  {
//        this.trackMessageService.delete(trackMessageParam);
//        return ResponseData.success();
//    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-09-07
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody TrackMessageParam trackMessageParam) {
        TrackMessage detail = this.trackMessageService.getById(trackMessageParam.getTrackMessageId());
        TrackMessageResult result = new TrackMessageResult();
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-09-07
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<TrackMessageResult> list(@RequestBody(required = false) TrackMessageParam trackMessageParam) {
        if(ToolUtil.isEmpty(trackMessageParam)){
            trackMessageParam = new TrackMessageParam();
        }
        return this.trackMessageService.findPageBySpec(trackMessageParam);
    }

    @RequestMapping(value = "/listAll", method = RequestMethod.POST)
    @ApiOperation("列表")
    public List<TrackMessageResult> listAll(@RequestBody(required = false) TrackMessageParam trackMessageParam) {
        if(ToolUtil.isEmpty(trackMessageParam)){
            trackMessageParam = new TrackMessageParam();
        }
        return this.trackMessageService.findListBySpec(trackMessageParam);
    }




}


