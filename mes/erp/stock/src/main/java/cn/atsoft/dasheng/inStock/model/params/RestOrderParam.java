package cn.atsoft.dasheng.inStock.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@Data
@ApiModel
public class RestOrderParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    private String keywords;

    private String skuName;


    @Override
    public String checkParam() {
        return null;
    }

}
