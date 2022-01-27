package cn.atsoft.dasheng.Excel;


import cn.atsoft.dasheng.Excel.pojo.ExcelInkind;
import cn.atsoft.dasheng.Excel.pojo.InstockExcel;
import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.Supply;
import cn.atsoft.dasheng.crm.service.SupplyService;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.InkindParam;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.params.OrCodeBindParam;
import cn.atsoft.dasheng.orCode.model.result.BackCodeRequest;
import cn.atsoft.dasheng.orCode.pojo.AutomaticBindResult;
import cn.atsoft.dasheng.orCode.pojo.BatchAutomatic;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.hutool.core.map.BiMap;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Excel")
@Slf4j
public class InstockExcelController {
    @Autowired
    private SkuService skuService;
    @Autowired
    private OrCodeService orCodeService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private StorehousePositionsService positionsService;
    @Autowired
    private InkindService inkindService;
    @Autowired
    private OrCodeBindService codeBindService;
    @Autowired
    private InstockOrderService orderService;
    @Autowired
    private StockService stockService;
    @Autowired
    private StorehousePositionsBindService positionsBindService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private SupplyService supplyService;

    protected static final Logger logger = LoggerFactory.getLogger(InstockExcelController.class);

    /**
     * 上传excel填报
     */
    @RequestMapping("/importInstock")
    @ResponseBody
    public ResponseData uploadExcel(@RequestParam("file") MultipartFile file) {

        String name = file.getOriginalFilename();
        String fileSavePath = ConstantsContext.getFileUploadPath();
        File excelFile = new File(fileSavePath + name);
        try {
            file.transferTo(excelFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ExcelReader reader = ExcelUtil.getReader(excelFile);
        reader.addHeaderAlias("成品码", "standard");
        reader.addHeaderAlias("仓库", "house");
        reader.addHeaderAlias("库位", "position");
        reader.addHeaderAlias("数量", "number");
        reader.addHeaderAlias("品牌", "brand");
        reader.addHeaderAlias("供应商", "supplier");

        List<InstockExcel> instockExcels = reader.readAll(InstockExcel.class);

        List<String> standards = new ArrayList<>();
        List<String> brands = new ArrayList<>();
        List<String> suppliers = new ArrayList<>();
        List<String> houses = new ArrayList<>();
        List<String> positions = new ArrayList<>();
        List<InstockExcel> errorList = new ArrayList<>();


        for (InstockExcel instockExcel : instockExcels) {
            standards.add(instockExcel.getStandard());
            brands.add(instockExcel.getBrand());
            suppliers.add(instockExcel.getSupplier());
            houses.add(instockExcel.getHouse());
            positions.add(instockExcel.getPosition());
        }
        List<Sku> skus = standards.size() == 0 ? new ArrayList<>() : skuService.query().in("standard", standards).eq("display", 1).list();
        List<Brand> brandList = brands.size() == 0 ? new ArrayList<>() : brandService.query().in("brand_name", brands).eq("display", 1).list();
        List<Customer> customers = suppliers.size() == 0 ? new ArrayList<>() : customerService.query().in("customer_name", suppliers).eq("display", 1).list();
        List<Storehouse> storehouses = houses.size() == 0 ? new ArrayList<>() : storehouseService.query().in("name", houses).eq("display", 1).list();
        List<StorehousePositions> positionsList = positions.size() == 0 ? new ArrayList<>() : positionsService.query().in("name", positions).eq("display", 1).list();

        for (InstockExcel instockExcel : instockExcels) {
            BackCodeRequest backCodeRequest = new BackCodeRequest();
            backCodeRequest.setNumber(instockExcel.getNumber());
            backCodeRequest.setSource("ExcelInstock");
            try {
                for (Sku sku : skus) {
                    if (sku.getStandard().equals(instockExcel.getStandard())) {
                        instockExcel.setSkuId(sku.getSkuId());
                        backCodeRequest.setId(sku.getSkuId());
                        break;
                    }
                }

                for (Brand brand : brandList) {
                    if (brand.getBrandName().equals(instockExcel.getBrand())) {
                        instockExcel.setBrandId(brand.getBrandId());
                        backCodeRequest.setBrandId(brand.getBrandId());
                        break;
                    }
                }

                for (Customer customer : customers) {
                    if (customer.getCustomerName().equals(instockExcel.getSupplier())) {
                        instockExcel.setSupplierId(customer.getCustomerId());
                        backCodeRequest.setCustomerId(customer.getCustomerId());
                        break;
                    }
                }

                for (Storehouse storehouse : storehouses) {
                    if (storehouse.getName().equals(instockExcel.getHouse())) {
                        instockExcel.setHouseId(storehouse.getStorehouseId());
                        break;
                    }
                }
                for (StorehousePositions storehousePositions : positionsList) {
                    if (storehousePositions.getName().equals(instockExcel.getPosition())) {
                        instockExcel.setPositionId(storehousePositions.getStorehousePositionsId());
                        break;
                    }
                }
            } catch (Exception e) {
                throw new ServiceException(500, "缺少参数");
            }


        }  //添加物料信息

        for (InstockExcel instockExcel : instockExcels) {
            if (ToolUtil.isEmpty(instockExcel.getSkuId())
                    || ToolUtil.isEmpty(instockExcel.getBrandId())
                    || ToolUtil.isEmpty(instockExcel.getSupplierId())
                    || ToolUtil.isEmpty(instockExcel.getNumber())
                    || ToolUtil.isEmpty(instockExcel.getPositionId())
                    || ToolUtil.isEmpty(instockExcel.getHouseId())
            ) {
                errorList.add(instockExcel);
                instockExcels.remove(instockExcel);
                break;
            }
            ExcelInkind excelInkind = createInkind(instockExcel.getSkuId(), instockExcel.getBrandId(), instockExcel.getSupplierId(), instockExcel.getNumber());

            instockExcel.setInkind(excelInkind.getInkind());
            instockExcel.setQrCodeId(excelInkind.getQrCodeId());


        }//创建实物

        List<InstockExcel> instock = instock(instockExcels);
        errorList.addAll(instock);
        return ResponseData.success(errorList);
    }

    /**
     * 绑定实物
     *
     * @param skuId
     * @param brandId
     * @param supplierId
     * @param number
     * @return
     */
    private ExcelInkind createInkind(Long skuId, Long brandId, Long supplierId, Long number) {
        OrCode orCode = new OrCode();
        orCode.setType("excel入库导入");
        orCode.setState(1);
        orCodeService.save(orCode);

        Inkind inkind = new Inkind();
        inkind.setSkuId(skuId);
        inkind.setType("0");
        inkind.setNumber(number);
        inkind.setBrandId(brandId);
        inkind.setSource("excel入库导入");
        inkind.setCustomerId(supplierId);
        inkindService.save(inkind);

        OrCodeBind orCodeBind = new OrCodeBind();
        orCodeBind.setOrCodeId(orCode.getOrCodeId());
        orCodeBind.setFormId(inkind.getInkindId());
        orCodeBind.setSource("excel入库导入");
        codeBindService.save(orCodeBind);

        return new ExcelInkind(orCode.getOrCodeId(), inkind);
    }

    /**
     * 实际入库操作
     *
     * @param instockExcels
     * @return
     */
    private List<InstockExcel> instock(List<InstockExcel> instockExcels) {
        List<InstockExcel> errorList = new ArrayList<>();
        List<Long> stockIds = new ArrayList<>();
        List<StockDetails> stockDetailsList = new ArrayList<>();
        List<Inkind> inkinds = new ArrayList<>();
        List<Stock> stocks = stockService.list();
        List<StorehousePositionsBind> positionsBinds = positionsBindService.list();
        List<Supply> supplies = supplyService.list();
        int i = 0;
        for (InstockExcel instockExcel : instockExcels) {
            i++;
            Inkind inkind = instockExcel.getInkind();
            Stock stock = orderService.judgeStockExist(inkind, stocks);
            Boolean position = orderService.judgePosition(positionsBinds, inkind);
            try {
                if (orderService.judgeSkuBind(inkind, supplies)) {
                    throw new ServiceException(500, "物料未有绑定");
                }
                if (position) {
                    throw new ServiceException(500, "未绑定库位");
                }
                Long stockId = null;
                if (ToolUtil.isNotEmpty(stock)) {
                    stockId = stock.getStockId();
                } else {  //没有相同库存
                    Stock newStock = new Stock();
                    newStock.setInventory(inkind.getNumber());
                    newStock.setBrandId(inkind.getBrandId());
                    newStock.setSkuId(inkind.getSkuId());
                    newStock.setStorehouseId(instockExcel.getHouseId());
                    newStock.setCustomerId(inkind.getCustomerId());
                    stockService.save(newStock);
                    stocks.add(newStock);
                    stockId = newStock.getStockId();
                }
                StockDetails stockDetails = new StockDetails();
                stockDetails.setStockId(stockId);
                stockDetails.setNumber(inkind.getNumber());
                stockDetails.setStorehousePositionsId(instockExcel.getPositionId());
                stockDetails.setQrCodeId(instockExcel.getQrCodeId());
                stockDetails.setInkindId(inkind.getInkindId());
                stockDetails.setStorehouseId(instockExcel.getHouseId());
                stockDetails.setCustomerId(inkind.getCustomerId());
                stockDetails.setBrandId(inkind.getBrandId());
                stockDetails.setSkuId(inkind.getSkuId());
                stockDetailsList.add(stockDetails);
                inkind.setType("1");
                inkinds.add(inkind);
                stockIds.add(stockId);

            } catch (Exception e) {
                errorList.add(instockExcel);
                logger.error("写入异常:" + "第" + i + "行" + instockExcel + "错误" + e);
            }
        }

        stockDetailsService.saveBatch(stockDetailsList);
        inkindService.updateBatchById(inkinds);
        stockService.updateNumber(stockIds);
        return errorList;
    }


}
