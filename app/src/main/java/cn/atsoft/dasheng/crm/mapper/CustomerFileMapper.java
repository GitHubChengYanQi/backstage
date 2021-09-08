package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.CustomerFile;
import cn.atsoft.dasheng.crm.model.params.CustomerFileParam;
import cn.atsoft.dasheng.crm.model.result.CustomerFileResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-09-08
 */
public interface CustomerFileMapper extends BaseMapper<CustomerFile> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-09-08
     */
    List<CustomerFileResult> customList(@Param("paramCondition") CustomerFileParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-09-08
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CustomerFileParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-09-08
     */
    Page<CustomerFileResult> customPageList(@Param("page") Page page, @Param("paramCondition") CustomerFileParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-09-08
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CustomerFileParam paramCondition);

}
