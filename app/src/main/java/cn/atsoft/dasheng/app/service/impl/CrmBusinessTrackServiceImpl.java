package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.CrmBusiness;
import cn.atsoft.dasheng.app.service.CrmBusinessService;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusinessTrack;
import cn.atsoft.dasheng.app.mapper.CrmBusinessTrackMapper;
import cn.atsoft.dasheng.app.model.params.CrmBusinessTrackParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessTrackResult;
import cn.atsoft.dasheng.app.service.CrmBusinessTrackService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.Competitor;
import cn.atsoft.dasheng.crm.entity.CompetitorQuote;
import cn.atsoft.dasheng.crm.model.params.CompetitorQuoteParam;
import cn.atsoft.dasheng.crm.model.params.TrackMessageParam;
import cn.atsoft.dasheng.crm.service.CompetitorQuoteService;
import cn.atsoft.dasheng.crm.service.CompetitorService;
import cn.atsoft.dasheng.crm.service.TrackMessageService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>
 * 商机跟踪表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-08-04
 */
@Service
public class CrmBusinessTrackServiceImpl extends ServiceImpl<CrmBusinessTrackMapper, CrmBusinessTrack> implements CrmBusinessTrackService {


    @Autowired
    private UserService userService;
    @Autowired
    private CrmBusinessService businessService;
    @Autowired
    private CompetitorService competitorService;
    @Autowired
    private TrackMessageService trackMessageService;
    @Autowired
    private CompetitorQuoteService competitorQuoteService;


    @Override
    public void add(CrmBusinessTrackParam param) {

        CrmBusinessTrack entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CrmBusinessTrackParam param) {
        this.removeById(getKey(param));
    }


    @Override
    public void update(CrmBusinessTrackParam param) {
        CrmBusinessTrack oldEntity = getOldEntity(param);
        CrmBusinessTrack newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CrmBusinessTrackResult findBySpec(CrmBusinessTrackParam param) {
        return null;
    }

    @Override
    public List<CrmBusinessTrackResult> findListBySpec(CrmBusinessTrackParam param) {
        return null;
    }

    @Override
    public PageInfo<CrmBusinessTrackResult> findPageBySpec(CrmBusinessTrackParam param) {
        Page<CrmBusinessTrackResult> pageContext = getPageContext();
        IPage<CrmBusinessTrackResult> page = this.baseMapper.customPageList(pageContext, param);
        List<Long> createUsers = new ArrayList<>();
        for (CrmBusinessTrackResult record : page.getRecords()) {
            createUsers.add(record.getCreateUser());
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("user_id", createUsers);
        List<User> userList = userService.list(userQueryWrapper);
        for (CrmBusinessTrackResult record : page.getRecords()) {
            for (User user : userList) {
                if (record.getCreateUser().equals(user.getUserId())) {
                    UserResult userResult = new UserResult();
                    ToolUtil.copyProperties(user, userResult);
                    record.setUser(userResult);
                    break;
                }
            }
        }
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CrmBusinessTrackParam param) {
        return param.getTrackId();
    }

    private Page<CrmBusinessTrackResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CrmBusinessTrack getOldEntity(CrmBusinessTrackParam param) {
        return this.getById(getKey(param));
    }

    private CrmBusinessTrack getEntity(CrmBusinessTrackParam param) {
        CrmBusinessTrack entity = new CrmBusinessTrack();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
