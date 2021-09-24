package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.app.entity.BusinessTrack;
import cn.atsoft.dasheng.app.model.params.BusinessTrackParam;
import cn.atsoft.dasheng.app.model.result.BusinessTrackResult;
import cn.atsoft.dasheng.app.service.BusinessTrackService;
import cn.atsoft.dasheng.app.service.CrmBusinessService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.CompetitorQuote;
import cn.atsoft.dasheng.crm.entity.TrackMessage;
import cn.atsoft.dasheng.crm.mapper.TrackMessageMapper;
import cn.atsoft.dasheng.crm.model.params.CompetitorQuoteParam;
import cn.atsoft.dasheng.crm.model.params.TrackMessageParam;
import cn.atsoft.dasheng.crm.model.result.TrackMessageResult;
import cn.atsoft.dasheng.crm.service.CompetitorQuoteService;
import cn.atsoft.dasheng.crm.service.CompetitorService;
import cn.atsoft.dasheng.crm.service.TrackMessageService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
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
    private CompetitorQuoteService competitorQuoteService;
    @Autowired
    private UserService userService;
    @Autowired
    private BusinessTrackService businessTrackService;


    //
    @Override
    @BussinessLog
    public TrackMessage add(TrackMessageParam param) {
        LoginUser user = LoginContextHolder.getContext().getUser();
        param.setUserId(user.getId());
        List<CompetitorQuoteParam> competitorQuoteParams = param.getCompetitorQuoteParam();
        if (ToolUtil.isEmpty(competitorQuoteParams)) {
            throw new ServiceException(500, "请选择当前流程");
        }
        // 添加对手/我放报价
        List<CompetitorQuote> competitorQuotes = new ArrayList<>();
        for (CompetitorQuoteParam data : competitorQuoteParams) {
            data.setBusinessId(param.getBusinessId());
            CompetitorQuote competitorQuote = new CompetitorQuote();
            ToolUtil.copyProperties(data, competitorQuote);
            competitorQuote.setCampType(1L);
            competitorQuotes.add(competitorQuote);
        }
        if (ToolUtil.isNotEmpty(param.getBusinessTrackParams())) {
            for (BusinessTrackParam businessTrackParam : param.getBusinessTrackParams()) {
                CompetitorQuote competitorQuote = new CompetitorQuote();
                competitorQuote.setCompetitorsQuote(businessTrackParam.getMoney());
                Integer classify = businessTrackParam.getClassify();
                competitorQuote.setCampType(0l);
                competitorQuotes.add(competitorQuote);
            }
        }

        competitorQuoteService.saveBatch(competitorQuotes);

        TrackMessage entity = getEntity(param);
        this.save(entity);
        // 添加跟进内容
        if (ToolUtil.isNotEmpty(param.getBusinessTrackParams())) {
            List<BusinessTrack> businessTracks = new ArrayList<>();
            for (BusinessTrackParam businessTrackParam : param.getBusinessTrackParams()) {
                businessTrackParam.setTrackMessageId(entity.getTrackMessageId());
                BusinessTrack businessTrack = new BusinessTrack();
                ToolUtil.copyProperties(businessTrackParam, businessTrack);
                LoginUser loginUser = LoginContextHolder.getContext().getUser();
                businessTrack.setUserId(loginUser.getId());
                businessTracks.add(businessTrack);
            }
            if (ToolUtil.isNotEmpty(businessTracks)) {
                businessTrackService.saveBatch(businessTracks);
            }

        }


        return entity;
    }


    @Override
    public TrackMessageResult findBySpec(TrackMessageParam param) {
        return null;
    }

    @Override
    public List<TrackMessageResult> findListBySpec(TrackMessageParam param) {
        List<TrackMessageResult> trackMessageResults = this.baseMapper.customList(param);
        return trackMessageResults;
    }

    @Override
    public PageInfo<TrackMessageResult> findPageBySpec(TrackMessageParam param) {
        Page<TrackMessageResult> pageContext = getPageContext();
        IPage<TrackMessageResult> page = this.baseMapper.customPageList(pageContext, param);
        List<Long> ids = new ArrayList<>();
        List<Long> trackMessageIds = new ArrayList<>();
        for (TrackMessageResult record : page.getRecords()) {
            ids.add(record.getUserId());
            trackMessageIds.add(record.getTrackMessageId());
        }
        if (ToolUtil.isNotEmpty(ids)) {
            List<User> users = userService.lambdaQuery().in(User::getUserId, ids).list();
            List<BusinessTrack> businessTracks = trackMessageIds.size() == 0 ? new ArrayList<>()
                    : businessTrackService.lambdaQuery()
                    .in(BusinessTrack::getTrackMessageId, trackMessageIds)
                    .list();
            if (ToolUtil.isNotEmpty(users)) {
                for (TrackMessageResult record : page.getRecords()) {
                    for (User user : users) {
                        UserResult userResult = new UserResult();
                        ToolUtil.copyProperties(user, userResult);
                        record.setUserResult(userResult);
                    }
                    for (BusinessTrack businessTrack : businessTracks) {
                        if (businessTrack.getTrackMessageId().equals(record.getTrackMessageId())) {
                            BusinessTrackResult businessTrackResult = new BusinessTrackResult();
                            ToolUtil.copyProperties(businessTrack, businessTrackResult);
                            record.setBusinessTrackResult(businessTrackResult);
                        }
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
