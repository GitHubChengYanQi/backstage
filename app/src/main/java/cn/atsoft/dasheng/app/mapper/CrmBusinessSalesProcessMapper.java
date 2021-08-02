package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.CrmBusinessSalesProcess;
import cn.atsoft.dasheng.app.model.params.CrmBusinessSalesProcessParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessSalesProcessResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 销售流程 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-07-31
 */
public interface CrmBusinessSalesProcessMapper extends BaseMapper<CrmBusinessSalesProcess> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-07-31
     */
    List<CrmBusinessSalesProcessResult> customList(@Param("paramCondition") CrmBusinessSalesProcessParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-07-31
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CrmBusinessSalesProcessParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-07-31
     */
    Page<CrmBusinessSalesProcessResult> customPageList(@Param("page") Page page, @Param("paramCondition") CrmBusinessSalesProcessParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-07-31
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CrmBusinessSalesProcessParam paramCondition);

}
