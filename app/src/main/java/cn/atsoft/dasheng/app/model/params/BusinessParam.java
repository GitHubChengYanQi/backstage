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
 * 商机表
 * </p>
 *
 * @author cheng
 * @since 2021-07-19
 */
@Data
@ApiModel
public class BusinessParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
private  String account;
 private  String iname;
private  String sname;
private  Long originId;

    /**
     * 商机id
     */
    @ApiModelProperty("商机id")
    private Long businessId;

    /**
     * 客户id
     */
    @ApiModelProperty("客户id")
    private Long customerId;

    /**
     * 机会来源
     */

    private  long stockId;
    public String getTime() {
        if (time!=null&&!time.equals("")){
            StringBuffer stringBuffer = new StringBuffer(time);
            String date = stringBuffer.substring(0,10);
            return date;
        }else {
            return time;
        }

    }

    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 立项日期
     */
    @ApiModelProperty("立项日期")
    private String time;

    /**
     * 商机状态
     */
    @ApiModelProperty("商机状态")
    private Integer state;

    /**
     * 商机阶段
     */
    @ApiModelProperty("商机阶段")
    private String stage;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private long person;

    private  String name;
    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 创建用户
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改用户
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;


    @Override
    public String checkParam() {
        return null;
    }

}
