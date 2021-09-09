package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.app.entity.CrmBusiness;
import cn.atsoft.dasheng.app.service.CrmBusinessService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Competitor;
import cn.atsoft.dasheng.crm.entity.TrackMessage;
import cn.atsoft.dasheng.crm.mapper.TrackMessageMapper;
import cn.atsoft.dasheng.crm.model.params.CompetitorQuoteParam;
import cn.atsoft.dasheng.crm.model.params.TrackMessageParam;
import cn.atsoft.dasheng.crm.model.result.TrackMessageResult;
import cn.atsoft.dasheng.crm.service.CompetitorQuoteService;
import cn.atsoft.dasheng.crm.service.CompetitorService;
import cn.atsoft.dasheng.crm.service.TrackMessageService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商机跟踪内容 服务实现类
 * </p>
 *
 * @author
 * @since 2021-09-07
 */
@Service
public class TrackMessageServiceImpl extends ServiceImpl<TrackMessageMapper, TrackMessage> implements TrackMessageService {

    @Autowired
    private CrmBusinessService crmBusinessService;
    @Autowired
    private CompetitorService competitorService;
    @Autowired
    private CompetitorQuoteService competitorQuoteService;
    @Autowired
    private UserService userService;

    @Override
    public void add(TrackMessageParam param) {
        LoginUser user = LoginContextHolder.getContext().getUser();
        param.setUserId(user.getId());
        String message = null;
        List<CompetitorQuoteParam> competitorQuoteParams = param.getCompetitorQuoteParam();
        List<Long> competitorIds = new ArrayList<>();
        Integer money = null;
        for (CompetitorQuoteParam data : competitorQuoteParams) {
            data.setBusinessId(param.getBusinessId());
            data.setCampType(0);
            competitorQuoteService.addTrack(data);

            competitorIds.add(data.getCompetitorId());
            money = data.getCompetitorsQuote();
            CompetitorQuoteParam competitorQuote = new CompetitorQuoteParam();
            competitorQuote.setCompetitorId(data.getCompetitorId());
            competitorQuote.setBusinessId(param.getBusinessId());
            competitorQuote.setQuoteStatus(param.getQuoteStatus());
            competitorQuote.setCompetitorsQuote(param.getMoney());
            competitorQuote.setCampType(1);
            competitorQuoteService.addTrack(competitorQuote);
        }
//        List<String> names = new ArrayList<>();
//        if (ToolUtil.isNotEmpty(competitorIds)) {
//            List<Competitor> competitors = competitorService.lambdaQuery().in(Competitor::getCompetitorId, competitorIds).list();
//            for (Competitor competitor : competitors) {
//                names.add(competitor.getName());
//            }
//            if (param.getBusinessId() != null) {
//                CrmBusiness crmBusiness = crmBusinessService.lambdaQuery().eq(CrmBusiness::getBusinessId, param.getBusinessId()).one();
//                param.setMessage("当前商机：" + crmBusiness.getBusinessName() + "   当前竞争对手：" + names.toString() + "   添加了新的报价：" + money + "RMB");
////                if (param.getTime()!=null) {
////                    message="商机：" + crmBusiness.getBusinessName() + "   竞争对手" + names.toString() + "   添加了报价" + money+"RMB"
////                }
//                if (param.getMoney() != null) {
//                    param.setMessage("当前商机：" + crmBusiness.getBusinessName() + "      自己添加了报价：" + param.getMoney() + "RMB" + "   当前竞争对手：" + names.toString() + "   添加了新的报价：" + money + "RMB");
//                    if (param.getNote() != null) {
//                        param.setMessage("当前商机：" + crmBusiness.getBusinessName() + "      自己添加了报价：" + param.getMoney() + "RMB" + "   当前竞争对手：" + names.toString() + "   添加了新的报价：" + money + "RMB" + "(设置了提醒消息)");
//                    }
//                }
//
//            }
//        }

        TrackMessage entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void byCompetitionAdd(TrackMessageParam param) {
        TrackMessage entity = getEntity(param);
        this.save(entity);
    }

//    @Override
//    public void delete(TrackMessageParam param){
//        this.removeById(getKey(param));
//    }
//
//    @Override
//    public void update(TrackMessageParam param){
//        TrackMessage oldEntity = getOldEntity(param);
//        TrackMessage newEntity = getEntity(param);
//        ToolUtil.copyProperties(newEntity, oldEntity);
//        this.updateById(newEntity);
//    }

    @Override
    public TrackMessageResult findBySpec(TrackMessageParam param) {
        return null;
    }

    @Override
    public List<TrackMessageResult> findListBySpec(TrackMessageParam param) {
        return null;
    }

    @Override
    public PageInfo<TrackMessageResult> findPageBySpec(TrackMessageParam param) {
        Page<TrackMessageResult> pageContext = getPageContext();
        IPage<TrackMessageResult> page = this.baseMapper.customPageList(pageContext, param);
        List<Long> ids = new ArrayList<>();
        for (TrackMessageResult record : page.getRecords()) {
            ids.add(record.getUserId());
        }
        if (ToolUtil.isNotEmpty(ids)) {
            List<User> users = userService.lambdaQuery().in(User::getUserId, ids).list();
            if (ToolUtil.isNotEmpty(users)) {
                for (TrackMessageResult record : page.getRecords()) {
                    for (User user : users) {
                        UserResult userResult = new UserResult();
                        ToolUtil.copyProperties(user, userResult);
                        record.setUserResult(userResult);
                    }

                }
            }

        }

        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(TrackMessageParam param) {
        return param.getTrackMessageId();
    }

    private Page<TrackMessageResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private TrackMessage getOldEntity(TrackMessageParam param) {
        return this.getById(getKey(param));
    }

    private TrackMessage getEntity(TrackMessageParam param) {
        TrackMessage entity = new TrackMessage();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
