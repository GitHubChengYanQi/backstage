package cn.atsoft.dasheng.commonArea.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 逐渐取代region表
 * </p>
 *
 * @author 
 * @since 2021-08-24
 */
@TableName("sys_common_area")
public class CommonArea implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("parentid")
    private Integer parentid;

    @TableField("title")
    private String title;

    @TableField("grade")
    private Boolean grade;

    /**
     * （字符型级别）
     */
    @TableField("grade_code")
    private String gradeCode;

    /**
     * 地区编码
     */
    @TableField("region_code")
    private String regionCode;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getGrade() {
        return grade;
    }

    public void setGrade(Boolean grade) {
        this.grade = grade;
    }

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    @Override
    public String toString() {
        return "CommonArea{" +
        "id=" + id +
        ", parentid=" + parentid +
        ", title=" + title +
        ", grade=" + grade +
        ", gradeCode=" + gradeCode +
        ", regionCode=" + regionCode +
        "}";
    }
}
