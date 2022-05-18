package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.DocumentsOperation;
import cn.atsoft.dasheng.form.entity.DocumentsPermissions;
import cn.atsoft.dasheng.form.mapper.DocumentsPermissionsMapper;
import cn.atsoft.dasheng.form.model.params.DocumentsOperationParam;
import cn.atsoft.dasheng.form.model.params.DocumentsPermissionsParam;
import cn.atsoft.dasheng.form.model.result.DocumentsPermissionsResult;
import cn.atsoft.dasheng.form.pojo.PermissionParam;
import cn.atsoft.dasheng.form.service.DocumentsOperationService;
import cn.atsoft.dasheng.form.service.DocumentsPermissionsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 单据权限 服务实现类
 * </p>
 *
 * @author
 * @since 2022-05-18
 */
@Service
public class DocumentsPermissionsServiceImpl extends ServiceImpl<DocumentsPermissionsMapper, DocumentsPermissions> implements DocumentsPermissionsService {

    @Autowired
    private DocumentsOperationService operationService;

    @Override
    public void add(DocumentsPermissionsParam param) {
        DocumentsPermissions entity = getEntity(param);
        this.save(entity);
    }

    /**
     * 批量添加
     *
     * @param param
     */
    @Override
    @Transactional
    public void addList(PermissionParam param) {

        for (DocumentsPermissionsParam paramParam : param.getParams()) {   //主表
            DocumentsPermissions entity = getEntity(paramParam);
            this.save(entity);

            for (DocumentsOperationParam operationParam : paramParam.getOperationParams()) {  //子表
                DocumentsOperation operation = operationService.getEntity(operationParam);
                String jsonString = JSON.toJSONString(operationParam.getCanDos());   //具体操作
                operation.setOperational(jsonString);
                operation.setPermissionsId(entity.getPermissionsId());
                operationService.save(operation);
            }

        }
    }

    @Override
    public void delete(DocumentsPermissionsParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(DocumentsPermissionsParam param) {
        DocumentsPermissions oldEntity = getOldEntity(param);
        DocumentsPermissions newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DocumentsPermissionsResult findBySpec(DocumentsPermissionsParam param) {
        return null;
    }

    @Override
    public List<DocumentsPermissionsResult> findListBySpec(DocumentsPermissionsParam param) {
        return null;
    }

    @Override
    public PageInfo<DocumentsPermissionsResult> findPageBySpec(DocumentsPermissionsParam param) {
        Page<DocumentsPermissionsResult> pageContext = getPageContext();
        IPage<DocumentsPermissionsResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(DocumentsPermissionsParam param) {
        return param.getPermissionsId();
    }

    private Page<DocumentsPermissionsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private DocumentsPermissions getOldEntity(DocumentsPermissionsParam param) {
        return this.getById(getKey(param));
    }

    private DocumentsPermissions getEntity(DocumentsPermissionsParam param) {
        DocumentsPermissions entity = new DocumentsPermissions();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
