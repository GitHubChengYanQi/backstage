package cn.atsoft.dasheng.view.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.view.entity.SkuPositionView;
import cn.atsoft.dasheng.view.mapper.SkuPositionViewMapper;
import cn.atsoft.dasheng.view.model.params.SkuPositionViewParam;
import cn.atsoft.dasheng.view.model.result.SkuPositionViewResult;
import  cn.atsoft.dasheng.view.service.SkuPositionViewService;
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
public class SkuPositionViewServiceImpl extends ServiceImpl<SkuPositionViewMapper, SkuPositionView> implements SkuPositionViewService {

    @Override
    public void add(SkuPositionViewParam param){
        SkuPositionView entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(SkuPositionViewParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(SkuPositionViewParam param){
        SkuPositionView oldEntity = getOldEntity(param);
        SkuPositionView newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SkuPositionViewResult findBySpec(SkuPositionViewParam param){
        return null;
    }

    @Override
    public List<SkuPositionViewResult> findListBySpec(SkuPositionViewParam param){
        return null;
    }

    @Override
    public PageInfo<SkuPositionViewResult> findPageBySpec(SkuPositionViewParam param){
        Page<SkuPositionViewResult> pageContext = getPageContext();
        IPage<SkuPositionViewResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SkuPositionViewParam param){
        return null;
    }

    private Page<SkuPositionViewResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private SkuPositionView getOldEntity(SkuPositionViewParam param) {
        return this.getById(getKey(param));
    }

    private SkuPositionView getEntity(SkuPositionViewParam param) {
        SkuPositionView entity = new SkuPositionView();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
