package cn.atsoft.dasheng.taxRate.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.taxRate.entity.TaxRate;
import cn.atsoft.dasheng.taxRate.mapper.TaxRateMapper;
import cn.atsoft.dasheng.taxRate.model.params.TaxRateParam;
import cn.atsoft.dasheng.taxRate.model.result.TaxRateResult;
import cn.atsoft.dasheng.taxRate.service.TaxRateService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author
 * @since 2021-12-21
 */
@Service
public class TaxRateServiceImpl extends ServiceImpl<TaxRateMapper, TaxRate> implements TaxRateService {

    @Override
    public void add(TaxRateParam param) {
        TaxRate entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(TaxRateParam param) {
        TaxRate taxRate = new TaxRate();
        taxRate.setDisplay(0);
        this.update(taxRate, new QueryWrapper<TaxRate>() {{
            eq("tax_rate_id", param.getTaxRateId());
        }});
    }

    @Override
    public void update(TaxRateParam param) {
        TaxRate taxRate = this.getById(param.getTaxRateId());
        if (!taxRate.getDisplay().equals(param.getDisplay())) {
            throw new ServiceException(500, "傻瓜");
        }
        TaxRate oldEntity = getOldEntity(param);
        TaxRate newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public TaxRateResult findBySpec(TaxRateParam param) {
        return null;
    }

    @Override
    public List<TaxRateResult> findListBySpec(TaxRateParam param) {
        return null;
    }

    @Override
    public PageInfo<TaxRateResult> findPageBySpec(TaxRateParam param) {
        Page<TaxRateResult> pageContext = getPageContext();
        IPage<TaxRateResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(TaxRateParam param) {
        return param.getTaxRateId();
    }

    private Page<TaxRateResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private TaxRate getOldEntity(TaxRateParam param) {
        return this.getById(getKey(param));
    }

    private TaxRate getEntity(TaxRateParam param) {
        TaxRate entity = new TaxRate();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
