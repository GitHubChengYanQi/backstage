package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.DocumentsOperation;
import cn.atsoft.dasheng.form.entity.DocumentsPermissions;
import cn.atsoft.dasheng.form.mapper.DocumentsOperationMapper;
import cn.atsoft.dasheng.form.model.params.DocumentsOperationParam;
import cn.atsoft.dasheng.form.model.result.DocumentsOperationResult;
import cn.atsoft.dasheng.form.pojo.CanDo;
import cn.atsoft.dasheng.form.service.DocumentsOperationService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
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
        DocumentsOperation entity = getEntity(param);
        entity.setDisplay(0);
        this.updateById(entity);
    }

    @Override
    public void update(DocumentsOperationParam param) {
        DocumentsOperation oldEntity = getOldEntity(param);
        DocumentsOperation newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public List<DocumentsOperationResult> getResultsByPermissionId(List<Long> ids) {
        if (ToolUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<DocumentsOperation> operations = this.query().in("permissions_id", ids).eq("display", 1).list();

        List<DocumentsOperationResult> results = BeanUtil.copyToList(operations, DocumentsOperationResult.class, new CopyOptions());
        for (DocumentsOperationResult result : results) {
            if (ToolUtil.isNotEmpty(result.getOperational())) {
                List<CanDo> canDos = JSON.parseArray(result.getOperational(), CanDo.class);
                result.setCanDos(canDos);
            }

        }
        return results;
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
    public PageInfo findPageBySpec(DocumentsOperationParam param) {
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
