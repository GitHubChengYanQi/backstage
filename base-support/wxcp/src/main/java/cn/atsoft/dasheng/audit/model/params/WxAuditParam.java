package cn.atsoft.dasheng.audit.model.params;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;
import me.chanjar.weixin.cp.bean.oa.SummaryInfo;
import me.chanjar.weixin.cp.bean.oa.WxCpOaApplyEventRequest;
import me.chanjar.weixin.cp.bean.oa.applydata.ApplyDataContent;
import me.chanjar.weixin.cp.util.json.WxCpGsonBuilder;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-18
 */
@Data
@ApiModel
public class WxAuditParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    private Long orderId;

    private List<Long> mediaIds;

    @ApiModelProperty("")
    private String spNo;

    /**
     * 	申请单状态：1-审批中；2-已通过；3-已驳回；4-已撤销；6-通过后撤销；7-已删除；10-已支付
     */
    @ApiModelProperty("	申请单状态：1-审批中；2-已通过；3-已驳回；4-已撤销；6-通过后撤销；7-已删除；10-已支付")
    private Integer status;

    @ApiModelProperty("")
    private Integer code;

    @ApiModelProperty("")
    private String msg;

    /**
     * 发起人
     */
    @ApiModelProperty("发起人")
    private Long creatorUser;

    /**
     * 删除状态
     */
    @ApiModelProperty("删除状态")
    private Integer display;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;


    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

    /**
     * 申请人userid，此审批申请将以此员工身份提交，申请人需在应用可见范围内
     */
    @SerializedName("creator_userid")
    private String creatorUserId;

    /**
     * 模板id。可在“获取审批申请详情”、“审批状态变化回调通知”中获得，也可在审批模板的模板编辑页面链接中获得。暂不支持通过接口提交[打卡补卡][调班]模板审批单。
     */
    @SerializedName("template_id")
    private String templateId;

    /**
     * 审批人模式：0-通过接口指定审批人、抄送人（此时approver、notifyer等参数可用）; 1-使用此模板在管理后台设置的审批流程，支持条件审批。默认为0
     */

    @ApiModelProperty("审批人模式：0-通过接口指定审批人、抄送人（此时approver、notifyer等参数可用）; 1-使用此模板在管理后台设置的审批流程(需要保证审批流程中没有“申请人自选”节点)，支持条件审批。默认为0")
    @SerializedName("use_template_approver")
    private Integer useTemplateApprover;

    /**
     * 审批流程信息，用于指定审批申请的审批流程，支持单人审批、多人会签、多人或签，可能有多个审批节点，仅use_template_approver为0时生效。
     */
    @SerializedName("approver")
    private List<WxCpOaApplyEventRequest.Approver> approvers;

    /**
     * 抄送人节点userid列表，仅use_template_approver为0时生效。
     */
    @SerializedName("notifyer")
    private String[] notifiers;

    /**
     * 抄送方式：1-提单时抄送（默认值）； 2-单据通过后抄送；3-提单和单据通过后抄送。仅use_template_approver为0时生效。
     */
    @SerializedName("notify_type")
    private Integer notifyType;

    /**
     * 审批申请数据，可定义审批申请中各个控件的值，其中必填项必须有值，选填项可为空，数据结构同“获取审批申请详情”接口返回值中同名参数“apply_data”
     */
    @SerializedName("apply_data")
    private WxCpOaApplyEventRequest.ApplyData applyData;

    /**
     * 摘要信息，用于显示在审批通知卡片、审批列表的摘要信息，最多3行
     */
    @SerializedName("summary_list")
    private List<SummaryInfo> summaryList;

    public String toJson() {
        return WxCpGsonBuilder.create().toJson(this);
    }

    @Data
    @Accessors(chain = true)
    public static class Approver implements Serializable {
        private static final long serialVersionUID = 7625206971546930988L;

        /**
         * 节点审批方式：1-或签；2-会签，仅在节点为多人审批时有效
         */
        private Integer attr;

        /**
         * 审批节点审批人userid列表，若为多人会签、多人或签，需填写每个人的userid
         */
        @SerializedName("userid")
        private String[] userIds;
    }

    @Data
    @Accessors(chain = true)
    public static class ApplyData implements Serializable {
        private static final long serialVersionUID = -2462732405265306981L;

        /**
         * 审批申请数据，可定义审批申请中各个控件的值，其中必填项必须有值，选填项可为空，
         * 数据结构同“获取审批申请详情”接口返回值中同名参数“apply_data”
         */
        @SerializedName("contents")
        private List<ApplyDataContent> contents;
    }


}
