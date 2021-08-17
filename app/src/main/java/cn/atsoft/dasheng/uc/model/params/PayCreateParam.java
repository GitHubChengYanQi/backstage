package cn.atsoft.dasheng.uc.model.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PayCreateParam {

    @NotBlank(message = "payData为必填")
    private String payData;
    @NotBlank(message = "postDta为必填")
    private String postData;
}
