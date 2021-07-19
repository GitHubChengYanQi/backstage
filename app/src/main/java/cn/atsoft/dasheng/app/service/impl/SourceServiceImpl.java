package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Source;
import cn.atsoft.dasheng.app.mapper.SourceMapper;
import cn.atsoft.dasheng.app.model.params.SourceParam;
import cn.atsoft.dasheng.app.model.result.SourceResult;
import  cn.atsoft.dasheng.app.service.SourceService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 来源表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-07-19
 */
@Service
public class SourceServiceImpl extends ServiceImpl<SourceMapper, Source> implements SourceService {

    @Override
    public void add(SourceParam param){
        Source entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(SourceParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(SourceParam param){
        Source oldEntity = getOldEntity(param);
        Source newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SourceResult findBySpec(SourceParam param){
        return null;
    }

    @Override
    public List<SourceResult> findListBySpec(SourceParam param){
        return null;
    }

    @Override
    public PageInfo<SourceResult> findPageBySpec(SourceParam param){
        Page<SourceResult> pageContext = getPageContext();
        IPage<SourceResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SourceParam param){
        return param.getSourceId();
    }

    private Page<SourceResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Source getOldEntity(SourceParam param) {
        return this.getById(getKey(param));
    }

    private Source getEntity(SourceParam param) {
        Source entity = new Source();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
