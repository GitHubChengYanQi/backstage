package cn.atsoft.dasheng.portal.remind.mapper;

import cn.atsoft.dasheng.portal.remind.entity.Remind;
import cn.atsoft.dasheng.portal.remind.model.params.RemindParam;
import cn.atsoft.dasheng.portal.remind.model.result.RemindResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 提醒表 Mapper 接口
 * </p>
 *
 * @author cheng
 * @since 2021-08-26
 */
public interface RemindMapper extends BaseMapper<Remind> {

    /**
     * 获取列表
     *
     * @author cheng
     * @Date 2021-08-26
     */
    List<RemindResult> customList(@Param("paramCondition") RemindParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cheng
     * @Date 2021-08-26
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RemindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cheng
     * @Date 2021-08-26
     */
    Page<RemindResult> customPageList(@Param("page") Page page, @Param("paramCondition") RemindParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author cheng
     * @Date 2021-08-26
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RemindParam paramCondition);

}
