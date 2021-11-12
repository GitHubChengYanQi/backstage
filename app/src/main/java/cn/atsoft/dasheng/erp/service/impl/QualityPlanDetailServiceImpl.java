package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityPlanDetail;
import cn.atsoft.dasheng.erp.mapper.QualityPlanDetailMapper;
import cn.atsoft.dasheng.erp.model.params.QualityPlanDetailParam;
import cn.atsoft.dasheng.erp.model.result.QualityPlanDetailResult;
import cn.atsoft.dasheng.erp.service.QualityPlanDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 质检方案详情 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-10-28
 */
@Service
public class QualityPlanDetailServiceImpl extends ServiceImpl<QualityPlanDetailMapper, QualityPlanDetail> implements QualityPlanDetailService {

    @Override
    public void add(QualityPlanDetailParam param) {
        QualityPlanDetail entity = getEntity(param);
        this.save(entity);
    }


    @Override
    public void delete(QualityPlanDetailParam param) {
        param.setDisplay(0);
        this.update(param);
    }


    @Override
    public void update(QualityPlanDetailParam param) {
        QualityPlanDetail oldEntity = getOldEntity(param);
        QualityPlanDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public QualityPlanDetailResult findBySpec(QualityPlanDetailParam param) {
        return null;
    }

    @Override
    public List<QualityPlanDetailResult> findListBySpec(QualityPlanDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<QualityPlanDetailResult> findPageBySpec(QualityPlanDetailParam param) {
        Page<QualityPlanDetailResult> pageContext = getPageContext();
        IPage<QualityPlanDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(QualityPlanDetailParam param) {
        return param.getPlanDetailId();
    }

    private Page<QualityPlanDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private QualityPlanDetail getOldEntity(QualityPlanDetailParam param) {
        return this.getById(getKey(param));
    }

    private QualityPlanDetail getEntity(QualityPlanDetailParam param) {
        QualityPlanDetail entity = new QualityPlanDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<QualityPlanDetailResult> data) {

        for (QualityPlanDetailResult datum : data) {
            
        }
    }
}
