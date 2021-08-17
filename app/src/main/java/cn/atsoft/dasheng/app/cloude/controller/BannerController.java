package cn.atsoft.dasheng.app.cloude.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.cloude.entity.Banner;
import cn.atsoft.dasheng.app.cloude.model.params.BannerParam;
import cn.atsoft.dasheng.app.cloude.model.result.BannerResult;
import cn.atsoft.dasheng.app.cloude.service.BannerService;
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
 * 轮播图控制器
 *
 * @author 
 * @Date 2021-08-17 14:05:06
 */
@RestController
@RequestMapping("/banner")
@Api(tags = "轮播图")
public class BannerController extends BaseController {

    @Autowired
    private BannerService bannerService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-08-17
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody BannerParam bannerParam) {
        this.bannerService.add(bannerParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-08-17
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody BannerParam bannerParam) {

        this.bannerService.update(bannerParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-08-17
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody BannerParam bannerParam)  {
        this.bannerService.delete(bannerParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-08-17
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<BannerResult> detail(@RequestBody BannerParam bannerParam) {
        Banner detail = this.bannerService.getById(bannerParam.getBannerId());
        BannerResult result = new BannerResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-08-17
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<BannerResult> list(@RequestBody(required = false) BannerParam bannerParam) {
        if(ToolUtil.isEmpty(bannerParam)){
            bannerParam = new BannerParam();
        }
        return this.bannerService.findPageBySpec(bannerParam);
    }




}


