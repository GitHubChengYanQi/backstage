package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.params.CustomerParam;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户管理表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-07-23
 */
public interface CustomerMapper extends BaseMapper<Customer> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-07-23
     */
    List<CustomerResult> customList(@Param("paramCondition") CustomerParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-07-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CustomerParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-07-23
     */
    Page<CustomerResult> customPageList(@Param("page") Page page, @Param("paramCondition") CustomerParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-07-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CustomerParam paramCondition);

}
