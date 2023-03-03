package cn.atsoft.dasheng.goods.brand.mapper;


import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.goods.brand.entity.RestBrand;
import cn.atsoft.dasheng.goods.brand.model.params.RestBrandParam;
import cn.atsoft.dasheng.goods.brand.model.result.RestBrandResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 品牌表 Mapper 接口
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
public interface RestBrandMapper extends BaseMapper<RestBrand> {

    /**
     * 获取列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    List<RestBrandResult> customList(@Param("paramCondition") RestBrandParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RestBrandParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    Page<RestBrandResult> customPageList(@Param("page") Page page, @Param("paramCondition") RestBrandParam paramCondition,@Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RestBrandParam paramCondition);

}
