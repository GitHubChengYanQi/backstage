package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.Data;
import cn.atsoft.dasheng.form.entity.DataValue;
import cn.atsoft.dasheng.form.mapper.DataMapper;
import cn.atsoft.dasheng.form.model.params.DataParam;
import cn.atsoft.dasheng.form.model.params.DataRequest;
import cn.atsoft.dasheng.form.model.result.DataResult;
import cn.atsoft.dasheng.form.service.DataService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.service.DataValueService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 自定义表单主表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-11-16
 */
@Service
public class DataServiceImpl extends ServiceImpl<DataMapper, Data> implements DataService {
    @Autowired
    private DataValueService dataValueService;

    @Override
    public void add(DataParam param) {
        Data entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(DataParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(DataParam param) {
        Data oldEntity = getOldEntity(param);
        Data newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DataResult findBySpec(DataParam param) {
        return null;
    }

    @Override
    public List<DataResult> findListBySpec(DataParam param) {
        return null;
    }

    @Override
    public PageInfo<DataResult> findPageBySpec(DataParam param) {
        Page<DataResult> pageContext = getPageContext();
        IPage<DataResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }


    @Override
    public void addByQuality(DataRequest dataRequest) {
        Data data = new Data();
        data.setFormId(dataRequest.getFormId());
        data.setModule(dataRequest.getModule());
        data.setMainId(0L);
        this.save(data);
        DataValue dataValue = new DataValue();
        dataValue.setDataId(data.getDataId());
        dataValue.setField(dataRequest.getField());
        dataValue.setValue(dataRequest.getValue());
        dataValueService.save(dataValue);
    }

    private Serializable getKey(DataParam param) {
        return param.getDataId();
    }

    private Page<DataResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Data getOldEntity(DataParam param) {
        return this.getById(getKey(param));
    }

    private Data getEntity(DataParam param) {
        Data entity = new Data();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
