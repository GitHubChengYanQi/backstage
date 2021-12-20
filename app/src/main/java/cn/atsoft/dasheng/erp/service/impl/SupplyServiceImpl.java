package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Supply;
import cn.atsoft.dasheng.erp.mapper.SupplyMapper;
import cn.atsoft.dasheng.erp.model.params.SupplyParam;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SupplyResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.SupplyService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 供应商供应物料 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-12-20
 */
@Service
public class SupplyServiceImpl extends ServiceImpl<SupplyMapper, Supply> implements SupplyService {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SkuService skuService;

    @Override
    public void add(SupplyParam param) {
        Supply entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(SupplyParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(SupplyParam param) {
        Supply oldEntity = getOldEntity(param);
        Supply newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SupplyResult findBySpec(SupplyParam param) {
        return null;
    }

    @Override
    public List<SupplyResult> findListBySpec(SupplyParam param) {
        return null;
    }

    @Override
    public PageInfo<SupplyResult> findPageBySpec(SupplyParam param) {
        Page<SupplyResult> pageContext = getPageContext();
        IPage<SupplyResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    /**
     * 批量增加供应物
     *
     * @param supplyParams
     */
    @Override
    public void addList(List<SupplyParam> supplyParams, Long customerId) {
        if (ToolUtil.isNotEmpty(supplyParams)) {
            throw new ServiceException(500, "请添加供应物料");
        }
        Customer customer = customerService.getById(customerId);
        if (ToolUtil.isEmpty(customer)) {
            throw new ServiceException(500, "客户错误");
        }
        if (customer.getSupply() != 1) {
            throw new ServiceException(500, "当前客户不是供应商");
        }
        List<Supply> supplies = new ArrayList<>();
        for (SupplyParam supplyParam : supplyParams) {
            supplyParam.setCustomerId(customerId);
            Supply supply = new Supply();
            ToolUtil.copyProperties(supplyParam, supply);
            supplies.add(supply);
        }
        this.saveBatch(supplies);
    }

    @Override
    public List<SupplyResult> detail(Long customerId) {
        List<Supply> supplies = this.query().eq("customer_id", customerId).list();

        List<SupplyResult> supplyResults = new ArrayList<>();
        for (Supply supply : supplies) {
            SupplyResult supplyResult = new SupplyResult();
            ToolUtil.copyProperties(supply, supplyResult);
            supplyResults.add(supplyResult);
        }
        format(supplyResults);
        return supplyResults;
    }

    private Serializable getKey(SupplyParam param) {
        return param.getSupplyId();
    }

    private Page<SupplyResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Supply getOldEntity(SupplyParam param) {
        return this.getById(getKey(param));
    }

    private Supply getEntity(SupplyParam param) {
        Supply entity = new Supply();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<SupplyResult> data) {

        for (SupplyResult datum : data) {
            SkuResult sku = skuService.getSku(datum.getSkuId());
            datum.setSkuResult(sku);
        }

    }
}
