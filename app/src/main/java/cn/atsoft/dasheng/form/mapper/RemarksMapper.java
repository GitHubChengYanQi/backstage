package cn.atsoft.dasheng.form.mapper;

import cn.atsoft.dasheng.form.entity.Remarks;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.form.model.result.RemarksResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * log备注 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-12-16
 */
public interface RemarksMapper extends BaseMapper<Remarks> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-12-16
     */
    List<RemarksResult> customList(@Param("paramCondition") RemarksParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-12-16
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RemarksParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-12-16
     */
    Page<RemarksResult> customPageList(@Param("page") Page page, @Param("paramCondition") RemarksParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-12-16
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RemarksParam paramCondition);

}
