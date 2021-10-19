package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Data;
import cn.atsoft.dasheng.crm.entity.DataClassification;
import cn.atsoft.dasheng.crm.mapper.DataClassificationMapper;
import cn.atsoft.dasheng.crm.model.params.DataClassificationParam;
import cn.atsoft.dasheng.crm.model.result.DataClassificationResult;
import cn.atsoft.dasheng.crm.model.result.DataResult;
import cn.atsoft.dasheng.crm.service.DataClassificationService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.service.DataService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 资料分类表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-09-13
 */
@Service
public class DataClassificationServiceImpl extends ServiceImpl<DataClassificationMapper, DataClassification> implements DataClassificationService {
    @Autowired
    private DataService dataService;

    @Override
    public void add(DataClassificationParam param) {
        DataClassification entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(DataClassificationParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void batchDelete(List<Long> ids) {
        DataClassification dataClassification = new DataClassification();
        dataClassification.setDisplay(0);
        QueryWrapper<DataClassification> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("data_classification_id");
        this.update(dataClassification,queryWrapper);
    }

    @Override
    public void update(DataClassificationParam param) {
        DataClassification oldEntity = getOldEntity(param);
        DataClassification newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DataClassificationResult findBySpec(DataClassificationParam param) {
        return null;
    }

    @Override
    public List<DataClassificationResult> findListBySpec(DataClassificationParam param) {
        return null;
    }

    @Override
    public PageInfo<DataClassificationResult> findPageBySpec(DataClassificationParam param) {
        Page<DataClassificationResult> pageContext = getPageContext();
        IPage<DataClassificationResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(DataClassificationParam param) {
        return param.getDataClassificationId();
    }

    private Page<DataClassificationResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private DataClassification getOldEntity(DataClassificationParam param) {
        return this.getById(getKey(param));
    }

    private DataClassification getEntity(DataClassificationParam param) {
        DataClassification entity = new DataClassification();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<DataClassificationResult> data) {
        List<Long> ids = new ArrayList<>();
        for (DataClassificationResult datum : data) {
            ids.add(datum.getDataClassificationId());
        }
        if (ToolUtil.isNotEmpty(ids)) {
            List<Data> dataList = dataService.lambdaQuery().in(Data::getDataClassificationId, ids).list();
            for (DataClassificationResult datum : data) {
                List<DataResult> dataResults = new ArrayList<>();
                for (Data data1 : dataList) {
                    if (data1.getDataClassificationId().equals(datum.getDataClassificationId())) {
                        DataResult dataResult = new DataResult();
                        ToolUtil.copyProperties(data1, dataResult);
                        dataResults.add(dataResult);
                    }
                }
                datum.setDataResults(dataResults);
            }
        }

    }
}
