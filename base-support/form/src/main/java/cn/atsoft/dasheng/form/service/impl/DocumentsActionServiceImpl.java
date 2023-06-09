package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.DocumentsAction;
import cn.atsoft.dasheng.form.mapper.DocumentsActionMapper;
import cn.atsoft.dasheng.form.model.params.DocumentsActionParam;
import cn.atsoft.dasheng.form.model.result.DocumentsActionResult;
import cn.atsoft.dasheng.form.pojo.ActionStatus;
import cn.atsoft.dasheng.form.service.DocumentsActionService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 单据动作 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-04-28
 */
@Service
public class DocumentsActionServiceImpl extends ServiceImpl<DocumentsActionMapper, DocumentsAction> implements DocumentsActionService {

    @Override
    public void add(DocumentsActionParam param) {
        DocumentsAction entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void removeAll(Long statusId) {
        this.remove(new QueryWrapper<DocumentsAction>() {{
            eq("documents_status_id", statusId);
        }});
    }


    @Override
    public void delete(DocumentsActionParam param) {
        DocumentsAction action = getEntity(param);
        action.setDisplay(0);
        this.updateById(action);
    }

    @Override
    public void update(DocumentsActionParam param) {
        DocumentsAction oldEntity = getOldEntity(param);
        DocumentsAction newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    /**
     * 组合
     *
     * @param actionStatuses
     */
    @Override
    public void combination(List<ActionStatus> actionStatuses) {
        List<Long> ids = new ArrayList<>();
        for (ActionStatus actionStatus : actionStatuses) {
            ids.add(actionStatus.getActionId());
        }
        List<DocumentsAction> documentsActions = ids.size() == 0 ? new ArrayList<>() : this.listByIds(ids);

        for (ActionStatus actionStatus : actionStatuses) {
            for (DocumentsAction documentsAction : documentsActions) {
                if (actionStatus.getActionId().equals(documentsAction.getDocumentsActionId())) {
                    actionStatus.setAction(documentsAction.getAction());
                    actionStatus.setFormType(documentsAction.getFormType());
                    actionStatus.setActionName(documentsAction.getActionName());
                    break;
                }
            }
        }

    }


    @Override
    public List<DocumentsActionResult> getList(Long statusId, String fromType) {
        if (ToolUtil.isEmpty(statusId)) {
            return new ArrayList<>();
        }
        List<DocumentsAction> documentsActions = this.query().eq("documents_status_id", statusId)
                .eq("form_type", fromType)
                .eq("display", 1)
                .orderByAsc("sort").list();
        return BeanUtil.copyToList(documentsActions, DocumentsActionResult.class);
    }

    @Override
    public DocumentsActionResult findBySpec(DocumentsActionParam param) {
        return null;
    }

    @Override
    public List<DocumentsActionResult> findListBySpec(DocumentsActionParam param) {
        return null;
    }

    @Override
    public PageInfo findPageBySpec(DocumentsActionParam param) {
        Page<DocumentsActionResult> pageContext = getPageContext();
        IPage<DocumentsActionResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(DocumentsActionParam param) {
        return param.getDocumentsActionId();
    }

    private Page<DocumentsActionResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private DocumentsAction getOldEntity(DocumentsActionParam param) {
        return this.getById(getKey(param));
    }

    private DocumentsAction getEntity(DocumentsActionParam param) {
        DocumentsAction entity = new DocumentsAction();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
