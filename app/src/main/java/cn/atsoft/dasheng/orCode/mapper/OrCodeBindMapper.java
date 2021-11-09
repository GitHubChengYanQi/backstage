package cn.atsoft.dasheng.orCode.mapper;

import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.params.OrCodeBindParam;
import cn.atsoft.dasheng.orCode.model.result.OrCodeBindResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 二维码绑定 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
public interface OrCodeBindMapper extends BaseMapper<OrCodeBind> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-29
     */
    List<OrCodeBindResult> customList(@Param("paramCondition") OrCodeBindParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-29
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") OrCodeBindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-29
     */
    Page<OrCodeBindResult> customPageList(@Param("page") Page page, @Param("paramCondition") OrCodeBindParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-29
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") OrCodeBindParam paramCondition);

}
