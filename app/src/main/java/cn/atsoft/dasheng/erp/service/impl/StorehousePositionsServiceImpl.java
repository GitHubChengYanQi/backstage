package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.BrandService;
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
import cn.atsoft.dasheng.erp.pojo.PositionLoop;
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
import cn.atsoft.dasheng.production.entity.ProductionPickListsCart;
import cn.atsoft.dasheng.production.service.ProductionPickListsCartService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductionPickListsCartService cartService;

    @Override
    public void add(StorehousePositionsParam param) {
        if (ToolUtil.isNotEmpty(param.getPid())) {
            List<StockDetails> stockDetails = stockDetailsService.query().eq("storehouse_positions_id", param.getPid()).eq("display", 1).list();
            if (ToolUtil.isNotEmpty(stockDetails)) {
                throw new ServiceException(500, "上级库位已使用，不能再创建下级库位");
            }
        }

        Integer count = this.query().eq("name", param.getName()).eq("pid", param.getPid()).eq("display", 1).count();
        if (count > 0) {
            throw new ServiceException(500, "名字已重复");
        }

        StorehousePositions entity = getEntity(param);
        this.save(entity);
        updateChildren(entity.getPid());
    }

    /**
     * 递归
     */
    public List<Long> getChildrens(Long id) {

        List<StorehousePositions> positions = this.query().like("pid", id).eq("display", 1).list();

        List<Long> childrenList = new ArrayList<>();
        for (StorehousePositions storehousePositions : positions) {
            childrenList.add(storehousePositions.getStorehousePositionsId());
//            try{
//                JSONArray childrensjsonArray = JSONUtil.parseArray(storehousePositions.getChildren());
//                List<Long> longs = JSONUtil.toList(childrensjsonArray, Long.class);
//                childrenList.addAll(longs);
//            }catch (Exception e){
//                childrenList.addAll(Arrays.stream(storehousePositions.getChildren().split(",")).map(Long::parseLong).collect(Collectors.toList()));
//            }
            childrenList.addAll(getChildrens(storehousePositions.getStorehousePositionsId()));
        }
        return childrenList;
    }


    /**
     * 更新包含它的
     */
    public void updateChildren(Long id) {
        StorehousePositions position = this.getById(id);
        if(ToolUtil.isEmpty(position)) return;

        List<StorehousePositions> positions = this.query().like("pid", id).eq("display", 1).list();

        List<Long> children = new ArrayList<>();
        children.addAll(positions.stream().map(StorehousePositions::getStorehousePositionsId).collect(Collectors.toList()));

        List<Long> childrens = new ArrayList<Long>(){{
            add(position.getStorehousePositionsId());
        }};
        childrens.addAll(getChildrens(id));

        position.setChildren(StringUtils.join(children,","));
        position.setChildrens(StringUtils.join(childrens,","));
        this.updateById(position);
        updateChildren(position.getPid());
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
    public List<StorehousePositionsResult> details(List<Long> positionIds) {
        if (ToolUtil.isEmpty(positionIds)) {
            return new ArrayList<>();
        }
        List<StorehousePositions> positions = this.listByIds(positionIds);
        if (ToolUtil.isEmpty(positions)) {
            return new ArrayList<>();
        }

        List<StorehousePositionsResult> positionsResults = BeanUtil.copyToList(positions, StorehousePositionsResult.class);
        format(positionsResults);
        return positionsResults;
    }

    @Override
    public void delete(StorehousePositionsParam param) {
        List<StorehousePositions> list = this.query().eq("pid", param.getStorehousePositionsId()).eq("display", 1).list();
        if (ToolUtil.isNotEmpty(list)) {
            throw new ServiceException(500, "当前仓位不能删除");
        }
        List<StockDetails> details = stockDetailsService.query().eq("storehouse_positions_id", param.getStorehousePositionsId()).eq("display", 1).list();
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
        if (ToolUtil.isNotEmpty(param.getPid()) && !newEntity.getPid().equals(oldEntity.getPid())) {
            List<StorehousePositionsBind> positionsBinds = storehousePositionsBindService.query()
                    .eq("position_id", newEntity.getPid()).eq("display", 1).list();
            if (ToolUtil.isNotEmpty(positionsBinds)) {
                throw new ServiceException(500, "上级库位已绑定物料，不可修改位置");
            }
        }

        if (!oldEntity.getName().equals(newEntity.getName())) {
            Integer count = this.query().eq("name", newEntity.getName()).eq("pid", newEntity.getPid()).ne("storehouse_positions_id",newEntity.getStorehousePositionsId()).count();
            if (count > 0) {
                throw new ServiceException(500, "库位名称重复");
            }
        }

        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
        updateChildren(newEntity.getPid());

    }

    @Override
    public List<StorehousePositionsResult> selectBySku(StorehousePositionsParam param) {
        if (ToolUtil.isEmpty(param.getSkuId())) {
            return new ArrayList<>();
        }
        QueryWrapper<StockDetails> stockDetailsQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(param.getBrandId())) {
            stockDetailsQueryWrapper.eq("brand_id", param.getBrandId());
        }
        stockDetailsQueryWrapper.eq("sku_id", param.getSkuId());
        stockDetailsQueryWrapper.gt("number", 0);
        stockDetailsQueryWrapper.eq("display", 1);

        List<StockDetails> stockDetails = stockDetailsService.list(stockDetailsQueryWrapper);

        List<Long> positionIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();

        for (StockDetails stockDetail : stockDetails) {
            positionIds.add(stockDetail.getStorehousePositionsId());
            brandIds.add(stockDetail.getBrandId());

        }
        List<StorehousePositions> positions = positionIds.size() == 0 ? new ArrayList<>() : this.listByIds(positionIds);
        List<StorehousePositionsResult> positionsResults = BeanUtil.copyToList(positions, StorehousePositionsResult.class, new CopyOptions());
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
        Map<Long, List<BrandResult>> map = new HashMap<>();
        Map<Long, Long> positionNum = new HashMap<>();


        for (StockDetails stockDetail : stockDetails) {
            Long num = positionNum.get(stockDetail.getStorehousePositionsId());   //当前库位下的库存数
            if (ToolUtil.isEmpty(num)) {
                num = 0L;
            }
            num = num + stockDetail.getNumber();
            positionNum.put(stockDetail.getStorehousePositionsId(), num);

            for (BrandResult brandResult : brandResults) {


                if (ToolUtil.isNotEmpty(stockDetail.getBrandId()) && stockDetail.getBrandId().equals(brandResult.getBrandId())) {

                    int brandNumber = getBrandNumber(stockDetails, stockDetail.getStorehousePositionsId(), brandResult.getBrandId());
                    brandResult.setNum(brandNumber);

                    List<BrandResult> brandResultList = map.get(stockDetail.getStorehousePositionsId());
                    if (ToolUtil.isEmpty(brandResultList)) {
                        brandResultList = new ArrayList<>();
                    }
                    //去重品牌
                    if (brandResultList.stream().noneMatch(i -> i.getBrandId().equals(brandResult.getBrandId()))) {
                        brandResultList.add(brandResult);
                    }
                    map.put(stockDetail.getStorehousePositionsId(), brandResultList);
                }
            }
        }

        for (StorehousePositionsResult positionsResult : positionsResults) {
            Long num = positionNum.get(positionsResult.getStorehousePositionsId());
            positionsResult.setNumber(num);
            positionsResult.setBrandResults(map.get(positionsResult.getStorehousePositionsId()));
        }


        return positionsResults;
    }

    /**
     * 返回品牌集合，库位集合
     *
     * @param skuId
     * @return
     */
    @Override
    public Object selectBySku(Long skuId) {
        QueryWrapper<StockDetails> stockDetailsQueryWrapper = new QueryWrapper<>();
        stockDetailsQueryWrapper.eq("sku_id", skuId);
        stockDetailsQueryWrapper.gt("number", 0);
        stockDetailsQueryWrapper.eq("display", 1);

        List<StockDetails> stockDetails = stockDetailsService.list(stockDetailsQueryWrapper);
        List<StockDetailsResult> stockDetailsResults = BeanUtil.copyToList(stockDetails, StockDetailsResult.class, new CopyOptions());

        List<Long> positionIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();

        for (StockDetails stockDetail : stockDetails) {
            positionIds.add(stockDetail.getStorehousePositionsId());
            brandIds.add(stockDetail.getBrandId());
        }

        List<StorehousePositions> positions = positionIds.size() == 0 ? new ArrayList<>() : this.listByIds(positionIds);
        List<StorehousePositionsResult> positionsResults = BeanUtil.copyToList(positions, StorehousePositionsResult.class, new CopyOptions());
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);

        for (StockDetailsResult stockDetail : stockDetailsResults) {
            if (ToolUtil.isEmpty(stockDetail.getBrandId())) {
                stockDetail.setBrandId(0L);
            }
            for (BrandResult brandResult : brandResults) {
                if (stockDetail.getBrandId().equals(brandResult.getBrandId())) {
                    brandResult.setPositionId(stockDetail.getStorehousePositionsId());
                    brandResult.setNum(Math.toIntExact(stockDetail.getNumber()));
                    stockDetail.setBrandResult(brandResult);
                    break;
                }
            }

            for (StorehousePositionsResult positionsResult : positionsResults) {
                if (positionsResult.getStorehousePositionsId().equals(stockDetail.getStorehousePositionsId())) {
                    positionsResult.setBrandId(stockDetail.getBrandId());
                    positionsResult.setNum(Math.toIntExact(stockDetail.getNumber()));
                    stockDetail.setStorehousePositionsResult(positionsResult);
                    break;
                }
            }
        }
        List<StorehousePositionsResult> positionsResultList = new ArrayList<>();
        List<BrandResult> brandResultList = new ArrayList<>();
        for (StockDetailsResult stockDetail : stockDetailsResults) {
            brandResultList.add(stockDetail.getBrandResult());
            positionsResultList.add(stockDetail.getStorehousePositionsResult());
        }
        return new HashMap<String, Object>() {{
            put("brand", brandResultList);
            put("position", positionsResultList);
        }};
    }

    /**
     * 品牌库位联动结构
     *
     * @param skuId
     * @return
     */
    @Override
    public List<BrandResult> selectByBrand(Long skuId, Long brandId, Long storehouseId, Long positionId) {
        List<ProductionPickListsCart> list = cartService.query().eq("status", 0).list();
        List<Long> inkindIds = new ArrayList<>();
        for (ProductionPickListsCart pickListsCart : list) {
            inkindIds.add(pickListsCart.getInkindId());
        }


        QueryWrapper<StockDetails> stockDetailsQueryWrapper = new QueryWrapper<>();
        stockDetailsQueryWrapper.eq("sku_id", skuId);

        if (ToolUtil.isNotEmpty(brandId)) {
            stockDetailsQueryWrapper.eq("brand_id", brandId);
        }
        if (ToolUtil.isNotEmpty(positionId)) {
            stockDetailsQueryWrapper.eq("storehouse_positions_id", positionId);
        }
        if (ToolUtil.isNotEmpty(storehouseId)) {
            stockDetailsQueryWrapper.eq("storehouse_id", storehouseId);
        }

        stockDetailsQueryWrapper.gt("number", 0);
        stockDetailsQueryWrapper.eq("display", 1);
        if (inkindIds.size() > 0) {
            stockDetailsQueryWrapper.notIn("inkind_id", inkindIds);//将已经备料的实物抛出
        }

        List<StockDetails> stockDetails = stockDetailsService.list(stockDetailsQueryWrapper);  //物料查询库存

        List<Long> positionIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();

        boolean otherBrand = false;
        for (StockDetails stockDetail : stockDetails) {
            if (ToolUtil.isEmpty(stockDetail.getBrandId()) || stockDetail.getBrandId() == 0) {    //其他品牌
                stockDetail.setBrandId(0L);
                otherBrand = true;
            }
            positionIds.add(stockDetail.getStorehousePositionsId());
            brandIds.add(stockDetail.getBrandId());
        }
        List<StockDetailsResult> stockDetailsResults = BeanUtil.copyToList(stockDetails, StockDetailsResult.class, new CopyOptions());

        List<StorehousePositions> positions = positionIds.size() == 0 ? new ArrayList<>() : this.listByIds(positionIds);
        List<StorehousePositionsResult> positionsResults = BeanUtil.copyToList(positions, StorehousePositionsResult.class, new CopyOptions());
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);


        for (StockDetailsResult stockDetail : stockDetailsResults) {
            for (StorehousePositionsResult positionsResult : positionsResults) {
                if (stockDetail.getStorehousePositionsId().equals(positionsResult.getStorehousePositionsId())) {
                    stockDetail.setStorehousePositionsResult(positionsResult);
                    break;
                }
            }

        }

        if (otherBrand) {
            brandResults.add(new BrandResult() {{    //添加个其他品牌对象
                setBrandId(0L);
                setBrandName("无品牌");
            }});//其他品牌
        }


        Map<Long, Long> brandNumber = new HashMap<>();

        /**
         * 计算当品牌 在库存的数量
         */
        for (StockDetails stockDetail : stockDetails) {
            if (ToolUtil.isEmpty(stockDetail.getBrandId())) {
                stockDetail.setBrandId(0L);
            }
            Long num = brandNumber.get(stockDetail.getBrandId());   //当前库位下的库存数
            if (ToolUtil.isEmpty(num)) {
                num = 0L;
            }
            num = num + stockDetail.getNumber();
            brandNumber.put(stockDetail.getBrandId(), num);
        }


        for (BrandResult brandResult : brandResults) {
            Long num = brandNumber.get(brandResult.getBrandId());
            if (ToolUtil.isNotEmpty(num)) {
                brandResult.setNum(Math.toIntExact(num));
            }
            //库位数量
            List<StorehousePositionsResult> positionsResultList = new ArrayList<>();


            for (StockDetailsResult stockDetailsResult : stockDetailsResults) {
                if (stockDetailsResult.getBrandId().equals(brandResult.getBrandId())) {
                    StorehousePositionsResult positionsResult = stockDetailsResult.getStorehousePositionsResult();
                    if (ToolUtil.isNotEmpty(positionsResult)) {
                        StorehousePositionsResult newPosition = new StorehousePositionsResult();
                        ToolUtil.copyProperties(positionsResult, newPosition);
                        newPosition.setNumber(stockDetailsResult.getNumber());
                        newPosition.setNum(Math.toIntExact(stockDetailsResult.getNumber()));
                        boolean b = positionSet(positionsResultList, newPosition);
                        if (b) {
                            positionsResultList.add(newPosition);
                        }
                    }
                }
            }

            brandResult.setPositionsResults(positionsResultList);
        }

        return brandResults;
    }

    /**
     * 库位去重  数量叠加
     *
     * @param positionsResultList
     * @param newPosition
     * @return
     */
    private boolean positionSet(List<StorehousePositionsResult> positionsResultList, StorehousePositionsResult newPosition) {

        for (StorehousePositionsResult positionsResult : positionsResultList) {

            if (newPosition.getStorehousePositionsId().equals(positionsResult.getStorehousePositionsId())) {
                positionsResult.setNum(newPosition.getNum() + positionsResult.getNum());
                positionsResult.setNumber(newPosition.getNumber() + positionsResult.getNumber());
                return false;
            }

        }
        return true;
    }


    /**
     * 品牌数量
     *
     * @param stockDetails
     * @param positionId
     * @param brandId
     * @return
     */
    private int getBrandNumber(List<StockDetails> stockDetails, Long positionId, Long brandId) {
        int num = 0;
        for (StockDetails stockDetail : stockDetails) {
            if (ToolUtil.isNotEmpty(stockDetail.getBrandId()) && stockDetail.getStorehousePositionsId().equals(positionId) && stockDetail.getBrandId().equals(brandId)) {
                num = (int) (num + stockDetail.getNumber());
            }
        }
        return num;
    }

    private int getPositionNumber(List<StockDetails> stockDetails, Long positionId, Long brandId) {
        int num = 0;

        for (StockDetails stockDetail : stockDetails) {
            if (stockDetail.getStorehousePositionsId().equals(positionId) && stockDetail.getBrandId().equals(brandId)) {
                num = (int) (num + stockDetail.getNumber());
            }
        }

        return num;
    }

    /**
     * 物料涉及库位的数量
     *
     * @param skuIds
     * @return
     */
    @Override
    public Integer getPositionNum(List<Long> skuIds) {
        if (ToolUtil.isEmpty(skuIds)) {
            return 0;
        }
        Set<Long> positionIds = new HashSet<>();
//        List<StorehousePositionsBind> positionsBinds = storehousePositionsBindService.query().in("sku_id", skuIds).eq("display", 1).list();

//        for (StorehousePositionsBind positionsBind : positionsBinds) {
//            positionIds.add(positionsBind.getPositionId());
//        }
        List<StockDetails> stockDetails = stockDetailsService.query().in("sku_id", skuIds).eq("display", 1).list();
        for (StockDetails stockDetail : stockDetails) {
            positionIds.add(stockDetail.getStorehousePositionsId());
        }
        return positionIds.size();
    }

    @Override
    public StorehousePositionsResult positionsResultById(Long Id) {

        StorehousePositions storehousePositions = this.getById(Id);

        if (ToolUtil.isNotEmpty(storehousePositions)) {
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
        if (templete.contains("${bind}") || templete.contains("${{物料编码}}") || templete.contains("${{产品名称}}") || templete.contains("${{型号}}") || templete.contains("${{规格}}")) {
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
        /**
         * 替换中文
         */
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


    @Override
    public List<PositionLoop> treeViewBySku(List<Long> skuIds) {
        if (ToolUtil.isEmpty(skuIds)) {
            return null;
        }
        List<StorehousePositionsBind> positionsBindList = skuIds.size() == 0 ? new ArrayList<>() : storehousePositionsBindService.query().in("sku_id", skuIds).eq("display", 1).list();
        List<Long> positionIds = new ArrayList<>();  //最下级库位id
        for (StorehousePositionsBind positionsBind : positionsBindList) {
            positionIds.add(positionsBind.getPositionId());
        }
        //所有库位
        List<StorehousePositions> positions = this.query().eq("display", 1).list();
        return treeView(positionIds, positions);

    }

    @Override
    public List<PositionLoop> treeViewByName(String name, Long houseId) {
        QueryWrapper<StorehousePositions> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("display", 1);
        if (ToolUtil.isNotEmpty(houseId)) {
            queryWrapper.eq("storehouse_id", houseId);
        }
        List<StorehousePositions> positions = this.list(queryWrapper);
        List<Long> positionIds = new ArrayList<>();
        for (StorehousePositions position : positions) {
            if (ToolUtil.isEmpty(name)) {
                positionIds.add(position.getStorehousePositionsId());
            } else if (position.getName().contains(name)) {
                positionIds.add(position.getStorehousePositionsId());
            }
        }

        return treeView(positionIds, positions);
    }

    private List<PositionLoop> treeView(List<Long> positionIds, List<StorehousePositions> positionsResults) {

        List<PositionLoop> allPositionLoop = new ArrayList<>();
        for (StorehousePositions positionsResult : positionsResults) {
            PositionLoop loop = new PositionLoop();
            loop.setTitle(positionsResult.getName());
            loop.setKey(positionsResult.getStorehousePositionsId());
            loop.setStoreHouseId(positionsResult.getStorehouseId());
            loop.setPid(positionsResult.getPid());
            allPositionLoop.add(loop);
        }

        //需要的下级库位
        List<PositionLoop> childs = new ArrayList<>();
        for (Long positionId : positionIds) {
            for (PositionLoop loop : allPositionLoop) {
                if (loop.getKey().equals(positionId)) {
                    childs.add(loop);

                    if (loop.getPid() == 0) {
                        loop.setB(true);
                    }
                    break;
                }
            }
        }

        for (PositionLoop child : childs) {
            loop(child, allPositionLoop);
        }

        childs.clear();
        for (PositionLoop loop : allPositionLoop) {
            if (loop.isB() && loop.getPid() == 0) {
                childs.add(loop);
            }
        }

        return childs;
    }


    private void loop(PositionLoop child, List<PositionLoop> positions) {
        for (PositionLoop position : positions) {
            if (position.getKey().equals(child.getPid())) {
                position.setB(true);
                if (ToolUtil.isEmpty(position.getLoops())) {
                    position.setLoops(new ArrayList<>());
                }
                if (position.getLoops().stream().noneMatch(i -> i.getKey().equals(child.getKey()))) {
                    position.getLoops().add(child);
                }
                loop(position, positions);
            }
        }
    }

    @Override
    public void positionFormat(List<PositionLoop> positionLoops, Long skuId) {
        for (PositionLoop positionLoop : positionLoops) {
            if (ToolUtil.isNotEmpty(positionLoop.getLoops())) {
                positionFormat(positionLoop.getLoops(), skuId);
            } else {
                loopFormat(positionLoop, skuId);   //最下级
            }
        }
    }

    private void loopFormat(PositionLoop positionLoop, Long skuId) {
        QueryWrapper<StockDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("display", 1);
        queryWrapper.eq("sku_id", skuId);
        queryWrapper.eq("storehouse_positions_id", positionLoop.getKey());
        queryWrapper.select("sum(number) as num ");
        queryWrapper.groupBy("sku_id", "storehouse_positions_id");
        StockDetails stockDetails = stockDetailsService.getOne(queryWrapper);

        if (ToolUtil.isNotEmpty(stockDetails)) {
            positionLoop.setSkuId(skuId);
            positionLoop.setNum(stockDetails.getNum());
        }
    }

    /**
     * 通过库位查询skuId
     *
     * @return
     */
    @Override
    public List<Long> getLoopPositionIds(Long positionId) {
        List<Long> ids = new ArrayList<>();
        List<StorehousePositions> positions = this.query().eq("display", 1).list();

        for (StorehousePositions position : positions) {
            if (position.getStorehousePositionsId().equals(positionId)) {
                List<Long> positionIds = loopChild(position, positions);
                ids.addAll(positionIds);
            }
        }

        return ids;
    }

    /**
     * 递归找下级库位id
     *
     * @param position
     * @param positions
     * @return
     */
    private List<Long> loopChild(StorehousePositions position, List<StorehousePositions> positions) {

        List<Long> positionIds = new ArrayList<>();
        positionIds.add(position.getStorehousePositionsId());

        for (StorehousePositions storehousePositions : positions) {
            if (storehousePositions.getPid().equals(position.getStorehousePositionsId())) {
                positionIds.addAll(loopChild(storehousePositions, positions));
            }
        }

        return positionIds;
    }

    private List<Long> getSkuIdsByBind(List<Long> positionIds) {
        List<Long> skuIds = new ArrayList<>();
        if (ToolUtil.isEmpty(positionIds)) {
            return skuIds;
        }
        List<StockDetails> details = stockDetailsService.query().in("storehouse_positions_id", positionIds).eq("display", 1).list();

        for (StockDetails detail : details) {
            skuIds.add(detail.getSkuId());
        }

        /**
         * 插叙绑定
         */
//        List<StorehousePositionsBind> bindList = storehousePositionsBindService.query().in("position_id", positionIds).eq("display", 1).list();
//        for (StorehousePositionsBind positionsBind : bindList) {
//            skuIds.add(positionsBind.getSkuId());
//        }
        return skuIds;
    }

    /**
     * 物料绑定的库位
     *
     * @param skuIds
     * @return
     */
    @Override
    public Map<Long, List<StorehousePositionsResult>> getMap(List<Long> skuIds) {

        List<StorehousePositionsBind> positionsBinds = skuIds.size() == 0 ? new ArrayList<>() : storehousePositionsBindService.query().in("sku_id", skuIds).eq("display", 1).list();
        List<Long> positionIds = new ArrayList<>();


        for (StorehousePositionsBind positionsBind : positionsBinds) {
            positionIds.add(positionsBind.getPositionId());
        }

        List<StorehousePositions> positions = positionIds.size() == 0 ? new ArrayList<>() : this.listByIds(positionIds);
        List<StorehousePositionsResult> positionsResults = BeanUtil.copyToList(positions, StorehousePositionsResult.class, new CopyOptions());
        format(positionsResults);
        Map<Long, List<StorehousePositionsResult>> map = new HashMap<>();


        for (StorehousePositionsBind positionsBind : positionsBinds) {
            List<StorehousePositionsResult> results = new ArrayList<>();
            for (StorehousePositionsResult positionsResult : positionsResults) {
                if (positionsBind.getPositionId().equals(positionsResult.getStorehousePositionsId())) {
                    results.add(positionsResult);
                }
            }
            List<StorehousePositionsResult> list = map.get(positionsBind.getSkuId());
            if (ToolUtil.isNotEmpty(list)) {
                results.addAll(list);
            }
            map.put(positionsBind.getSkuId(), results);
        }
        return map;
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
                if (ToolUtil.isNotEmpty(skuResults) && skuResults.size() > 0) {
                    SkuResult skuResult = skuResults.get(0);
                    skuResults = new ArrayList<>();
                    skuResults.add(skuResult);
                }
                skuResults.removeIf(i -> i.getDisplay().equals(0));
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
        this.format(positionsResults);
        return positionsResults;
    }

    @Override
    public List<StorehousePositionsResult> resultsByIds(List<Long> ids) {
        if (ToolUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<StorehousePositions> positions = this.query().in("storehouse_positions_id", ids).eq("display", 1).list();
        List<StorehousePositionsResult> results = BeanUtil.copyToList(positions, StorehousePositionsResult.class, new CopyOptions());

        return results;
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

    @Override
    /**
     * 查询自己 和自己所有下级
     */
    public List<Long> getEndChild(Long positionId) {
        StorehousePositions self = this.getById(positionId);
        List<StorehousePositions> positions = this.query().eq("storehouse_id", self.getStorehouseId()).eq("display", 1).list();
        List<Long> positionIds = new ArrayList<>();
        positionIds.add(positionId);
        for (StorehousePositions position : positions) {
            if (position.getStorehousePositionsId().equals(positionId)) {
                positionIds.addAll(loop(positionId, positions));
            }
        }
        return positionIds;
    }

    @Override
    /**
     *
     */
    public List<Long> getEndChild(Long positionId, List<StorehousePositions> positions) {
        List<Long> positionIds = new ArrayList<>();
        positionIds.add(positionId);
        for (StorehousePositions position : positions) {
            if (position.getStorehousePositionsId().equals(positionId)) {
                positionIds.addAll(loop(positionId, positions));
            }
        }
        return positionIds;
    }


    private List<Long> loop(Long positionId, List<StorehousePositions> positions) {
        List<Long> positionIds = new ArrayList<>();
        for (StorehousePositions position : positions) {
            if (position.getPid().equals(positionId)) {
                positionIds.add(position.getStorehousePositionsId());
                positionIds.addAll(loop(position.getStorehousePositionsId(), positions));
            }
        }
        return positionIds;
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

    @Override
    public void format(List<StorehousePositionsResult> data) {
        List<Long> storeIds = new ArrayList<>();

        for (StorehousePositionsResult datum : data) {
            storeIds.add(datum.getStorehouseId());

        }
        List<Storehouse> storehouses = storeIds.size() == 0 ? new ArrayList<>() : storehouseService.query().in("storehouse_id", storeIds).list();
        for (StorehousePositionsResult datum : data) {
            for (Storehouse storehouse : storehouses) {
                if (datum.getStorehouseId() != null && storehouse.getStorehouseId().equals(datum.getStorehouseId())) {
                    StorehouseResult storehouseResult = new StorehouseResult();
                    ToolUtil.copyProperties(storehouse, storehouseResult);
                    datum.setStorehouseResult(storehouseResult);
                }
            }
        }


    }

    /**
     * 专门为物料查询仓库与库位 (库存对比)
     *
     * @param data
     */
    @Override
    public void skuFormat(List<SkuResult> data) {

        List<Long> houseIds = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();

        for (SkuResult datum : data) {
            houseIds.add(datum.getStorehouseId());
            skuIds.add(datum.getSkuId());
        }
        List<StorehousePositionsBind> binds = skuIds.size() == 0 ? new ArrayList<>() : storehousePositionsBindService.query().in("sku_id", skuIds).eq("display", 1).list();

        List<Long> positionIds = new ArrayList<>();
        for (StorehousePositionsBind bind : binds) {
            positionIds.add(bind.getPositionId());
        }
        List<StorehousePositions> positions = positionIds.size() == 0 ? new ArrayList<>() : this.listByIds(positionIds);
        List<StorehousePositionsResult> positionsResults = BeanUtil.copyToList(positions, StorehousePositionsResult.class, new CopyOptions());


        Map<Long, List<StorehousePositionsResult>> map = new HashMap<>();
        for (StorehousePositionsBind bind : binds) {

            for (StorehousePositionsResult positionsResult : positionsResults) {
                if (bind.getPositionId().equals(positionsResult.getStorehousePositionsId())) {

                    List<StorehousePositionsResult> positionsResultList = map.get(bind.getSkuId());
                    if (ToolUtil.isEmpty(positionsResultList)) {
                        positionsResultList = new ArrayList<>();
                    }
                    if (positionsResultList.stream().noneMatch(i -> i.getStorehousePositionsId().equals(positionsResult.getStorehousePositionsId()))) {
                        positionsResultList.add(positionsResult);
                    }
                    map.put(bind.getSkuId(), positionsResultList);
                }
            }
        }


        List<Storehouse> storehouses = houseIds.size() == 0 ? new ArrayList<>() : storehouseService.listByIds(houseIds);
        List<StorehouseResult> storehouseResults = BeanUtil.copyToList(storehouses, StorehouseResult.class, new CopyOptions());

        for (SkuResult datum : data) {

            datum.setPositionsResult(map.get(datum.getSkuId()));

            for (StorehouseResult storehouseResult : storehouseResults) {
                if (ToolUtil.isNotEmpty(datum.getStorehouseId()) && storehouseResult.getStorehouseId().equals(datum.getStorehouseId())) {
                    datum.setStorehouseResult(storehouseResult);
                    break;
                }
            }
        }
    }
}
