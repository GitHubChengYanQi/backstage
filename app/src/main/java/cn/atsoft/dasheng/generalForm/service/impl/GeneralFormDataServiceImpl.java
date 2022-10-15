package cn.atsoft.dasheng.generalForm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.generalForm.entity.GeneralFormData;
import cn.atsoft.dasheng.generalForm.mapper.GeneralFormDataMapper;
import cn.atsoft.dasheng.generalForm.model.params.GeneralFormDataParam;
import cn.atsoft.dasheng.generalForm.model.result.GeneralFormDataResult;
import  cn.atsoft.dasheng.generalForm.service.GeneralFormDataService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-09-08
 */
@Service
public class GeneralFormDataServiceImpl extends ServiceImpl<GeneralFormDataMapper, GeneralFormData> implements GeneralFormDataService {

    @Override
    public void add(GeneralFormDataParam param){
        GeneralFormData entity = getEntity(param);
        this.save(entity);
    }
    @Override
    public void addBatch(List<GeneralFormDataParam> params){
         params.removeIf(i->ToolUtil.isEmpty(i.getFieldName()) || ToolUtil.isEmpty(i.getValue()));
            List<GeneralFormData> entityList = new ArrayList<>();
        for (GeneralFormDataParam param : params) {
            entityList.add(getEntity(param));
        }

        List<GeneralFormData> list = this.lambdaQuery().in(GeneralFormData::getValue, entityList.stream().map(GeneralFormData::getValue).collect(Collectors.toList())).list();
        for (GeneralFormData generalFormData : list) {
            entityList.removeIf(i->i.getValue().equals(generalFormData.getValue()) && i.getFieldName().equals(generalFormData.getFieldName()));
        }
        if (entityList.size()>0) {
            this.saveBatch(entityList);
        }
    }

    @Override
    public void delete(GeneralFormDataParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(GeneralFormDataParam param){
        GeneralFormData oldEntity = getOldEntity(param);
        GeneralFormData newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public GeneralFormDataResult findBySpec(GeneralFormDataParam param){
        return null;
    }

    @Override
    public List<GeneralFormDataResult> findListBySpec(GeneralFormDataParam param){
        return null;
    }

    @Override
    public PageInfo<GeneralFormDataResult> findPageBySpec(GeneralFormDataParam param){
        Page<GeneralFormDataResult> pageContext = getPageContext();
        IPage<GeneralFormDataResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(GeneralFormDataParam param){
        return param.getId();
    }

    private Page<GeneralFormDataResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private GeneralFormData getOldEntity(GeneralFormDataParam param) {
        return this.getById(getKey(param));
    }

    private GeneralFormData getEntity(GeneralFormDataParam param) {
        GeneralFormData entity = new GeneralFormData();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
