package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.DocumentsStatus;
import cn.atsoft.dasheng.form.mapper.DocumentStatusMapper;
import cn.atsoft.dasheng.form.model.params.DocumentsStatusParam;
import cn.atsoft.dasheng.form.model.result.DocumentsActionResult;
import cn.atsoft.dasheng.form.model.result.DocumentsStatusResult;
import cn.atsoft.dasheng.form.service.DocumentStatusService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.service.DocumentsActionService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
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
 * 单据状态 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-04-28
 */
@Service
public class DocumentStatusServiceImpl extends ServiceImpl<DocumentStatusMapper, DocumentsStatus> implements DocumentStatusService {
    @Autowired
    private DocumentsActionService actionService;

    @Override
    public Long add(DocumentsStatusParam param) {
        DocumentsStatus entity = getEntity(param);
        this.save(entity);
        return entity.getDocumentsStatusId();
    }

    @Override
    public void delete(DocumentsStatusParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public DocumentsStatus update(DocumentsStatusParam param) {
        DocumentsStatus oldEntity = getOldEntity(param);
        DocumentsStatus newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
        return newEntity;
    }

    @Override
    public DocumentsStatusResult detail(Long statusId) {

        DocumentsStatusResult documentsStatusResult = new DocumentsStatusResult();
        if (ToolUtil.isEmpty(statusId)) {
            return documentsStatusResult;
        }
        if (statusId == 0) {
            documentsStatusResult.setName("发起");
        } else if (statusId == 50) {
            documentsStatusResult.setName("拒绝");
        } else if (statusId == 99) {
            documentsStatusResult.setName("完成");
        } else if (ToolUtil.isNotEmpty(getResult(statusId))) {
            documentsStatusResult = getResult(statusId);
        }

        List<DocumentsActionResult> actionResults = actionService.getList(documentsStatusResult.getDocumentsStatusId(), documentsStatusResult.getFormType());
        documentsStatusResult.setActionResults(actionResults);


        return documentsStatusResult;
    }

    private DocumentsStatusResult getResult(Long statusId) {
        DocumentsStatusResult documentsStatusResult = new DocumentsStatusResult();
        DocumentsStatus documentsStatus = this.getById(statusId);
        if (ToolUtil.isEmpty(documentsStatus)) {
            return documentsStatusResult;
        }
        ToolUtil.copyProperties(documentsStatus, documentsStatusResult);
        return documentsStatusResult;
    }

    @Override
    public List<DocumentsStatusResult> details(String formType) {
        List<DocumentsStatus> statuses = this.query().eq("form_type", formType).eq("display", 1).list();

        List<DocumentsStatusResult> statusResults = new ArrayList<>();

        DocumentsStatusResult start = new DocumentsStatusResult();
        start.setDocumentsStatusId(0L);
        start.setFormType(formType);
        start.setName("发起");

        DocumentsStatusResult done = new DocumentsStatusResult();
        done.setDocumentsStatusId(50L);
        start.setFormType(formType);
        done.setName("拒绝");

        DocumentsStatusResult refuse = new DocumentsStatusResult();
        refuse.setDocumentsStatusId(99L);
        start.setFormType(formType);
        refuse.setName("完成");
        statusResults.add(start);
        statusResults.add(done);
        statusResults.add(refuse);


        statusResults.addAll(BeanUtil.copyToList(statuses, DocumentsStatusResult.class, new CopyOptions()));


        for (DocumentsStatusResult statusResult : statusResults) {
            List<DocumentsActionResult> actionResults = actionService.getList(statusResult.getDocumentsStatusId(), statusResult.getFormType());
            statusResult.setActionResults(actionResults);
        }

        return statusResults;
    }

    @Override
    public DocumentsStatusResult findBySpec(DocumentsStatusParam param) {
        return null;
    }

    @Override
    public List<DocumentsStatusResult> findListBySpec(DocumentsStatusParam param) {
        return null;
    }

    @Override
    public PageInfo<DocumentsStatusResult> findPageBySpec(DocumentsStatusParam param) {
        Page<DocumentsStatusResult> pageContext = getPageContext();
        IPage<DocumentsStatusResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(DocumentsStatusParam param) {
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

    @Override
    public List<DocumentsStatusResult> resultsByIds(List<Long> ids) {
        if (ToolUtil.isEmpty(ids) || ids.size() == 0) {
            return new ArrayList<>();
        }

        List<DocumentsStatus> documentsStatuses = this.listByIds(ids);
        List<DocumentsStatusResult> documentsStatusResults = BeanUtil.copyToList(documentsStatuses, DocumentsStatusResult.class, new CopyOptions());
        for (Long id : ids) {
            if (ToolUtil.isEmpty(id) || id == 0) {
                DocumentsStatusResult result = new DocumentsStatusResult();
                result.setName("发起");
                result.setDocumentsStatusId(0L);
                documentsStatusResults.add(result);
            } else if (id == 50) {
                DocumentsStatusResult result = new DocumentsStatusResult();
                result.setDocumentsStatusId(50L);
                result.setName("拒绝");
                documentsStatusResults.add(result);
            } else if (id == 99) {
                DocumentsStatusResult result = new DocumentsStatusResult();
                result.setDocumentsStatusId(99L);
                result.setName("完成");
                documentsStatusResults.add(result);
            }
        }
        return documentsStatusResults;

    }

}
