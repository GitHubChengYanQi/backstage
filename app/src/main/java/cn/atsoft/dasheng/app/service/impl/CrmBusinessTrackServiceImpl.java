package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.CrmBusinessSalesProcess;
import cn.atsoft.dasheng.app.entity.CrmBusinessTrackNote;
import cn.atsoft.dasheng.app.model.result.CrmBusinessTrackNoteResult;
import cn.atsoft.dasheng.app.service.CrmBusinessTrackNoteService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusinessTrack;
import cn.atsoft.dasheng.app.mapper.CrmBusinessTrackMapper;
import cn.atsoft.dasheng.app.model.params.CrmBusinessTrackParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessTrackResult;
import cn.atsoft.dasheng.app.service.CrmBusinessTrackService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商机跟踪表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-08-04
 */
@Service
public class CrmBusinessTrackServiceImpl extends ServiceImpl<CrmBusinessTrackMapper, CrmBusinessTrack> implements CrmBusinessTrackService {
    @Autowired
    private CrmBusinessTrackNoteService crmBusinessTrackNoteService;

    @Override
    public void add(CrmBusinessTrackParam param) {
        CrmBusinessTrack entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CrmBusinessTrackParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(CrmBusinessTrackParam param) {
        CrmBusinessTrack oldEntity = getOldEntity(param);
        CrmBusinessTrack newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CrmBusinessTrackResult findBySpec(CrmBusinessTrackParam param) {
        return null;
    }

    @Override
    public List<CrmBusinessTrackResult> findListBySpec(CrmBusinessTrackParam param) {
        return null;
    }

    @Override
    public PageInfo<CrmBusinessTrackResult> findPageBySpec(CrmBusinessTrackParam param) {
        Page<CrmBusinessTrackResult> pageContext = getPageContext();
        IPage<CrmBusinessTrackResult> page = this.baseMapper.customPageList(pageContext, param);
        List<Long> noteid = new ArrayList<>();
        for (CrmBusinessTrackResult item : page.getRecords()) {
            noteid.add(item.getNoteId());
        }
        QueryWrapper<CrmBusinessTrackNote> queryWrapper = new QueryWrapper<>();
      queryWrapper.in("note_id",noteid);

        List<CrmBusinessTrackNote> res = crmBusinessTrackNoteService.list(queryWrapper);
        for (CrmBusinessTrackResult item : page.getRecords()) {
            List<CrmBusinessTrackNoteResult> results = new ArrayList<>();
            for (CrmBusinessTrackNote it : res) {
                if (item.getTrackId().equals(it.getNoteId())) {
                    CrmBusinessTrackNoteResult tmp = new CrmBusinessTrackNoteResult();
                    ToolUtil.copyProperties(it, tmp);
                    results.add(tmp);
                }
            }
            item.setGetnote(results);
        }
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CrmBusinessTrackParam param) {
        return param.getTrackId();
    }

    private Page<CrmBusinessTrackResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CrmBusinessTrack getOldEntity(CrmBusinessTrackParam param) {
        return this.getById(getKey(param));
    }

    private CrmBusinessTrack getEntity(CrmBusinessTrackParam param) {
        CrmBusinessTrack entity = new CrmBusinessTrack();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
