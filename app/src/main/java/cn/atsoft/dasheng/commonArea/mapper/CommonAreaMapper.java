package cn.atsoft.dasheng.commonArea.mapper;

import cn.atsoft.dasheng.commonArea.entity.CommonArea;
import cn.atsoft.dasheng.commonArea.model.params.CommonAreaParam;
import cn.atsoft.dasheng.commonArea.model.result.CommonAreaResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 逐渐取代region表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-24
 */
public interface CommonAreaMapper extends BaseMapper<CommonArea> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-08-24
     */
    List<CommonAreaResult> customList(@Param("paramCondition") CommonAreaParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-08-24
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CommonAreaParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-08-24
     */
    Page<CommonAreaResult> customPageList(@Param("page") Page page, @Param("paramCondition") CommonAreaParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-08-24
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CommonAreaParam paramCondition);

}
