package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.SkuHandleRecord;
import cn.atsoft.dasheng.erp.mapper.SkuHandleRecordMapper;
import cn.atsoft.dasheng.erp.model.params.SkuHandleRecordParam;
import cn.atsoft.dasheng.erp.model.result.SkuHandleRecordResult;
import cn.atsoft.dasheng.erp.service.SkuHandleRecordService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * sku 任务操作记录 服务实现类
 * </p>
 *
 * @author
 * @since 2022-10-25
 */
@Service
public class SkuHandleRecordServiceImpl extends ServiceImpl<SkuHandleRecordMapper, SkuHandleRecord> implements SkuHandleRecordService {

    @Autowired
    private ActivitiProcessTaskService taskService;

    @Override
    public void add(SkuHandleRecordParam param) {
        SkuHandleRecord entity = getEntity(param);
        this.save(entity);
    }


    public void addRecord(SkuHandleRecord record) {

        ActivitiProcessTask task = taskService.getById(record.getSourceId());
        record.setTheme(task.getTheme());
        this.save(record);
    }

    @Override
    public void delete(SkuHandleRecordParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(SkuHandleRecordParam param) {
        SkuHandleRecord oldEntity = getOldEntity(param);
        SkuHandleRecord newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SkuHandleRecordResult findBySpec(SkuHandleRecordParam param) {
        return null;
    }

    @Override
    public List<SkuHandleRecordResult> findListBySpec(SkuHandleRecordParam param) {
        return null;
    }

    @Override
    public PageInfo<SkuHandleRecordResult> findPageBySpec(SkuHandleRecordParam param) {
        Page<SkuHandleRecordResult> pageContext = getPageContext();
        IPage<SkuHandleRecordResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SkuHandleRecordParam param) {
        return param.getRecordId();
    }

    private Page<SkuHandleRecordResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private SkuHandleRecord getOldEntity(SkuHandleRecordParam param) {
        return this.getById(getKey(param));
    }

    private SkuHandleRecord getEntity(SkuHandleRecordParam param) {
        SkuHandleRecord entity = new SkuHandleRecord();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
