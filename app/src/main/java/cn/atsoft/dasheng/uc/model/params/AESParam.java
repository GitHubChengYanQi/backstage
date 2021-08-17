package cn.atsoft.dasheng.uc.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import lombok.Data;

import java.io.Serializable;

@Data
public class AESParam implements Serializable, BaseValidatingParam {
    private String key;
}
