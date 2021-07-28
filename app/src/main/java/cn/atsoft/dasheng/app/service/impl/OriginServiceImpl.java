package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Origin;
import cn.atsoft.dasheng.app.mapper.OriginMapper;
import cn.atsoft.dasheng.app.model.params.OriginParam;
import cn.atsoft.dasheng.app.model.result.OriginResult;
import cn.atsoft.dasheng.app.service.OriginService;
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
public class OriginServiceImpl extends ServiceImpl<OriginMapper, Origin> implements OriginService {

    @Override
    public void add(OriginParam param){
        Origin entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(OriginParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(OriginParam param){
        Origin oldEntity = getOldEntity(param);
        Origin newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public OriginResult findBySpec(OriginParam param){
        return null;
    }

    @Override
    public List<OriginResult> findListBySpec(OriginParam param){
        return null;
    }

    @Override
    public PageInfo<OriginResult> findPageBySpec(OriginParam param){
        Page<OriginResult> pageContext = getPageContext();
        IPage<OriginResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(OriginParam param){
        return param.getOriginId();
    }

    private Page<OriginResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Origin getOldEntity(OriginParam param) {
        return this.getById(getKey(param));
    }

    private Origin getEntity(OriginParam param) {
        Origin entity = new Origin();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
