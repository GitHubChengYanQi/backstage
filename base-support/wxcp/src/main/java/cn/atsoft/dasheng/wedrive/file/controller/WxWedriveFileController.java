package cn.atsoft.dasheng.wedrive.file.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.wedrive.file.entity.WxWedriveFile;
import cn.atsoft.dasheng.wedrive.file.model.params.WxWedriveFileParam;
import cn.atsoft.dasheng.wedrive.file.model.result.WxWedriveFileResult;
import cn.atsoft.dasheng.wedrive.file.service.WxWedriveFileService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 微信微盘文件管理控制器
 *
 * @author Captain_Jazz
 * @Date 2023-04-01 11:27:01
 */
@RestController
@RequestMapping("/wxWedriveFile")
@Api(tags = "微信微盘文件管理")
public class WxWedriveFileController extends BaseController {

    @Autowired
    private WxWedriveFileService wxWedriveFileService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody WxWedriveFileParam wxWedriveFileParam) throws IOException, WxErrorException {
        this.wxWedriveFileService.add(wxWedriveFileParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody WxWedriveFileParam wxWedriveFileParam) {

        this.wxWedriveFileService.update(wxWedriveFileParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody WxWedriveFileParam wxWedriveFileParam)  {
        this.wxWedriveFileService.delete(wxWedriveFileParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<WxWedriveFileResult> detail(@RequestBody WxWedriveFileParam wxWedriveFileParam) {
        WxWedriveFile detail = this.wxWedriveFileService.getById(wxWedriveFileParam.getFileId());
        WxWedriveFileResult result = new WxWedriveFileResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<WxWedriveFileResult> list(@RequestBody(required = false) WxWedriveFileParam wxWedriveFileParam) {
        if(ToolUtil.isEmpty(wxWedriveFileParam)){
            wxWedriveFileParam = new WxWedriveFileParam();
        }
        return this.wxWedriveFileService.findPageBySpec(wxWedriveFileParam);
    }




}


