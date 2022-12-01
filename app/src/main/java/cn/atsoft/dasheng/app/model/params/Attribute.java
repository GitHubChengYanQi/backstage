package cn.atsoft.dasheng.app.model.params;

import cn.atsoft.dasheng.erp.entity.AttributeValues;
import lombok.Data;

import java.util.List;

@Data
public class Attribute   implements Comparable<Attribute> {
    private String attributeId;
    private Long id;
    private String attribute;
    private List<Values> attributeValues;
    //------------------------
    private String k;
    private Long ks;

    @Override
    public int compareTo(Attribute o) {
        return (int) (this.id - o.getId());
    }


}
