package cn.atsoft.dasheng.app.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 数据库信息表
 * </p>
 *
 * @author sing
 * @since 2020-12-07
 */
@Data
@ApiModel
public class InfoParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long dbId;

    /**
     * 数据库名称（英文名称）
     */
    @ApiModelProperty("数据库名称（英文名称）")
    private String dbName;

    /**
     * jdbc的驱动类型
     */
    @ApiModelProperty("jdbc的驱动类型")
    private String jdbcDriver;

    /**
     * 数据库连接的账号
     */
    @ApiModelProperty("数据库连接的账号")
    private String userName;

    /**
     * 数据库连接密码
     */
    @ApiModelProperty("数据库连接密码")
    private String password;

    /**
     * jdbc的url
     */
    @ApiModelProperty("jdbc的url")
    private String jdbcUrl;

    /**
     * 备注，摘要
     */
    @ApiModelProperty("备注，摘要")
    private String remarks;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    @Override
    public String checkParam() {
        return null;
    }

}
