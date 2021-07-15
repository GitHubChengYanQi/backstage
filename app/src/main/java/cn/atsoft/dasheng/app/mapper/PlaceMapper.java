package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Place;
import cn.atsoft.dasheng.app.model.params.PlaceParam;
import cn.atsoft.dasheng.app.model.result.PlaceResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 地点表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
public interface PlaceMapper extends BaseMapper<Place> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-07-15
     */
    List<PlaceResult> customList(@Param("paramCondition") PlaceParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-07-15
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") PlaceParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-07-15
     */
    Page<PlaceResult> customPageList(@Param("page") Page page, @Param("paramCondition") PlaceParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-07-15
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PlaceParam paramCondition);

}
