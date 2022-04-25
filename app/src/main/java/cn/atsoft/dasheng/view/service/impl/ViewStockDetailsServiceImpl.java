package cn.atsoft.dasheng.view.service.impl;


import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.view.entity.ViewStockDetails;
import cn.atsoft.dasheng.view.mapper.ViewStockDetailsMapper;
import cn.atsoft.dasheng.view.model.params.ViewStockDetailsParam;
import cn.atsoft.dasheng.view.model.result.ViewStockDetailsResult;
import cn.atsoft.dasheng.view.service.ViewStockDetailsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
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
    @Autowired
    private StorehousePositionsService positionsService;
    @Autowired
    private StorehouseService storehouseService;


    @Override
    public ViewStockDetailsResult findBySpec(ViewStockDetailsParam param) {
        return null;
    }

    @Override
    public PageInfo<ViewStockDetailsResult> findListBySpec(ViewStockDetailsParam param) {
        Page<ViewStockDetailsResult> results = null;
        Page<ViewStockDetailsResult> page = getPageContext();
        if (ToolUtil.isEmpty(param.getType())) {
            param.setType("sku");
        }
        switch (param.getType()) {
            case "sku":
                results = this.baseMapper.skuList(page, param);
                break;
            case "className":
                results = this.baseMapper.classNameList(page, param);
                break;
            case "spu":
                results = this.baseMapper.spuList(page, param);
                break;
            case "bom":
                Parts parts = partsService.getById(param.getPartId());
                List<Long> skuIds = JSON.parseArray(parts.getChildrens(), Long.class);
                param.setSkuIds(skuIds);
                results = this.baseMapper.bomList(page, param);
                break;
            default:
                return null;
        }

        List<ViewStockDetails> details = this.query().select(" sum(number) as  skuCount  ").groupBy("sku_id").list();

        long skuCount = 0L;
        for (ViewStockDetails detail : details) {
            skuCount = skuCount + detail.getSkuCount();
        }
        format(results.getRecords(), (long) details.size(), skuCount);
        return PageFactory.createPageInfo(page);
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

    private void format(List<ViewStockDetailsResult> data, Long skuTypeNum, Long skuCount) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> positionIds = new ArrayList<>();
        List<Long> houseIds = new ArrayList<>();


        for (ViewStockDetailsResult dutm : data) {
            skuIds.add(dutm.getSkuId());
            positionIds.add(dutm.getStorehousePositionsId());
            houseIds.add(dutm.getStorehouseId());
        }
        List<StorehousePositionsResult> positionsResultList = positionsService.getDetails(positionIds);
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);

        List<Storehouse> storehouses = houseIds.size() == 0 ? new ArrayList<>() : storehouseService.listByIds(houseIds);
        List<StorehouseResult> storehouseResults = BeanUtil.copyToList(storehouses, StorehouseResult.class, new CopyOptions());

        for (ViewStockDetailsResult datum : data) {

            datum.setSkuTypeNum(skuTypeNum);
            datum.setSkuCount(skuCount);

            for (SkuResult skuResult : skuResults) {
                if (datum.getSkuId().equals(skuResult.getSkuId())) {
                    datum.setSkuResult(skuResult);
                    break;
                }
            }
            for (StorehousePositionsResult positionsResult : positionsResultList) {
                if (ToolUtil.isNotEmpty(datum.getStorehousePositionsId()) && positionsResult.getStorehousePositionsId().equals(datum.getStorehousePositionsId())) {
                    datum.setPositionsResult(positionsResult);
                    break;
                }
            }
            for (StorehouseResult storehouseResult : storehouseResults) {
                if (ToolUtil.isNotEmpty(datum.getStorehouseId()) && storehouseResult.getStorehouseId().equals(datum.getStorehouseId())) {
                    datum.setStorehouseResult(storehouseResult);
                    break;
                }
            }

        }
    }

}
