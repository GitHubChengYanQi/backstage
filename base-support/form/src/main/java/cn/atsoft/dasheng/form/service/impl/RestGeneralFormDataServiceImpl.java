package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.RestGeneralFormData;
import cn.atsoft.dasheng.form.mapper.RestGeneralFormDataMapper;
import cn.atsoft.dasheng.form.model.params.RestGeneralFormDataParam;
import cn.atsoft.dasheng.form.model.result.RestGeneralFormDataResult;
import cn.atsoft.dasheng.form.service.RestGeneralFormDataService;
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
public class RestGeneralFormDataServiceImpl extends ServiceImpl<RestGeneralFormDataMapper, RestGeneralFormData> implements RestGeneralFormDataService {

    @Override
    public void add(RestGeneralFormDataParam param){
        RestGeneralFormData entity = getEntity(param);
        this.save(entity);
    }
    @Override
    public void addBatch(List<RestGeneralFormDataParam> params){
         params.removeIf(i->ToolUtil.isEmpty(i.getFieldName()) || ToolUtil.isEmpty(i.getValue()));
            List<RestGeneralFormData> entityList = new ArrayList<>();
        for (RestGeneralFormDataParam param : params) {
            entityList.add(getEntity(param));
        }

        List<RestGeneralFormData> list = this.lambdaQuery().in(RestGeneralFormData::getValue, entityList.stream().map(RestGeneralFormData::getValue).collect(Collectors.toList())).list();
        for (RestGeneralFormData generalFormData : list) {
            entityList.removeIf(i->i.getValue().equals(generalFormData.getValue()) && i.getFieldName().equals(generalFormData.getFieldName()));
        }
        if (entityList.size()>0) {
            this.saveBatch(entityList);
        }
    }

    @Override
    public void delete(RestGeneralFormDataParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(RestGeneralFormDataParam param){
        RestGeneralFormData oldEntity = getOldEntity(param);
        RestGeneralFormData newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RestGeneralFormDataResult findBySpec(RestGeneralFormDataParam param){
        return null;
    }

    @Override
    public List<RestGeneralFormDataResult> findListBySpec(RestGeneralFormDataParam param){
        return null;
    }

    @Override
    public PageInfo<RestGeneralFormDataResult> findPageBySpec(RestGeneralFormDataParam param){
        Page<RestGeneralFormDataResult> pageContext = getPageContext();
        IPage<RestGeneralFormDataResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RestGeneralFormDataParam param){
        return param.getId();
    }

    private Page<RestGeneralFormDataResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestGeneralFormData getOldEntity(RestGeneralFormDataParam param) {
        return this.getById(getKey(param));
    }

    private RestGeneralFormData getEntity(RestGeneralFormDataParam param) {
        RestGeneralFormData entity = new RestGeneralFormData();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
