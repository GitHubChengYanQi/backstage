package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Logistics;
import cn.atsoft.dasheng.app.mapper.LogisticsMapper;
import cn.atsoft.dasheng.app.model.params.LogisticsParam;
import cn.atsoft.dasheng.app.model.result.LogisticsResult;
import  cn.atsoft.dasheng.app.service.LogisticsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 物流表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
@Service
public class LogisticsServiceImpl extends ServiceImpl<LogisticsMapper, Logistics> implements LogisticsService {

    @Override
    public void add(LogisticsParam param){
        Logistics entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(LogisticsParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(LogisticsParam param){
        Logistics oldEntity = getOldEntity(param);
        Logistics newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public LogisticsResult findBySpec(LogisticsParam param){
        return null;
    }

    @Override
    public List<LogisticsResult> findListBySpec(LogisticsParam param){
        return null;
    }

    @Override
    public PageInfo<LogisticsResult> findPageBySpec(LogisticsParam param){
        Page<LogisticsResult> pageContext = getPageContext();
        IPage<LogisticsResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(LogisticsParam param){
        return param.getLogisticsId();
    }

    private Page<LogisticsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Logistics getOldEntity(LogisticsParam param) {
        return this.getById(getKey(param));
    }

    private Logistics getEntity(LogisticsParam param) {
        Logistics entity = new Logistics();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
