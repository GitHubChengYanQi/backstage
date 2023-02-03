package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.appBase.model.result.MediaResult;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SkuListResult {
    //物料Id
    private Long skuId;
    //成品码
    private String standard;
    //物料名字
    private String skuName;
    //物料价格
    private double price;

    List<MediaResult> imgResults;    //图片返回


    public double getPrice() {
        return price/100;
    }

    //品牌名称
    private String brandName;

    //供应商名称
    private String customerName;

    private String skuValue;

    private Long spuId;

    private String spuName;



    private Long categoryId;

    private String categoryName;

    private String materialId;

    private String materialName;

    //bom数
    private Integer bomNum;

    //工艺数
    private Integer shipNum;

    private Integer stockNum;

    //单位
    private Long unitId;

    //单位名称
    private String unitName;

    //库存预警最小值
    private Integer stockWarningMin;

    //库存预警最大值
    private Integer stockWarningMax;

    private String remarks;

    private String coding;

    private String modelCoding;

    private String viewFrame;

    private String model;

    private String packaging;

    private String heatTreatment;

    private String level;

    private String skuSize;

    private String color;

    private String weight;


    private String partNo;

    private String nationalStandard;

    private Integer maintenancePeriod;

    private Integer addMethod;

    private String enclosure;

    private String drawing;

    private String images;

    private Long customerId;

    private String fileId;

    private Integer batch;

    private Long qualityPlanId;

    private Integer type;

    private String specifications;

    private Integer isBan;

    private String skuValueMd5;
    //
    private Date createTime;
    //仓库
    private String storeName;
    //库位
    private String storePosName;
    //材质名称
    private String materiaName;
}
