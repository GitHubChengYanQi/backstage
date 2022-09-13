package cn.atsoft.dasheng.portal.bannerDifference.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.portal.banner.wrapper.BannerSelectWrapper;
import cn.atsoft.dasheng.portal.bannerDifference.entity.BannerDifference;
import cn.atsoft.dasheng.portal.bannerDifference.model.params.BannerDifferenceParam;
import cn.atsoft.dasheng.portal.bannerDifference.model.result.BannerDifferenceResult;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.bannerDifference.service.BannerDifferenceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;


/**
 * 轮播图分类控制器
 *
 * @author 
 * @Date 2021-08-18 10:42:10
 */
@RestController
@RequestMapping("/bannerDifference")
@Api(tags = "轮播图分类")
public class BannerDifferenceController extends BaseController {

    @Autowired
    private BannerDifferenceService bannerDifferenceService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody BannerDifferenceParam bannerDifferenceParam) {
        this.bannerDifferenceService.add(bannerDifferenceParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody BannerDifferenceParam bannerDifferenceParam) {

        this.bannerDifferenceService.update(bannerDifferenceParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody BannerDifferenceParam bannerDifferenceParam)  {
        this.bannerDifferenceService.delete(bannerDifferenceParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody BannerDifferenceParam bannerDifferenceParam) {
        BannerDifference detail = this.bannerDifferenceService.getById(bannerDifferenceParam.getClassificationId());
        BannerDifferenceResult result = new BannerDifferenceResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<BannerDifferenceResult> list(@RequestBody(required = false) BannerDifferenceParam bannerDifferenceParam) {
        if(ToolUtil.isEmpty(bannerDifferenceParam)){
            bannerDifferenceParam = new BannerDifferenceParam();
        }
        return this.bannerDifferenceService.findPageBySpec(bannerDifferenceParam);
    }

    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {
        QueryWrapper<BannerDifference> bannerDifferenceQueryWrapper = new QueryWrapper<>();
        bannerDifferenceQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.bannerDifferenceService.listMaps(bannerDifferenceQueryWrapper);
        BannerSelectWrapper bannerSelectWrapper = new BannerSelectWrapper(list);
        List<Map<String, Object>> result = bannerSelectWrapper.wrap();
        return ResponseData.success(result);

    }




}


