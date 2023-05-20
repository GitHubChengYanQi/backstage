package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.model.params.PartsParam;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 清单 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-21
 */
public interface PartsMapper extends BaseMapper<Parts> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-21
     */
    List<PartsResult> customList(@Param("paramCondition") PartsParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-21
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") PartsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-21
     */
    Page<PartsResult> customPageList(@Param("page") Page page, @Param("paramCondition") PartsParam paramCondition, @Param("dataScope") DataScope dataScope);

    /**
     * 获取boms列表
     */
    Page<PartsResult> findPageBySkuId(@Param("page") Page page, @Param("skuId") Long skuId);


    /**
     * 获取旧分页实体列表
     *
     * @author song
     * @Date 2021-10-21
     */
    Page<PartsResult> oldCustomPageList(@Param("page") Page page, @Param("paramCondition") PartsParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-21
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PartsParam paramCondition);


    List<Long> getTopSkuId();
}
