package cn.atsoft.dasheng.erp.service;


import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.model.params.SkuListParam;
import cn.atsoft.dasheng.erp.model.result.SkuListResult;

import java.util.List;

public interface SkuListService {
    PageInfo<SkuListResult> listByKeyWord(SkuListParam skuListParam);
}
