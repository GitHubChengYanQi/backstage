package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.CustomerDynamic;
import cn.atsoft.dasheng.app.model.params.CustomerDynamicParam;
import cn.atsoft.dasheng.app.model.result.CustomerDynamicResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户动态表 Mapper 接口
 * </p>
 *
 * @author cheng
 * @since 2021-08-10
 */
public interface CustomerDynamicMapper extends BaseMapper<CustomerDynamic> {

    /**
     * 获取列表
     *
     * @author cheng
     * @Date 2021-08-10
     */
    List<CustomerDynamicResult> customList(@Param("paramCondition") CustomerDynamicParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cheng
     * @Date 2021-08-10
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CustomerDynamicParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cheng
     * @Date 2021-08-10
     */
    Page<CustomerDynamicResult> customPageList(@Param("page") Page page, @Param("paramCondition") CustomerDynamicParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author cheng
     * @Date 2021-08-10
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CustomerDynamicParam paramCondition);

}
