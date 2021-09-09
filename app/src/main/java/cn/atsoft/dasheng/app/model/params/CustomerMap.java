package cn.atsoft.dasheng.app.model.params;

import lombok.Data;

import java.util.List;

@Data
public class CustomerMap {
    private String address;
    private List<Double> map;
}
