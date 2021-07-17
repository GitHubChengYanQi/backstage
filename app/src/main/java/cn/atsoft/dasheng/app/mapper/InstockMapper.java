package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Instock;
import cn.atsoft.dasheng.app.model.params.InstockParam;
import cn.atsoft.dasheng.app.model.result.InstockResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 入库表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-07-17
 */
public interface InstockMapper extends BaseMapper<Instock> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-07-17
     */
    List<InstockResult> customList(@Param("paramCondition") InstockParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-07-17
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") InstockParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-07-17
     */
    Page<InstockResult> customPageList(@Param("page") Page page, @Param("paramCondition") InstockParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-07-17
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") InstockParam paramCondition);

}
