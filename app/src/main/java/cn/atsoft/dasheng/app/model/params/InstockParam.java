package cn.atsoft.dasheng.app.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 入库表
 * </p>
 *
 * @author song
 * @since 2021-07-17
 */
@Data
@ApiModel
public class InstockParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    private String name;
    private String brandName;
    private String placeName;

    /**
     * 物品编号
     */
    @ApiModelProperty("物品编号")
    private Long instockId;

    @ApiModelProperty("地点id")
    private Long palceId;

    /**
     * 物品名称
     */
    @ApiModelProperty("物品名称")
    private Long itemId;

    public String getRegisterTime() {
        if (registerTime != null && !registerTime.equals("")) {
            StringBuffer stringBuffer = new StringBuffer(registerTime);
            String date = stringBuffer.substring(0, 10);
            return date;
        } else {
            return registerTime;
        }

    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    /**
     * 登记时间
     */
    @ApiModelProperty("登记时间")
    private String registerTime;

    /**
     * 入库数量
     */
    @ApiModelProperty("入库数量")
    private Long number;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private Integer price;

    /**
     * 品牌
     */
    @ApiModelProperty("品牌")
    private Long brandId;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty("")
    private Integer display;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}