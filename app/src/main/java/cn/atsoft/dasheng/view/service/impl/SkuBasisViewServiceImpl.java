package cn.atsoft.dasheng.view.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.view.entity.SkuBasisView;
import cn.atsoft.dasheng.view.mapper.SkuBasisViewMapper;
import cn.atsoft.dasheng.view.model.params.SkuBasisViewParam;
import cn.atsoft.dasheng.view.model.result.SkuBasisViewResult;
import  cn.atsoft.dasheng.view.service.SkuBasisViewService;
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
public class SkuBasisViewServiceImpl extends ServiceImpl<SkuBasisViewMapper, SkuBasisView> implements SkuBasisViewService {

    @Override
    public void add(SkuBasisViewParam param){
        SkuBasisView entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(SkuBasisViewParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(SkuBasisViewParam param){
        SkuBasisView oldEntity = getOldEntity(param);
        SkuBasisView newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SkuBasisViewResult findBySpec(SkuBasisViewParam param){
        return null;
    }

    @Override
    public List<SkuBasisViewResult> findListBySpec(SkuBasisViewParam param){
        return null;
    }

    @Override
    public PageInfo<SkuBasisViewResult> findPageBySpec(SkuBasisViewParam param){
        Page<SkuBasisViewResult> pageContext = getPageContext();
        IPage<SkuBasisViewResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SkuBasisViewParam param){
        return null;
    }

    private Page<SkuBasisViewResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private SkuBasisView getOldEntity(SkuBasisViewParam param) {
        return this.getById(getKey(param));
    }

    private SkuBasisView getEntity(SkuBasisViewParam param) {
        SkuBasisView entity = new SkuBasisView();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
