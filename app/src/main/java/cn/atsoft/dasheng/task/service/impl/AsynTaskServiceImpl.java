package cn.atsoft.dasheng.task.service.impl;


import cn.atsoft.dasheng.app.pojo.AllBomResult;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.task.entity.AsynTask;
import cn.atsoft.dasheng.task.mapper.AsynTaskMapper;
import cn.atsoft.dasheng.task.model.params.AsynTaskParam;
import cn.atsoft.dasheng.task.model.result.AsynTaskResult;
import cn.atsoft.dasheng.task.service.AsynTaskService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 等待任务表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-04-01
 */
@Service
public class AsynTaskServiceImpl extends ServiceImpl<AsynTaskMapper, AsynTask> implements AsynTaskService {

    @Override
    public void add(AsynTaskParam param) {
        AsynTask entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(AsynTaskParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(AsynTaskParam param) {
        AsynTask oldEntity = getOldEntity(param);
        AsynTask newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AsynTaskResult findBySpec(AsynTaskParam param) {
        return null;
    }

    @Override
    public List<AsynTaskResult> findListBySpec(AsynTaskParam param) {
        return null;
    }

    @Override
    public PageInfo<AsynTaskResult> findPageBySpec(AsynTaskParam param) {
        Page<AsynTaskResult> pageContext = getPageContext();
        IPage<AsynTaskResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AsynTaskParam param) {
        return param.getTaskId();
    }

    private Page<AsynTaskResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private AsynTask getOldEntity(AsynTaskParam param) {
        return this.getById(getKey(param));
    }

    private AsynTask getEntity(AsynTaskParam param) {
        AsynTask entity = new AsynTask();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<AsynTaskResult> data) {
        for (AsynTaskResult datum : data) {
            AllBomResult allBomResult = JSON.parseObject(datum.getContent(), AllBomResult.class);
            datum.setAllBomResult(allBomResult);
            break;
        }
    }
}
