package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Outstock;
import cn.atsoft.dasheng.app.model.params.OutstockParam;
import cn.atsoft.dasheng.app.model.result.OutstockResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 出库表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-07-17
 */
public interface OutstockMapper extends BaseMapper<Outstock> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-07-17
     */
    List<OutstockResult> customList(@Param("paramCondition") OutstockParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-07-17
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") OutstockParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-07-17
     */
    Page<OutstockResult> customPageList(@Param("page") Page page, @Param("paramCondition") OutstockParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-07-17
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") OutstockParam paramCondition);

}
