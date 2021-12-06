package cn.atsoft.dasheng.erp.model.request;

import lombok.Data;

import java.util.List;

@Data
public class FormDataPojo {
    private String module;//所属模块
    private Long qrCodeId;//二维码
    private Long planId;

}
