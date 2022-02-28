package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.production.entity.ProductionTaskDetail;
import cn.atsoft.dasheng.production.model.params.ProductionTaskDetailParam;
import cn.atsoft.dasheng.production.model.result.ProductionTaskDetailResult;
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
 * @since 2022-02-28
 */
public interface ProductionTaskDetailMapper extends BaseMapper<ProductionTaskDetail> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2022-02-28
     */
    List<ProductionTaskDetailResult> customList(@Param("paramCondition") ProductionTaskDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2022-02-28
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ProductionTaskDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2022-02-28
     */
    Page<ProductionTaskDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") ProductionTaskDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2022-02-28
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ProductionTaskDetailParam paramCondition);

}
