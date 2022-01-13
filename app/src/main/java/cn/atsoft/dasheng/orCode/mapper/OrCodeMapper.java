package cn.atsoft.dasheng.orCode.mapper;

import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.model.params.OrCodeParam;
import cn.atsoft.dasheng.orCode.model.result.OrCodeResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.function.ObjLongConsumer;

/**
 * <p>
 * 二维码 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
public interface OrCodeMapper extends BaseMapper<OrCode> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-29
     */
    List<OrCodeResult> customList(@Param("paramCondition") OrCodeParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-29
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") OrCodeParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-29
     */
    Page<OrCodeResult> customPageList(@Param("page") Page page, @Param("paramCondition") OrCodeParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-29
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") OrCodeParam paramCondition);



}
