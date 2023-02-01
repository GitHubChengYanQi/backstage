package cn.atsoft.dasheng.goods.spu.mapper;

import cn.atsoft.dasheng.goods.spu.entity.RestSpu;
import cn.atsoft.dasheng.goods.spu.model.params.RestSpuParam;
import cn.atsoft.dasheng.goods.spu.model.result.RestSpuResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
 * @since 2021-10-18
 */
public interface RestSpuMapper extends BaseMapper<RestSpu> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-10-18
     */
    List<RestSpuResult> customList(@Param("paramCondition") RestSpuParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-10-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestSpuParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-10-18
     */
    Page<RestSpuResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestSpuParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-10-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestSpuParam paramCondition);

}
