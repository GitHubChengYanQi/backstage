package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Outbound;
import cn.atsoft.dasheng.app.mapper.OutboundMapper;
import cn.atsoft.dasheng.app.model.params.OutboundParam;
import cn.atsoft.dasheng.app.model.result.OutboundResult;
import  cn.atsoft.dasheng.app.service.OutboundService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 出库表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
@Service
public class OutboundServiceImpl extends ServiceImpl<OutboundMapper, Outbound> implements OutboundService {

    @Override
    public void add(OutboundParam param){
        Outbound entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(OutboundParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(OutboundParam param){
        Outbound oldEntity = getOldEntity(param);
        Outbound newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public OutboundResult findBySpec(OutboundParam param){
        return null;
    }

    @Override
    public List<OutboundResult> findListBySpec(OutboundParam param){
        return null;
    }

    @Override
    public PageInfo<OutboundResult> findPageBySpec(OutboundParam param){
        Page<OutboundResult> pageContext = getPageContext();
        IPage<OutboundResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(OutboundParam param){
        return param.getOutboundId();
    }

    private Page<OutboundResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Outbound getOldEntity(OutboundParam param) {
        return this.getById(getKey(param));
    }

    private Outbound getEntity(OutboundParam param) {
        Outbound entity = new Outbound();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
