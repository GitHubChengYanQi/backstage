package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.CrmBusinessSales;
import cn.atsoft.dasheng.app.model.params.CrmBusinessSalesParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessSalesResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 销售 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-04
 */
public interface CrmBusinessSalesMapper extends BaseMapper<CrmBusinessSales> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-08-04
     */
    List<CrmBusinessSalesResult> customList(@Param("paramCondition") CrmBusinessSalesParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-08-04
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CrmBusinessSalesParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-08-04
     */
    Page<CrmBusinessSalesResult> customPageList(@Param("page") Page page, @Param("paramCondition") CrmBusinessSalesParam paramCondition,@Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-08-04
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CrmBusinessSalesParam paramCondition);

}
