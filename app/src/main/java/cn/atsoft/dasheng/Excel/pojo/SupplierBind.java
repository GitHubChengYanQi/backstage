package cn.atsoft.dasheng.Excel.pojo;

import lombok.Data;


@Data
public class SupplierBind {

    private String coding;  //物料编码

    private String brand;   //品牌

    private String supplier;  //供应商

    private Integer line;  //行

    private String error;  //错误信息

}
