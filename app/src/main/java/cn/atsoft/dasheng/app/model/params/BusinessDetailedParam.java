package cn.atsoft.dasheng.app.model.params;


import lombok.Data;

import java.util.List;
@Data
public class BusinessDetailedParam {

    private List<CrmBusinessDetailedParam> businessDetailedParam;
    private Long businessId;
    private Long packageId;

}
