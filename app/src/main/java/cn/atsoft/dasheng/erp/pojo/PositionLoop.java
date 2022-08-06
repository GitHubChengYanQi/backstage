package cn.atsoft.dasheng.erp.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class PositionLoop {
    private Long key;
    private String title;
    private Long pid;
    private Long storeHouseId;
    private Long skuId;
    private Long num;
    private List<PositionLoop> loops;


    @JSONField(serialize = false)
    private boolean b = false;    //判断库位 需要不需要返回  为真返回
}
