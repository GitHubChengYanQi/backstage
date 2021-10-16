package cn.atsoft.dasheng.portal.repair.service;

import cn.atsoft.dasheng.appBase.service.WxCpService;
import cn.atsoft.dasheng.appBase.service.sendTemplae;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.commonArea.entity.CommonArea;
import cn.atsoft.dasheng.commonArea.service.CommonAreaService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.region.RegionResult;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.portal.dispatChing.entity.Dispatching;
import cn.atsoft.dasheng.portal.dispatChing.service.DispatchingService;
import cn.atsoft.dasheng.portal.remind.entity.Remind;
import cn.atsoft.dasheng.portal.remind.model.params.WxTemplateData;
import cn.atsoft.dasheng.portal.remind.service.RemindService;
import cn.atsoft.dasheng.portal.remindUser.entity.RemindUser;
import cn.atsoft.dasheng.portal.remindUser.service.RemindUserService;
import cn.atsoft.dasheng.portal.repair.model.params.RepairParam;
import cn.atsoft.dasheng.portal.repair.model.result.RepairResult;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.atsoft.dasheng.uc.entity.UcMember;
import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.service.UcMemberService;
import cn.atsoft.dasheng.uc.service.UcOpenUserInfoService;
import cn.atsoft.dasheng.uc.utils.UserUtils;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private CommonAreaService commonAreaService;
    @Autowired
    private UcMemberService ucMemberService;
    @Autowired
    private WxCpService wxCpService;

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

        if (repairParam.getProgress() == null) {
            throw new ServiceException(500, "请确定步骤流程");
        }
        Long progress = repairParam.getProgress();
        Remind reminds = getReminds(progress);
        List<Long> getremindUserids = getremindUserids(reminds.getRemindId());
        List<Long> userIds = new ArrayList<>();

        for (Long getremindUserid : getremindUserids) {
            userIds.add(getremindUserid);
        }

        List<WxuserInfo> wxuserInfoList = wxuserInfoService.lambdaQuery().in(WxuserInfo::getUserId, userIds).
                and(i -> i.in(WxuserInfo::getSource, "wxMp")).
                list();
        List<Long> memberIds = new ArrayList<>();
        for (WxuserInfo wxuserInfo : wxuserInfoList) {
            memberIds.add(wxuserInfo.getMemberId());
        }
        List<String> openids = new ArrayList<>();
        if (wxuserInfoList.size() != 0) {
            QueryWrapper<UcOpenUserInfo> infoQueryWrapper = new QueryWrapper<>();
            infoQueryWrapper.in("member_id", memberIds).eq("source", "wxMp");

            if (memberIds.size() != 0) {
                List<UcOpenUserInfo> ucOpenUserInfos = userInfoService.list(infoQueryWrapper);

                for (UcOpenUserInfo ucOpenUserInfo : ucOpenUserInfos) {
                    openids.add(ucOpenUserInfo.getUuid());
                }
                return openids;
            }
        }


        return openids;
    }

    @Override
    public List<WxMpTemplateData> getTemplateData() {
        String backTemplat = "";

        List<WxMpTemplateData> dataList = new ArrayList<>();

        if (repairParam.getProgress() == null) {
            throw new ServiceException(500, "请确定步骤流程");
        }
        Remind reminds = getReminds(repairParam.getProgress());
        backTemplat = reminds.getTemplateType();

        //获取模板推送数据
        String note = "";
        note = repairParam.getComment();


/**
 * 判断登录是否小程序
 */
        if (ToolUtil.isNotEmpty(repairParam.getName())) {
            QueryWrapper<WxuserInfo> wxuserInfoQueryWrapper = new QueryWrapper<>();
            wxuserInfoQueryWrapper.in("member_id", repairParam.getName());
            wxuserInfoQueryWrapper.in("source", "wxMp");
            WxuserInfo wxuserInfo = wxuserInfoService.getOne(wxuserInfoQueryWrapper);
            if (wxuserInfo != null) {
                //公众号查询派工人
                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.in("user_id", wxuserInfo.getUserId());
                User user = userService.getOne(userQueryWrapper);
                if (user != null) {
                    if (backTemplat.contains("{{name}}")) {
                        backTemplat = backTemplat.replace("{{name}}", user.getName());
                    }
                }
            } else {
                //企业微信查询派工人
                User user = userService.lambdaQuery().eq(User::getUserId, UserUtils.getUserId()).one();
                if (backTemplat.contains("{{name}}")) {
                    backTemplat = backTemplat.replace("{{name}}", user.getName());
                }
            }

        } else {
            if (backTemplat.contains("{{name}}")) {
                LoginUser user = LoginContextHolder.getContext().getUser();
                User one = userService.lambdaQuery().eq(User::getUserId, user.getId()).one();
                backTemplat = backTemplat.replace("{{name}}", one.getName());
            }
        }


        try {
            if (backTemplat.contains("{{time}}")) {
                Date date = new Date();
                String reateTime = String.valueOf(date);
                DateTime parse = DateUtil.parse(reateTime);
                String time = String.valueOf(parse);
                if (reminds.getTemplateType() != null) {
                    backTemplat = backTemplat.replace("{{time}}", time);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * 判断备注是否存在
         */
        if (reminds.getTemplateType().contains("{{note}}")) {
            if (note != null && backTemplat != null) {
                backTemplat = backTemplat.replace("{{note}}", note);
            } else {
                backTemplat = backTemplat.replace("{{note}}", "系统");
            }
        }

        /**
         * 判断详情是否存在
         */

        if (reminds.getTemplateType().contains("{{details}}")) {
            if (remindResult.getComment() != null && backTemplat != "") {

            } else {
                backTemplat = backTemplat.replace("{{details}}", " 祝你每天都有好心情! ");
            }
        }


        if (reminds.getTemplateType().contains("{{area}}")) {
            if (repairParam.getArea() != null) {
                RegionResult area = getArea(repairParam.getArea());
                backTemplat = backTemplat.replace("{{area}}", area.getProvince() + "/" + area.getCity() + "/" + area.getArea());
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
        Remind reminds = getReminds(repairParam.getProgress());
        WxTemplateData wxTemplateData = JSON.parseObject(reminds.getTemplateType(), WxTemplateData.class);
        String url = wxTemplateData.getUrl().replace("{{user}}", repairParam.getRepairId().toString());
        return url;
    }


    private Remind getReminds(Long type) {
        QueryWrapper<Remind> remindQueryWrapper = new QueryWrapper<>();
        Remind remindServiceOne = remindService.getOne(remindQueryWrapper.in("type", type));
        if (ToolUtil.isEmpty(remindServiceOne)) {
            throw new ServiceException(500, "模板消息发送失败!");
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

    private RegionResult getArea(String area) {
        RegionResult regionResult = new RegionResult();
        CommonArea commonArea = commonAreaService.lambdaQuery().eq(CommonArea::getId, area).one();
        regionResult.setArea(commonArea.getTitle());
        CommonArea city = commonAreaService.lambdaQuery().eq(CommonArea::getId, commonArea.getParentid()).one();
        regionResult.setCity(city.getTitle());
        CommonArea province = commonAreaService.lambdaQuery().eq(CommonArea::getId, city.getParentid()).one();
        regionResult.setProvince(province.getTitle());
        return regionResult;
    }

    /**
     * 企业微信推送消息
     *
     * @return
     */
    @Override
    public List<String> userIds() {
        if (repairParam.getProgress() == null) {
            throw new ServiceException(500, "请确定步骤流程");
        }
        Remind reminds = getReminds(0L);
        List<Long> memberIds = new ArrayList<>();
        List<String> uuIds = new ArrayList<>();
        List<Long> getremindUserids = getremindUserids(reminds.getRemindId());
        //添加派工人到集合 去绑定表里查
        if (ToolUtil.isNotEmpty(repairParam.getName())) {
            getremindUserids.add(repairParam.getName());
        }

        if (ToolUtil.isNotEmpty(getremindUserids)) {
            List<WxuserInfo> wxuserInfos = wxuserInfoService.lambdaQuery().in(WxuserInfo::getUserId, getremindUserids)
                    .and(i -> i.in(WxuserInfo::getSource, "wxCp"))
                    .list();

            if (ToolUtil.isNotEmpty(wxuserInfos)) {
                for (WxuserInfo wxuserInfo : wxuserInfos) {
                    memberIds.add(wxuserInfo.getMemberId());
                }
            }
            if (ToolUtil.isNotEmpty(memberIds)) {
                List<UcOpenUserInfo> ucOpenUserInfos = userInfoService.lambdaQuery().in(UcOpenUserInfo::getMemberId, memberIds)
                        .and(i -> i.in(UcOpenUserInfo::getSource, "wxCp"))
                        .list();
                if (ToolUtil.isNotEmpty(ucOpenUserInfos)) {
                    for (UcOpenUserInfo ucOpenUserInfo : ucOpenUserInfos) {
                        uuIds.add(ucOpenUserInfo.getUuid());
                    }
                }
            }

        }

        return uuIds;
    }

    @Override
    public String getTitle() {
        repairParam.getProgress();
        switch (repairParam.getProgress().toString()){
            case "0" :
                return "报修提醒";

            case "1":
                return "派工提醒";
            case "2" :
                return "派工接单提醒";

            case "3" :
                return "维修人员到达现场提醒";

            case "4" :
                return "维修完成提醒";
            case "5" :
                return "评价保修申请提醒";
            default:
                return "保修提醒";
        }
    }

    @Override
    public String getDescription() {
        repairParam.getProgress();
        String time = null;
        String note = null;
        String thing = null;
        String name = null;
        StringBuffer stringBuffer = new StringBuffer();
        List<WxMpTemplateData> data = this.getTemplateData();
        for (WxMpTemplateData datum : data) {
            if (datum.getName().equals("time2")) {
                time = datum.getValue();
                stringBuffer.append("时间"+"\n"+"\t"+time+"\n");
            } else if (datum.getName().equals("name1")) {
                name = datum.getValue();
                stringBuffer.append("名字"+"\n"+"\t"+name+"\n");
            } else if (datum.getName().equals("thing4")) {
                thing = datum.getValue();
                stringBuffer.append("事项"+"\n"+"\t"+thing+"\n");

            } else if (datum.getName().equals("thing5")) {
                note = datum.getValue();
                stringBuffer.append("备注"+"\n"+"\t"+note+"\n");
            }
        }



        return stringBuffer.toString();
    }

    @Override
    public String getUrl() {
        Remind reminds = getReminds(repairParam.getProgress());
        WxTemplateData wxTemplateData = JSON.parseObject(reminds.getTemplateType(), WxTemplateData.class);
        String url = wxTemplateData.getUrl().replace("{{user}}", repairParam.getRepairId().toString());
        return url;
    }


}
