package cn.atsoft.dasheng.asyn.mapper;

import cn.atsoft.dasheng.asyn.entity.AsynTaskDetail;
import cn.atsoft.dasheng.asyn.model.params.AsynTaskDetailParam;
import cn.atsoft.dasheng.asyn.model.result.AsynTaskDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 任务详情表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-04-26
 */
public interface AsynTaskDetailMapper extends BaseMapper<AsynTaskDetail> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-04-26
     */
    List<AsynTaskDetailResult> customList(@Param("paramCondition") AsynTaskDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-04-26
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") AsynTaskDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-04-26
     */
    Page<AsynTaskDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") AsynTaskDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-04-26
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") AsynTaskDetailParam paramCondition);

}
