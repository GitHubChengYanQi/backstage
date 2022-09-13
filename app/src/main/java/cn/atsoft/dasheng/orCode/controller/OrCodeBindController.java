package cn.atsoft.dasheng.orCode.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.params.OrCodeBindParam;
import cn.atsoft.dasheng.orCode.model.result.OrCodeBindResult;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 二维码绑定控制器
 *
 * @author song
 * @Date 2021-10-29 10:23:27
 */
@RestController
@RequestMapping("/orCodeBind")
@Api(tags = "二维码绑定")
public class OrCodeBindController extends BaseController {

    @Autowired
    private OrCodeBindService orCodeBindService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody OrCodeBindParam orCodeBindParam) {
        this.orCodeBindService.add(orCodeBindParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update() {
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody OrCodeBindParam orCodeBindParam) {
        this.orCodeBindService.delete(orCodeBindParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody OrCodeBindParam orCodeBindParam) {
        OrCodeBind detail = this.orCodeBindService.getById(orCodeBindParam.getOrCodeBindId());
        OrCodeBindResult result = new OrCodeBindResult();
        ToolUtil.copyProperties(detail, result);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<OrCodeBindResult> list(@RequestBody(required = false) OrCodeBindParam orCodeBindParam) {
        if (ToolUtil.isEmpty(orCodeBindParam)) {
            orCodeBindParam = new OrCodeBindParam();
        }
        return this.orCodeBindService.findPageBySpec(orCodeBindParam);
    }


}


