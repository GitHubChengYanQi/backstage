package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.CrmCustomerLevel;
import cn.atsoft.dasheng.app.model.params.CrmCustomerLevelParam;
import cn.atsoft.dasheng.app.model.result.CrmCustomerLevelResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户级别表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-07-30
 */
public interface CrmCustomerLevelMapper extends BaseMapper<CrmCustomerLevel> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-07-30
     */
    List<CrmCustomerLevelResult> customList(@Param("paramCondition") CrmCustomerLevelParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-07-30
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CrmCustomerLevelParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-07-30
     */
    Page<CrmCustomerLevelResult> customPageList(@Param("page") Page page, @Param("paramCondition") CrmCustomerLevelParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-07-30
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CrmCustomerLevelParam paramCondition);

}
