package cn.atsoft.dasheng.general.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
@Data
@ApiModel
public class GeneralParam implements Serializable, BaseValidatingParam {
    //关键词
    public String keyWord;
}
