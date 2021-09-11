package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Speechcraft;
import cn.atsoft.dasheng.crm.mapper.SpeechcraftMapper;
import cn.atsoft.dasheng.crm.model.params.SpeechcraftParam;
import cn.atsoft.dasheng.crm.model.result.SpeechcraftResult;
import  cn.atsoft.dasheng.crm.service.SpeechcraftService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 话术基础资料 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-09-11
 */
@Service
public class SpeechcraftServiceImpl extends ServiceImpl<SpeechcraftMapper, Speechcraft> implements SpeechcraftService {

    @Override
    public void add(SpeechcraftParam param){
        Speechcraft entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(SpeechcraftParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(SpeechcraftParam param){
        Speechcraft oldEntity = getOldEntity(param);
        Speechcraft newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SpeechcraftResult findBySpec(SpeechcraftParam param){
        return null;
    }

    @Override
    public List<SpeechcraftResult> findListBySpec(SpeechcraftParam param){
        return null;
    }

    @Override
    public PageInfo<SpeechcraftResult> findPageBySpec(SpeechcraftParam param){
        Page<SpeechcraftResult> pageContext = getPageContext();
        IPage<SpeechcraftResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SpeechcraftParam param){
        return param.getSpeechcraftId();
    }

    private Page<SpeechcraftResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Speechcraft getOldEntity(SpeechcraftParam param) {
        return this.getById(getKey(param));
    }

    private Speechcraft getEntity(SpeechcraftParam param) {
        Speechcraft entity = new Speechcraft();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
