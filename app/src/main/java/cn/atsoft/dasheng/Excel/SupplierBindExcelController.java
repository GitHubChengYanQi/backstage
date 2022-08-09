package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.Excel.pojo.SupplierBind;
import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.Supply;
import cn.atsoft.dasheng.crm.service.SupplyService;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.FileInfo;
import cn.atsoft.dasheng.sys.modular.system.service.FileInfoService;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/Excel")
public class SupplierBindExcelController {
    @Autowired
    private SkuService skuService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private SupplyService supplyService;
    @Autowired
    private FileInfoService fileInfoService;

    @RequestMapping("/importSupplierBind")
    @ResponseBody
    public ResponseData importSupplierBind(@RequestParam("fileId") Long fileId) throws Exception {

        FileInfo fileInfo = fileInfoService.getById(fileId);
        File excelFile = new File(fileInfo.getFilePath());

        ExcelReader reader = ExcelUtil.getReader(excelFile);
        reader.addHeaderAlias("物料编码", "coding");
        reader.addHeaderAlias("厂家", "supplier");
        reader.addHeaderAlias("品牌", "brand");

        List<Sku> skuList = skuService.query().eq("display", 1).list();
        List<Customer> supply = customerService.query().eq("display", 1).eq("supply", 1).list();
        List<Brand> brandList = brandService.query().eq("display", 1).list();
        List<Supply> supplyList = supplyService.query().eq("display", 1).list();
        List<SupplierBind> supplierBinds = reader.readAll(SupplierBind.class);

        List<SupplierBind> errorList = new ArrayList<>();
        int i = 0;
        for (SupplierBind supplierBind : supplierBinds) {
            i++;
            try {
                Long skuId = null;
                for (Sku sku : skuList) {
                    if (supplierBind.getCoding().equals(sku.getStandard())) {
                        skuId = sku.getSkuId();
                        break;
                    }
                }
                if (ToolUtil.isEmpty(skuId)) {
                    throw new ServiceException(500, "物料不存在");
                }
                //--------------------------------------品牌-------------------------------------
                Long brandId = 0L;
                if (ToolUtil.isNotEmpty(supplierBind.getBrand())) {
                    for (Brand brand : brandList) {
                        if (brand.getBrandName().equals(supplierBind.getBrand())) {
                            brandId = brand.getBrandId();
                            break;
                        }
                    }
                    if (ToolUtil.isEmpty(brandId)) {
                        Brand brand = new Brand();
                        brand.setBrandName(supplierBind.getBrand());
                        brandService.save(brand);
                        brandId = brand.getBrandId();
                        brandList.add(brand);
                    }
                }

                //---------------------------------供应商-------------------------------------
                Long customerId = null;
                for (Customer customer : supply) {
                    if (customer.getCustomerName().equals(supplierBind.getSupplier())) {
                        customerId = customer.getCustomerId();
                        break;
                    }
                }
                if (ToolUtil.isEmpty(customerId)) {
                    Customer customer = new Customer();
                    customer.setSupply(1);
                    customer.setCustomerName(supplierBind.getSupplier());
                    customerService.save(customer);
                    supply.add(customer);
                    customerId = customer.getCustomerId();
                }
                //----------------------绑定----------------------
                boolean t = true;
                for (Supply supply1 : supplyList) {
                    if (supply1.getCustomerId().equals(customerId) &&
                            supply1.getSkuId().equals(skuId) &&
                            supply1.getBrandId().equals(brandId)) {
                        t = false;
                        break;
                    }
                }
                if (t) {
                    Supply newSupply = new Supply();
                    newSupply.setSkuId(skuId);
                    newSupply.setBrandId(brandId);
                    newSupply.setCustomerId(customerId);
                    supplyService.save(newSupply);
                    supplyList.add(newSupply);
                }
            } catch (Exception e) {
                supplierBind.setLine(i);
                supplierBind.setError(e.getMessage());
                errorList.add(supplierBind);
            }

        }

        return ResponseData.success(errorList);
    }

}
