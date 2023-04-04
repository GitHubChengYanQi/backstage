package cn.atsoft.dasheng.wedrive.space.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.wedrive.space.model.params.WxWedriveSpaceParam;
import cn.atsoft.dasheng.wedrive.space.model.result.WxWedriveSpaceResult;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.wedrive.space.service.WxWedriveSpaceService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 微信微盘空间控制器
 *
 * @author Captain_Jazz
 * @Date 2023-03-31 11:15:47
 */
@RestController
@RequestMapping("/wxWedriveSpace")
@Api(tags = "微信微盘空间")
public class WxWedriveSpaceController extends BaseController {

    @Autowired
    private WxWedriveSpaceService wxWedriveSpaceService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-31
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody WxWedriveSpaceParam wxWedriveSpaceParam) throws WxErrorException {
        this.wxWedriveSpaceService.add(wxWedriveSpaceParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-31
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody WxWedriveSpaceParam wxWedriveSpaceParam) {

        this.wxWedriveSpaceService.update(wxWedriveSpaceParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-31
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody WxWedriveSpaceParam wxWedriveSpaceParam)  {
        this.wxWedriveSpaceService.delete(wxWedriveSpaceParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-31
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<WxWedriveSpaceResult> detail(@RequestBody WxWedriveSpaceParam wxWedriveSpaceParam) throws WxErrorException {


        return ResponseData.success(this.wxWedriveSpaceService.detail(wxWedriveSpaceParam.getSpaceId()));
    }
    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-31
     */
    @RequestMapping(value = "/spaceAclAdd", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData spaceAclAdd(@RequestBody WxWedriveSpaceParam wxWedriveSpaceParam) throws WxErrorException {


        return ResponseData.success(this.wxWedriveSpaceService.spaceAclAdd(wxWedriveSpaceParam));
    }
    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-31
     */
    @RequestMapping(value = "/spaceAclDel", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData spaceAclDel(@RequestBody WxWedriveSpaceParam wxWedriveSpaceParam) throws WxErrorException {


        return ResponseData.success(this.wxWedriveSpaceService.spaceAclDelete(wxWedriveSpaceParam));
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-31
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<WxWedriveSpaceResult> list(@RequestBody(required = false) WxWedriveSpaceParam wxWedriveSpaceParam) {
        if(ToolUtil.isEmpty(wxWedriveSpaceParam)){
            wxWedriveSpaceParam = new WxWedriveSpaceParam();
        }
        return this.wxWedriveSpaceService.findPageBySpec(wxWedriveSpaceParam);
    }




}


