package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.SkuList;
import cn.atsoft.dasheng.erp.model.params.SkuListParam;
import cn.atsoft.dasheng.erp.model.result.SkuListResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SkuListMapper extends BaseMapper<SkuList> {
    Page<SkuListResult> customPageListBySkuView(@Param("page") Page page , @Param("params") SkuListParam skuListParam);
    List<SkuListResult> customListBySkuView(@Param("params") SkuListParam skuListParam);
}
