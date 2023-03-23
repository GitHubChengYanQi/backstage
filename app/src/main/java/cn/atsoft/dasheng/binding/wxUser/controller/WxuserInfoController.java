package cn.atsoft.dasheng.binding.wxUser.controller;

import cn.atsoft.dasheng.app.entity.Contacts;
import cn.atsoft.dasheng.app.wrapper.ContactsSelectWrapper;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.model.params.WxuserInfoParam;
import cn.atsoft.dasheng.binding.wxUser.model.result.WxuserInfoResult;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.binding.wxUser.wrapper.WxuserInfoSelectWrapper;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.atsoft.dasheng.sys.modular.system.warpper.UserWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 用户小程序关联控制器
 *
 * @author
 * @Date 2021-08-24 14:40:25
 */
@RestController
@RequestMapping("/wxuserInfo")
@Api(tags = "用户小程序关联")
public class WxuserInfoController extends BaseController {

    @Autowired
    private WxuserInfoService wxuserInfoService;
    @Autowired
    private UserService userService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    @Permission
    public ResponseData addItem(@RequestBody WxuserInfoParam wxuserInfoParam) {
//        this.wxuserInfoService.add(wxuserInfoParam);
        Boolean aBoolean = wxuserInfoService.sendPermissions(4L, 1430422569372217346L);
        return ResponseData.success(aBoolean);
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    @Permission
    public ResponseData update(@RequestBody WxuserInfoParam wxuserInfoParam) {

        this.wxuserInfoService.update(wxuserInfoParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    @Permission
    public ResponseData delete(@RequestBody WxuserInfoParam wxuserInfoParam) {
        this.wxuserInfoService.delete(wxuserInfoParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    @Permission
    public ResponseData detail(@RequestBody WxuserInfoParam wxuserInfoParam) {
        WxuserInfo detail = this.wxuserInfoService.getById(wxuserInfoParam.getUserInfoId());
        WxuserInfoResult result = new WxuserInfoResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    @Permission
    public PageInfo<WxuserInfoResult> list(@RequestBody(required = false) WxuserInfoParam wxuserInfoParam) {
        if (ToolUtil.isEmpty(wxuserInfoParam)) {
            wxuserInfoParam = new WxuserInfoParam();
        }
        return this.wxuserInfoService.findPageBySpec(wxuserInfoParam);
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    @Permission
    public ResponseData listSelect() {
        List<WxuserInfo> wxuserInfos = wxuserInfoService.lambdaQuery().in(WxuserInfo::getSource, "wxMp").list();
        List<Long> ids = new ArrayList<>();
        for (WxuserInfo wxuserInfo : wxuserInfos) {
            ids.add(wxuserInfo.getUserId());
        }
        QueryWrapper<User> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.in("user_id", ids);
        List<Map<String, Object>> list = this.userService.listMaps(QueryWrapper);
        WxuserInfoSelectWrapper wxuserInfoSelectWrapper = new WxuserInfoSelectWrapper(list);
        List<Map<String, Object>> result = wxuserInfoSelectWrapper.wrap();
        return ResponseData.success(result);

    }

}


