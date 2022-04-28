package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.DocumentsStatus;
import cn.atsoft.dasheng.form.mapper.DocumentsStatusMapper;
import cn.atsoft.dasheng.form.model.params.DocumentsStatusParam;
import cn.atsoft.dasheng.form.model.result.DocumentsStatusResult;
import  cn.atsoft.dasheng.form.service.DocumentsStatusService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 单据状态 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-04-28
 */
@Service
public class DocumentsStatusServiceImpl extends ServiceImpl<DocumentsStatusMapper, DocumentsStatus> implements DocumentsStatusService {

    @Override
    public void add(DocumentsStatusParam param){
        DocumentsStatus entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(DocumentsStatusParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(DocumentsStatusParam param){
        DocumentsStatus oldEntity = getOldEntity(param);
        DocumentsStatus newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DocumentsStatusResult findBySpec(DocumentsStatusParam param){
        return null;
    }

    @Override
    public List<DocumentsStatusResult> findListBySpec(DocumentsStatusParam param){
        return null;
    }

    @Override
    public PageInfo<DocumentsStatusResult> findPageBySpec(DocumentsStatusParam param){
        Page<DocumentsStatusResult> pageContext = getPageContext();
        IPage<DocumentsStatusResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(DocumentsStatusParam param){
        return param.getDocumentsStatusId();
    }

    private Page<DocumentsStatusResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private DocumentsStatus getOldEntity(DocumentsStatusParam param) {
        return this.getById(getKey(param));
    }

    private DocumentsStatus getEntity(DocumentsStatusParam param) {
        DocumentsStatus entity = new DocumentsStatus();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
