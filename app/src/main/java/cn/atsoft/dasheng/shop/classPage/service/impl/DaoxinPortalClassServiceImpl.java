package cn.atsoft.dasheng.protal.classPage.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.protal.classPage.entity.DaoxinPortalClass;
import cn.atsoft.dasheng.protal.classPage.mapper.DaoxinPortalClassMapper;
import cn.atsoft.dasheng.protal.classPage.model.params.DaoxinPortalClassParam;
import cn.atsoft.dasheng.protal.classPage.model.result.DaoxinPortalClassResult;
import  cn.atsoft.dasheng.protal.classPage.service.DaoxinPortalClassService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 分类导航 服务实现类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-18
 */
@Service
public class DaoxinPortalClassServiceImpl extends ServiceImpl<DaoxinPortalClassMapper, DaoxinPortalClass> implements DaoxinPortalClassService {

    @Override
    public void add(DaoxinPortalClassParam param){
        DaoxinPortalClass entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(DaoxinPortalClassParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(DaoxinPortalClassParam param){
        DaoxinPortalClass oldEntity = getOldEntity(param);
        DaoxinPortalClass newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DaoxinPortalClassResult findBySpec(DaoxinPortalClassParam param){
        return null;
    }

    @Override
    public List<DaoxinPortalClassResult> findListBySpec(DaoxinPortalClassParam param){
        return null;
    }

    @Override
    public PageInfo<DaoxinPortalClassResult> findPageBySpec(DaoxinPortalClassParam param){
        Page<DaoxinPortalClassResult> pageContext = getPageContext();
        IPage<DaoxinPortalClassResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(DaoxinPortalClassParam param){
        return param.getClassId();
    }

    private Page<DaoxinPortalClassResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private DaoxinPortalClass getOldEntity(DaoxinPortalClassParam param) {
        return this.getById(getKey(param));
    }

    private DaoxinPortalClass getEntity(DaoxinPortalClassParam param) {
        DaoxinPortalClass entity = new DaoxinPortalClass();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
