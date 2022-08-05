package cn.atsoft.dasheng.Excel.pojo;


import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import lombok.Data;

@Data
public class PositionBind {

    private String spuClass;  //分类

    private String strand;     //编码

    private String item;        //产品

    private String spuName;        //型号

    private Integer stockNumber;   //库存数量

    private String supperPosition;   //上级库位

    private String position;     //库位

    private String brand;      //品牌

    private String storeHouse;  //仓库

    private String customer; //供应商

    private Long customerId;
    // ----------------------------------------------------------
    private Long storehouseId;

    private Long skuId;

    private Long positionId;

    private Integer line;

    private String error;
}
