package cn.atsoft.dasheng.portal.dispatching.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.dispatching.entity.Dispatching;
import cn.atsoft.dasheng.portal.dispatching.model.params.DispatchingParam;
import cn.atsoft.dasheng.portal.dispatching.model.result.DispatchingResult;
import cn.atsoft.dasheng.portal.dispatching.service.DispatchingService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.portal.dispatching.service.WxTemplate;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 派工表控制器
 *
 * @author
 * @Date 2021-08-23 10:31:48
 */
@RestController
@RequestMapping("/dispatching")
@Api(tags = "派工表")
public class DispatchingController extends BaseController {

    @Autowired
    private DispatchingService dispatchingService;
    @Autowired
    private WxTemplate wxTemplate;


    /**
     * 新增接口
     *
     * @author
     * @Date 2021-08-23
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody DispatchingParam dispatchingParam) {
        this.dispatchingService.add(dispatchingParam);
        return ResponseData.success();
    }

    @RequestMapping(value = "/addWx", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addWx(String openid, String templateId, String page, DispatchingParam dispatchingParam) {

        List<WxMaSubscribeMessage.MsgData> data = new ArrayList();

        data.add(new WxMaSubscribeMessage.MsgData("name", dispatchingParam.getName()));
        data.add(new WxMaSubscribeMessage.MsgData("address", dispatchingParam.getAddress()));
        data.add(new WxMaSubscribeMessage.MsgData("phone", dispatchingParam.getPhone()));

        String reateTime = String.valueOf(dispatchingParam.getCreateTime());
        DateTime parse = DateUtil.parse(reateTime);
        String time = String.valueOf(parse);
        data.add(new WxMaSubscribeMessage.MsgData("time", time));

        wxTemplate.send(openid, templateId, page, data);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-08-23
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody DispatchingParam dispatchingParam) {

        this.dispatchingService.update(dispatchingParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-08-23
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody DispatchingParam dispatchingParam) {
        this.dispatchingService.delete(dispatchingParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-08-23
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<DispatchingResult> detail(@RequestBody DispatchingParam dispatchingParam) {
        Dispatching detail = this.dispatchingService.getById(dispatchingParam.getDispatchingId());
        DispatchingResult result = new DispatchingResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-08-23
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<DispatchingResult> list(@RequestBody(required = false) DispatchingParam dispatchingParam) {
        if (ToolUtil.isEmpty(dispatchingParam)) {
            dispatchingParam = new DispatchingParam();
        }
        return this.dispatchingService.findPageBySpec(dispatchingParam);
    }


}


