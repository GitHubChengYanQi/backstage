package cn.atsoft.dasheng.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 零件表
 * </p>
 *
 * @author 
 * @since 2021-07-23
 */
@TableName("daoxin_parts_list")
public class PartsList implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 零件表id
     */
      @TableId(value = "Parts_list_id", type = IdType.ID_WORKER)
    private Long partsListId;

    /**
     * 零件id
     */
    @TableField("items")
    private Long items;

    /**
     * 零件名称
     */
    @TableField("name")
    private String name;

    /**
     * 零件数量
     */
    @TableField("number")
    private Integer number;


    public Long getPartsListId() {
        return partsListId;
    }

    public void setPartsListId(Long partsListId) {
        this.partsListId = partsListId;
    }

    public Long getItems() {
        return items;
    }

    public void setItems(Long items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "PartsList{" +
        "partsListId=" + partsListId +
        ", items=" + items +
        ", name=" + name +
        ", number=" + number +
        "}";
    }
}
