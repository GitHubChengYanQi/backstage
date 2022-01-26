package cn.atsoft.dasheng.Excel;


import cn.atsoft.dasheng.Excel.pojo.InstockExcel;
import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.orCode.model.result.BackCodeRequest;
import cn.atsoft.dasheng.orCode.pojo.AutomaticBindResult;
import cn.atsoft.dasheng.orCode.pojo.BatchAutomatic;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        List<Storehouse> storehouses = houses.size() == 0 ? new ArrayList<>() : storehouseService.query().eq("name", houses).eq("display", 1).list();
        List<StorehousePositions> positionsList = positions.size() == 0 ? new ArrayList<>() : positionsService.query().eq("name", positions).eq("display", 1).list();


        List<BackCodeRequest> backCodeRequests = new ArrayList<>();
        for (InstockExcel instockExcel : instockExcels) {

            BackCodeRequest backCodeRequest = new BackCodeRequest();
            backCodeRequest.setNumber(instockExcel.getNumber());
            backCodeRequest.setSource("ExcelInstock");
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
            backCodeRequests.add(backCodeRequest);
        }

        BatchAutomatic batchAutomatic = new BatchAutomatic();
        batchAutomatic.setCodeRequests(backCodeRequests);

        List<AutomaticBindResult> bindResults = orCodeService.batchAutomaticBinding(batchAutomatic);


        return ResponseData.success();
    }
}
