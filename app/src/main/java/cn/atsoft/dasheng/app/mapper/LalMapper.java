package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Lal;
import cn.atsoft.dasheng.app.model.params.LalParam;
import cn.atsoft.dasheng.app.model.result.LalResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 经纬度表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-07-16
 */
public interface LalMapper extends BaseMapper<Lal> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-07-16
     */
    List<LalResult> customList(@Param("paramCondition") LalParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-07-16
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") LalParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-07-16
     */
    Page<LalResult> customPageList(@Param("page") Page page, @Param("paramCondition") LalParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-07-16
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") LalParam paramCondition);

}
