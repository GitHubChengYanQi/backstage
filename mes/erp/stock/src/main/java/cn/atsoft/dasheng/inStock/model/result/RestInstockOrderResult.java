package cn.atsoft.dasheng.inStock.model.result;

import cn.atsoft.dasheng.form.model.result.DocumentsStatusResult;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
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
public class RestInstockOrderResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String statusName;
    private List<Long> noticeIds = new ArrayList<>();
    private List<Long> mediaIds = new ArrayList<>();
//    private List<StorehousePositionsResult> bindTreeView;
//    private List<InstockLogResult> logResults;
    private UserResult stockUserResult;
    private UserResult createUserResult;
    private UserResult updateUserResult;
//    private ThemeAndOrigin themeAndOrigin;
    private DocumentsStatusResult statusResult;
    private String pushPeople;
//    private CustomerResult customerResult;
    private Long customerId;
    private String type;
    private Long status;
    private Long enoughNumber;
    private Long realNumber;
    private Long notNumber;
//    private List<Announcements> announcementsList;
    private List<String> url;
    private Integer waitInStockNum;
    private Integer instockErrorNum;
    private long applyNum;
    private long inStockNum;
    private int positionNum;
    private int skuNum;
//    private List<InstockHandleResult> handleResults;
    private Boolean canPut;

    /**
     * 来源
     */
    private String source;

    /**
     * 注意事项
     */
    private String noticeId;

    /**
     * 来源id
     */
    private Long sourceId;

    private UserResult userResult;

    private String coding;

//    private StorehouseResult storehouseResult;

//    private List<InstockListResult> instockListResults;

    private Date registerTime;

    /**
     * 库位id
     */
    private Long storehousePositionsId;

    @ApiModelProperty("仓库id")
    private Long storeHouseId;

    private Date time;
    /**
     * 入库单
     */
    @ApiModelProperty("入库单")
    private Long instockOrderId;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private Long userId;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    @JSONField(deserialize = false)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    @JSONField(deserialize = false)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    @JSONField(deserialize = false)
    private Long updateUser;

    @ApiModelProperty("")
    private Integer display;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;


    /**
     * 库管人员负责人
     */
    @ApiModelProperty("库管人员负责人")
    private Long stockUserId;

    /**
     * 是否加急
     */
    @ApiModelProperty("是否加急")
    private Integer urgent;


    /**
     * 附件（最多5个，逗号分隔）
     */
    @ApiModelProperty("附件")
    private String enclosure;

    /**
     * 主题
     */
    @ApiModelProperty("主题")
    private String theme;


    /**
     * 入库时间
     */
    @ApiModelProperty("入库时间")
    private Date instockTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String origin;

    /**
     * 入库状态
     */
    @ApiModelProperty("入库状态")
    private Integer state;

    /**
     * 入库类型
     */
    @ApiModelProperty("入库类型")
    private String instockType;



    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
