package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.AllocationDetail;
import cn.atsoft.dasheng.erp.model.params.AllocationDetailParam;
import cn.atsoft.dasheng.erp.model.result.AllocationDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 调拨子表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-07-13
 */
public interface AllocationDetailMapper extends BaseMapper<AllocationDetail> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    List<AllocationDetailResult> customList(@Param("paramCondition") AllocationDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") AllocationDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    Page<AllocationDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") AllocationDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") AllocationDetailParam paramCondition);

}
