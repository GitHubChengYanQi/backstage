package cn.atsoft.dasheng.Excel;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.atsoft.dasheng.Excel.pojo.SkuExcelItem;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.service.UnitService;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.excel.CustomerExcelItem;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.Spu;
import cn.atsoft.dasheng.erp.entity.SpuClassification;
import cn.atsoft.dasheng.erp.service.SpuClassificationService;
import cn.atsoft.dasheng.erp.service.SpuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Excel")
@Slf4j
public class SkuExcelController {

    @Autowired
    private SpuClassificationService classService;
    @Autowired
    private SpuService spuService;
    @Autowired
    private UnitService unitService;

    /**
     * 上传excel填报
     */
    @RequestMapping("/importSku")
    @ResponseBody
    public ResponseData uploadExcel(@RequestParam("file") MultipartFile file) {
        String name = file.getOriginalFilename();
        String fileSavePath = ConstantsContext.getFileUploadPath();
        try {
            File excelFile = new File(fileSavePath + name);
            file.transferTo(excelFile);
            try {
                ExcelReader reader = ExcelUtil.getReader(excelFile);
                List<SkuExcelItem> skuExcelItems = reader.readAll(SkuExcelItem.class);
                List<List<Object>> read = reader.read(1);
                for (int i = 0; i < read.size(); i++) {
                    List<Object> objects = read.get(i);
                    SkuExcelItem skuExcelItem = skuExcelItems.get(i);
                    List<String> attributes = new ArrayList<>();
                    for (int j = 6; j < objects.size() - 1; j++) {
                        Object o = objects.get(j);
                        if (ToolUtil.isNotEmpty(o)) {
                            attributes.add(o.toString());
                        }
                    }
                    skuExcelItem.setAttributes(attributes);
                }
//-------------------------------------------------------------------------------------------
                List<Sku> skus = new ArrayList<>();
                Long spuId = null;
                for (SkuExcelItem skuExcelItem : skuExcelItems) {
                    Sku sku = new Sku();
                    //编码
                    if (ToolUtil.isEmpty(skuExcelItem.get编码())) {
                        throw new ServiceException(500, "请保证编码完整");
                    }
                    sku.setCoding(skuExcelItem.get编码());
                    //物料名称
                    if (ToolUtil.isEmpty(skuExcelItem.get物料名称())) {
                        throw new ServiceException(500, "请保证物料名称完整");
                    }
                    Spu spu = spuService.query().eq("name", skuExcelItem.get物料名称()).one();
                    if (ToolUtil.isNotEmpty(spu)) {
                        sku.setSpuId(spu.getSpuId());
                        spuId = spu.getSpuId();
                    } else {
                        Spu newSpu = new Spu();
                        newSpu.setName(skuExcelItem.get物料名称());
                        spuService.save(newSpu);
                        spuId = newSpu.getSpuId();
                        sku.setSpuId(newSpu.getSpuId());
                    }
                    //单位
                    if (ToolUtil.isEmpty(skuExcelItem.get单位())) {
                        throw new ServiceException(500, "请保证单位完整");
                    }
                    Unit unit = unitService.query().eq("unit_name", skuExcelItem.get单位()).one();
                    Spu spuById = spuService.getById(spuId);
                    if (ToolUtil.isNotEmpty(unit)) {
                        spuById.setUnitId(unit.getUnitId());
                    } else {
                        Unit newUnit = new Unit();
                        newUnit.setUnitName(skuExcelItem.get单位());
                        unitService.save(unit);
                        spuById.setUnitId(newUnit.getUnitId());
                    }
                    spuService.updateById(spuById);
                    //分类
                    if (ToolUtil.isEmpty(skuExcelItem.get分类())) {
                        throw new ServiceException(500, "请保证分类完整");
                    }
                }

                return ResponseData.success();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            log.error("上传那文件出错！", e);
            throw new ServiceException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return null;
    }
}
