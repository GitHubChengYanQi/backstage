package cn.atsoft.dasheng.message.service;

import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.crm.entity.Order;
import cn.atsoft.dasheng.crm.entity.OrderDetail;
import cn.atsoft.dasheng.crm.entity.Supply;
import cn.atsoft.dasheng.crm.model.params.OrderDetailParam;
import cn.atsoft.dasheng.crm.model.params.OrderParam;
import cn.atsoft.dasheng.crm.model.params.PaymentParam;
import cn.atsoft.dasheng.crm.service.OrderService;
import cn.atsoft.dasheng.crm.service.SupplyService;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.SkuParam;
import cn.atsoft.dasheng.erp.model.params.SpuParam;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.sys.modular.system.entity.Tenant;
import cn.atsoft.dasheng.sys.modular.system.service.TenantService;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
public class InitTenantService {

    @Autowired
    private TenantService tenantService;
    @Autowired
    private UnitService unitService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private SpuClassificationService spuClassificationService;


    @Autowired
    private SpuService spuService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private StorehouseService storehouseService;

    @Autowired
    private SkuBrandBindService brandBindService;

    @Autowired
    private StorehousePositionsService storehousePositionsService;

    @Autowired
    private InkindService inkindService;

    @Autowired
    private StockDetailsService stockDetailsService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SupplyService supplyService;

    @Autowired
    private OrderService orderService;


    @Autowired
    private InstockOrderService instockOrderService;

    @Autowired
    private InstockListService instockListService;

//    @Autowired


    public void initTenant(Long tenantId) {
        System.out.println("init tenant data for " + tenantId);
        //初始化租户 创建一些基础信息
        Tenant tenant = tenantService.getById(tenantId);

        //初始化物料
        Long skuId = initSku(tenantId);
        //初始化仓库
        Long storehousePositionId = initStorehouse(tenantId);
        Brand brand = new Brand() {{
            setBrandName("默认品牌");
            setTenantId(tenantId);
        }};
        //初始化品牌
        brandService.save(brand);
        SkuBrandBind skuBrandBind = new SkuBrandBind() {{
            setTenantId(tenantId);
            setBrandId(brand.getBrandId());
            setCreateUser(tenant.getCreateUser());
            setSkuId(skuId);
        }};
        brandBindService.save(skuBrandBind);
        Customer customer = new Customer();
        customer.setCustomerName("默认供应商");
        customer.setTenantId(tenantId);
        customer.setCreateUser(tenant.getCreateUser());
        customerService.save(customer);

        supplyService.save(new Supply() {{
            setTenantId(tenantId);
            setCustomerId(customer.getCustomerId());
            setBrandId(brand.getBrandId());
            setSkuBrandBind(skuBrandBind.getSkuBrandBind());
            setCreateUser(tenant.getCreateUser());
            setSkuId(skuId);
        }});
        //初始化自身企业
        Customer selfCustomer = new Customer();
        selfCustomer.setTenantId(tenantId);
        selfCustomer.setCustomerName(tenant.getName());
        selfCustomer.setStatus(99);
        selfCustomer.setCreateUser(tenant.getCreateUser());
        selfCustomer.setUserId(tenant.getCreateUser());
        customerService.save(selfCustomer);

        //初始化采购单
        OrderParam order = new OrderParam();
        order.setType(1);
        order.setCoding("defaultCreate_"+new Date().getTime());
        order.setCurrency("人民币");
        order.setDeliveryDate(new Date());
        order.setCreateUser(tenant.getCreateUser());
        order.setDetailParams(new ArrayList<OrderDetailParam>(){{
            add(new OrderDetailParam(){{
                setSkuId(skuId);
                setBrandId(brand.getBrandId());
                setOnePrice(10000.00);
                setTotalPrice(1000000.00);
                setPurchaseNumber(100L);
                setCreateUser(tenant.getCreateUser());
                setCustomerId(customer.getCustomerId());
            }});
        }});
        order.setRemark("初始化采购单");
        order.setPaymentParam(new PaymentParam(){{
            setMoney(1000000.0);
            setTotalAmount(1000000.0);
            setFreight(1);
            setFloatingAmount(0.0);
        }});
        order.setSellerId(selfCustomer.getCustomerId());
        order.setTheme("初始化采购单");
        order.setUserId(tenant.getCreateUser());
        order.setTenantId(tenantId);
        Order orderResult = orderService.add(order);

        //入库单
        InstockOrder instockOrder = new InstockOrder();
        instockOrder.setCoding("defaultCreate_"+new Date().getTime());
        instockOrder.setCustomerId(customer.getCustomerId());
        instockOrder.setStatus(99L);
        instockOrder.setTheme("初始化入库单");
        instockOrder.setTenantId(tenantId);
        instockOrder.setUserId(tenant.getCreateUser());

        instockOrder.setSource("order");
        instockOrder.setSourceId(orderResult.getOrderId());
        instockOrderService.save(instockOrder);

        //入库单详情
        InstockList instockList = new InstockList();
        instockList.setInstockOrderId(instockOrder.getInstockOrderId());
        instockList.setSkuId(skuId);
        instockList.setBrandId(brand.getBrandId());
        instockList.setNumber(100L);
        instockList.setTenantId(tenantId);
        instockList.setCustomerId(customer.getCustomerId());
        instockList.setStatus(99L);
        instockListService.save(instockList);


        //初始化库存
        Inkind inkind = new Inkind() {{
            setSkuId(skuId);
            setNumber(100L);
            setTenantId(tenantId);
            setBrandId(brand.getBrandId());
            setCustomerId(customer.getCustomerId());
        }};
        inkindService.save(inkind);
        StorehousePositions storehousePositions = storehousePositionsService.getById(storehousePositionId);
        stockDetailsService.save(new StockDetails() {{
            setSkuId(skuId);
            setNumber(100L);
            setTenantId(tenantId);
            setBrandId(brand.getBrandId());
            setCustomerId(customer.getCustomerId());
            setStorehouseId(storehousePositions.getStorehouseId());
            setStorehousePositionsId(storehousePositionId);
            setInkindId(inkind.getInkindId());
        }});
    }

    /**
     * 初始化sku
     *
     * @param tenantId
     * @return
     */
    private Long initSku(Long tenantId) {
        Tenant tenant = tenantService.getById(tenantId);
        //初始化单位
        Unit unit = new Unit();
        unit.setUnitName("个");
        unit.setTenantId(tenantId);
        unitService.save(unit);

        //初始化分类
        Category category = new Category();
        category.setCategoryName("默认分类");
        category.setTenantId(tenantId);
        categoryService.save(category);

        //初始化材质
        Material material = new Material();
        material.setName("默认材质");
        material.setTenantId(tenantId);
        materialService.save(material);

        //初始化规格
        SpuClassification spuClassification = new SpuClassification();
        spuClassification.setName("默认规格");
        spuClassification.setTenantId(tenantId);
        spuClassificationService.save(spuClassification);

        //初始化产品
        Spu spu = new Spu();
        spu.setName("默认产品");
        spu.setTenantId(tenantId);
        spu.setCategoryId(category.getCategoryId());
        spu.setSpuClassificationId(spuClassification.getSpuClassificationId());
        spuService.save(spu);

        //初始化sku
        SkuParam sku = new SkuParam();
        sku.setUnitId(unit.getUnitId());
        sku.setSpuId(spu.getSpuId());
        sku.setMaterialId(material.getMaterialId().toString());
        sku.setBatch(1);
        sku.setSpu(BeanUtil.copyProperties(spu, SpuParam.class));
        sku.setType(0);
        sku.setTenantId(tenantId);
        sku.setSpuClass(spuClassification.getSpuClassificationId());
        Map<String, Sku> add = skuService.add(sku);
        return add.get("success").getSkuId();
    }

    /**
     * 初始化仓库
     */
    public Long initStorehouse(Long tenantId) {
        //创建仓库
        Storehouse storehouse = new Storehouse();
        storehouse.setName("默认仓库");
        storehouse.setTenantId(tenantId);
        storehouse.setCoding("default");
        storehouseService.save(storehouse);
        //创建库位
        StorehousePositions storehousePositions = new StorehousePositions();
        storehousePositions.setName("默认库位");
        storehousePositions.setTenantId(tenantId);
        storehousePositions.setStorehouseId(storehouse.getStorehouseId());
        storehousePositionsService.save(storehousePositions);
        return storehousePositions.getStorehousePositionsId();
    }
}
