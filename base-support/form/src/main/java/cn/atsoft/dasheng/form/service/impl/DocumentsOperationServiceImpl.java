package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.DocumentsOperation;
import cn.atsoft.dasheng.form.mapper.DocumentsOperationMapper;
import cn.atsoft.dasheng.form.model.params.DocumentsOperationParam;
import cn.atsoft.dasheng.form.model.result.DocumentsOperationResult;
import cn.atsoft.dasheng.form.service.DocumentsOperationService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 单据权限操作 服务实现类
 * </p>
 *
 * @author
 * @since 2022-05-18
 */
@Service
public class DocumentsOperationServiceImpl extends ServiceImpl<DocumentsOperationMapper, DocumentsOperation> implements DocumentsOperationService {

    @Override
    public void add(DocumentsOperationParam param) {
        DocumentsOperation entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(DocumentsOperationParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(DocumentsOperationParam param) {
        DocumentsOperation oldEntity = getOldEntity(param);
        DocumentsOperation newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DocumentsOperationResult findBySpec(DocumentsOperationParam param) {
        return null;
    }

    @Override
    public List<DocumentsOperationResult> findListBySpec(DocumentsOperationParam param) {
        return null;
    }

    @Override
    public PageInfo<DocumentsOperationResult> findPageBySpec(DocumentsOperationParam param) {
        Page<DocumentsOperationResult> pageContext = getPageContext();
        IPage<DocumentsOperationResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(DocumentsOperationParam param) {
        return null;
    }

    private Page<DocumentsOperationResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private DocumentsOperation getOldEntity(DocumentsOperationParam param) {
        return this.getById(getKey(param));
    }

    @Override
    public DocumentsOperation getEntity(DocumentsOperationParam param) {
        DocumentsOperation entity = new DocumentsOperation();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
