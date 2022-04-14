package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.StorehousePositionsMapper;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsParam;
import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsBindService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.orCode.service.impl.QrCodeCreateService;
import cn.atsoft.dasheng.printTemplate.entity.PrintTemplate;
import cn.atsoft.dasheng.printTemplate.model.result.PrintTemplateResult;
import cn.atsoft.dasheng.printTemplate.service.PrintTemplateService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    @Autowired
    private OrCodeService codeService;
    @Autowired
    private StorehousePositionsBindService storehousePositionsBindService;

    @Override
    public void add(StorehousePositionsParam param) {
        if (ToolUtil.isNotEmpty(param.getPid())) {
            List<StockDetails> stockDetails = stockDetailsService.query().eq("storehouse_positions_id", param.getPid()).list();
//            if (ToolUtil.isNotEmpty(stockDetails)) {
//                throw new ServiceException(500, "上级库位以使用，不能再创建下级库位");
//            }
        }

        Integer count = this.query().eq("name", param.getName()).eq("pid", param.getPid()).eq("display", 1).count();
        if (count > 0) {
            throw new ServiceException(500, "名字以重复");
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

        List<Long> idSet = new ArrayList<>();
        StorehousePositions positions = this.query().eq("storehouse_positions_id", id).eq("display", 1).one();
        if (ToolUtil.isNotEmpty(positions)) {
            List<StorehousePositions> details = this.query().eq("pid", positions.getStorehousePositionsId()).eq("display", 1).list();
            for (StorehousePositions detail : details) {
                idSet.add(detail.getStorehousePositionsId());
                childrensSkuIds.add(detail.getStorehousePositionsId());
                Map<String, List<Long>> childrenMap = this.getChildrens(detail.getStorehousePositionsId());
                childrensSkuIds.addAll(childrenMap.get("childrens"));
            }
            result.put("children", idSet);
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

    //判断当前库位是否为最下级
    @Override
    public Long judgePosition(Long positionId) {
        if (ToolUtil.isEmpty(positionId)) {
            throw new ServiceException(500, "库位 IS NULL");
        }
        StorehousePositions positions = this.query().eq("pid", positionId).one();
        if (ToolUtil.isNotEmpty(positions)) {
            throw new ServiceException(500, "当前库位不是最下级");
        }
        return positionId;
    }


    @Override
    public void delete(StorehousePositionsParam param) {
        List<StorehousePositions> list = this.query().eq("pid", param.getStorehousePositionsId()).list();
        if (ToolUtil.isNotEmpty(list)) {
            throw new ServiceException(500, "当前仓位不能删除");
        }
        List<StockDetails> details = stockDetailsService.query().eq("storehouse_positions_id", param.getStorehousePositionsId()).list();
        if (ToolUtil.isNotEmpty(details)) {
            throw new ServiceException(500, "库位已被使用，不可以删除");
        }
        StorehousePositions positions = this.getById(param.getStorehousePositionsId());
        positions.setDisplay(0);
        this.updateById(positions);
    }

    @Override
    @Transactional
    public void update(StorehousePositionsParam param) {

        StorehousePositions oldEntity = getOldEntity(param);
        StorehousePositions newEntity = getEntity(param);

        if (!oldEntity.getPid().equals(newEntity.getPid())) {
            List<StorehousePositions> storehousePositions = this.query().like("childrens", param.getStorehousePositionsId()).list();
            for (StorehousePositions positions : storehousePositions) {

                JSONArray jsonArray = JSONUtil.parseArray(positions.getChildrens());
                JSONArray childrenJson = JSONUtil.parseArray(positions.getChildren());

                List<Long> oldchildrenList = JSONUtil.toList(childrenJson, Long.class);
                List<Long> newChildrenList = new ArrayList<>();
                List<Long> longs = JSONUtil.toList(jsonArray, Long.class);
                longs.remove(param.getStorehousePositionsId());

                for (Long aLong : oldchildrenList) {
                    if (aLong.equals(param.getStorehousePositionsId())) {
                        newChildrenList.remove(aLong);
                    }
                }
                positions.setChildren(JSON.toJSONString(newChildrenList));
                positions.setChildrens(JSON.toJSONString(longs));
                this.update(positions, new QueryWrapper<StorehousePositions>().in("storehouse_positions_id", positions.getStorehousePositionsId()));
            }

        }

        if (ToolUtil.isNotEmpty(param.getPid())) {
            StorehousePositions one = this.query().eq("storehouse_positions_id", param.getStorehousePositionsId()).eq("display", 1).one();
            JSONArray jsonArray = JSONUtil.parseArray(one.getChildrens());
            List<Long> longs = JSONUtil.toList(jsonArray, Long.class);
            for (Long aLong : longs) {
                if (param.getPid().equals(aLong)) {
                    throw new ServiceException(500, "请勿循环添加");
                }
            }

        }

        // 更新当前节点，及下级
        StorehousePositions storehousePositions = new StorehousePositions();
        Map<String, List<Long>> childrenMap = getChildrens(param.getPid());
        List<Long> childrens = childrenMap.get("childrens");

        if (childrens.stream().noneMatch(i -> i.equals(param.getStorehousePositionsId()))) {
            childrens.add(param.getStorehousePositionsId());
        }

        storehousePositions.setChildrens(JSON.toJSONString(childrens));
        List<Long> children = childrenMap.get("children");

        if (children.stream().noneMatch(i -> i.equals(param.getStorehousePositionsId()))) {
            children.add(param.getStorehousePositionsId());
        }

        storehousePositions.setChildren(JSON.toJSONString(children));
        QueryWrapper<StorehousePositions> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("storehouse_positions_id", param.getPid());
        this.update(storehousePositions, QueryWrapper);

        updateChildren(param.getPid());
        //---------------------------------------------------------------------------------------------------------------------


        if (ToolUtil.isNotEmpty(param.getPid()) && !newEntity.getPid().equals(oldEntity.getPid())) {
            List<StorehousePositionsBind> positionsBinds = storehousePositionsBindService.query()
                    .eq("position_id", newEntity.getPid()).eq("display", 1).list();
            if (ToolUtil.isNotEmpty(positionsBinds)) {
                throw new ServiceException(500, "上级库位已绑定物料，不可修改位置");
            }
        }

        if (!oldEntity.getName().equals(newEntity.getName())) {
            Integer count = this.query().eq("name", newEntity.getName()).eq("pid", newEntity.getPid()).count();
            if (count > 0) {
                throw new ServiceException(500, "名字以重复");
            }
        }

        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);


    }

    @Override
    public StorehousePositionsResult positionsResultById(Long Id) {

        StorehousePositions storehousePositions = this.getById(Id);

        if (ToolUtil.isNotEmpty(storehousePositions) && ToolUtil.isEmpty(storehousePositions.getChildren())) {
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
            eq("display", 1);
        }});

        if (ToolUtil.isEmpty(printTemplate)) {
            throw new ServiceException(500, "请先定义二维码模板");
        }

        String templete = printTemplate.getTemplete();
        Long codeId = null;
        if (templete.contains("${qrCode}")) {
            OrCodeBind orCodeBind = orCodeBindService.getOne(new QueryWrapper<OrCodeBind>() {{
                eq("form_id", param.getStorehousePositionsId());
            }});

            if (ToolUtil.isEmpty(orCodeBind)) { //绑定
                OrCode orCode = new OrCode();
                orCode.setType("storehousePositions");
                orCode.setState(1);
                codeService.save(orCode);
                OrCodeBind codeBind = new OrCodeBind();
                codeBind.setOrCodeId(orCode.getOrCodeId());
                codeBind.setFormId(param.getStorehousePositionsId());
                codeBind.setSource("storehousePositions");
                orCodeBindService.save(codeBind);
                codeId = orCode.getOrCodeId();
            } else {
                codeId = orCodeBind.getOrCodeId();
            }
//            String url = mobileService.getMobileConfig().getUrl() + "/cp/#/OrCode?id=" + codeId;
//            String qrCode = qrCodeCreateService.createQrCode(url);
            //二维码中只存放id
            String qrCode = qrCodeCreateService.createQrCode(codeId.toString());
            templete = templete.replace("${qrCode}", qrCode);
        }
        if (templete.contains("${bind}")) {
            List<StorehousePositionsBind> binds = storehousePositionsBindService.query().eq("position_id", param.getStorehousePositionsId()).list();
            List<Long> skuIds = new ArrayList<>();
            for (StorehousePositionsBind bind : binds) {
                skuIds.add(bind.getSkuId());
            }
            StringBuffer stringBuffer = new StringBuffer();
            List<SkuResult> skuResultListAndFormat = skuService.formatSkuResult(skuIds);
            for (SkuResult skuResult : skuResultListAndFormat) {
                String name = skuResult.getSpuResult().getName();
                String name1 = skuResult.getStandard();
                stringBuffer.append(name1).append("/").append(name).append("<br/>");
            }
            templete = templete.replace("${bind}", stringBuffer.toString());
        }
        if (templete.contains("${name}")) {
            templete = templete.replace("${name}", param.getName());
        }
        if (templete.contains("${parent}")) {
            templete = templete.replace("${parent}", this.getParent(param.getStorehousePositionsId()));
        }
        /**
         * 物料替换
         */
        List<StorehousePositionsBind> storehousePositionsBinds = storehousePositionsBindService.query().eq("position_id", param.getStorehousePositionsId()).eq("display", 1).list();
        List<Long> skuIds = new ArrayList<>();
        for (StorehousePositionsBind storehousePositionsBind : storehousePositionsBinds) {
            skuIds.add(storehousePositionsBind.getSkuId());
        }
        templete = replace(templete, skuIds);
        templete = templete.replaceAll("\\n", "");

        PrintTemplateResult printTemplateResult = new PrintTemplateResult();
        ToolUtil.copyProperties(printTemplate, printTemplateResult);

        printTemplateResult.setTemplete(templete);
        param.setPrintTemplateResult(printTemplateResult);
    }

    private String getParent(Long id) {
        StorehousePositions positions = this.getById(id);
        Storehouse storehouse = storehouseService.getById(positions.getStorehouseId());
        List<StorehousePositions> storehousePositionsList = this.query().eq("storehouse_id", positions.getStorehouseId()).eq("display", 1).list();
        StringBuffer stringBuffer = this.formatParentStringBuffer(positions, storehousePositionsList, new StringBuffer());
        stringBuffer = new StringBuffer().append(storehouse.getName()).append("/").append(stringBuffer);


        return stringBuffer.toString();
    }

    /**
     * 模板替换
     *
     * @param templete
     * @param skuIds
     * @return
     */
    private String replace(String templete, List<Long> skuIds) {

        if (ToolUtil.isEmpty(skuIds)) {
            if (templete.contains("${{序号}}")) {
                templete = templete.replace("${{序号}}", "");
            }
            if (templete.contains("${{物料编码}}")) {
                templete = templete.replace("${{物料编码}}", "");
            }
            if (templete.contains("${{产品名称}}")) {
                templete = templete.replace("${{产品名称}}", "");
            }
            if (templete.contains("${{型号}}")) {
                templete = templete.replace("${{型号}}", "");
            }
            if (templete.contains("${{规格}}")) {
                templete = templete.replace("${{规格}}", "");
            }
            return templete;
        }

        String regStr = "\\<tr(.*?)\\>([\\w\\W]+?)<\\/tr>";
        Pattern pattern = Pattern.compile(regStr);
        Matcher m = pattern.matcher(templete);
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer append = stringBuffer.append(templete);

        while (m.find()) {
            String skuGroup = m.group(0);
            if (skuGroup.contains("sku")) {
                StringBuilder all = new StringBuilder();
                List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
                int i = 0;
                for (SkuResult skuResult : skuResults) {
                    StringBuilder group = new StringBuilder(m.group(0));
                    i++;
                    if (group.toString().contains("${{序号}}")) {
                        group = new StringBuilder(group.toString().replace("${{序号}}", i + ""));
                    }
                    if (group.toString().contains("${{物料编码}}") && ToolUtil.isNotEmpty(skuResult)) {
                        group = new StringBuilder(group.toString().replace("${{物料编码}}", skuResult.getStandard()));
                    }
                    if (group.toString().contains("${{产品名称}}") && ToolUtil.isNotEmpty(skuResult)) {
                        group = new StringBuilder(group.toString().replace("${{产品名称}}", skuResult.getSpuResult().getName()));
                    }
                    if (group.toString().contains("${{型号}}") && ToolUtil.isNotEmpty(skuResult)) {
                        group = new StringBuilder(group.toString().replace("${{型号}}", skuResult.getSkuName()));
                    }
                    if (group.toString().contains("${{规格}}")) {
                        if (ToolUtil.isNotEmpty(skuResult) && ToolUtil.isNotEmpty(skuResult.getSpecifications())) {
                            group = new StringBuilder(group.toString().replace("${{规格}}", skuResult.getSpecifications()));
                        } else {
                            group = new StringBuilder(group.toString().replace("${{规格}}", ""));
                        }
                    }

                    all.append(group);
                }
                String toString = all.toString();
                String group = m.group(0);
//                String string = append.toString();
                templete = templete.replace(group, toString);
            }
        }
        return templete;
    }


    private StringBuffer formatParentStringBuffer(StorehousePositions positions, List<StorehousePositions> storehousePositionsList, StringBuffer stringBuffer) {
        if (!positions.getPid().equals(0L)) {
            for (StorehousePositions storehousePositions : storehousePositionsList) {
                if (positions.getPid().equals(storehousePositions.getStorehousePositionsId())) {
                    StringBuffer now = new StringBuffer().append(positions.getName()).append("/").append(stringBuffer);
                    stringBuffer = now;
                    stringBuffer = this.formatParentStringBuffer(storehousePositions, storehousePositionsList, stringBuffer);
                }

            }
        } else {
            StringBuffer now = new StringBuffer().append(positions.getName()).append("/").append(stringBuffer);
            stringBuffer = now;
        }
        return stringBuffer;
    }


    @Override
    public StorehousePositionsResult findBySpec(StorehousePositionsParam param) {
        return null;
    }

    @Override
    public List<StorehousePositionsResult> findListBySpec(StorehousePositionsParam param, DataScope dataScope) {

        List<StorehousePositionsResult> storehousePositionsResults = this.baseMapper.customList(param, dataScope);
        List<Long> positionIds = new ArrayList<>();
        for (StorehousePositionsResult storehousePositions : storehousePositionsResults) {
            positionIds.add(storehousePositions.getStorehousePositionsId());
        }
        List<StorehousePositionsBind> binds = positionIds.size() == 0 ? new ArrayList<>() : storehousePositionsBindService.query().in("position_id", positionIds).list();
        List<Long> skuIds = new ArrayList<>();
        for (StorehousePositionsBind bind : binds) {
            skuIds.add(bind.getSkuId());
        }
//        List<SkuSimpleResult> skuList = skuIds.size() == 0 ? new ArrayList<>() : skuService.backSkuList(skuIds);
        List<SkuSimpleResult> skuList = skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);
//        skuService.format(skuList);
        for (StorehousePositionsResult storehousePositions : storehousePositionsResults) {
            List<SkuSimpleResult> skuResults = new ArrayList<>();
            for (StorehousePositionsBind bind : binds) {
                for (SkuSimpleResult result : skuList) {
                    if (storehousePositions.getStorehousePositionsId().equals(bind.getPositionId()) && bind.getSkuId().equals(result.getSkuId())) {
                        skuResults.add(result);
                    }
                }
            }
            storehousePositions.setSkuResults(skuResults);
        }
        return storehousePositionsResults;
    }

    @Override
    public PageInfo<StorehousePositionsResult> findPageBySpec(StorehousePositionsParam param) {
        Page<StorehousePositionsResult> pageContext = getPageContext();
        IPage<StorehousePositionsResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    /**
     * 详情 带结构
     *
     * @param id
     * @param positions
     * @return
     */
    @Override
    public StorehousePositionsResult getDetail(Long id, List<StorehousePositions> positions) {
        List<StorehousePositionsResult> results = BeanUtil.copyToList(positions, StorehousePositionsResult.class, new CopyOptions());
        for (StorehousePositionsResult result : results) {
            if (result.getStorehousePositionsId().equals(id)) {
                getSupper(result, results);
                return result;
            }
        }
        return new StorehousePositionsResult();
    }

    @Override
    public List<StorehousePositionsResult> getDetails(List<Long> ids) {
        if (ToolUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<StorehousePositions> positions = this.list();
        List<StorehousePositionsResult> results = BeanUtil.copyToList(positions, StorehousePositionsResult.class, new CopyOptions());
        List<StorehousePositionsResult> positionsResults = new ArrayList<>();

        for (Long id : ids) {
            for (StorehousePositionsResult result : results) {
                if (ToolUtil.isNotEmpty(id) && id.equals(result.getStorehousePositionsId())) {
                    StorehousePositionsResult positionsResult = getSupper(result, results);
                    positionsResults.add(positionsResult);
                    break;
                }
            }
        }
        return positionsResults;
    }

    private StorehousePositionsResult getSupper(StorehousePositionsResult result, List<StorehousePositionsResult> data) {

        for (StorehousePositionsResult datum : data) {
            if (result.getPid().equals(datum.getStorehousePositionsId())) {
                result.setSupper(datum);
                getSupper(datum, data);
            }
        }
        return result;
    }

    @Override
    public Map<String, Map<String, Object>> takeStock(StorehousePositionsParam param) {
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

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public StorehousePositionsResult detail(Long id) {
        StorehousePositions positions = this.getById(id);
        StorehousePositionsResult result = new StorehousePositionsResult();
        ToolUtil.copyProperties(positions, result);

        if (ToolUtil.isNotEmpty(positions)) {
            JSONArray jsonArray = JSONUtil.parseArray(positions.getChildrens()); //查询所有子集
            List<Long> longs = JSONUtil.toList(jsonArray, Long.class);
            List<StorehousePositionsResult> results = positionsResults(longs);

            recursive(result, results);
        }
        return result;
    }

    /**
     * 返回多个
     *
     * @param ids
     * @return
     */
    @Override
    public List<StorehousePositionsResult> positionsResults(List<Long> ids) {
        if (ToolUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<StorehousePositions> storehousePositions = this.listByIds(ids);

        return BeanUtil.copyToList(storehousePositions, StorehousePositionsResult.class, new CopyOptions());
    }

    /**
     * @param skuId
     * @return
     */
    @Override
    public List<StorehousePositionsResult> getSupperBySkuId(Long skuId) {
        List<StorehousePositionsResult> results = new ArrayList<>();

        QueryWrapper<StockDetails> stockDetailsQW = new QueryWrapper<>();
        stockDetailsQW.select("sku_id,storehouse_positions_id,sum(number) as num").eq("sku_id", skuId).groupBy("sku_id,storehouse_positions_id");
        List<StockDetails> stockDetails = stockDetailsService.list(stockDetailsQW);

        Map<Long, Integer> map = new HashMap<>();
        for (StockDetails stockDetail : stockDetails) {
            map.put(stockDetail.getStorehousePositionsId(), Math.toIntExact(stockDetail.getNum()));
        }
        List<StorehousePositions> positionsList = this.list();
        List<StorehousePositionsResult> positionsResults = BeanUtil.copyToList(positionsList, StorehousePositionsResult.class, new CopyOptions());

        for (StorehousePositionsResult positionsResult : positionsResults) {
            for (Long id : map.keySet()) {
                if (id.equals(positionsResult.getStorehousePositionsId())) {
                    loopSuppler(positionsResult, positionsResults);
                    Integer skuNumber = map.get(id);
                    positionsResult.setSkuNumber(skuNumber);
                    results.add(positionsResult);
                    break;
                }
            }
        }

        return results;
    }

    /**
     * 递归取上级
     */
    private void loopSuppler(StorehousePositionsResult positionsResult, List<StorehousePositionsResult> positionsResults) {
        for (StorehousePositionsResult result : positionsResults) {
            if (positionsResult.getPid().equals(result.getStorehousePositionsId())) {
                positionsResult.setSupper(result);
                loopSuppler(result, positionsResults);
            }
        }

    }

    /**
     * 递归取下级
     *
     * @param result
     * @param results
     */
    private void recursive(StorehousePositionsResult result, List<StorehousePositionsResult> results) {
        List<StorehousePositionsResult> positionsList = new ArrayList<>();


        for (StorehousePositionsResult storehousePositionsResult : results) {
            if (storehousePositionsResult.getPid().equals(result.getStorehousePositionsId())) {
                positionsList.add(storehousePositionsResult);
                recursive(storehousePositionsResult, results);
            }
        }
        result.setStorehousePositionsResults(positionsList);
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

    /**
     * 通过库位id查询仓库id
     *
     * @param postitionIds
     * @return
     */
    @Override
    public Map<Long, Long> getHouseByPositionId(List<Long> postitionIds) {
        Map<Long, Long> map = new HashMap<>();
        List<Long> houseIds = new ArrayList<>();

        List<StorehousePositions> positions = postitionIds.size() == 0 ? new ArrayList<>() : this.listByIds(postitionIds);

        for (StorehousePositions position : positions) {
            houseIds.add(position.getStorehouseId());
        }
        List<Storehouse> storehouses = houseIds.size() == 0 ? new ArrayList<>() : storehouseService.listByIds(houseIds);

        for (StorehousePositions position : positions) {
            for (Storehouse storehouse : storehouses) {
                if (position.getStorehouseId().equals(storehouse.getStorehouseId())) {
                    map.put(position.getStorehousePositionsId(), storehouse.getStorehouseId());
                    break;
                }
            }
        }

        return map;
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
