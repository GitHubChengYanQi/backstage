package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.SpeechcraftTypeDetail;
import cn.atsoft.dasheng.crm.mapper.SpeechcraftTypeDetailMapper;
import cn.atsoft.dasheng.crm.model.params.SpeechcraftTypeDetailParam;
import cn.atsoft.dasheng.crm.model.result.SpeechcraftTypeDetailResult;
import  cn.atsoft.dasheng.crm.service.SpeechcraftTypeDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 话术分类详细 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-09-13
 */
@Service
public class SpeechcraftTypeDetailServiceImpl extends ServiceImpl<SpeechcraftTypeDetailMapper, SpeechcraftTypeDetail> implements SpeechcraftTypeDetailService {

    @Override
    public void add(SpeechcraftTypeDetailParam param){
        SpeechcraftTypeDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(SpeechcraftTypeDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(SpeechcraftTypeDetailParam param){
        SpeechcraftTypeDetail oldEntity = getOldEntity(param);
        SpeechcraftTypeDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SpeechcraftTypeDetailResult findBySpec(SpeechcraftTypeDetailParam param){
        return null;
    }

    @Override
    public List<SpeechcraftTypeDetailResult> findListBySpec(SpeechcraftTypeDetailParam param){
        return null;
    }

    @Override
    public PageInfo<SpeechcraftTypeDetailResult> findPageBySpec(SpeechcraftTypeDetailParam param){
        Page<SpeechcraftTypeDetailResult> pageContext = getPageContext();
        IPage<SpeechcraftTypeDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SpeechcraftTypeDetailParam param){
        return param.getSpeechcraftTypeDetailId();
    }

    private Page<SpeechcraftTypeDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private SpeechcraftTypeDetail getOldEntity(SpeechcraftTypeDetailParam param) {
        return this.getById(getKey(param));
    }

    private SpeechcraftTypeDetail getEntity(SpeechcraftTypeDetailParam param) {
        SpeechcraftTypeDetail entity = new SpeechcraftTypeDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
