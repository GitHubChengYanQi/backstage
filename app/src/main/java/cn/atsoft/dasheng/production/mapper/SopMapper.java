package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.production.entity.Sop;
import cn.atsoft.dasheng.production.model.params.SopParam;
import cn.atsoft.dasheng.production.model.result.SopResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * sop 主表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
public interface SopMapper extends BaseMapper<Sop> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-02-10
     */
    List<SopResult> customList(@Param("paramCondition") SopParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-02-10
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SopParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-02-10
     */
    Page<SopResult> customPageList(@Param("page") Page page, @Param("paramCondition") SopParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-02-10
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SopParam paramCondition);

}
