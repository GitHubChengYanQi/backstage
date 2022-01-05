package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.Category;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.mapper.StorehousePositionsMapper;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsParam;
import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.InkindResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.impl.QrCodeCreateService;
import cn.atsoft.dasheng.printTemplate.entity.PrintTemplate;
import cn.atsoft.dasheng.printTemplate.model.result.PrintTemplateResult;
import cn.atsoft.dasheng.printTemplate.service.PrintTemplateService;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.atsoft.dasheng.form.pojo.PrintTemplateEnum.PHYSICALDETAIL;
import static cn.atsoft.dasheng.form.pojo.PrintTemplateEnum.POSITIONS;

/**
 * <p>
 * 仓库库位表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
@Service
public class StorehousePositionsServiceImpl extends ServiceImpl<StorehousePositionsMapper, StorehousePositions> implements StorehousePositionsService {
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private OrCodeBindService orCodeBindService;
    @Autowired
    private QrCodeCreateService qrCodeCreateService;

    @Autowired
    private PrintTemplateService printTemplateService;

    @Override
    public void add(StorehousePositionsParam param) {
        if (ToolUtil.isNotEmpty(param.getPid())) {
            StockDetails stockDetails = stockDetailsService.query().eq("storehouse_positions_id", param.getPid()).one();
            if (ToolUtil.isNotEmpty(stockDetails)) {
                throw new ServiceException(500, "上级库位以使用，不能再创建下级库位");
            }
        }

        StorehousePositions entity = getEntity(param);
        this.save(entity);

        StorehousePositions positions = new StorehousePositions();
        Map<String, List<Long>> childrenMap = getChildrens(entity.getPid());
        positions.setChildrens(JSON.toJSONString(childrenMap.get("childrens")));
        positions.setChildren(JSON.toJSONString(childrenMap.get("children")));
        QueryWrapper<StorehousePositions> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("storehouse_positions_id", entity.getPid());
        this.update(positions, QueryWrapper);

        updateChildren(entity.getPid());
    }

    /**
     * 递归
     */
    public Map<String, List<Long>> getChildrens(Long id) {

        List<Long> childrensSkuIds = new ArrayList<>();
        Map<String, List<Long>> result = new HashMap<String, List<Long>>() {
            {
                put("children", new ArrayList<>());
                put("childrens", new ArrayList<>());
            }
        };

        List<Long> skuIds = new ArrayList<>();
        StorehousePositions positions = this.query().eq("storehouse_positions_id", id).eq("display", 1).one();
        if (ToolUtil.isNotEmpty(positions)) {
            List<StorehousePositions> details = this.query().eq("pid", positions.getStorehousePositionsId()).eq("display", 1).list();
            for (StorehousePositions detail : details) {
                skuIds.add(detail.getStorehousePositionsId());
                childrensSkuIds.add(detail.getStorehousePositionsId());
                Map<String, List<Long>> childrenMap = this.getChildrens(detail.getStorehousePositionsId());
                childrensSkuIds.addAll(childrenMap.get("childrens"));
            }
            result.put("children", skuIds);
            result.put("childrens", childrensSkuIds);
        }
        return result;
    }

    /**
     * 更新包含它的
     */
    public void updateChildren(Long id) {
        List<StorehousePositions> positions = this.query().like("children", id).eq("display", 1).list();
        for (StorehousePositions storehousePositions : positions) {
            Map<String, List<Long>> childrenMap = getChildrens(id);
            JSONArray childrensjsonArray = JSONUtil.parseArray(storehousePositions.getChildrens());
            List<Long> longs = JSONUtil.toList(childrensjsonArray, Long.class);
            List<Long> list = childrenMap.get("childrens");
            longs.addAll(list);
            storehousePositions.setChildrens(JSON.toJSONString(longs));
            // update
            QueryWrapper<StorehousePositions> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("storehouse_positions_id", storehousePositions.getStorehousePositionsId());
            this.update(storehousePositions, queryWrapper);
            updateChildren(storehousePositions.getStorehousePositionsId());
        }
    }

    @Override
    public void delete(StorehousePositionsParam param) {
        StorehousePositions positions = this.getById(param.getStorehousePositionsId());
        if (ToolUtil.isNotEmpty(positions.getChildren())) {
            throw new ServiceException(500, "当前仓位不能删除");
        }
        List<StockDetails> details = stockDetailsService.query().eq("storehouse_positions_id", param.getStorehousePositionsId()).list();
        if (ToolUtil.isNotEmpty(details)) {
            throw new ServiceException(500, "库位已被使用，不可以删除");
        }
        param.setDisplay(0);
        this.update(param);
    }

    @Override
    public void update(StorehousePositionsParam param) {
        StorehousePositions positions = new StorehousePositions();
        positions.setName(param.getName());
        this.update(positions, new QueryWrapper<StorehousePositions>() {{
            eq("storehouse_positions_id", param.getStorehousePositionsId());
        }});
    }

    public StorehousePositionsResult positionsResultByCodeId(Long codeId) {

        OrCodeBind orCodeBind = orCodeBindService.query().eq("qr_code_id", codeId).one();

        StorehousePositions storehousePositions = this.getById(orCodeBind.getFormId());

        if (ToolUtil.isEmpty(storehousePositions.getChildren())) {

            StorehousePositionsResult positionsResult = new StorehousePositionsResult();
            ToolUtil.copyProperties(storehousePositions, positionsResult);
            StorehouseResult serviceDetail = storehouseService.getDetail(positionsResult.getStorehouseId());
            positionsResult.setStorehouseResult(serviceDetail);
            returnPrintTemplate(positionsResult);
            return positionsResult;
        }

        return null;
    }

    private void returnPrintTemplate(StorehousePositionsResult param) {
        PrintTemplate printTemplate = printTemplateService.getOne(new QueryWrapper<PrintTemplate>() {{
            eq("type", POSITIONS);
        }});

        if (ToolUtil.isEmpty(printTemplate)) {
            throw new ServiceException(500, "请先定义二维码模板");
        }

        String templete = printTemplate.getTemplete();
        if (templete.contains("${qrCode}")) {
            OrCodeBind orCodeBind = orCodeBindService.getOne(new QueryWrapper<OrCodeBind>() {{
                eq("form_id", param.getStorehousePositionsId());
            }});
            String url = mobileService.getMobileConfig().getUrl() + "/cp/#/OrCode?id=" + orCodeBind.getOrCodeId();
            String qrCode = qrCodeCreateService.createQrCode(url);
            templete = templete.replace("${qrCode}", qrCode);
        }
        if (templete.contains("${name}")) {
            templete = templete.replace("${name}", param.getName());
        }
        PrintTemplateResult printTemplateResult = new PrintTemplateResult();
        ToolUtil.copyProperties(printTemplate, printTemplateResult);
        printTemplateResult.setTemplete(templete);
        param.setPrintTemplateResult(printTemplateResult);
    }


    @Override
    public StorehousePositionsResult findBySpec(StorehousePositionsParam param) {
        return null;
    }

    @Override
    public List<StorehousePositionsResult> findListBySpec(StorehousePositionsParam param) {
        return null;
    }

    @Override
    public PageInfo<StorehousePositionsResult> findPageBySpec(StorehousePositionsParam param) {
        Page<StorehousePositionsResult> pageContext = getPageContext();
        IPage<StorehousePositionsResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public Map<String, Map<String, Object>> takeStock(StorehousePositionsParam param) {
        param.getStorehousePositionsId();
        List<Long> skuIds = new ArrayList<>();
        List<StockDetails> stockDetailsList = stockDetailsService.lambdaQuery().in(StockDetails::getStorehousePositionsId, param.getStorehousePositionsId()).list();
        for (StockDetails stockDetails : stockDetailsList) {
            skuIds.add(stockDetails.getSkuId());

        }

        List<Long> collect = skuIds.stream().distinct().collect(Collectors.toList());
        List<Sku> skuList = skuService.lambdaQuery().in(Sku::getSkuId, collect).list();

        Map<String, Map<String, Object>> resultMap = new HashMap<>();
        List<List<StockDetails>> listArrayList = new ArrayList<>();
        for (Long skuId : collect) {
            List<StockDetails> stockDetails = new ArrayList<>();
            List<BackSku> backSkus = skuService.backSku(skuId);
            for (StockDetails stockDetail : stockDetailsList) {
                if (skuId.equals(stockDetail.getSkuId())) {
                    Map<String, Object> map = new HashMap<>();
                    ToolUtil.copyProperties(stockDetail, map);
                    map.put("skuValue", backSkus);
                    resultMap.put(stockDetail.getStockItemId().toString(), map);
                }
            }
            listArrayList.add(stockDetails);
        }
        System.out.println(listArrayList);
        return resultMap;
    }

    private Serializable getKey(StorehousePositionsParam param) {
        return param.getStorehousePositionsId();
    }

    private Page<StorehousePositionsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private StorehousePositions getOldEntity(StorehousePositionsParam param) {
        return this.getById(getKey(param));
    }

    private StorehousePositions getEntity(StorehousePositionsParam param) {
        StorehousePositions entity = new StorehousePositions();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<StorehousePositionsResult> data) {
        List<Long> storeIds = new ArrayList<>();
        List<Long> pids = new ArrayList<>();
        for (StorehousePositionsResult datum : data) {
            storeIds.add(datum.getStorehouseId());
            pids.add(datum.getPid());
        }
        List<Storehouse> storehouses = storeIds.size() == 0 ? new ArrayList<>() : storehouseService.query().in("storehouse_id", storeIds).list();

        List<StorehousePositions> positions = pids.size() == 0 ? new ArrayList<>() :
                this.query().in("storehouse_positions_id", pids).list();

        for (StorehousePositionsResult datum : data) {
            if (ToolUtil.isNotEmpty(storehouses)) {
                for (Storehouse storehouse : storehouses) {
                    if (datum.getStorehouseId() != null && storehouse.getStorehouseId().equals(datum.getStorehouseId())) {
                        StorehouseResult storehouseResult = new StorehouseResult();
                        ToolUtil.copyProperties(storehouse, storehouseResult);
                        datum.setStorehouseResult(storehouseResult);
                    }
                }
            }
            if (ToolUtil.isNotEmpty(positions)) {
                for (StorehousePositions position : positions) {
                    if (position.getStorehousePositionsId().equals(datum.getPid())) {
                        StorehousePositionsResult storehousePositionsResult = new StorehousePositionsResult();
                        ToolUtil.copyProperties(datum, storehousePositionsResult);
                        datum.setStorehousePositionsResult(storehousePositionsResult);
                    }
                }
            }
        }
    }
}
