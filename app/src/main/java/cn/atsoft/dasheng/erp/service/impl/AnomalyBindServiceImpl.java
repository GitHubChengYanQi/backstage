package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AnomalyBind;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.mapper.AnomalyBindMapper;
import cn.atsoft.dasheng.erp.model.params.AnomalyBindParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyBindResult;
import cn.atsoft.dasheng.erp.service.AnomalyBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.InkindService;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.params.OrCodeBindParam;
import cn.atsoft.dasheng.orCode.model.result.OrCodeBindResult;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private InkindService inkindService;
    @Autowired
    private OrCodeBindService orCodeBindService;
    @Autowired
    private OrCodeService orCodeService;
    @Autowired
    private StockDetailsService stockDetailsService;

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

    /**
     * 异常临时生成实物
     */
    @Override
    public OrCodeBind addInKindByAnomaly(AnomalyBindParam param) {


        Inkind inkind = new Inkind();
        inkind.setNumber(param.getNumber());
        inkind.setSkuId(param.getSkuId());
        inkind.setSource("临时异常");
        inkind.setType("1");
        inkind.setCustomerId(param.getCustomerId());
        inkind.setBrandId(param.getBrandId());
        inkind.setPositionId(param.getPositionId());
        inkindService.save(inkind);

        OrCode orCode = new OrCode();
        orCode.setState(1);
        orCode.setType("item");
        orCodeService.save(orCode);

        OrCodeBindParam bindParam = new OrCodeBindParam();
        bindParam.setOrCodeId(orCode.getOrCodeId());
        bindParam.setFormId(inkind.getInkindId());
        bindParam.setSource("item");


        return orCodeBindService.add(bindParam);
    }


    @Override
    public List<OrCodeBindResult> backStockInKind(AnomalyBindParam param) {

        QueryWrapper<StockDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sku_id", param.getSkuId());
        queryWrapper.eq("brand_id", param.getBrandId());
        if (ToolUtil.isNotEmpty(param.getCustomerId())) {
            queryWrapper.eq("customer_id", param.getCustomerId());
        }
        queryWrapper.eq("storehouse_positions_id", param.getPositionId());
        queryWrapper.eq("display", 1);
        List<StockDetails> stockDetails = stockDetailsService.list(queryWrapper);

        List<Long> inkindIds = new ArrayList<>();
        Map<Long, Long> inkindNum = new HashMap<>();
        for (StockDetails stockDetail : stockDetails) {
            inkindIds.add(stockDetail.getInkindId());
            inkindNum.put(stockDetail.getInkindId(), stockDetail.getNumber());
        }

        List<OrCodeBind> orCodeBinds = inkindIds.size() == 0 ? new ArrayList<>() : orCodeBindService.query().in("form_id", inkindIds).eq("display", 1).list();
        List<OrCodeBindResult> orCodeBindResults = BeanUtil.copyToList(orCodeBinds, OrCodeBindResult.class);

        for (OrCodeBindResult orCodeBindResult : orCodeBindResults) {
            Long num = inkindNum.get(orCodeBindResult.getFormId());
            orCodeBindResult.setNum(num);
        }
        return orCodeBindResults;
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
