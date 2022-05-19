package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.DocumentsOperation;
import cn.atsoft.dasheng.form.entity.DocumentsPermissions;
import cn.atsoft.dasheng.form.mapper.DocumentsPermissionsMapper;
import cn.atsoft.dasheng.form.model.params.DocumentsOperationParam;
import cn.atsoft.dasheng.form.model.params.DocumentsPermissionsParam;
import cn.atsoft.dasheng.form.model.result.DocumentsOperationResult;
import cn.atsoft.dasheng.form.model.result.DocumentsPermissionsResult;
import cn.atsoft.dasheng.form.pojo.CanDo;
import cn.atsoft.dasheng.form.pojo.PermissionParam;
import cn.atsoft.dasheng.form.pojo.RolePermission;
import cn.atsoft.dasheng.form.service.DocumentsOperationService;
import cn.atsoft.dasheng.form.service.DocumentsPermissionsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
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

        updateDisplay(param.getFormType());
        for (DocumentsPermissionsParam paramParam : param.getParams()) {   //主表
            paramParam.setFormType(param.getFormType());
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

    /**
     * 更新状态
     *
     * @param formType
     */
    private void updateDisplay(String formType) {

        List<DocumentsPermissions> permissions = this.query().eq("form_type", formType).list();
        List<Long> ids = new ArrayList<>();

        for (DocumentsPermissions permission : permissions) {
            permission.setDisplay(0);
            ids.add(permission.getPermissionsId());
        }

        QueryWrapper<DocumentsOperation> operationQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(ids)) {
            operationQueryWrapper.in("permissions_id", ids);
        }
        DocumentsOperation documentsOperation = new DocumentsOperation();
        documentsOperation.setDisplay(0);
        operationService.update(documentsOperation, operationQueryWrapper);


        this.updateBatchById(permissions);
    }

    /**
     * 获取角色权限
     * @param formType
     * @param filedName
     * @return
     */
    @Override
    public List<RolePermission> getRolePermission(String formType, String filedName) {
        List<RolePermission> rolePermissions = new ArrayList<>();
        if (ToolUtil.isEmpty(formType)&&ToolUtil.isEmpty(filedName)) {
            return rolePermissions;
        }

        DocumentsPermissions permissions = this.query().eq("form_type", formType).eq("filed_name", filedName).eq("display", 1).one();
        if (ToolUtil.isEmpty(permissions)) {
            return rolePermissions;
        }
        List<DocumentsOperation> operationList = operationService.query().eq("permissions_id", permissions.getPermissionsId()).eq("display", 1).list();
        for (DocumentsOperation documentsOperation : operationList) {
            RolePermission rolePermission = new RolePermission();

            rolePermission.setRoleId(documentsOperation.getRoleId());
            List<CanDo> canDos = JSON.parseArray(documentsOperation.getOperational(), CanDo.class);
            rolePermission.setCanDos(canDos);
            rolePermissions.add(rolePermission);
        }
        return rolePermissions;
    }

    @Override
    public List<DocumentsPermissionsResult> getDetails(String formType) {
        List<DocumentsPermissions> permissions = this.query().eq("form_type", formType).eq("display", 1).list();
        List<DocumentsPermissionsResult> results = BeanUtil.copyToList(permissions, DocumentsPermissionsResult.class, new CopyOptions());

        List<Long> ids = new ArrayList<>();
        for (DocumentsPermissionsResult result : results) {
            ids.add(result.getPermissionsId());
        }
        List<DocumentsOperationResult> operationResultList = operationService.getResultsByPermissionId(ids);

        for (DocumentsPermissionsResult result : results) {
            List<DocumentsOperationResult> operationResults = new ArrayList<>();

            for (DocumentsOperationResult documentsOperationResult : operationResultList) {
                if (result.getPermissionsId().equals(documentsOperationResult.getPermissionsId())) {
                    operationResults.add(documentsOperationResult);
                }
                result.setOperationResults(operationResults);
            }

        }

        return results;
    }


    @Override
    public void delete(DocumentsPermissionsParam param) {
        DocumentsPermissions entity = getEntity(param);
        entity.setDisplay(0);
        this.updateById(entity);
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
