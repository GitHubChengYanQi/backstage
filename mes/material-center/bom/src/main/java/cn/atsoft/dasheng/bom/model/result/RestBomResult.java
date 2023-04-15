package cn.atsoft.dasheng.bom.model.result;

import cn.atsoft.dasheng.goods.sku.model.result.RestSkuResult;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel
public class RestBomResult implements Serializable {

    private List<RestBomDetailResult> detailResults;

    private Long parentId;

    private UserResult userResult;

    private Long productionTaskId;
    private Integer done ;

    private Date lastTime;

    @ApiModelProperty("sku返回类")
    private RestSkuResult skuResult;

    @ApiModelProperty("bomId")
    private Long bomId;

    @ApiModelProperty("skuId")
    private Long skuId;

    @ApiModelProperty("version")
    private String version;

    @ApiModelProperty("note")
    private String note;

    @ApiModelProperty("number")
    private Integer number;

    @ApiModelProperty("children")
    private String children;

    @ApiModelProperty("childrens")
    private String childrens;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long createUser;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long updateUser;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;
}
