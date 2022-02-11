package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;

import java.util.*;

@Controller
@RequestMapping("/Excel")
@Slf4j
public class PicturController {
    @RequestMapping("/importPictur")
    @ResponseBody
    public ResponseData uploadExcel(@RequestParam("file") MultipartFile file) throws IOException, InvalidFormatException {



        String name = file.getOriginalFilename();
        String fileSavePath = ConstantsContext.getFileUploadPath();
        File excelFile = new File(fileSavePath + name);
        //创建流
        FileInputStream inputStream = new FileInputStream(excelFile.getPath());
        //创建workbook
        Workbook wb = null;
        //创建sheet
        Sheet sheet = null;
        //根据后缀判断excel
        if (excelFile.getPath().endsWith("xls")) {
            wb = new HSSFWorkbook(inputStream);
        } else {
            wb = new XSSFWorkbook(inputStream);
        }

        // sheet list
        List<Map<String, PictureData>> sheetList = new ArrayList<Map<String, PictureData>>();

        sheet = wb.getSheetAt(0);

        Map<String, XSSFPictureData> pictures = getPictures((XSSFSheet) sheet);
        // map等待存储excel图片
        Map<String, PictureData> sheetIndexPicMap;

        // 判断用07还是03的方法获取图片
        if (excelFile.getPath().endsWith("xls")) {
            sheetIndexPicMap = getSheetPictrues03(0, (HSSFSheet) sheet, (HSSFWorkbook) wb);
        } else {
//            sheetIndexPicMap = getSheetPictrues07(0, (XSSFSheet) sheet, (XSSFWorkbook) wb);
            sheetIndexPicMap = getPictures2((XSSFSheet) sheet);

        }
        // 将当前sheet图片map存入list
        sheetList.add(sheetIndexPicMap);


        printImg(sheetList);
        return ResponseData.success();
    }

    /**
     * 获取Excel2003图片
     *
     * @param sheetNum 当前sheet编号
     * @param sheet    当前sheet对象
     * @param workbook 工作簿对象
     * @return Map key:图片单元格索引（0_1_1）String，value:图片流PictureData
     * @throws IOException
     */
    public static Map<String, PictureData> getSheetPictrues03(int sheetNum,
                                                              HSSFSheet sheet, HSSFWorkbook workbook) {

        Map<String, PictureData> sheetIndexPicMap = new HashMap<String, PictureData>();
        List<HSSFPictureData> pictures = workbook.getAllPictures();
        if (pictures.size() != 0) {
            for (HSSFShape shape : sheet.getDrawingPatriarch().getChildren()) {
                HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();
                if (shape instanceof HSSFPicture) {
                    HSSFPicture pic = (HSSFPicture) shape;
                    int pictureIndex = pic.getPictureIndex() - 1;
                    HSSFPictureData picData = pictures.get(pictureIndex);
                    String picIndex = String.valueOf(sheetNum) + "_"
                            + String.valueOf(anchor.getRow1()) + "_"
                            + String.valueOf(anchor.getCol1());
                    sheetIndexPicMap.put(picIndex, picData);
                }
            }
            return sheetIndexPicMap;
        } else {
            return null;
        }
    }

    /**
     * 获取Excel2007图片
     *
     * @param sheetNum 当前sheet编号
     * @param sheet    当前sheet对象
     * @param workbook 工作簿对象
     * @return Map key:图片单元格索引（0_1_1）String，value:图片流PictureData
     */
    public static Map<String, PictureData> getSheetPictrues07(int sheetNum,
                                                              XSSFSheet sheet, XSSFWorkbook workbook) {
        Map<String, PictureData> sheetIndexPicMap = new HashMap<String, PictureData>();

        for (POIXMLDocumentPart dr : sheet.getRelations()) {
            if (dr instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) dr;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    XSSFPicture pic = (XSSFPicture) shape;
                    XSSFClientAnchor anchor = pic.getPreferredSize();
                    CTMarker ctMarker = anchor.getFrom();
                    String picIndex = ctMarker.getRow() + "_" + ctMarker.getCol();
                    sheetIndexPicMap.put(picIndex, pic.getPictureData());
                }
            }
        }

        return sheetIndexPicMap;
    }

    public static void printImg(List<Map<String, PictureData>> sheetList) throws IOException {

        for (Map<String, PictureData> map : sheetList) {
            Object key[] = map.keySet().toArray();
            for (int i = 0; i < map.size(); i++) {
                // 获取图片流
                PictureData pic = map.get(key[i]);
                // 获取图片索引
                String picName = key[i].toString();
                // 获取图片格式
                String ext = pic.suggestFileExtension();

                byte[] data = pic.getData();

                FileOutputStream out = new FileOutputStream("D:\\pic" + picName + "." + ext);
                out.write(data);
                out.close();
            }
        }

    }


    /**
     * 获取图片和位置 (xlsx)
     *
     * @param sheet
     * @return
     * @throws IOException
     */
    public static Map<String, XSSFPictureData> getPictures(XSSFSheet sheet) throws IOException {
        Map<String, XSSFPictureData> map = new HashMap<String, XSSFPictureData>();
        List<POIXMLDocumentPart> list = sheet.getRelations();
        for (POIXMLDocumentPart part : list) {
            if (part instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) part;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    XSSFPicture picture = (XSSFPicture) shape;
                    XSSFClientAnchor anchor = picture.getPreferredSize();
                    CTMarker marker = anchor.getFrom();
                    String key = marker.getRow() + "-" + marker.getCol();
                    map.put(key, picture.getPictureData());
                }
            }
        }
        return map;
    }

    /**
     * @param cell
     * @return
     */
    public static String getString(Cell cell) {
        String str = null;
        if (cell == null)
            return str;

        CellType t = cell.getCellTypeEnum();
        if (CellType.STRING == t) {
            str = cell.getStringCellValue();
        } else if (CellType.BOOLEAN == t) {
            str = String.valueOf(cell.getBooleanCellValue());
        } else if (CellType.FORMULA == t) {
            str = String.valueOf(cell.getCellFormula());
        } else if (CellType.ERROR == t) {
            str = String.valueOf(cell.getErrorCellValue());
        }

        if (str != null)
            str = str.trim();

        return str;

    }


    /**
     * 获取图片和位置 (xlsx)
     *
     * @param sheet
     * @return
     * @throws IOException
     */
    public static Map<String, PictureData> getPictures2(XSSFSheet sheet) throws IOException {
        Map<String, PictureData> map = new HashMap<String, PictureData>();
        List<POIXMLDocumentPart> list = sheet.getRelations();
        for (POIXMLDocumentPart part : list) {
            if (part instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) part;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    XSSFPicture picture = (XSSFPicture) shape;
                    XSSFClientAnchor anchor = picture.getPreferredSize();
                    CTMarker marker = anchor.getFrom();
                    String key = marker.getRow() + "-" + marker.getCol();
                    map.put(key, picture.getPictureData());
                }
            }
        }
        return map;
    }

}
