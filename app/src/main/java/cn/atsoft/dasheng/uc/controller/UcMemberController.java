package cn.atsoft.dasheng.uc.controller;


import cn.atsoft.dasheng.api.uc.entity.OpenUserInfo;
import cn.atsoft.dasheng.api.uc.service.OpenUserInfoService;
import cn.atsoft.dasheng.appBase.service.WxCpService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.atsoft.dasheng.uc.entity.UcMember;
import cn.atsoft.dasheng.uc.model.params.UcMemberParam;
import cn.atsoft.dasheng.uc.model.result.UcMemberResult;
import cn.atsoft.dasheng.uc.service.UcMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.WxCpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * member
 *
 * @author cheng
 * @Date 2021-10-18 09:22:54
 */
@RestController
@RequestMapping("/ucMember")
@Api(tags = "资料")

public class UcMemberController extends BaseController {

    @Autowired
    private UcMemberService ucMemberService;
    @Autowired
    private WxCpService wxCpService;
    @Autowired
    private OpenUserInfoService openUserInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private WxuserInfoService wxuserInfoService;
    @Autowired
    private StepsService appStepService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody UcMemberParam ucMemberParam) {
        this.ucMemberService.add(ucMemberParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody UcMemberParam ucMemberParam) {

        this.ucMemberService.update(ucMemberParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody UcMemberParam ucMemberParam) {
        this.ucMemberService.delete(ucMemberParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody UcMemberParam ucMemberParam) {
        UcMember byId = this.ucMemberService.getById(ucMemberParam.getMemberId());
        UcMemberResult result = new UcMemberResult();
        ToolUtil.copyProperties(byId, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo list(@RequestBody(required = false) UcMemberParam ucMemberParam) {
        if (ToolUtil.isEmpty(ucMemberParam)) {
            ucMemberParam = new UcMemberParam();
        }
        return this.ucMemberService.findPageBySpec(ucMemberParam);

    }


    @RequestMapping(value = "/getUserByCp", method = RequestMethod.GET)
    @ApiOperation(value = "通过CpUserId 获取系统用户")
    public ResponseData getUserByCp(@RequestParam String CpUserId) {
        User user = null;
        try {
            WxCpUser wxCpUser = wxCpService.getWxCpClient().getUserService().getById(CpUserId);
            if (ToolUtil.isNotEmpty(wxCpUser) && ToolUtil.isNotEmpty(wxCpUser.getUserId())) {
                OpenUserInfo openUserInfo = ToolUtil.isEmpty(wxCpUser.getUserId()) ? new OpenUserInfo() : openUserInfoService.query().eq("uuid", wxCpUser.getUserId()).eq("source", "wxCp").one();
                if (ToolUtil.isEmpty(openUserInfo)) {
                    openUserInfo = new OpenUserInfo();
                }
                Long memberId = openUserInfo.getMemberId();
                WxuserInfo wxuserInfo = ToolUtil.isEmpty(memberId) ? new WxuserInfo() : wxuserInfoService.query().eq("member_id", memberId).eq("source", "wxCp").isNotNull("user_id").one();
                if (ToolUtil.isEmpty(wxuserInfo)) {
                    wxuserInfo = new WxuserInfo();
                }
                user = ToolUtil.isEmpty(wxuserInfo.getUserId()) ? new User() : userService.getById(wxuserInfo.getUserId());
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return ResponseData.success(user);
    }


    @RequestMapping(value = "/getUserByCps", method = RequestMethod.POST)
    public ResponseData getUserByCps(@RequestBody UcMemberParam param) {
        List<User> users = new ArrayList<>();
        for (String cpUserId : param.getCpUserIds()) {
            try {
                WxCpUser wxCpUser = wxCpService.getWxCpClient().getUserService().getById(cpUserId);
                if (ToolUtil.isNotEmpty(wxCpUser) && ToolUtil.isNotEmpty(wxCpUser.getUserId())) {
                    OpenUserInfo openUserInfo = ToolUtil.isEmpty(wxCpUser.getUserId()) ? new OpenUserInfo() : openUserInfoService.query().eq("uuid", wxCpUser.getUserId()).eq("source", "wxCp").one();
                    if (ToolUtil.isEmpty(openUserInfo)) {
                        openUserInfo = new OpenUserInfo();
                    }
                    Long memberId = openUserInfo.getMemberId();
                    if (ToolUtil.isNotEmpty(memberId)) {
                        WxuserInfo wxuserInfo =  wxuserInfoService.query().eq("member_id", memberId).eq("source", "wxCp").isNotNull("user_id").one();
                        if (ToolUtil.isNotEmpty(wxuserInfo)) {
                            User user = userService.getById(wxuserInfo.getUserId());
                            if (ToolUtil.isNotEmpty(user)) {
                                String imgUrl = appStepService.imgUrl(user.getUserId().toString());
                                user.setAvatar(imgUrl);
                                users.add(user);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseData.success(users);
    }


//    /**
//     * 批量删除
//     *
//     * @author song
//     * @Date 2021-09-11
//     */
//    @Permission
//    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData<DataResult> batchDelete(@RequestBody DataRequest dataRequest) {
//        dataService.batchDelete(dataRequest.getIds());
//        return ResponseData.success();
//    }

}
