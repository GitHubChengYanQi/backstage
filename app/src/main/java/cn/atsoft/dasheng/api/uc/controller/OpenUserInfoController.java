package cn.atsoft.dasheng.api.uc.controller;

import cn.atsoft.dasheng.api.uc.wrapper.OpenUserInfoSelectWrapper;
import cn.atsoft.dasheng.app.entity.ErpPackage;
import cn.atsoft.dasheng.app.wrapper.ErpPackageSelectWrapper;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.api.uc.entity.OpenUserInfo;
import cn.atsoft.dasheng.api.uc.model.params.OpenUserInfoParam;
import cn.atsoft.dasheng.api.uc.model.result.OpenUserInfoResult;
import cn.atsoft.dasheng.api.uc.service.OpenUserInfoService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 控制器
 *
 * @author
 * @Date 2021-08-25 08:31:10
 */
@RestController
@RequestMapping("/openUserInfo")
@Api(tags = "")
public class OpenUserInfoController extends BaseController {

    @Autowired
    private OpenUserInfoService openUserInfoService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-08-25
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody OpenUserInfoParam openUserInfoParam) {
        this.openUserInfoService.add(openUserInfoParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-08-25
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody OpenUserInfoParam openUserInfoParam) {

        this.openUserInfoService.update(openUserInfoParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-08-25
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody OpenUserInfoParam openUserInfoParam) {
        this.openUserInfoService.delete(openUserInfoParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-08-25
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody OpenUserInfoParam openUserInfoParam) {
        OpenUserInfo detail = this.openUserInfoService.getById(openUserInfoParam.getPrimaryKey());
        OpenUserInfoResult result = new OpenUserInfoResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-08-25
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<OpenUserInfoResult> list(@RequestBody(required = false) OpenUserInfoParam openUserInfoParam) {
        if (ToolUtil.isEmpty(openUserInfoParam)) {
            openUserInfoParam = new OpenUserInfoParam();
        }
        return this.openUserInfoService.findPageBySpec(openUserInfoParam);
    }


    /**
     * 选择列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {
        QueryWrapper<OpenUserInfo> openUserInfoQueryWrapper = new QueryWrapper<>();
        openUserInfoQueryWrapper.in("source", "wxMp");
        List<Map<String, Object>> list = this.openUserInfoService.listMaps(openUserInfoQueryWrapper);
        OpenUserInfoSelectWrapper factory = new OpenUserInfoSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }


}


