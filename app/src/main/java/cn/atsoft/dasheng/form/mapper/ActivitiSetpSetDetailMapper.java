package cn.atsoft.dasheng.form.mapper;

import cn.atsoft.dasheng.form.entity.ActivitiSetpSetDetail;
import cn.atsoft.dasheng.form.model.params.ActivitiSetpSetDetailParam;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工序步骤详情表 Mapper 接口
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
public interface ActivitiSetpSetDetailMapper extends BaseMapper<ActivitiSetpSetDetail> {

    /**
     * 获取列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<ActivitiSetpSetDetailResult> customList(@Param("paramCondition") ActivitiSetpSetDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ActivitiSetpSetDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    Page<ActivitiSetpSetDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") ActivitiSetpSetDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ActivitiSetpSetDetailParam paramCondition);

}
