package cn.atsoft.dasheng.crm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 竞争对手报价
 * </p>
 *
 * @author 
 * @since 2021-09-06
 */
@TableName("daoxin_crm_competitor_quote")
public class CompetitorQuote implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 竞争对手id
     */
      @TableId(value = "competitors_quote_id", type = IdType.ID_WORKER)
    private Long competitorsQuoteId;

    /**
     * 竞争对手报价
     */
    @TableField("competitors_quote")
    private Integer competitorsQuote;

    /**
     * 竞争对手id
     */
    @TableField("competitor_id")
    private Long competitorId;

    /**
     * 创建者
     */
      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改者
     */
      @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    /**
     * 创建时间
     */
      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
      @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 状态
     */
    @TableField("display")
    private Integer display;


    public Long getCompetitorsQuoteId() {
        return competitorsQuoteId;
    }

    public void setCompetitorsQuoteId(Long competitorsQuoteId) {
        this.competitorsQuoteId = competitorsQuoteId;
    }

    public Integer getCompetitorsQuote() {
        return competitorsQuote;
    }

    public void setCompetitorsQuote(Integer competitorsQuote) {
        this.competitorsQuote = competitorsQuote;
    }

    public Long getCompetitorId() {
        return competitorId;
    }

    public void setCompetitorId(Long competitorId) {
        this.competitorId = competitorId;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return "CompetitorQuote{" +
        "competitorsQuoteId=" + competitorsQuoteId +
        ", competitorsQuote=" + competitorsQuote +
        ", competitorId=" + competitorId +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", display=" + display +
        "}";
    }
}
