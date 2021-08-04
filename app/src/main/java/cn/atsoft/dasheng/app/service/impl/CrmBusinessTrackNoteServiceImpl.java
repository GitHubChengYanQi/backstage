package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusinessTrackNote;
import cn.atsoft.dasheng.app.mapper.CrmBusinessTrackNoteMapper;
import cn.atsoft.dasheng.app.model.params.CrmBusinessTrackNoteParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessTrackNoteResult;
import  cn.atsoft.dasheng.app.service.CrmBusinessTrackNoteService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商机跟踪备注 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-08-04
 */
@Service
public class CrmBusinessTrackNoteServiceImpl extends ServiceImpl<CrmBusinessTrackNoteMapper, CrmBusinessTrackNote> implements CrmBusinessTrackNoteService {

    @Override
    public void add(CrmBusinessTrackNoteParam param){
        CrmBusinessTrackNote entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CrmBusinessTrackNoteParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(CrmBusinessTrackNoteParam param){
        CrmBusinessTrackNote oldEntity = getOldEntity(param);
        CrmBusinessTrackNote newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CrmBusinessTrackNoteResult findBySpec(CrmBusinessTrackNoteParam param){
        return null;
    }

    @Override
    public List<CrmBusinessTrackNoteResult> findListBySpec(CrmBusinessTrackNoteParam param){
        return null;
    }

    @Override
    public PageInfo<CrmBusinessTrackNoteResult> findPageBySpec(CrmBusinessTrackNoteParam param){
        Page<CrmBusinessTrackNoteResult> pageContext = getPageContext();
        IPage<CrmBusinessTrackNoteResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CrmBusinessTrackNoteParam param){
        return param.getNoteId();
    }

    private Page<CrmBusinessTrackNoteResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CrmBusinessTrackNote getOldEntity(CrmBusinessTrackNoteParam param) {
        return this.getById(getKey(param));
    }

    private CrmBusinessTrackNote getEntity(CrmBusinessTrackNoteParam param) {
        CrmBusinessTrackNote entity = new CrmBusinessTrackNote();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
