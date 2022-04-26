package cn.atsoft.dasheng.task.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.task.entity.AsynTaskDetail;
import cn.atsoft.dasheng.task.mapper.AsynTaskDetailMapper;
import cn.atsoft.dasheng.task.model.params.AsynTaskDetailParam;
import cn.atsoft.dasheng.task.model.result.AsynTaskDetailResult;
import  cn.atsoft.dasheng.task.service.AsynTaskDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 任务详情表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-04-26
 */
@Service
public class AsynTaskDetailServiceImpl extends ServiceImpl<AsynTaskDetailMapper, AsynTaskDetail> implements AsynTaskDetailService {

    @Override
    public void add(AsynTaskDetailParam param){
        AsynTaskDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(AsynTaskDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(AsynTaskDetailParam param){
        AsynTaskDetail oldEntity = getOldEntity(param);
        AsynTaskDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AsynTaskDetailResult findBySpec(AsynTaskDetailParam param){
        return null;
    }

    @Override
    public List<AsynTaskDetailResult> findListBySpec(AsynTaskDetailParam param){
        return null;
    }

    @Override
    public PageInfo<AsynTaskDetailResult> findPageBySpec(AsynTaskDetailParam param){
        Page<AsynTaskDetailResult> pageContext = getPageContext();
        IPage<AsynTaskDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AsynTaskDetailParam param){
        return param.getDetailId();
    }

    private Page<AsynTaskDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private AsynTaskDetail getOldEntity(AsynTaskDetailParam param) {
        return this.getById(getKey(param));
    }

    private AsynTaskDetail getEntity(AsynTaskDetailParam param) {
        AsynTaskDetail entity = new AsynTaskDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
