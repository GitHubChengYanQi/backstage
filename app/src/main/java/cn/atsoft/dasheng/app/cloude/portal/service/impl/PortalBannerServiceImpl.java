package cn.atsoft.dasheng.app.cloude.portal.service.impl;


import cn.atsoft.dasheng.app.cloude.portal.entity.PortalBanner;
import cn.atsoft.dasheng.app.cloude.portal.mapper.PortalBannerMapper;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.cloude.portal.model.params.PortalBannerParam;
import cn.atsoft.dasheng.app.cloude.portal.model.result.PortalBannerResult;
import cn.atsoft.dasheng.app.cloude.portal.service.PortalBannerService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 轮播图 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-08-17
 */
@Service
public class PortalBannerServiceImpl extends ServiceImpl<PortalBannerMapper, PortalBanner> implements PortalBannerService {

    @Override
    public void add(PortalBannerParam param){
        PortalBanner entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PortalBannerParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(PortalBannerParam param){
        PortalBanner oldEntity = getOldEntity(param);
        PortalBanner newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PortalBannerResult findBySpec(PortalBannerParam param){
        return null;
    }

    @Override
    public List<PortalBannerResult> findListBySpec(PortalBannerParam param){
        return null;
    }

    @Override
    public PageInfo<PortalBannerResult> findPageBySpec(PortalBannerParam param){
        Page<PortalBannerResult> pageContext = getPageContext();
        IPage<PortalBannerResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PortalBannerParam param){
        return param.getBannerId();
    }

    private Page<PortalBannerResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private PortalBanner getOldEntity(PortalBannerParam param) {
        return this.getById(getKey(param));
    }

    private PortalBanner getEntity(PortalBannerParam param) {
        PortalBanner entity = new PortalBanner();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
