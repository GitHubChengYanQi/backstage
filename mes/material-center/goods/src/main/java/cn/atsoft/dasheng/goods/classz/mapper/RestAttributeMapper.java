package cn.atsoft.dasheng.goods.classz.mapper;


import cn.atsoft.dasheng.goods.classz.entity.RestAttribute;
import cn.atsoft.dasheng.goods.classz.model.params.RestAttributeParam;
import cn.atsoft.dasheng.goods.classz.model.result.RestAttributeResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品属性表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-18
 */
public interface RestAttributeMapper extends BaseMapper<RestAttribute> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-18
     */
    List<RestAttributeResult> customList(@Param("paramCondition") RestAttributeParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestAttributeParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-18
     */
    Page<RestAttributeResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestAttributeParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestAttributeParam paramCondition);

}
