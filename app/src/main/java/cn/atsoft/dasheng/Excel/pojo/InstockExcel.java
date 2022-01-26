package cn.atsoft.dasheng.Excel.pojo;

import lombok.Data;

@Data
public class InstockExcel {
    private String standard;  //成品码

    private Long number;      //数量

    private String house;      //仓库

    private String position;       //库位

    private String brand;     //品牌

    private String supplier;        //供应商



    private Long skuId;

    private Long brandId;

    private Long supplierId;

    private Long positionId;

    private Long houseId;

}
