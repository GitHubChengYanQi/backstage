package cn.atsoft.dasheng.inStock.model.params;

import cn.atsoft.dasheng.base.dict.AbstractDictMap;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 入库单
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
@Data
@ApiModel
public class RestInstockOrderParam extends AbstractDictMap implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    //跳转路径
    private RestOrderParam orderParam;
    private Boolean directInStock = false;   //是否直接入库

    List<RestInstockOrderDetailParam> detailParams;

    private Long orderId;

    private Long bomId;

    private Integer number;


    @Override
    public void init() {

    }

    @Override
    protected void initBeWrapped() {

    }
}
