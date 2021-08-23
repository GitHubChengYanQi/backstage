package cn.atsoft.dasheng.portal.dispatching.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.dispatching.entity.Dispatching;
import cn.atsoft.dasheng.portal.dispatching.mapper.DispatchingMapper;
import cn.atsoft.dasheng.portal.dispatching.model.params.DispatchingParam;
import cn.atsoft.dasheng.portal.dispatching.model.result.DispatchingResult;
import  cn.atsoft.dasheng.portal.dispatching.service.DispatchingService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 派工表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-08-23
 */
@Service
public class DispatchingServiceImpl extends ServiceImpl<DispatchingMapper, Dispatching> implements DispatchingService {

    @Override
    public void add(DispatchingParam param){
        Dispatching entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(DispatchingParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(DispatchingParam param){
        Dispatching oldEntity = getOldEntity(param);
        Dispatching newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DispatchingResult findBySpec(DispatchingParam param){
        return null;
    }

    @Override
    public List<DispatchingResult> findListBySpec(DispatchingParam param){
        return null;
    }

    @Override
    public PageInfo<DispatchingResult> findPageBySpec(DispatchingParam param){
        Page<DispatchingResult> pageContext = getPageContext();
        IPage<DispatchingResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(DispatchingParam param){
        return param.getDispatchingId();
    }

    private Page<DispatchingResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Dispatching getOldEntity(DispatchingParam param) {
        return this.getById(getKey(param));
    }

    private Dispatching getEntity(DispatchingParam param) {
        Dispatching entity = new Dispatching();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
