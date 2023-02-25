package cn.atsoft.dasheng.goods.classz.model;

import lombok.Data;

import java.util.List;
import java.util.jar.Attributes;

@Data
public class RestAttributes implements Comparable<RestAttributes> {
    private String attributeId;
    private Long id;
    private String attribute;
    private List<RestValues> attributeValues;
    //------------------------
    private String k;
    private Long ks;

    @Override
    public int compareTo(RestAttributes o) {
        return (int) (this.id - o.getId());
    }


}
