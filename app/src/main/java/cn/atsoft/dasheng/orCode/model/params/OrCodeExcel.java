package cn.atsoft.dasheng.orCode.model.params;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 二维码
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
@Data
@ApiModel
public class OrCodeExcel implements Serializable, BaseValidatingParam {
    private static final long serialVersionUID = 1L;
    @Excel(name = "二维码", orderNum = "0")
    private Long code;


    @Excel(name = "路径", orderNum = "1")
    private String url;

}
