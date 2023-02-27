package cn.atsoft.dasheng.stockDetail.service;

//import cn.atsoft.dasheng.Excel.pojo.StockDetailExcel;
//import cn.atsoft.dasheng.app.entity.StockDetails;
//import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
//import cn.atsoft.dasheng.app.model.request.StockDetailView;
//import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
//import cn.atsoft.dasheng.app.pojo.SpuClassDetail;
//import cn.atsoft.dasheng.app.pojo.StockCensus;
//import cn.atsoft.dasheng.app.pojo.StockSkuBrand;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
//import cn.atsoft.dasheng.production.model.params.ProductionPickListsCartParam;
//import cn.atsoft.dasheng.purchase.pojo.ListingPlan;
import cn.atsoft.dasheng.stockDetail.entity.RestStockDetails;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 仓库物品明细表 服务类
 * </p>
 *
 * @author
 * @since 2021-07-15
 */
public interface RestStockDetailsService extends IService<RestStockDetails> {
//    @Override
//    public List<StockCensus> stockCensus() {
//        List<StockCensus> stockCensuses = new ArrayList<>();
//
////        List<StockDetails> stock = this.getStock();
//        List<RestStockDetailsResult> stock = this.baseMapper.stockInKindList();
//        Set<Long> skuIds = new HashSet<>();
//
//        long count = 0L;
//        for (RestStockDetailsResult details : stock) {
//            skuIds.add(details.getSkuId());
//            count = count + details.getNumber();
//        }
//
//        //库存所有类
//        StockCensus allSku = new StockCensus();
//        allSku.setName("skuNumber");
//        allSku.setNumber((long) skuIds.size());
//        stockCensuses.add(allSku);
//
//        //库存数
//        StockCensus stockCount = new StockCensus();
//        stockCount.setName("stockCount");
//        stockCount.setNumber(count);
//        stockCensuses.add(stockCount);
//
//
//        long normal = 0L;
//        long error = 0L;
//        Set<Long> normalSkuNum = new HashSet<>();
//        Set<Long> errorSkuNum = new HashSet<>();
//
//        for (RestStockDetailsResult details : stock) {
//            if (details.getAnomaly() == 0) {
//                normalSkuNum.add(details.getSkuId());
//                normal = normal + details.getNumber();
//            } else {
//                errorSkuNum.add(details.getSkuId());
//                error = error + details.getNumber();
//            }
//
//
//        }
//
//        //正常数
//        StockCensus normalCount = new StockCensus();
//        normalCount.setName("normal");
//        normalCount.setNumber(normal);
//        normalCount.setTypeNum((long) normalSkuNum.size());
//        stockCensuses.add(normalCount);
//
//        //异常数
//        StockCensus errorCount = new StockCensus();
//        errorCount.setName("error");
//        errorCount.setNumber(error);
//        errorCount.setTypeNum((long) errorSkuNum.size());
//        stockCensuses.add(errorCount);
//
//        return stockCensuses;
//    }
//
//
//    @Override
//
//    public PageInfo findPageBySpec(RestStockDetailsParam param, DataScope dataScope) {
//
//
////       if (ToolUtil.isNotEmpty(param.getMaintenanceId())) {
////            List<Long> inkindIds = getMaintenanceInkindIds(param.getMaintenanceId());
////            param.setInkindIds(inkindIds);
////        }
//        List<Long> lockedInkindIds = pickListsCartService.getLockedInkindIds();
//        if (ToolUtil.isNotEmpty(lockedInkindIds)) {
//            param.setLockedInkindIds(lockedInkindIds);
//        }
//
//        Page<RestStockDetailsResult> pageContext = getPageContext();
//        IPage<RestStockDetailsResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
//
//
//        format(page.getRecords());
//        return PageFactory.createPageInfo(page);
//    }
//
//    /**
//     * 需要养护的实物
//     *
//     * @param maintenanceId
//     * @return
//     */
//    private List<Long> getMaintenanceInkindIds(Long maintenanceId) {
//        Maintenance maintenance = maintenanceService.getById(maintenanceId);
//        List<StockDetails> stockDetails = maintenanceService.needMaintenanceByRequirement(maintenance);
//        List<Long> inkindIds = new ArrayList<>();
//        for (StockDetails stockDetail : stockDetails) {
//            inkindIds.add(stockDetail.getInkindId());
//        }
//        return inkindIds;
//    }
//
//    @Override
//    public List<Long> backSkuByStoreHouse(Long id) {
//        List<StockDetails> details = this.query().eq("storehouse_id", id).list();
//        List<Long> skuIds = new ArrayList<>();
//        if (ToolUtil.isNotEmpty(details)) {
//            for (StockDetails detail : details) {
//                skuIds.add(detail.getSkuId());
//            }
//        }
//        return skuIds;
//    }
//
//    /**
//     * 预购获取库存数量
//     *
//     * @param plans
//     */
//    @Override
//    public void preorder(List<ListingPlan> plans) {
//        if (ToolUtil.isNotEmpty(plans)) {
//
//            List<Long> skuIds = new ArrayList<>();
//            List<Long> brandIds = new ArrayList<>();
//            for (ListingPlan plan : plans) {
//                skuIds.add(plan.getSkuId());
//                brandIds.add(plan.getBrandId());
//            }
//            List<StockDetails> details = this.query().in("sku_id", skuIds).in("brand_id", brandIds).list();
//            for (ListingPlan plan : plans) {
//                long stockNumber = 0L;
//                for (StockDetails detail : details) {
//                    if (plan.getSkuId().equals(detail.getSkuId()) && plan.getBrandId().equals(detail.getBrandId())) {
//                        stockNumber = stockNumber + detail.getNumber();
//                    }
//                }
//                plan.setStockNumber(stockNumber);
//            }
//        }
//
//    }
//
//    /**
//     * 库存报表方法
//     */
//    @Override
//    @Scheduled(cron = "0 0 1 * * ?")   //每日凌晨一点执行
//    public void statement() {
//        statementAsync.startAnalyse();
//    }
//
//
//    @Override
//    public void splitInKind(Long inKind) {
//
//        Inkind inkindResult = inkindService.getById(inKind);
//        Sku sku = skuService.getById(inkindResult.getSkuId());
//        if (ToolUtil.isEmpty(sku.getBatch()) || sku.getBatch() == 0) {
//            StockDetails stockDetails = this.query().eq("inkind_id", inKind).one();   //库存有当前实物 无需操作
//            if (ToolUtil.isNotEmpty(stockDetails)) {
//                return;
//            }
//        }
//
//
//        /**
//         * 把之前实物数量拆开  入一个新的实物
//         */
//        StockDetails details = new StockDetails();
//        details.setSkuId(inkindResult.getSkuId());
//        details.setBrandId(inkindResult.getBrandId());
//        details.setCustomerId(inkindResult.getCustomerId());
//        if (ToolUtil.isEmpty(inkindResult.getPositionId())) {
//            throw new ServiceException(500, "缺少库位id");
//        }
//        StorehousePositions storehousePositions = positionsService.getById(inkindResult.getPositionId());
//        details.setStorehouseId(storehousePositions.getStorehouseId());
//        details.setStorehousePositionsId(inkindResult.getPositionId());
//        details.setNumber(inkindResult.getNumber());
//        details.setInkindId(inkindResult.getInkindId());
//
//        QueryWrapper<StockDetails> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("sku_id", inkindResult.getSkuId());
//        queryWrapper.eq("brand_id", inkindResult.getBrandId());
//        queryWrapper.eq("storehouse_positions_id", inkindResult.getPositionId());
//        if (ToolUtil.isNotEmpty(inkindResult.getCustomerId())) {
//            queryWrapper.eq("customer_id", inkindResult.getCustomerId());
//        }
//        queryWrapper.eq("display", 1);
//        queryWrapper.orderByAsc("create_time");
//
//        Long num = inkindResult.getNumber();
//        List<StockDetails> stockDetailsList = this.list(queryWrapper);
//        for (StockDetails stock : stockDetailsList) {
//            if (num == 0) {
//                break;
//            }
//            if (stock.getNumber() > num) {
//                stock.setNumber(stock.getNumber() - num);
//                break;
//            } else {
//                num = num - stock.getNumber();
//                stock.setNumber(0L);
//                stock.setDisplay(0);
//            }
//        }
//        this.updateBatchById(stockDetailsList);
//        this.save(details);
//        inkindResult.setSource("临时异常(生效)");
//        inkindService.updateById(inkindResult);
//    }
//
//    @Override
//    public List<RestStockDetailsResult> inkindList(Long skuId) {
//        List<StockDetails> stockDetails = this.query().eq("sku_id", skuId).eq("display", 1).list();
//        if (ToolUtil.isEmpty(stockDetails)) {
//            return new ArrayList<>();
//        }
//        List<RestStockDetailsResult> detailsResults = BeanUtil.copyToList(stockDetails, RestStockDetailsResult.class);
//        format(detailsResults);
//        return detailsResults;
//    }
//
//
//    @Override
//    public List<RestStockDetailsResult> getStockDetails(Long stockId) {
//        List<RestStockDetailsResult> detailsResults = new ArrayList<>();
//        if (ToolUtil.isEmpty(stockId)) {
//            return detailsResults;
//        }
//        List<StockDetails> details = this.query().eq("stock_id", stockId).eq("display", 1).list();
//
//        for (StockDetails detail : details) {
//            RestStockDetailsResult stockDetailsResult = new RestStockDetailsResult();
//            ToolUtil.copyProperties(detail, stockDetailsResult);
//            detailsResults.add(stockDetailsResult);
//        }
//        return detailsResults;
//    }
//
//    /**
//     * 获取库存物料品牌数量
//     *
//     * @return
//     */
//    @Override
//    public List<StockSkuBrand> stockSkuBrands() {
//
//        List<StockDetails> stockDetails = this.query().select("sku_id AS skuId,brand_id AS brandId ,sum(number) as num")
//                .eq("display", 1)
//                .groupBy("sku_id,brand_id").list();
//
//
//        List<StockSkuBrand> stockSkuBrands = new ArrayList<>();
//
//        for (StockDetails stockDetail : stockDetails) {
//            StockSkuBrand stockSkuBrand = new StockSkuBrand();
//            stockSkuBrand.setBrandId(stockDetail.getBrandId());
//            stockSkuBrand.setSkuId(stockDetail.getSkuId());
//            stockSkuBrand.setNumber(stockDetail.getNum());
//            stockSkuBrands.add(stockSkuBrand);
//        }
//
//
//        return stockSkuBrands;
//    }
//
//    @Override
//    public Integer getNumberByStock(Long skuId, Long brandId, Long positionId) {
//        Integer number = this.baseMapper.getNumberByStock(skuId, brandId, positionId);
//        if (ToolUtil.isEmpty(number)) {
//            number = 0;
//        }
//        return number;
//    }
//
//    @Override
//    public List<RestStockDetailsResult> getStockNumberBySkuId(Long skuId, Long storehouseId) {
//        List<StockDetails> details = this.query().eq("storehouse_id", storehouseId).eq("sku_id", skuId).list();
//        List<StockDetails> totalList = new ArrayList<>();
//        List<ProductionPickListsCart> carts = pickListsCartService.query().eq("display", 1).eq("status", 0).list();
//        List<Long> inkindIds = new ArrayList<>();
//        for (ProductionPickListsCart cart : carts) {
//            inkindIds.add(cart.getInkindId());
//        }
//
//        for (Long inkindId : inkindIds) {
//            details.removeIf(i -> i.getInkindId().equals(inkindId));
//        }
//        details.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_" + (ToolUtil.isEmpty(item.getBrandId()) ? 0 : item.getBrandId()) + "_" + item.getStorehousePositionsId(), Collectors.toList())).forEach(
//                (id, transfer) -> {
//                    transfer.stream().reduce((a, b) -> new StockDetails() {{
//                        setNumber(a.getNumber() + b.getNumber());
//                        setSkuId(a.getSkuId());
//                        setStorehousePositionsId(a.getStorehousePositionsId());
//                        setBrandId(a.getBrandId());
//                        setStorehouseId(a.getStorehouseId());
//                    }}).ifPresent(totalList::add);
//                }
//        );
//        List<RestStockDetailsResult> results = totalList.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(totalList, RestStockDetailsResult.class);
//        this.format(results);
//        return results;
//    }
//
//    @Override
//    public List<RestStockDetailsResult> getStockNumberBySkuId(RestStockDetailsParam param) {
//        if (ToolUtil.isEmpty(param.getSkuId())) {
//            return new ArrayList<>();
//        }
//        List<StockDetails> details = new ArrayList<>();
//        if (ToolUtil.isEmpty(param.getBrandIds()) || param.getBrandIds().size() == 0) {
//            details = this.query().eq("sku_id", param.getSkuId()).eq("display", 1).list();
//
//        } else {
//            details = this.query().in("brand_id", param.getBrandIds()).eq("sku_id", param.getSkuId()).eq("display", 1).list();
//        }
//        List<RestStockDetailsResult> results = details.size() == 0 ? new ArrayList<>() : BeanUtil.copyToList(details, RestStockDetailsResult.class);
//        this.format(results);
//        List<RestStockDetailsResult> totalList = new ArrayList<>();
//        results.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_" + (ToolUtil.isEmpty(item.getBrandId()) ? 0 : item.getBrandId()) + "_" + item.getStorehousePositionsId(), Collectors.toList())).forEach(
//                (id, transfer) -> {
//                    transfer.stream().reduce((a, b) -> {
//                        RestStockDetailsResult stockDetailsResult = BeanUtil.copyProperties(a, RestStockDetailsResult.class);
//                        stockDetailsResult.setNumber(a.getNumber() + b.getNumber());
//                        return stockDetailsResult;
//                    }).ifPresent(totalList::add);
//                }
//        );
//        return totalList;
//    }
//
//    @Override
//    public List<StockSkuBrand> stockSku() {
//        QueryWrapper<StockDetails> queryWrapper = new QueryWrapper<>();
//        queryWrapper.select("*", "sum(number) AS num").groupBy("sku_id");
//        List<StockDetails> details = this.list(queryWrapper);
//        List<StockSkuBrand> stockSkuBrands = new ArrayList<>();
//
//        for (StockDetails detail : details) {
//            StockSkuBrand stockSkuBrand = new StockSkuBrand();
//            stockSkuBrand.setSkuId(detail.getSkuId());
//            stockSkuBrand.setNumber(detail.getNum());
//        }
//        return stockSkuBrands;
//    }
//
//
//    private Serializable getKey(RestStockDetailsParam param) {
//        return param.getStockItemId();
//    }
//
//    private Page<RestStockDetailsResult> getPageContext() {
//        List<String> fields = new ArrayList<String>() {{
//            add("iname");
//            add("price");
//            add("storageTime");
//        }};
//        return PageFactory.defaultPage(fields);
//    }
//
//    private StockDetails getOldEntity(RestStockDetailsParam param) {
//        return this.getById(getKey(param));
//    }
//
//    private StockDetails getEntity(RestStockDetailsParam param) {
//        StockDetails entity = new StockDetails();
//        ToolUtil.copyProperties(param, entity);
//        return entity;
//    }
//
//    @Override
//    public void format(List<RestStockDetailsResult> data) {
//        List<Long> pIds = new ArrayList<>();
//        List<Long> stoIds = new ArrayList<>();
//        List<Long> customerIds = new ArrayList<>();
//        List<Long> brandIds = new ArrayList<>();
//        List<Long> skuIds = new ArrayList<>();
//        List<Long> inkindIds = new ArrayList<>();
//        List<Long> userIds = new ArrayList<>();
//        for (RestStockDetailsResult datum : data) {
//            stoIds.add(datum.getStorehouseId());
//            customerIds.add(datum.getCustomerId());
//            brandIds.add(datum.getBrandId());
//            pIds.add(datum.getStorehousePositionsId());
//            skuIds.add(datum.getSkuId());
//            inkindIds.add(datum.getInkindId());
//            userIds.add(datum.getCreateUser());
//        }
//
//
//        List<Inkind> inkinds = inkindIds.size() == 0 ? new ArrayList<>() : inkindService.listByIds(inkindIds);
//        List<OrCodeBind> codeBinds = inkindIds.size() == 0 ? new ArrayList<>() : codeBindService.query().in("form_id", inkindIds).list();
//        List<MaintenanceLogDetailResult> maintenanceLogDetailResults = maintenanceLogDetailService.lastLogByInkindIds(inkindIds);
//        List<CustomerResult> results = customerService.getResults(customerIds);
//        List<StorehousePositionsResult> positions = positionsService.details(pIds);
//        List<StorehouseResult> storehouseResults = storehouseService.getDetails(stoIds);
//        List<BrandResult> brandList = brandService.getBrandResults(brandIds);
//        List<SkuSimpleResult> skuSimpleResultList = skuService.simpleFormatSkuResult(skuIds);
//        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);
//
//
//        for (RestStockDetailsResult datum : data) {
//            for (Inkind inkind : inkinds) {
//                if (ToolUtil.isNotEmpty(datum.getInkindId()) && inkind.getInkindId().equals(datum.getInkindId())) {
//                    datum.setAnomaly(inkind.getAnomaly());
//                    break;
//                }
//            }
//
//            for (OrCodeBind codeBind : codeBinds) {
//                if (codeBind.getFormId().equals(datum.getInkindId())) {
//                    datum.setQrCodeId(codeBind.getOrCodeId());
//                    break;
//                }
//            }
//
//            for (User user : userList) {
//                if (user.getUserId().equals(datum.getCreateUser())) {
//                    user.setAvatar(stepsService.imgUrl(user.getUserId().toString()));
//                    datum.setUser(user);
//                    break;
//                }
//            }
//
//            for (MaintenanceLogDetailResult maintenanceLogDetailResult : maintenanceLogDetailResults) {
//                if (ToolUtil.isNotEmpty(datum.getInkindId()) && datum.getInkindId().equals(maintenanceLogDetailResult.getInkindId())) {
//                    datum.setMaintenanceLogDetailResult(maintenanceLogDetailResult);
//                }
//            }
//            for (SkuSimpleResult skuSimpleResult : skuSimpleResultList) {
//                if (datum.getSkuId().equals(skuSimpleResult.getSkuId())) {
//                    datum.setSkuResult(skuSimpleResult);
//                    break;
//                }
//            }
//
//            for (CustomerResult result : results) {
//                if (result.getCustomerId().equals(datum.getCustomerId())) {
//                    datum.setCustomerResult(result);
//                    break;
//                }
//            }
//
//            if (ToolUtil.isNotEmpty(positions)) {
//                for (StorehousePositionsResult position : positions) {
//                    if (datum.getStorehousePositionsId() != null && position.getStorehousePositionsId().equals(datum.getStorehousePositionsId())) {
//                        datum.setStorehousePositionsResult(position);
//                        break;
//                    }
//                }
//            }
//
//            for (StorehouseResult storehouseResult : storehouseResults) {
//                if (storehouseResult.getStorehouseId().equals(datum.getStorehouseId())) {
//                    datum.setStorehouseResult(storehouseResult);
//                    break;
//                }
//            }
//
//            if (!brandList.isEmpty()) {
//                for (BrandResult brand : brandList) {
//                    if (ToolUtil.isNotEmpty(datum.getBrandId()) && datum.getBrandId().equals(brand.getBrandId())) {
//                        datum.setBrandResult(brand);
//                        break;
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public List<StockDetailExcel> getStockDetail() {
//        List<StockDetailExcel> stockDetailExcels = this.baseMapper.stockDetailExcelExport();
//        List<Long> skuIds = new ArrayList<>();
//        List<Long> brandIds = new ArrayList<>();
//        List<Long> customerIds = new ArrayList<>();
//        List<Long> storeHousePositionIds = new ArrayList<>();
//
//        /**
//         * 添加id查询
//         */
//        for (StockDetailExcel stockDetailExcel : stockDetailExcels) {
//            if (ToolUtil.isNotEmpty(stockDetailExcel.getSkuId())) {
//                skuIds.add(stockDetailExcel.getSkuId());
//            }
//            if (ToolUtil.isNotEmpty(stockDetailExcel.getBrandId())) {
//                brandIds.add(stockDetailExcel.getBrandId());
//            }
//            if (ToolUtil.isNotEmpty(stockDetailExcel.getCustomerId())) {
//                customerIds.add(stockDetailExcel.getCustomerId());
//            }
//            if (ToolUtil.isNotEmpty(stockDetailExcel.getStorehousePositionsId())) {
//                storeHousePositionIds.add(stockDetailExcel.getStorehousePositionsId());
//            }
//        }
//
//        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
//        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
//        List<CustomerResult> customerResults = customerService.getResults(customerIds);
//        List<StorehousePositionsResult> storehousePositionsResults = positionsService.positionsResults(storeHousePositionIds);
//        for (StockDetailExcel stockDetailExcel : stockDetailExcels) {
//            for (SkuResult skuResult : skuResults) {
//                if (stockDetailExcel.getSkuId().equals(skuResult.getSkuId())) {
//                    stockDetailExcel.setSkuResult(skuResult);
//                    break;
//                }
//            }
//            for (BrandResult brandResult : brandResults) {
//                if (ToolUtil.isNotEmpty(stockDetailExcel.getBrandId()) && stockDetailExcel.getBrandId().equals(brandResult.getBrandId())) {
//                    stockDetailExcel.setBrandResult(brandResult);
//                }
//            }
//            for (CustomerResult customerResult : customerResults) {
//                if (ToolUtil.isNotEmpty(stockDetailExcel.getCustomerId()) && stockDetailExcel.getCustomerId().equals(customerResult.getCustomerId())) {
//                    stockDetailExcel.setCustomerResult(customerResult);
//                }
//            }
//            for (StorehousePositionsResult storehousePositionsResult : storehousePositionsResults) {
//                if (ToolUtil.isNotEmpty(stockDetailExcel.getStorehousePositionsId()) && stockDetailExcel.getStorehousePositionsId().equals(storehousePositionsResult.getStorehousePositionsId())) {
//                    stockDetailExcel.setStorehousePositionsResult(storehousePositionsResult);
//                }
//            }
//        }
//        return stockDetailExcels;
//    }
//
//    @Override
//    public List<StockDetails> maintenanceQuerry(RestStockDetailsParam param) {
//        return this.baseMapper.maintenanceQuerry(param);
//    }
//
//    @Override
//    public StockDetails getInkind(RestStockDetailsParam param) {
////        List<Long> ids = this.baseMapper.getInkindIds(param);
//        List<OrCodeBind> list = orCodeBindService.query().eq("source", "item").likeLeft("substr( qr_code_id, length( qr_code_id )- 5, 6 )", param.getInkind()).eq("display", 1).list();
//        List<Long> inkindIds = new ArrayList<>();
//        for (OrCodeBind bind : list) {
//            inkindIds.add(bind.getFormId());
//        }
//        if (ToolUtil.isNotEmpty(inkindIds)) {
//            QueryWrapper<StockDetails> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("display", 1);
//            queryWrapper.eq("inkind_id", inkindIds.get(0));
//            queryWrapper.eq("sku_id", param.getSkuId());
//            queryWrapper.eq("brand_id", param.getBrandId());
//            queryWrapper.eq("storehouse_positions_id", param.getStorehousePositionsId());
//            StockDetails stockDetails = this.getOne(queryWrapper);
//            if (ToolUtil.isNotEmpty(stockDetails)) {
//                stockDetails.setQrCodeId(list.get(0).getOrCodeId());
//                return stockDetails;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<StockDetails> fundStockDetailByCart(ProductionPickListsCartParam param) {
//        List<StockDetails> list = new ArrayList<>();
//        /**
//         * 获取购物车中实物
//         */
//        List<Long> skuIds = ToolUtil.isEmpty(param.getProductionPickListsCartParams())?new ArrayList<>() : param.getProductionPickListsCartParams().stream().map(ProductionPickListsCartParam::getSkuId).distinct().collect(Collectors.toList());
//        List<Long> cartInkindIds = pickListsCartService.getCartInkindIds(skuIds);
//        List<Long> inkindIds = new ArrayList<>();
//        for (ProductionPickListsCartParam productionPickListsCartParam : param.getProductionPickListsCartParams()) {
//            if (ToolUtil.isNotEmpty(productionPickListsCartParam.getInkindId())) {
//                inkindIds.add(productionPickListsCartParam.getInkindId());
//            }
//        }
//        if (inkindIds.size() > 0) {
//            list.addAll(this.query().in("inkind_id", inkindIds).eq("display", 1).list());
//        }
//        for (ProductionPickListsCartParam cartParam : param.getProductionPickListsCartParams()) {
//            if (ToolUtil.isEmpty(cartParam.getInkindId())) {
//                QueryWrapper<StockDetails> queryWrapper = new QueryWrapper<>();
//                queryWrapper.eq("sku_id", cartParam.getSkuId());
//                if (ToolUtil.isNotEmpty(cartParam.getBrandId()) && !cartParam.getBrandId().equals(0L)) {
//                    queryWrapper.eq("brand_id", cartParam.getBrandId());
//                }
//                queryWrapper.eq("storehouse_positions_id", cartParam.getStorehousePositionsId());
//                queryWrapper.eq("display", 1);
//                queryWrapper.eq("stage", 1);
//
//                list.addAll(this.list(queryWrapper));
//            }
//        }
//        list = list.stream().distinct().collect(Collectors.toList());
//        for (Long inkindId : cartInkindIds) {
//            list.removeIf(i -> i.getInkindId().equals(inkindId));
//        }
//        return list;
//    }
//    @Override
//    public List<StockDetailView> stockDetailViews(){
//        return this.baseMapper.stockDetailView();
//    }

    List<RestStockDetails> resultByTraceability(List<Long> ids);

//    /**
//     * 新增
//     *
//     * @author
//     * @Date 2021-07-15
//     */
//    Long add(StockDetailsParam param);
//
//    /**
//     * 删除
//     *
//     * @author
//     * @Date 2021-07-15
//     */
//    void delete(StockDetailsParam param);
//
//    /**
//     * 更新
//     *
//     * @author
//     * @Date 2021-07-15
//     */
//    void update(StockDetailsParam param);
//
//    /**
//     * 查询单条数据，Specification模式
//     *
//     * @author
//     * @Date 2021-07-15
//     */
//    StockDetailsResult findBySpec(StockDetailsParam param);
//
//    /**
//     * 查询列表，Specification模式
//     *
//     * @author
//     * @Date 2021-07-15
//     */
//    List<StockDetailsResult> findListBySpec(StockDetailsParam param);
//
//    PageInfo<StockDetailsResult> skuDetailView(StockDetailsParam param, DataScope dataScope);
//
//    List<StockDetails> getStock();
//
//    List<StockDetailsResult> getDetailsBySkuId(Long id);
//
//    List<StockDetailsResult> stockInKindList();
//
//    List<SpuClassDetail> detailed();
//
//    List<StockCensus> stockCensus();
//
//    /**
//     * 查询分页数据，Specification模式
//     *
//     * @author
//     * @Date 2021-07-15
//     */
//    PageInfo findPageBySpec(StockDetailsParam param, DataScope dataScope);
//
//    /**
//     * 通过仓库返回所有sku
//     *
//     * @param id
//     * @return
//     */
//    List<Long> backSkuByStoreHouse(Long id);
//
//
//    void statement();
//
//    void splitInKind(Long inKind);
//
//    List<StockDetailsResult> inkindList(Long skuId);
//
//    /**
//     * 返回当前仓库下所有数据
//     *
//     * @param stockId
//     * @return
//     */
//    List<StockDetailsResult> getStockDetails(Long stockId);
//
//    /**
//     * 预购返回库存数量
//     *
//     * @param plans
//     */
//    void preorder(List<ListingPlan> plans);
//
//
//    List<StockSkuBrand> stockSkuBrands();
//
//    Integer getNumberByStock(Long skuId, Long brandId, Long positionId);
//
//    List<StockDetailsResult> getStockNumberBySkuId(Long skuId,Long storehouseId);
//
//    List<StockDetailsResult> getStockNumberBySkuId(StockDetailsParam stockDetailsParam);
//
//    List<StockSkuBrand> stockSku();
//
//    void format(List<StockDetailsResult> data);
//
//    List<StockDetailExcel> getStockDetail();
//
//    List<StockDetails> maintenanceQuerry(StockDetailsParam param);
//
//    StockDetails getInkind(StockDetailsParam param);
//
//    List<StockDetails> fundStockDetailByCart(ProductionPickListsCartParam param);
//
//    List<StockDetailView> stockDetailViews();

}
