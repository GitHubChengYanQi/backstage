package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProcessRoute;
import cn.atsoft.dasheng.production.mapper.ProcessRouteMapper;
import cn.atsoft.dasheng.production.model.params.ProcessRouteParam;
import cn.atsoft.dasheng.production.model.result.ProcessRouteResult;
import cn.atsoft.dasheng.production.service.ProcessRouteService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 工艺路线列表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-02-15
 */
@Service
public class ProcessRouteServiceImpl extends ServiceImpl<ProcessRouteMapper, ProcessRoute> implements ProcessRouteService {

    @Override
    public void add(ProcessRouteParam param) {
        ProcessRoute entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProcessRouteParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProcessRouteParam param) {
        ProcessRoute oldEntity = getOldEntity(param);
        ProcessRoute newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProcessRouteResult findBySpec(ProcessRouteParam param) {
        return null;
    }

    @Override
    public List<ProcessRouteResult> findListBySpec(ProcessRouteParam param) {
        return null;
    }

    @Override
    public PageInfo<ProcessRouteResult> findPageBySpec(ProcessRouteParam param) {
        Page<ProcessRouteResult> pageContext = getPageContext();
        IPage<ProcessRouteResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProcessRouteParam param) {
        return null;
    }

    private Page<ProcessRouteResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProcessRoute getOldEntity(ProcessRouteParam param) {
        return this.getById(getKey(param));
    }

    private ProcessRoute getEntity(ProcessRouteParam param) {
        ProcessRoute entity = new ProcessRoute();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public ProcessRouteResult detail(Long id) {
        ProcessRoute processRoute = this.getById(id);
        ProcessRouteResult routeResult = new ProcessRouteResult();
        ToolUtil.copyProperties(processRoute, routeResult);
        return routeResult;
    }
}
