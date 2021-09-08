package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.TrackMessage;
import cn.atsoft.dasheng.crm.mapper.TrackMessageMapper;
import cn.atsoft.dasheng.crm.model.params.TrackMessageParam;
import cn.atsoft.dasheng.crm.model.result.TrackMessageResult;
import  cn.atsoft.dasheng.crm.service.TrackMessageService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商机跟踪内容 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-09-07
 */
@Service
public class TrackMessageServiceImpl extends ServiceImpl<TrackMessageMapper, TrackMessage> implements TrackMessageService {

    @Override
    public void add(TrackMessageParam param){
        TrackMessage entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(TrackMessageParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(TrackMessageParam param){
        TrackMessage oldEntity = getOldEntity(param);
        TrackMessage newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public TrackMessageResult findBySpec(TrackMessageParam param){
        return null;
    }

    @Override
    public List<TrackMessageResult> findListBySpec(TrackMessageParam param){
        return null;
    }

    @Override
    public PageInfo<TrackMessageResult> findPageBySpec(TrackMessageParam param){
        Page<TrackMessageResult> pageContext = getPageContext();
        IPage<TrackMessageResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(TrackMessageParam param){
        return param.getTrackMessageId();
    }

    private Page<TrackMessageResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private TrackMessage getOldEntity(TrackMessageParam param) {
        return this.getById(getKey(param));
    }

    private TrackMessage getEntity(TrackMessageParam param) {
        TrackMessage entity = new TrackMessage();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
