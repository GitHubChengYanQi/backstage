package cn.atsoft.dasheng.view.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.view.entity.SkuSupplyView;
import cn.atsoft.dasheng.view.mapper.SkuSupplyViewMapper;
import cn.atsoft.dasheng.view.model.params.SkuSupplyViewParam;
import cn.atsoft.dasheng.view.model.result.SkuSupplyViewResult;
import  cn.atsoft.dasheng.view.service.SkuSupplyViewService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author 
 * @since 2022-05-20
 */
@Service
public class SkuSupplyViewServiceImpl extends ServiceImpl<SkuSupplyViewMapper, SkuSupplyView> implements SkuSupplyViewService {

    @Override
    public void add(SkuSupplyViewParam param){
        SkuSupplyView entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(SkuSupplyViewParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(SkuSupplyViewParam param){
        SkuSupplyView oldEntity = getOldEntity(param);
        SkuSupplyView newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SkuSupplyViewResult findBySpec(SkuSupplyViewParam param){
        return null;
    }

    @Override
    public List<SkuSupplyViewResult> findListBySpec(SkuSupplyViewParam param){
        return null;
    }

    @Override
    public PageInfo<SkuSupplyViewResult> findPageBySpec(SkuSupplyViewParam param){
        Page<SkuSupplyViewResult> pageContext = getPageContext();
        IPage<SkuSupplyViewResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SkuSupplyViewParam param){
        return null;
    }

    private Page<SkuSupplyViewResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private SkuSupplyView getOldEntity(SkuSupplyViewParam param) {
        return this.getById(getKey(param));
    }

    private SkuSupplyView getEntity(SkuSupplyViewParam param) {
        SkuSupplyView entity = new SkuSupplyView();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
