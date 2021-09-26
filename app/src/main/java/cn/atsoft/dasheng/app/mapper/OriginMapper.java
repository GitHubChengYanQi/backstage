package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Origin;
import cn.atsoft.dasheng.app.model.params.OriginParam;
import cn.atsoft.dasheng.app.model.result.OriginResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 来源表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-07-19
 */
public interface OriginMapper extends BaseMapper<Origin> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-07-19
     */
    List<OriginResult> customList(@Param("paramCondition") OriginParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-07-19
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") OriginParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-07-19
     */
    Page<OriginResult> customPageList(@Param("page") Page page, @Param("paramCondition") OriginParam paramCondition,@Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-07-19
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") OriginParam paramCondition);

}
