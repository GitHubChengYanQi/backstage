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
    @Autowired
    private ExcelAsync excelAsync;

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



        List<PositionBind> excels = reader.readAll(PositionBind.class);
        /**
         * 调用异步
         */
        excelAsync.positionAdd(excels);

        return ResponseData.success("ok");
    }


}
