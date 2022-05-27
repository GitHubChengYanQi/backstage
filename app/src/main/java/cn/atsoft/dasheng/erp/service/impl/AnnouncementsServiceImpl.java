package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Announcements;
import cn.atsoft.dasheng.erp.mapper.AnnouncementsMapper;
import cn.atsoft.dasheng.erp.model.params.AnnouncementsParam;
import cn.atsoft.dasheng.erp.model.result.AnnouncementsResult;
import  cn.atsoft.dasheng.erp.service.AnnouncementsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 注意事项 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-05-27
 */
@Service
public class AnnouncementsServiceImpl extends ServiceImpl<AnnouncementsMapper, Announcements> implements AnnouncementsService {

    @Override
    public void add(AnnouncementsParam param){
        Announcements entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(AnnouncementsParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(AnnouncementsParam param){
        Announcements oldEntity = getOldEntity(param);
        Announcements newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AnnouncementsResult findBySpec(AnnouncementsParam param){
        return null;
    }

    @Override
    public List<AnnouncementsResult> findListBySpec(AnnouncementsParam param){
        return null;
    }

    @Override
    public PageInfo<AnnouncementsResult> findPageBySpec(AnnouncementsParam param){
        Page<AnnouncementsResult> pageContext = getPageContext();
        IPage<AnnouncementsResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AnnouncementsParam param){
        return param.getNoticeId();
    }

    private Page<AnnouncementsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Announcements getOldEntity(AnnouncementsParam param) {
        return this.getById(getKey(param));
    }

    private Announcements getEntity(AnnouncementsParam param) {
        Announcements entity = new Announcements();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
