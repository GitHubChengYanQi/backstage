package cn.atsoft.dasheng.portal.banner.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.banner.service.BannerService;
import cn.atsoft.dasheng.portal.banner.mapper.BannerMapper;
import cn.atsoft.dasheng.portal.banner.entity.Banner;
import cn.atsoft.dasheng.portal.banner.model.params.BannerParam;
import cn.atsoft.dasheng.portal.banner.model.result.BannerResult;
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
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    @Override
    public void add(BannerParam param){
        Banner entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(BannerParam param){
        param.setDisplay(0);
        this.update(param);
//        this.removeById(getKey(param));
    }

    @Override
    public void update(BannerParam param){
        Banner oldEntity = getOldEntity(param);
        Banner newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public BannerResult findBySpec(BannerParam param){
        return null;
    }

    @Override
    public List<BannerResult> findListBySpec(BannerParam param){
        return null;
    }

    @Override
    public PageInfo<BannerResult> findPageBySpec(BannerParam param){
        Page<BannerResult> pageContext = getPageContext();
        IPage<BannerResult> page = this.baseMapper.customPageList(pageContext, param);

        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(BannerParam param){
        return param.getBannerId();
    }

    private Page<BannerResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Banner getOldEntity(BannerParam param) {
        return this.getById(getKey(param));
    }

    private Banner getEntity(BannerParam param) {
        Banner entity = new Banner();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
