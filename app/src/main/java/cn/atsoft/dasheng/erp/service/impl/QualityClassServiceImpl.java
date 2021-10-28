package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityClass;
import cn.atsoft.dasheng.erp.mapper.QualityClassMapper;
import cn.atsoft.dasheng.erp.model.params.QualityClassParam;
import cn.atsoft.dasheng.erp.model.result.QualityClassResult;
import cn.atsoft.dasheng.erp.service.QualityClassService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 质检方案详情分类 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-28
 */
@Service
public class QualityClassServiceImpl extends ServiceImpl<QualityClassMapper, QualityClass> implements QualityClassService {

    @Override
    public Long add(QualityClassParam param) {
        QualityClass entity = getEntity(param);
        this.save(entity);
        return entity.getQualityPlanClassId();
    }

    @Override
    public void delete(QualityClassParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(QualityClassParam param) {
        QualityClass oldEntity = getOldEntity(param);
        QualityClass newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public QualityClassResult findBySpec(QualityClassParam param) {
        return null;
    }

    @Override
    public List<QualityClassResult> findListBySpec(QualityClassParam param) {
        return null;
    }

    @Override
    public PageInfo<QualityClassResult> findPageBySpec(QualityClassParam param) {
        Page<QualityClassResult> pageContext = getPageContext();
        IPage<QualityClassResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(QualityClassParam param) {
        return param.getQualityPlanClassId();
    }

    private Page<QualityClassResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private QualityClass getOldEntity(QualityClassParam param) {
        return this.getById(getKey(param));
    }

    private QualityClass getEntity(QualityClassParam param) {
        QualityClass entity = new QualityClass();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
