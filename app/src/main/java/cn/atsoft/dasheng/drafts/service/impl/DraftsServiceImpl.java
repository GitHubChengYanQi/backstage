package cn.atsoft.dasheng.drafts.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.drafts.entity.Drafts;
import cn.atsoft.dasheng.drafts.mapper.DraftsMapper;
import cn.atsoft.dasheng.drafts.model.params.DraftsParam;
import cn.atsoft.dasheng.drafts.model.result.DraftsResult;
import  cn.atsoft.dasheng.drafts.service.DraftsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 草稿箱 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-21
 */
@Service
public class DraftsServiceImpl extends ServiceImpl<DraftsMapper, Drafts> implements DraftsService {

    @Autowired
    private UserService userService;
    @Override
    public void add(DraftsParam param){
        Drafts entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(DraftsParam param){
        Drafts byId = this.getById(getKey(param));
        byId.setDisplay(0);
        this.updateById(byId);
    }

    @Override
    public void update(DraftsParam param){
        Drafts oldEntity = getOldEntity(param);
        Drafts newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        newEntity.setDisplay(0);
        this.updateById(newEntity);
    }

    @Override
    public DraftsResult findBySpec(DraftsParam param){
        return null;
    }

    @Override
    public List<DraftsResult> findListBySpec(DraftsParam param){
        return null;
    }

    @Override
    public PageInfo<DraftsResult> findPageBySpec(DraftsParam param){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (ToolUtil.isNotEmpty(param.getDates()) && param.getDates().size() == 2){
            param.setStartDay(sdf.format(param.getDates().get(0)));
            param.setEndDay(sdf.format(param.getDates().get(1)));
        }

        Page<DraftsResult> pageContext = getPageContext();
        IPage<DraftsResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());

        for (DraftsResult record : page.getRecords()) {
            record.setInfo(null);
        }
        return PageFactory.createPageInfo(page);
    }
    @Override
    public void format(List<DraftsResult> param){
        List<Long> userIds = new ArrayList<>();
        for (DraftsResult draftsResult : param) {
            userIds.add(draftsResult.getCreateUser());
        }
        List<UserResult> userResults = userService.getUserResultsByIds(userIds);
        for (UserResult userResult : userResults) {
            for (DraftsResult draftsResult : param) {
                if (userResult.getUserId().equals(draftsResult.getCreateUser())){
                    draftsResult.setCreateUserResult(userResult);
                }
            }
        }
    }

    private Serializable getKey(DraftsParam param){
        return param.getDraftsId();
    }

    private Page<DraftsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Drafts getOldEntity(DraftsParam param) {
        return this.getById(getKey(param));
    }

    private Drafts getEntity(DraftsParam param) {
        Drafts entity = new Drafts();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
