package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.BusinessDynamic;
import cn.atsoft.dasheng.app.model.params.BusinessDynamicParam;
import cn.atsoft.dasheng.app.model.result.BusinessDynamicResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商机动态表 Mapper 接口
 * </p>
 *
 * @author cheng
 * @since 2021-08-10
 */
public interface BusinessDynamicMapper extends BaseMapper<BusinessDynamic> {

    /**
     * 获取列表
     *
     * @author cheng
     * @Date 2021-08-10
     */
    List<BusinessDynamicResult> customList(@Param("paramCondition") BusinessDynamicParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cheng
     * @Date 2021-08-10
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") BusinessDynamicParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cheng
     * @Date 2021-08-10
     */
    Page<BusinessDynamicResult> customPageList(@Param("page") Page page, @Param("paramCondition") BusinessDynamicParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author cheng
     * @Date 2021-08-10
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") BusinessDynamicParam paramCondition);

}
