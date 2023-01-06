package cn.atsoft.dasheng.general.model.result;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
@ApiModel
public class GeneralResult implements Serializable {
   private List<ClassListResult> classListResults;
   private List<BomListResult> bomListResults;
}
