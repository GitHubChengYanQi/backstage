package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.SpeechcraftType;
import cn.atsoft.dasheng.crm.mapper.SpeechcraftTypeMapper;
import cn.atsoft.dasheng.crm.model.params.SpeechcraftTypeParam;
import cn.atsoft.dasheng.crm.model.result.SpeechcraftTypeResult;
import  cn.atsoft.dasheng.crm.service.SpeechcraftTypeService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 话术分类 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-09-13
 */
@Service
public class SpeechcraftTypeServiceImpl extends ServiceImpl<SpeechcraftTypeMapper, SpeechcraftType> implements SpeechcraftTypeService {

    @Override
    public void add(SpeechcraftTypeParam param){
        SpeechcraftType entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(SpeechcraftTypeParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void batchDelete(List<Long> ids) {
        SpeechcraftType speechcraftType = new SpeechcraftType();
        speechcraftType.setDisplay(0);
        QueryWrapper<SpeechcraftType> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("speechcraft_type_id");
        this.update(speechcraftType,queryWrapper);
    }

    @Override
    public void update(SpeechcraftTypeParam param){
        SpeechcraftType oldEntity = getOldEntity(param);
        SpeechcraftType newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SpeechcraftTypeResult findBySpec(SpeechcraftTypeParam param){
        return null;
    }

    @Override
    public List<SpeechcraftTypeResult> findListBySpec(SpeechcraftTypeParam param){
        return null;
    }

    @Override
    public PageInfo<SpeechcraftTypeResult> findPageBySpec(SpeechcraftTypeParam param){
        Page<SpeechcraftTypeResult> pageContext = getPageContext();
        IPage<SpeechcraftTypeResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SpeechcraftTypeParam param){
        return param.getSpeechcraftTypeId();
    }

    private Page<SpeechcraftTypeResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private SpeechcraftType getOldEntity(SpeechcraftTypeParam param) {
        return this.getById(getKey(param));
    }

    private SpeechcraftType getEntity(SpeechcraftTypeParam param) {
        SpeechcraftType entity = new SpeechcraftType();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
