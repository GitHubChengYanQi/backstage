package cn.atsoft.dasheng.wedrive.file.entity;

import cn.atsoft.dasheng.wedrive.file.model.enums.FileType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 微信微盘文件管理
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-01
 */
@TableName("wx_wedrive_file")
public class WedriveFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件id
     */
      @TableId(value = "file_id", type = IdType.ID_WORKER)
    private String fileId;

    /**
     * 文件名称
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 空间id
     */
    @TableField("space_id")
    private String spaceId;

    /**
     * 父目录fileid,在根目录时为空间id
     */
    @TableField("father_id")
    private String fatherId;

    /**
     * 分类
     */
    @TableField("type")
    private String type;

    /**
     * 创建者
     */
      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 部门编号
     */
@TableField(value = "deptId" , fill = FieldFill.INSERT)
    private Long deptId;
    /**
     * 租户编号
     */
    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * 部门编号
     */
    @TableField("file_type")
    private Integer fileType;

    /**
     * 状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 修改时间
     */
      @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 创建时间
     */
      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改者
     */
      @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;


    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "WedriveFile{" +
                "fileId='" + fileId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", spaceId='" + spaceId + '\'' +
                ", fatherId='" + fatherId + '\'' +
                ", type='" + type + '\'' +
                ", createUser=" + createUser +
                ", deptId=" + deptId +
                ", fileType=" + fileType +
                ", display=" + display +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                ", updateUser=" + updateUser +
                '}';
    }
}
