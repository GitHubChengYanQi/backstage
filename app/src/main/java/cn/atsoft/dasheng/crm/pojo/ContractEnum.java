package cn.atsoft.dasheng.crm.pojo;

import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.form.pojo.MoneyTypeEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ContractEnum {

    contractCoding("采购合同编号"),  //采购合同编号
    ASite("合同签订地点"),           //签订地点
    ATime("合同签订时间"),           //签订时间
    ACustomerName("需方公司名称"),   //需方公司名称
    BCustomerName("供方公司名称"),   //供方公司名称
    extractPlace("提取地点"),    //提取地点
    pickUpMan("接货人员"),          //接货人员
    pickUpManPhone("接货人电话"),          //接货人电话
    ACustomerAdress("需方公司地址"),         //需方公司地址
    ACustomerPhone("需方公司电话"),         //需方公司电话
    AFax("需方公司传真"),         //需方公司传真
    ALegalMan("需方法人代表"),         //需方法人代表

    ALegalManPhone("需方法人电话"),            //需方法人电话
    AContacts("需方委托代表"),             //需方委托代表
    APhone("需方代表电话"),         //需方代表电话
    ABank("需方开户银行"),         //需方开户银行
    ABankAccount("需方银行账号"),         //需方银行账号
    ABankNo("需方开户行号"),         //需方开户行号

    AZipCode("需方邮政编码"),         //需方邮政编码
    AEmail("需方公司电邮"),         //需方公司电邮
    AEin("需方税号"),            //需方税号
    DeliveryAddress("提取(交付)地点"),           //交货地址

    supplierPhone("供货人及电话"),         //供货人及电话
    BCustomerAdress("供方公司地址"),         //供方公司地址
    BCustomerPhone("供方公司电话"),         //供方公司电话
    BFax("供方公司传真"),         //供方公司传真
    BLegalMan("供方法人代表"),         //供方法人代表
    BLegalManPhone("供方法人电话"),            //需方法人电话
    BContacts("供方委托代表"),             //需方委托代表
    BPhone("供方代表电话"),         //需方代表电话
    BBank("供方开户银行"),         //需方开户银行
    BBankAccount("供方银行账号"),         //需方银行账号
    BBankNo("供方开户行号"),         //需方开户行号
    BZipCode("供方邮政编码"),         //需方邮政编码
    BEmail("供方公司电邮"),         //需方公司电邮
    BEin("供方税号"),
    DateWay("日期方式"),
    PaymentProportion("付款比例"),
    PaymentDate("付款日期"),
    skuStrand("物料编码"),
    spuName("产品名称"),
    skuName("型号规格"),
    brand("品牌厂家"),
    unit("单位"),
    TotalPrice("总价"),
    number("数量"),
    UnitPrice("单价"),
    DeliveryDate("交货日期"),
    DeliveryCycle("交货周期"),
    TotalAmountInWords("合计金额大写"),
    TotalAmountInFigures("合计金额小写"),

    remakr("产品备注"),
    Line("序号"),
    payMoney("付款金额"),
    TotalNumber("合计数量"),
    mailingAddress("发票邮寄地址"),
    invoiceType("发票类型");


    public String getDetail() {
        return type;
    }

    ContractEnum(String type) {
        this.type = type;
    }

    @EnumValue
    private final String type;

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }



    public static ContractEnum fromString(String text) {
        for (ContractEnum b : ContractEnum.values()) {
            if (b.type.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
