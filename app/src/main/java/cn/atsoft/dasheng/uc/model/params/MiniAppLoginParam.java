package cn.atsoft.dasheng.uc.model.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MiniAppLoginParam {

    @NotBlank(message = "code不能为空")
    private String code;

}
