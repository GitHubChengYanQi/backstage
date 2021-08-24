package cn.atsoft.dasheng.common_area.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 逐渐取代region表
 * </p>
 *
 * @author 
 * @since 2021-08-24
 */
@Data
@ApiModel
public class CommonAreaResult implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Integer id;

    @ApiModelProperty("")
    private Integer parentid;

    @ApiModelProperty("")
    private String title;

    @ApiModelProperty("")
    private Boolean grade;

    /**
     * （字符型级别）
     */
    @ApiModelProperty("（字符型级别）")
    private String gradeCode;

    /**
     * 地区编码
     */
    @ApiModelProperty("地区编码")
    private String regionCode;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
