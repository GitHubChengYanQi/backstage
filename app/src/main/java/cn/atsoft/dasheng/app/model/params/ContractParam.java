package cn.atsoft.dasheng.app.model.params;

import cn.atsoft.dasheng.Excel.pojo.LabelResult;
import cn.atsoft.dasheng.app.entity.ContractDetail;
import cn.atsoft.dasheng.app.pojo.ContractReplace;
import cn.atsoft.dasheng.app.pojo.CycleReplace;
import cn.atsoft.dasheng.app.pojo.PayReplace;
import com.baomidou.mybatisplus.annotation.TableField;
import cn.atsoft.dasheng.crm.pojo.Payment;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 合同表
 * </p>
 *
 * @author
 * @since 2021-07-21
 */
@Data
@ApiModel
public class ContractParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private String coding;

    private List<ContractDetail> contractDetailList;

    private List<ContractReplace> contractReplaces; //替换

    private List<CycleReplace> cycleReplaces;

    private List<PayReplace> payReplaces;

    private String contractCoding;

    private List<LabelResult> labelResults;

    private String coding;

    /**
     * 合同分类
     */
    private Long contractClassId;

    /**
     * 合同id
     */
    @ApiModelProperty("合同id")
    private Long contractId;

    private String source;
    private Long sourceId;

    private Integer audit;
    private Integer allMoney;


    private Long partyAPhone;

    private Long partyBPhone;
    private Long templateId;

    private Long partyA;

    private Long partyAAdressId;
    private Long partyBAdressId;
    private Long partyAContactsId;
    private Long partyBContactsId;

    private Long partyB;
    private Payment payment;

    /**
     * 合同名称
     */
    @ApiModelProperty("合同名称")
    private String name;


    /**
     * 主题
     */
    @ApiModelProperty("主题")
    private String theme;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String origin;


    /**
     * 负责人id
     */
    @ApiModelProperty("负责人id")
    private String userId;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date time;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

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

    private Long deptId;
}
