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
 * 报价表
 * </p>
 *
 * @author cheng
 * @since 2021-07-19
 */
@Data
@ApiModel
public class TrackParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    private String name;
    private String iname;
    private String note;
    private String time;

    public String getTime() {
//        if(time!=null&&!time.equals("")){
//            StringBuffer stringBuffer = new StringBuffer(time);
//            String data = stringBuffer.substring(0, 10);
//            return data;
//        }else {
//            return time;
//        }
        if (time != null && !time.equals("")) {
            StringBuffer stringBuffer = new StringBuffer(time);
            String date = stringBuffer.substring(0, 10);
            return date;
        } else {
            return time;
        }

    }


    public void setNumber(Long number) {
        this.number = number;
    }

    /**
     * 报价编号
     */
    @ApiModelProperty("报价编号")
    private Long trackId;

    /**
     * 商机编号
     */
    @ApiModelProperty("商机编号")
    private Long businessId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private Long stockId;

    /**
     * 报价金额
     */
    @ApiModelProperty("报价金额")
    private Integer money;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Long number;

    /**
     * 报价阶段
     */
    @ApiModelProperty("报价阶段")
    private Integer stage;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
