package cn.atsoft.dasheng.portal.repair.service;

import cn.atsoft.dasheng.appBase.service.sendTemplae;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.dispatChing.entity.Dispatching;
import cn.atsoft.dasheng.portal.dispatChing.model.params.DispatchingParam;
import cn.atsoft.dasheng.portal.dispatChing.service.DispatchingService;
import cn.atsoft.dasheng.portal.remind.entity.Remind;
import cn.atsoft.dasheng.portal.remind.model.params.WxTemplateData;
import cn.atsoft.dasheng.portal.remind.model.result.RemindResult;
import cn.atsoft.dasheng.portal.remind.service.RemindService;
import cn.atsoft.dasheng.portal.remindUser.entity.RemindUser;
import cn.atsoft.dasheng.portal.remindUser.service.RemindUserService;
import cn.atsoft.dasheng.portal.repair.entity.Repair;
import cn.atsoft.dasheng.portal.repair.model.params.RepairParam;
import cn.atsoft.dasheng.portal.repair.model.result.RepairResult;
import cn.atsoft.dasheng.portal.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.portal.wxUser.model.params.WxuserInfoParam;
import cn.atsoft.dasheng.portal.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.service.UcOpenUserInfoService;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class RepairSendTemplate extends sendTemplae {
    @Autowired
    private RepairService repairService;

    @Autowired
    private RemindService remindService;

    @Autowired
    private UserService userService;

    @Autowired
    private WxuserInfoService wxuserInfoService;

    @Autowired
    private DispatchingService dispatchingService;

    @Autowired
    private RemindUserService remindUserService;

    @Autowired
    private UcOpenUserInfoService userInfoService;


    private RepairParam repairParam;

    RepairResult remindResult = new RepairResult();

    @Override
    public String getTemplateId() {

        Remind reminds = getReminds(repairParam.getProgress());
        WxTemplateData wxTemplateData = JSON.parseObject(reminds.getTemplateType(), WxTemplateData.class);
        return wxTemplateData.getTemplateId();
    }

    @Override
    public List<String> getOpenIds() {

        Long progress = repairParam.getProgress();
        Remind reminds = getReminds(progress);
        List<Long> getremindUserids = getremindUserids(reminds.getRemindId());
        List<Long> userIds = new ArrayList<>();
        if (repairParam.getName() != null) {
            userIds.add(repairParam.getName());
        }
        for (Long getremindUserid : getremindUserids) {
            userIds.add(getremindUserid);
        }
        QueryWrapper<WxuserInfo> wxuserInfoQueryWrapper = new QueryWrapper<>();
        List<WxuserInfo> wxuserInfos = wxuserInfoService.list(wxuserInfoQueryWrapper.in("user_id", userIds));
        List<Long> memberIds = new ArrayList<>();
        for (WxuserInfo wxuserInfo : wxuserInfos) {
            memberIds.add(wxuserInfo.getMemberId());
        }
        QueryWrapper<UcOpenUserInfo> infoQueryWrapper = new QueryWrapper<>();
        infoQueryWrapper.in("member_id", memberIds);
        List<UcOpenUserInfo> ucOpenUserInfos = userInfoService.list(infoQueryWrapper);
        List<String> openids = new ArrayList<>();
        for (UcOpenUserInfo ucOpenUserInfo : ucOpenUserInfos) {
            openids.add(ucOpenUserInfo.getUuid());
        }
        return openids;
    }

    @Override
    public List<WxMpTemplateData> getTemplateData() {
        String backTemplat = "";

        List<WxMpTemplateData> dataList = new ArrayList<>();


        Remind reminds = getReminds(repairParam.getProgress());
        backTemplat = reminds.getTemplateType();
        Dispatching dispatching = getDispatching(repairParam.getRepairId());
        Long name = dispatching.getName();
        String note = dispatching.getNote();

        String userId = "";
        if (name != null) {
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.in("user_id", name);
            User username = userService.getOne(userQueryWrapper);
            userId = username.getName();
        }
/**
 * 判断登录是否小程序
 */
        if (repairParam.getName() != null) {
            QueryWrapper<WxuserInfo> wxuserInfoQueryWrapper = new QueryWrapper<>();
            wxuserInfoQueryWrapper.in("member_id", repairParam.getName());
            WxuserInfo wxuserInfo = wxuserInfoService.getOne(wxuserInfoQueryWrapper);
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.in("user_id", wxuserInfo.getUserId());
            User user = userService.getOne(userQueryWrapper);
            if (reminds.getTemplateType().contains("{{name}}")) {
                if (user != null) {
                    backTemplat = backTemplat.replace("{{name}}", user.getName());
                } else {
                    backTemplat = backTemplat.replace("{{name}}", "系统");
                }
            }

        } else {
            if (reminds.getTemplateType().contains("{{name}}")) {
                if (userId != null && userId != "") {
                    backTemplat =backTemplat .replace("{{name}}", "系统");
                }
            }
        }


        String reateTime = String.valueOf(repairParam.getCreateTime());
        DateTime parse = DateUtil.parse(reateTime);
        String time = String.valueOf(parse);


        if (reminds.getTemplateType() != null) {
            backTemplat = backTemplat.replace("{{time}}", time);
        }
        /**
         * 判断备注是否存在
         */
//        if (reminds.getTemplateType().contains("{{note}}")) {
//            if (note != null && backTemplat != null) {
//                backTemplat = backTemplat.replace("{{note}}", note);
//            } else {
//                backTemplat = backTemplat.replace("{{note}}", "系统");
//            }
//        }

        /**
         * 判断详情是否存在
         */

        if (reminds.getTemplateType().contains("{{details}}")) {
            if (remindResult.getComment() != null && backTemplat != null) {
                backTemplat = backTemplat.replace("{{details}}", remindResult.getComment());
            } else {
                backTemplat = backTemplat.replace("{{details}}", "无");
            }
        }

        WxTemplateData wxTemplateData = JSON.parseObject(backTemplat, WxTemplateData.class);


        for (WxTemplateData.DataList list : wxTemplateData.getDataList()) {
            WxMpTemplateData wxMpTemplateData = new WxMpTemplateData();
            wxMpTemplateData.setName(list.getKey());
            wxMpTemplateData.setValue(list.getValue());
            dataList.add(wxMpTemplateData);
        }

        return dataList;
    }

    @Override
    public String getPage() {
        return repairParam.getPage();
    }


    private Remind getReminds(Long type) {
        QueryWrapper<Remind> remindQueryWrapper = new QueryWrapper<>();
        Remind remindServiceOne = remindService.getOne(remindQueryWrapper.in("type", type));
        if (ToolUtil.isEmpty(remindServiceOne)) {
            Remind remind = new Remind();
            return remind;
        }
        return remindServiceOne;
    }

    private Dispatching getDispatching(Long id) {
        QueryWrapper<Dispatching> dispatchingQueryWrapper = new QueryWrapper<>();
        dispatchingQueryWrapper.in("repair_id", id);

        Dispatching dispatchingServiceOne = dispatchingService.getOne(dispatchingQueryWrapper);
        if (ToolUtil.isEmpty(dispatchingServiceOne)) {
            Dispatching dispatching = new Dispatching();
            return dispatching;
        }
        return dispatchingServiceOne;
    }

    private List<Long> getremindUserids(Long ids) {
        QueryWrapper<RemindUser> remindUserQueryWrapper = new QueryWrapper<>();
        remindUserQueryWrapper.in("remind_id", ids);
        List<RemindUser> remindUserList = remindUserService.list(remindUserQueryWrapper);
        if (ToolUtil.isEmpty(remindUserList)) {
            return new ArrayList<>();
        }
        List<Long> remindUserIds = new ArrayList<>();
        for (RemindUser remindUser : remindUserList) {
            remindUserIds.add(remindUser.getUserId());
        }
        return remindUserIds;
    }
}
