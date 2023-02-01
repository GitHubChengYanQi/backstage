package cn.atsoft.dasheng.erp.service;


import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.SkuList;
import cn.atsoft.dasheng.erp.model.params.SkuListParam;
import cn.atsoft.dasheng.erp.model.result.SkuListResult;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SkuListService  extends IService<SkuList> {
    PageInfo<SkuListResult> listByKeyWord(SkuListParam skuListParam);

    List<SkuListResult> resultByIds(List<Long> ids);
}
