package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityCheckClassification;
import cn.atsoft.dasheng.erp.mapper.QualityCheckClassificationMapper;
import cn.atsoft.dasheng.erp.model.params.QualityCheckClassificationParam;
import cn.atsoft.dasheng.erp.model.result.QualityCheckClassificationResult;
import cn.atsoft.dasheng.erp.service.QualityCheckClassificationService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 质检分类表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-27
 */
@Service
public class QualityCheckClassificationServiceImpl extends ServiceImpl<QualityCheckClassificationMapper, QualityCheckClassification> implements QualityCheckClassificationService {

    @Override
    public void add(QualityCheckClassificationParam param) {
        Integer count = this.query().eq("name", param.getName()).count();
        if (count > 0) {
            throw new ServiceException(500, "已有重复分类");
        }
        QualityCheckClassification entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(QualityCheckClassificationParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(QualityCheckClassificationParam param) {
        QualityCheckClassification oldEntity = getOldEntity(param);
        QualityCheckClassification newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public QualityCheckClassificationResult findBySpec(QualityCheckClassificationParam param) {
        return null;
    }

    @Override
    public List<QualityCheckClassificationResult> findListBySpec(QualityCheckClassificationParam param) {
        return null;
    }

    @Override
    public PageInfo<QualityCheckClassificationResult> findPageBySpec(QualityCheckClassificationParam param) {
        Page<QualityCheckClassificationResult> pageContext = getPageContext();
        IPage<QualityCheckClassificationResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(QualityCheckClassificationParam param) {
        return param.getQualityCheckClassificationId();
    }

    private Page<QualityCheckClassificationResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private QualityCheckClassification getOldEntity(QualityCheckClassificationParam param) {
        return this.getById(getKey(param));
    }

    private QualityCheckClassification getEntity(QualityCheckClassificationParam param) {
        QualityCheckClassification entity = new QualityCheckClassification();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
