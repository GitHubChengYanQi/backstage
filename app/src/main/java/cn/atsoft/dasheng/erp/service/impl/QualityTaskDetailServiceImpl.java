package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityTaskDetail;
import cn.atsoft.dasheng.erp.mapper.QualityTaskDetailMapper;
import cn.atsoft.dasheng.erp.model.params.QualityTaskDetailParam;
import cn.atsoft.dasheng.erp.model.result.QualityTaskDetailResult;
import  cn.atsoft.dasheng.erp.service.QualityTaskDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 质检任务详情 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-11-16
 */
@Service
public class QualityTaskDetailServiceImpl extends ServiceImpl<QualityTaskDetailMapper, QualityTaskDetail> implements QualityTaskDetailService {

    @Override
    public void add(QualityTaskDetailParam param){
        QualityTaskDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(QualityTaskDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(QualityTaskDetailParam param){
        QualityTaskDetail oldEntity = getOldEntity(param);
        QualityTaskDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public QualityTaskDetailResult findBySpec(QualityTaskDetailParam param){
        return null;
    }

    @Override
    public List<QualityTaskDetailResult> findListBySpec(QualityTaskDetailParam param){
        return null;
    }

    @Override
    public PageInfo<QualityTaskDetailResult> findPageBySpec(QualityTaskDetailParam param){
        Page<QualityTaskDetailResult> pageContext = getPageContext();
        IPage<QualityTaskDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(QualityTaskDetailParam param){
        return param.getQualityTaskDetailId();
    }

    private Page<QualityTaskDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private QualityTaskDetail getOldEntity(QualityTaskDetailParam param) {
        return this.getById(getKey(param));
    }

    private QualityTaskDetail getEntity(QualityTaskDetailParam param) {
        QualityTaskDetail entity = new QualityTaskDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
