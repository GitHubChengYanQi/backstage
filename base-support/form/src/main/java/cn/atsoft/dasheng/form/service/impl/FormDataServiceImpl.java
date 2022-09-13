package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.FormData;
import cn.atsoft.dasheng.form.mapper.FormDataMapper;
import cn.atsoft.dasheng.form.model.params.FormDataParam;
import cn.atsoft.dasheng.form.model.result.FormDataResult;
import cn.atsoft.dasheng.form.service.FormDataService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 自定义表单主表 服务实现类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Service
public class FormDataServiceImpl extends ServiceImpl<FormDataMapper, FormData> implements FormDataService {

    @Override
    public void add(FormDataParam param) {
        FormData entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(FormDataParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(FormDataParam param) {
        FormData oldEntity = getOldEntity(param);
        FormData newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public FormDataResult findBySpec(FormDataParam param) {
        return null;
    }

    @Override
    public List<FormDataResult> findListBySpec(FormDataParam param) {
        return null;
    }

    @Override
    public PageInfo findPageBySpec(FormDataParam param) {
        Page<FormDataResult> pageContext = getPageContext();
        IPage<FormDataResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<FormDataResult> getDataIds(List<Long> formId) {
        if (ToolUtil.isEmpty(formId)) {
            return new ArrayList<>();
        }
        List<FormData> dataList = this.query().in("form_id", formId).list();
        List<FormDataResult> dataResults = new ArrayList<>();


        for (FormData formData : dataList) {
            FormDataResult dataResult = new FormDataResult();
            ToolUtil.copyProperties(formData, dataResult);
            dataResults.add(dataResult);
        }

        return dataResults;
    }

    private Serializable getKey(FormDataParam param) {
        return param.getDataId();
    }

    private Page<FormDataResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private FormData getOldEntity(FormDataParam param) {
        return this.getById(getKey(param));
    }

    private FormData getEntity(FormDataParam param) {
        FormData entity = new FormData();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
