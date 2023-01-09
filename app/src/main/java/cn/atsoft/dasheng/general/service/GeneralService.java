package cn.atsoft.dasheng.general.service;


import cn.atsoft.dasheng.general.model.result.BomListResult;
import cn.atsoft.dasheng.general.model.result.ClassListResult;
import cn.atsoft.dasheng.general.model.result.GeneralResult;

import java.util.List;

public interface GeneralService {

    List<ClassListResult> listByspuClassName(String keyWord);

    List<BomListResult> listBySkuName(String keyWord);
}
