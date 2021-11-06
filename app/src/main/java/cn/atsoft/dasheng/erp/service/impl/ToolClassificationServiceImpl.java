package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ToolClassification;
import cn.atsoft.dasheng.erp.mapper.ToolClassificationMapper;
import cn.atsoft.dasheng.erp.model.params.ToolClassificationParam;
import cn.atsoft.dasheng.erp.model.result.ToolClassificationResult;
import  cn.atsoft.dasheng.erp.service.ToolClassificationService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 工具分类表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-23
 */
@Service
public class ToolClassificationServiceImpl extends ServiceImpl<ToolClassificationMapper, ToolClassification> implements ToolClassificationService {

    @Override
    public void add(ToolClassificationParam param){
        ToolClassification entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ToolClassificationParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ToolClassificationParam param){
        ToolClassification oldEntity = getOldEntity(param);
        ToolClassification newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ToolClassificationResult findBySpec(ToolClassificationParam param){
        return null;
    }

    @Override
    public List<ToolClassificationResult> findListBySpec(ToolClassificationParam param){
        return null;
    }

    @Override
    public PageInfo<ToolClassificationResult> findPageBySpec(ToolClassificationParam param){
        Page<ToolClassificationResult> pageContext = getPageContext();
        IPage<ToolClassificationResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ToolClassificationParam param){
        return param.getToolClassificationId();
    }

    private Page<ToolClassificationResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ToolClassification getOldEntity(ToolClassificationParam param) {
        return this.getById(getKey(param));
    }

    private ToolClassification getEntity(ToolClassificationParam param) {
        ToolClassification entity = new ToolClassification();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
