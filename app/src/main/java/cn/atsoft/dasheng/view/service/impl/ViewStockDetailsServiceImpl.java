package cn.atsoft.dasheng.view.service.impl;


import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.view.entity.ViewStockDetails;
import cn.atsoft.dasheng.view.mapper.ViewStockDetailsMapper;
import cn.atsoft.dasheng.view.model.params.ViewStockDetailsParam;
import cn.atsoft.dasheng.view.model.result.ViewStockDetailsResult;
import cn.atsoft.dasheng.view.service.ViewStockDetailsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.alibaba.fastjson.JSON;
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
 * VIEW 服务实现类
 * </p>
 *
 * @author
 * @since 2022-01-27
 */
@Service
public class ViewStockDetailsServiceImpl extends ServiceImpl<ViewStockDetailsMapper, ViewStockDetails> implements ViewStockDetailsService {

    @Autowired
    StockDetailsService stockDetailsService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private PartsService partsService;


    @Override
    public ViewStockDetailsResult findBySpec(ViewStockDetailsParam param) {
        return null;
    }

    @Override
    public List<ViewStockDetailsResult> findListBySpec(ViewStockDetailsParam param) {
        List<ViewStockDetailsResult> results = null;
        switch (param.getType()) {
            case "sku":
                results = this.baseMapper.skuList(param);
                break;
            case "className":
                results = this.baseMapper.classNameList(param);
                break;
            case "spu":
                results = this.baseMapper.spuList(param);
                break;
            case "bom":
                Parts parts = partsService.getById(param.getPartId());
                List<Long> skuIds = JSON.parseArray(parts.getChildrens(), Long.class);
                param.setSkuIds(skuIds);
                results = this.baseMapper.bomList(param);
                break;
            default:
                return null;
        }
        format(results);
        return results;
    }

    @Override
    public PageInfo<ViewStockDetailsResult> findPageBySpec(ViewStockDetailsParam param) {
        Page<ViewStockDetailsResult> pageContext = getPageContext();
        IPage<ViewStockDetailsResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ViewStockDetailsParam param) {
        return null;
    }

    private Page<ViewStockDetailsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ViewStockDetails getOldEntity(ViewStockDetailsParam param) {
        return this.getById(getKey(param));
    }

    private ViewStockDetails getEntity(ViewStockDetailsParam param) {
        ViewStockDetails entity = new ViewStockDetails();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<ViewStockDetailsResult> data) {
        List<Long> skuIds = new ArrayList<>();

        for (ViewStockDetailsResult dutm : data) {
            skuIds.add(dutm.getSkuId());
        }
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        for (ViewStockDetailsResult datum : data) {
            for (SkuResult skuResult : skuResults) {
                if (datum.getSkuId().equals(skuResult.getSkuId())) {
                    datum.setSkuResult(skuResult);
                    break;
                }
            }
        }
    }

}
