package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.production.entity.SopDetail;
import cn.atsoft.dasheng.production.model.params.SopDetailParam;
import cn.atsoft.dasheng.production.model.result.SopDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * sop 详情 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
public interface SopDetailMapper extends BaseMapper<SopDetail> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-02-10
     */
    List<SopDetailResult> customList(@Param("paramCondition") SopDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-02-10
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SopDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-02-10
     */
    Page<SopDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") SopDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-02-10
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SopDetailParam paramCondition);

}
