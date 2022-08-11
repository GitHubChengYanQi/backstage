package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.AllocationLogDetail;
import cn.atsoft.dasheng.erp.model.params.AllocationLogDetailParam;
import cn.atsoft.dasheng.erp.model.result.AllocationLogDetailResult;
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
 * @author Captain_Jazz
 * @since 2022-08-11
 */
public interface AllocationLogDetailMapper extends BaseMapper<AllocationLogDetail> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    List<AllocationLogDetailResult> customList(@Param("paramCondition") AllocationLogDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") AllocationLogDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    Page<AllocationLogDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") AllocationLogDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") AllocationLogDetailParam paramCondition);

}
