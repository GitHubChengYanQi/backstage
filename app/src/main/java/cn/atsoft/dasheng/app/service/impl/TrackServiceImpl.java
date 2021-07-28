package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Track;
import cn.atsoft.dasheng.app.mapper.TrackMapper;
import cn.atsoft.dasheng.app.model.params.TrackParam;
import cn.atsoft.dasheng.app.model.result.TrackResult;
import cn.atsoft.dasheng.app.service.TrackService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 报价表 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-07-19
 */
@Service
public class TrackServiceImpl extends ServiceImpl<TrackMapper, Track> implements TrackService {

    @Override
    public Long add(TrackParam param){
        Track entity = getEntity(param);
        this.save(entity);
        return entity.getTrackId();
    }

    @Override
    public void delete(TrackParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(TrackParam param){
        Track oldEntity = getOldEntity(param);
        System.err.println("这是老的="+oldEntity+".......................................................................................");
        Track newEntity = getEntity(param);
        System.err.println("这是新的="+newEntity+".......................................................................................");
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public TrackResult findBySpec(TrackParam param){
        return null;
    }

    @Override
    public List<TrackResult> findListBySpec(TrackParam param){
        return null;
    }

    @Override
    public PageInfo<TrackResult> findPageBySpec(TrackParam param){
        Page<TrackResult> pageContext = getPageContext();
        IPage<TrackResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(TrackParam param){
        return param.getTrackId();
    }

    private Page<TrackResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Track getOldEntity(TrackParam param) {
        return this.getById(getKey(param));
    }

    private Track getEntity(TrackParam param) {
        Track entity = new Track();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
