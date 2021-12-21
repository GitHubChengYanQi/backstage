package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlanDetal;
import cn.atsoft.dasheng.purchase.mapper.ProcurementPlanDetalMapper;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanDetalParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanDetalResult;
import  cn.atsoft.dasheng.purchase.service.ProcurementPlanDetalService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 采购计划单子表  整合数据后的 子表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-12-21
 */
@Service
public class ProcurementPlanDetalServiceImpl extends ServiceImpl<ProcurementPlanDetalMapper, ProcurementPlanDetal> implements ProcurementPlanDetalService {

    @Override
    public void add(ProcurementPlanDetalParam param){
        ProcurementPlanDetal entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProcurementPlanDetalParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProcurementPlanDetalParam param){
        ProcurementPlanDetal oldEntity = getOldEntity(param);
        ProcurementPlanDetal newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProcurementPlanDetalResult findBySpec(ProcurementPlanDetalParam param){
        return null;
    }

    @Override
    public List<ProcurementPlanDetalResult> findListBySpec(ProcurementPlanDetalParam param){
        return null;
    }

    @Override
    public PageInfo<ProcurementPlanDetalResult> findPageBySpec(ProcurementPlanDetalParam param){
        Page<ProcurementPlanDetalResult> pageContext = getPageContext();
        IPage<ProcurementPlanDetalResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProcurementPlanDetalParam param){
        return param.getDetailId();
    }

    private Page<ProcurementPlanDetalResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProcurementPlanDetal getOldEntity(ProcurementPlanDetalParam param) {
        return this.getById(getKey(param));
    }

    private ProcurementPlanDetal getEntity(ProcurementPlanDetalParam param) {
        ProcurementPlanDetal entity = new ProcurementPlanDetal();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
