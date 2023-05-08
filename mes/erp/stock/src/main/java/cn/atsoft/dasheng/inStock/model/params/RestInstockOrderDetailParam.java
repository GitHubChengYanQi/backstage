package cn.atsoft.dasheng.inStock.model.params;

import cn.atsoft.dasheng.base.dict.AbstractDictMap;
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
 * 入库清单
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
@Data
@ApiModel
public class RestInstockOrderDetailParam extends AbstractDictMap implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private Long inkind;

    private Long detailId;

    private Long customerId;

    private Long brandId;

    private Long skuId;


    /**
     * 数量
     */
    @NotNull
    @ApiModelProperty("数量")
    private Long number;


    @Override
    public String checkParam() {
        return null;
    }

    @Override
    public void init() {

    }

    @Override
    protected void initBeWrapped() {

    }
}
