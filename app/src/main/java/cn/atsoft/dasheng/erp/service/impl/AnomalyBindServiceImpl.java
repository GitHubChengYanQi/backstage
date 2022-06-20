package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AnomalyBind;
import cn.atsoft.dasheng.erp.mapper.AnomalyBindMapper;
import cn.atsoft.dasheng.erp.model.params.AnomalyBindParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyBindResult;
import cn.atsoft.dasheng.erp.service.AnomalyBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 异常生成的实物 绑定 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-05-28
 */
@Service
public class AnomalyBindServiceImpl extends ServiceImpl<AnomalyBindMapper, AnomalyBind> implements AnomalyBindService {

    @Override
    public void add(AnomalyBindParam param) {
        AnomalyBind entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(AnomalyBindParam param) {
        AnomalyBind entity = getEntity(param);
        entity.setDisplay(0);
        this.updateById(entity);
    }

    @Override
    public void update(AnomalyBindParam param) {
        AnomalyBind oldEntity = getOldEntity(param);
        AnomalyBind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AnomalyBindResult findBySpec(AnomalyBindParam param) {
        return null;
    }

    @Override
    public List<AnomalyBindResult> findListBySpec(AnomalyBindParam param) {
        return this.baseMapper.customList(param);
    }


    @Override
    public PageInfo<AnomalyBindResult> findPageBySpec(AnomalyBindParam param) {
        Page<AnomalyBindResult> pageContext = getPageContext();
        IPage<AnomalyBindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AnomalyBindParam param) {
        return param.getBindId();
    }

    private Page<AnomalyBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private AnomalyBind getOldEntity(AnomalyBindParam param) {
        return this.getById(getKey(param));
    }

    private AnomalyBind getEntity(AnomalyBindParam param) {
        AnomalyBind entity = new AnomalyBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
