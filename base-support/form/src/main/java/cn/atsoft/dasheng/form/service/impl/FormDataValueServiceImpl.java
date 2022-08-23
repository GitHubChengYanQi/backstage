package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.FormDataValue;
import cn.atsoft.dasheng.form.mapper.FormDataValueMapper;
import cn.atsoft.dasheng.form.model.params.FormDataValueParam;
import cn.atsoft.dasheng.form.model.result.FormDataValueResult;
import cn.atsoft.dasheng.form.service.FormDataValueService;
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
 * 自定义表单各个字段数据	 服务实现类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Service
public class FormDataValueServiceImpl extends ServiceImpl<FormDataValueMapper, FormDataValue> implements FormDataValueService {

    @Override
    public void add(FormDataValueParam param) {
        FormDataValue entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(FormDataValueParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(FormDataValueParam param) {
        FormDataValue oldEntity = getOldEntity(param);
        FormDataValue newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public FormDataValueResult findBySpec(FormDataValueParam param) {
        return null;
    }

    @Override
    public List<FormDataValueResult> findListBySpec(FormDataValueParam param) {
        return null;
    }

    @Override
    public PageInfo findPageBySpec(FormDataValueParam param) {
        Page<FormDataValueResult> pageContext = getPageContext();
        IPage<FormDataValueResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<FormDataValueResult> getDataValuesByDataId(List<Long> dataId) {
        List<FormDataValue> dataValues = this.query().in("data_id", dataId).list();

        List<FormDataValueResult> results = new ArrayList<>();

        for (FormDataValue dataValue : dataValues) {
            FormDataValueResult dataValueResult = new FormDataValueResult();
            ToolUtil.copyProperties(dataValue, dataValueResult);
            results.add(dataValueResult);
        }
        return results;
    }

    @Override
    public List<FormDataValueResult> getDataValuesResults(Long dataId) {
        List<FormDataValueResult> valueResults = new ArrayList<>();
        if (ToolUtil.isEmpty(dataId)) {
            return valueResults;
        }
        List<FormDataValue> dataValues = this.query().eq("data_id", dataId).list();
        if (ToolUtil.isEmpty(dataValues)) {
            return valueResults;
        }
        for (FormDataValue dataValue : dataValues) {
            FormDataValueResult dataValueResult = new FormDataValueResult();
            ToolUtil.copyProperties(dataValue, dataValueResult);
            valueResults.add(dataValueResult);
        }
        return valueResults;
    }

    private Serializable getKey(FormDataValueParam param) {
        return param.getValueId();
    }

    private Page<FormDataValueResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private FormDataValue getOldEntity(FormDataValueParam param) {
        return this.getById(getKey(param));
    }

    private FormDataValue getEntity(FormDataValueParam param) {
        FormDataValue entity = new FormDataValue();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
