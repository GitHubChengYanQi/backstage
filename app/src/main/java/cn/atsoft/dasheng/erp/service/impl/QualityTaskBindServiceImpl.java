package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityTaskBind;
import cn.atsoft.dasheng.erp.mapper.QualityTaskBindMapper;
import cn.atsoft.dasheng.erp.model.params.QualityTaskBindParam;
import cn.atsoft.dasheng.erp.model.result.QualityTaskBindResult;
import  cn.atsoft.dasheng.erp.service.QualityTaskBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author song
 * @since 2021-11-17
 */
@Service
public class QualityTaskBindServiceImpl extends ServiceImpl<QualityTaskBindMapper, QualityTaskBind> implements QualityTaskBindService {

    @Override
    public void add(QualityTaskBindParam param){
        QualityTaskBind entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(QualityTaskBindParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(QualityTaskBindParam param){
        QualityTaskBind oldEntity = getOldEntity(param);
        QualityTaskBind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public QualityTaskBindResult findBySpec(QualityTaskBindParam param){
        return null;
    }

    @Override
    public List<QualityTaskBindResult> findListBySpec(QualityTaskBindParam param){
        return null;
    }

    @Override
    public PageInfo<QualityTaskBindResult> findPageBySpec(QualityTaskBindParam param){
        Page<QualityTaskBindResult> pageContext = getPageContext();
        IPage<QualityTaskBindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(QualityTaskBindParam param){
        return param.getBingId();
    }

    private Page<QualityTaskBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private QualityTaskBind getOldEntity(QualityTaskBindParam param) {
        return this.getById(getKey(param));
    }

    private QualityTaskBind getEntity(QualityTaskBindParam param) {
        QualityTaskBind entity = new QualityTaskBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
