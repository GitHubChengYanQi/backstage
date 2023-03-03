package cn.atsoft.dasheng.model.params;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

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
public class QrCodeExcel implements Serializable, BaseValidatingParam {
    private static final long serialVersionUID = 1L;
    @Excel(name = "二维码", orderNum = "0")
    private Long code;


    @Excel(name = "路径", orderNum = "1")
    private String url;

}
