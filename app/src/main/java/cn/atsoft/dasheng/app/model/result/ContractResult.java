package cn.atsoft.dasheng.app.model.result;

import cn.atsoft.dasheng.crm.model.result.ContractClassResult;
import cn.atsoft.dasheng.crm.pojo.Payment;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.Data;

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
public class ContractResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private ContractClassResult classResult;

    private List<ContractDetailResult> contractDetailResults;
    /**
     * 合同id
     */
    @ApiModelProperty("合同id")
    private Long contractId;
    private CustomerResult partA;
    private CustomerResult partB;

    private String partAName;
    private String partBName;
    private Long partyA;
    private Long partyB;

    private Long partyAAdressId;
    private Long partyBAdressId;
    private Long partyAContactsId;
    private Long partyBContactsId;

    private ContactsResult partyAContacts;
    private ContactsResult partyBContacts;

    private AdressResult partyAAdress;
    private AdressResult partyBAdress;


    private Long partyAPhone;

    private Long partyBPhone;

    private Long templateId;
    private Integer audit;
    private Integer allMoney;

    private PhoneResult phoneA;
    private PhoneResult phoneB;

    @TableField(typeHandler = FastjsonTypeHandler.class)
    private Payment payment;


    /**
     * 合同分类
     */
    private Long contractClassId;

    /**
     * 合同名称
     */
    @ApiModelProperty("合同名称")
    private String name;

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
    private Long deptId;
    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
