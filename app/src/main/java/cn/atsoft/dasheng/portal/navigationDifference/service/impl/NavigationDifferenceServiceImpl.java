package cn.atsoft.dasheng.portal.navigationDifference.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.navigationDifference.entity.NavigationDifference;
import cn.atsoft.dasheng.portal.navigationDifference.mapper.NavigationDifferenceMapper;
import cn.atsoft.dasheng.portal.navigationDifference.model.params.NavigationDifferenceParam;
import cn.atsoft.dasheng.portal.navigationDifference.model.result.NavigationDifferenceResult;
import  cn.atsoft.dasheng.portal.navigationDifference.service.NavigationDifferenceService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 导航分类 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-08-18
 */
@Service
public class NavigationDifferenceServiceImpl extends ServiceImpl<NavigationDifferenceMapper, NavigationDifference> implements NavigationDifferenceService {

    @Override
    public void add(NavigationDifferenceParam param){
        NavigationDifference entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(NavigationDifferenceParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(NavigationDifferenceParam param){
        NavigationDifference oldEntity = getOldEntity(param);
        NavigationDifference newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public NavigationDifferenceResult findBySpec(NavigationDifferenceParam param){
        return null;
    }

    @Override
    public List<NavigationDifferenceResult> findListBySpec(NavigationDifferenceParam param){
        return null;
    }

    @Override
    public PageInfo<NavigationDifferenceResult> findPageBySpec(NavigationDifferenceParam param){
        Page<NavigationDifferenceResult> pageContext = getPageContext();
        IPage<NavigationDifferenceResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(NavigationDifferenceParam param){
        return param.getClassificationId();
    }

    private Page<NavigationDifferenceResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private NavigationDifference getOldEntity(NavigationDifferenceParam param) {
        return this.getById(getKey(param));
    }

    private NavigationDifference getEntity(NavigationDifferenceParam param) {
        NavigationDifference entity = new NavigationDifference();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
