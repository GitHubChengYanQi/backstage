package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.entity.CrmBusiness;
import cn.atsoft.dasheng.app.model.result.TrackNumberResult;
import cn.atsoft.dasheng.app.service.ContactsService;
import cn.atsoft.dasheng.app.service.ContractService;
import cn.atsoft.dasheng.app.service.CrmBusinessService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.BusinessTrack;
import cn.atsoft.dasheng.app.mapper.BusinessTrackMapper;
import cn.atsoft.dasheng.app.model.params.BusinessTrackParam;
import cn.atsoft.dasheng.app.model.result.BusinessTrackResult;
import cn.atsoft.dasheng.app.service.BusinessTrackService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.Hutool;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xkcoding.http.support.hutool.HutoolImpl;
import io.swagger.annotations.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 跟进内容 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-09-17
 */
@Service
public class BusinessTrackServiceImpl extends ServiceImpl<BusinessTrackMapper, BusinessTrack> implements BusinessTrackService {
    @Autowired
    private UserService userService;
    @Autowired
    private CrmBusinessService crmBusinessService;
    @Autowired
    private ContractService contractService;

    @Override
    public void add(BusinessTrackParam param) {
        BusinessTrack entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(BusinessTrackParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(BusinessTrackParam param) {
        BusinessTrack oldEntity = getOldEntity(param);
        BusinessTrack newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public BusinessTrackResult findBySpec(BusinessTrackParam param) {
        return null;
    }

    @Override
    public List<BusinessTrackResult> findListBySpec(BusinessTrackParam param) {
        return null;
    }

    @Override
    public PageInfo findPageBySpec(BusinessTrackParam param, DataScope dataScope) {

        List<Long> trackMessageIds = param.getTrackMessageIds();

        if (trackMessageIds == null || trackMessageIds.size() <= 0) {
            trackMessageIds = new ArrayList<>();
            trackMessageIds.add(0L);
        }

        Page<BusinessTrackResult> pageContext = getPageContext();
        IPage<BusinessTrackResult> page = this.baseMapper.customPageList(trackMessageIds, pageContext, param, dataScope);
        List<Long> ids = new ArrayList<>();
        for (BusinessTrackResult record : page.getRecords()) {
            ids.add(record.getUserId());
        }

        List<User> users = userService.list();


        for (BusinessTrackResult record : page.getRecords()) {
            switch (record.getClassify()) {
                case 0:
                    record.setCategoryName("日常");
                    break;
                case 1:
                    CrmBusiness crmBusiness = crmBusinessService.lambdaQuery().eq(CrmBusiness::getBusinessId, record.getClassifyId()).one();
                    if (ToolUtil.isNotEmpty(crmBusiness)) {
                        record.setCategoryName("项目");
                        record.setName(crmBusiness.getBusinessName());
                    }
                    break;
                case 2:
                    Contract contract = contractService.lambdaQuery().eq(Contract::getContractId, record.getClassifyId()).one();
                    if (ToolUtil.isNotEmpty(contract)) {
                        record.setCategoryName("合同");
                        record.setName(contract.getName());
                    }
                    break;
                case 3:
                    record.setCategoryName("订单");
                    break;
                case 4:
                    record.setCategoryName("回款");
                    break;
            }


            for (User user : users) {
                if (ToolUtil.isNotEmpty(record.getUserId())) {
                    if (record.getUserId().equals(user.getUserId())) {
                        UserResult userResult = new UserResult();
                        ToolUtil.copyProperties(user, userResult);
                        record.setUserResult(userResult);
                        break;
                    }
                }
            }

        }
        return PageFactory.createPageInfo(page);
    }

    @Override
    public TrackNumberResult findNumber() {
        //查询日常数量
        Integer dailyNumber = this.lambdaQuery().in(BusinessTrack::getClassify, "0").count();
        //查询项目数量
        Integer businessNumber = this.lambdaQuery().in(BusinessTrack::getClassify, "1").count();
        //查询合同数量
        Integer contractNumber = this.lambdaQuery().in(BusinessTrack::getClassify, "2").count();
        //查询已超时数量
        List<BusinessTrack> businessTracks = this.list();
        int i = 0;
        for (BusinessTrack businessTrack : businessTracks) {
            if (ToolUtil.isNotEmpty(businessTrack)) {
                if (ToolUtil.isNotEmpty(businessTrack.getTime())) {
                    Date date = new Date();
                    boolean after = date.after(businessTrack.getTime());
                    if (after) {
                        ++i;
                    }
                }
            }
        }

        TrackNumberResult trackNumberResult = new TrackNumberResult();
        trackNumberResult.setBusinessNumber(businessNumber);
        trackNumberResult.setDailyNumber(dailyNumber);
        trackNumberResult.setContractNumber(contractNumber);
        trackNumberResult.setTimeout(i);
        return trackNumberResult;
    }

    private Serializable getKey(BusinessTrackParam param) {
        return param.getTrackId();
    }

    private Page<BusinessTrackResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private BusinessTrack getOldEntity(BusinessTrackParam param) {
        return this.getById(getKey(param));
    }

    private BusinessTrack getEntity(BusinessTrackParam param) {
        BusinessTrack entity = new BusinessTrack();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
