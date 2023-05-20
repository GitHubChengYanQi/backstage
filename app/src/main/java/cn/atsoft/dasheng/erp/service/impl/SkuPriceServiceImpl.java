package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.SkuPrice;
import cn.atsoft.dasheng.erp.mapper.SkuPriceMapper;
import cn.atsoft.dasheng.erp.model.params.SkuPriceParam;
import cn.atsoft.dasheng.erp.model.result.SkuPriceListResult;
import cn.atsoft.dasheng.erp.model.result.SkuPriceResult;
import cn.atsoft.dasheng.erp.service.SkuPriceService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品价格设置表 服务实现类
 * </p>
 *
 * @author sjl
 * @since 2023-01-09
 */
@Service
public class SkuPriceServiceImpl extends ServiceImpl<SkuPriceMapper, SkuPrice> implements SkuPriceService {


    @Override
    public void add(SkuPriceParam param) {

        if (ToolUtil.isNotEmpty(param.getPrice()) && ToolUtil.isNotEmpty(param.getSkuId())) {
            this.update(new SkuPrice() {{
                setDisplay(0);
            }}, new QueryWrapper<SkuPrice>() {{
                eq("sku_id", param.getSkuId());
                eq("type",param.getType());
            }});
            SkuPrice entity = getEntity(param);
//            entity.setPrice((int)Math.round(param.getPrice() * 100));
            this.save(entity);
        }

    }
    @Override
    public void messageAdd(SkuPriceParam param) {

        if (ToolUtil.isNotEmpty(param.getPrice()) && ToolUtil.isNotEmpty(param.getSkuId())) {
            this.update(new SkuPrice() {{
                setDisplay(0);
            }}, new QueryWrapper<SkuPrice>() {{
                eq("sku_id", param.getSkuId());
                eq("type",param.getType());
            }});
            SkuPrice entity = getEntity(param);
            entity.setPrice((int)Math.round(param.getPrice() * 100));
            this.save(entity);
        }

    }

    @Override
    public void addBatch(List<SkuPriceParam> params) {
    }

    @Override
    public void delete(SkuPriceParam param) {

    }

    @Override
    public void update(SkuPriceParam param) {

    }

    @Override
    public List<SkuPriceListResult> skuPriceResultBySkuIds(List<Long> skuIds) {
        List<SkuPrice> skuPriceList = skuIds.size() == 0 ? new ArrayList<>() : this.query().in("sku_id", skuIds).eq("display", 1).list();
        return  BeanUtil.copyToList(skuPriceList, SkuPriceListResult.class);
    }

    @Override
    public SkuPriceResult findBySpec(SkuPriceParam param) {
        return null;
    }

    @Override
    public List<SkuPriceResult> findListBySpec(SkuPriceParam param) {
        return null;
    }

    @Override
    public PageInfo<SkuPriceResult> findPageBySpec(SkuPriceParam param) {
        Page<SkuPriceResult> pageContext = getPageContext();
        IPage<SkuPriceResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }


    private Page<SkuPriceResult> getPageContext() {
        return PageFactory.defaultPage();
    }


    private SkuPrice getEntity(SkuPriceParam param) {
        SkuPrice entity = new SkuPrice();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
