package cn.atsoft.dasheng.goods.texture.mapper;


import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.goods.texture.entity.RestTextrue;
import cn.atsoft.dasheng.goods.texture.model.params.RestTextrueParam;
import cn.atsoft.dasheng.goods.texture.model.result.RestTextrueResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 材质 Mapper 接口
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
public interface RestTextureMapper extends BaseMapper<RestTextrue> {

    /**
     * 获取列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    List<RestTextrueResult> customList(@Param("paramCondition") RestTextrueParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestTextrueParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    Page<RestTextrueResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestTextrueParam paramCondition,@Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestTextrueParam paramCondition);

}
