package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.DataValue;
import cn.atsoft.dasheng.form.mapper.DataValueMapper;
import cn.atsoft.dasheng.form.model.params.DataValueParam;
import cn.atsoft.dasheng.form.model.result.DataValueResult;
import  cn.atsoft.dasheng.form.service.DataValueService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 自定义表单各个字段数据	 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-11-16
 */
@Service
public class DataValueServiceImpl extends ServiceImpl<DataValueMapper, DataValue> implements DataValueService {

    @Override
    public void add(DataValueParam param){
        DataValue entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(DataValueParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(DataValueParam param){
        DataValue oldEntity = getOldEntity(param);
        DataValue newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DataValueResult findBySpec(DataValueParam param){
        return null;
    }

    @Override
    public List<DataValueResult> findListBySpec(DataValueParam param){
        return null;
    }

    @Override
    public PageInfo<DataValueResult> findPageBySpec(DataValueParam param){
        Page<DataValueResult> pageContext = getPageContext();
        IPage<DataValueResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(DataValueParam param){
        return param.getValueId();
    }

    private Page<DataValueResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private DataValue getOldEntity(DataValueParam param) {
        return this.getById(getKey(param));
    }

    private DataValue getEntity(DataValueParam param) {
        DataValue entity = new DataValue();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
