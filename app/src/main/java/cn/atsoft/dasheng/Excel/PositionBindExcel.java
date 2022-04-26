package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.Excel.pojo.ExcelPositionResult;
import cn.atsoft.dasheng.Excel.pojo.PositionBind;
import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.InkindParam;
import cn.atsoft.dasheng.erp.service.InkindService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsBindService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.orCode.pojo.BatchAutomatic;
import cn.atsoft.dasheng.orCode.pojo.InkindQrcode;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.sys.modular.system.entity.FileInfo;
import cn.atsoft.dasheng.sys.modular.system.service.FileInfoService;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
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
import java.util.List;

@Controller
@RequestMapping("/Excel")
@Slf4j
public class PositionBindExcel {
    @Autowired
    private StorehousePositionsBindService bindService;
    @Autowired
    private StorehousePositionsService positionsService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private StockDetailsService detailsService;
    @Autowired
    private InkindService inkindService;
    @Autowired
    private OrCodeService codeService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private FileInfoService fileInfoService;
    protected static final Logger logger = LoggerFactory.getLogger(SkuExcelController.class);

    @RequestMapping("/importPositionBind")
    @ResponseBody
    public ResponseData uploadExcel(@RequestParam("fileId") Long fileId) {

        FileInfo fileInfo = fileInfoService.getById(fileId);
        File excelFile = new File(fileInfo.getFilePath());

        ExcelReader reader = ExcelUtil.getReader(excelFile);
        reader.addHeaderAlias("分类", "spuClass");
        reader.addHeaderAlias("物料编码", "strand");
        reader.addHeaderAlias("产品", "item");
        reader.addHeaderAlias("型号", "spuName");
        reader.addHeaderAlias("库位", "position");
        reader.addHeaderAlias("库存余额", "stockNumber");
        reader.addHeaderAlias("上级库位", "supperPosition");
        reader.addHeaderAlias("品牌", "brand");

        List<String> strands = new ArrayList<>();

        List<PositionBind> excels = reader.readAll(PositionBind.class);
        List<PositionBind> errorList = new ArrayList<>();
        List<PositionBind> successList = new ArrayList<>();
        for (PositionBind excel : excels) {
            strands.add(excel.getStrand());
        }
        List<Sku> skuList = skuService.query().in("standard", strands).list();
        List<StorehousePositionsBind> positionsBinds = bindService.query().eq("display", 1).list();
        List<StorehousePositions> positions = positionsService.query().eq("display", 1).list();
        List<Brand> brands = brandService.list();
        List<StockDetails> stockDetailsList = new ArrayList<>();

        int i = 0;

        for (PositionBind excel : excels) {
            i++;
            try {
                excel.setLine(i);
                //物料---------------------------------------------------------
                for (Sku sku : skuList) {
                    if (sku.getStandard().equals(excel.getStrand())) {
                        excel.setSkuId(sku.getSkuId());
                        break;
                    }
                }
                if (ToolUtil.isEmpty(excel.getSkuId())) {
                    throw new ServiceException(500, "当前物料不存在");
                }
                //  品牌-------------------------------------------------------------
                if (ToolUtil.isEmpty(excel.getBrand())) {
                    throw new ServiceException(500, "缺少品牌");
                }
                Brand brand = null;
                brand = judgeBrand(excel.getBrand(), brands);
                if (ToolUtil.isEmpty(brand)) {
                    brand = new Brand();
                    brand.setBrandName(excel.getBrand());
                }
                //上级库位-------------------------------------------------------------
                StorehousePositions supper = getPosition(excel.getSupperPosition(), positions);
                if (ToolUtil.isEmpty(supper)) {
                    throw new ServiceException(500, "没有上级库位");
                }
                if (ToolUtil.isEmpty(excel.getPosition())) {
                    excel.setPositionId(supper.getStorehousePositionsId());
                } else {
                    StorehousePositions position = getPosition(excel.getPosition(), positions);    //库位
                    if (ToolUtil.isEmpty(position)) {
                        StorehousePositions end = new StorehousePositions();
                        end.setName(excel.getPosition());
                        end.setPid(supper.getStorehousePositionsId());
                        end.setStorehouseId(supper.getStorehouseId());
                        positionsService.save(end);
                        positions.add(end);
                        excel.setPositionId(end.getStorehousePositionsId());
                    } else {
                        excel.setPositionId(position.getStorehousePositionsId());
                    }
                }

                StorehousePositionsBind positionBind = judge(excel, positionsBinds);
                if (ToolUtil.isEmpty(positionBind)) {
                    positionBind = new StorehousePositionsBind();
                    positionBind.setSkuId(excel.getSkuId());
                    positionBind.setPositionId(excel.getPositionId());
                    bindService.save(positionBind);
                    positionsBinds.add(positionBind);
                }

                if (ToolUtil.isNotEmpty(excel.getStockNumber()) && excel.getStockNumber() > 0) {
                    //库存
                    InkindQrcode inkindQrcode = codeService.ExcelBind(excel.getSkuId(), Long.valueOf(excel.getStockNumber()), brand.getBrandId());
                    StockDetails stockDetails = new StockDetails();
                    stockDetails.setSkuId(excel.getSkuId());
                    stockDetails.setNumber(Long.valueOf(excel.getStockNumber()));
                    stockDetails.setBrandId(brand.getBrandId());
                    stockDetails.setStorehouseId(supper.getStorehouseId());
                    stockDetails.setStorehousePositionsId(excel.getPositionId());
                    stockDetails.setInkindId(inkindQrcode.getInkindId());
                    stockDetails.setQrCodeId(inkindQrcode.getQrCodeId());
                    stockDetailsList.add(stockDetails);
                }
                successList.add(excel);
            } catch (Exception e) {
                logger.error(excel.getLine() + "------->" + e);
                excel.setError(e.getMessage());
                errorList.add(excel);
            }
        }

        detailsService.saveBatch(stockDetailsList);


        return ResponseData.success(new ExcelPositionResult() {{
            setErrorList(errorList);
            setSuccessList(successList);
        }});
    }

    /**
     * 比对库位
     */
    private StorehousePositions getPosition(String name, List<StorehousePositions> positions) {
        if (ToolUtil.isEmpty(name)) {
            return null;
        }

        for (StorehousePositions position : positions) {
            if (position.getName().equals(name)) {
                return position;
            }
        }
        return null;
    }

    /**
     * 判断是否重复绑定
     *
     * @param excel
     * @param positionsBinds
     * @return
     */
    private StorehousePositionsBind judge(PositionBind excel, List<StorehousePositionsBind> positionsBinds) {

        for (StorehousePositionsBind positionsBind : positionsBinds) {
            if (positionsBind.getSkuId().equals(excel.getSkuId()) && positionsBind.getPositionId().equals(excel.getPositionId())) {
                return positionsBind;
            }
        }

        return null;
    }

    /**
     * 对比品牌
     *
     * @param name
     * @param brands
     * @return
     */
    private Brand judgeBrand(String name, List<Brand> brands) {

        for (Brand brand : brands) {
            if (brand.getBrandName().equals(name)) {
                return brand;
            }
        }
        return null;
    }
}
