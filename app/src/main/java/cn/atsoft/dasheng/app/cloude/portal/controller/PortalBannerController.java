package cn.atsoft.dasheng.app.cloude.portal.controller;

import cn.atsoft.dasheng.app.cloude.portal.entity.PortalBanner;
import cn.atsoft.dasheng.app.cloude.portal.model.result.PortalBannerResult;
import cn.atsoft.dasheng.app.cloude.portal.service.PortalBannerService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.cloude.portal.model.params.PortalBannerParam;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 轮播图控制器
 *
 * @author 
 * @Date 2021-08-17 11:41:05
 */
@RestController
@RequestMapping("/api")
@Api(tags = "轮播图")
public class PortalBannerController extends BaseController {

    @Autowired
    private PortalBannerService portalBannerService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-08-17
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET )
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody PortalBannerParam portalBannerParam) {
        this.portalBannerService.add(portalBannerParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-08-17
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody PortalBannerParam portalBannerParam) {

        this.portalBannerService.update(portalBannerParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-08-17
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody PortalBannerParam portalBannerParam)  {
        this.portalBannerService.delete(portalBannerParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-08-17
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ApiOperation("详情")
    public ResponseData<PortalBannerResult> detail(@RequestBody PortalBannerParam portalBannerParam) {
        PortalBanner detail = this.portalBannerService.getById(portalBannerParam.getBannerId());
        PortalBannerResult result = new PortalBannerResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-08-17
     */
    @RequestMapping(value = "/BannerList", method = RequestMethod.GET)
    @ApiOperation("列表")
    public PageInfo<PortalBannerResult> list(@RequestBody(required = false) PortalBannerParam portalBannerParam) {
        if(ToolUtil.isEmpty(portalBannerParam)){
            portalBannerParam = new PortalBannerParam();
        }
        return this.portalBannerService.findPageBySpec(portalBannerParam);
    }




}


