package cn.atsoft.dasheng.form.mapper;

import cn.atsoft.dasheng.form.entity.DocumentsAction;
import cn.atsoft.dasheng.form.model.params.DocumentsActionParam;
import cn.atsoft.dasheng.form.model.result.DocumentsActionResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 单据动作 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-04-28
 */
public interface DocumentsActionMapper extends BaseMapper<DocumentsAction> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-04-28
     */
    List<DocumentsActionResult> customList(@Param("paramCondition") DocumentsActionParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-04-28
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") DocumentsActionParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-04-28
     */
    Page<DocumentsActionResult> customPageList(@Param("page") Page page, @Param("paramCondition") DocumentsActionParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-04-28
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") DocumentsActionParam paramCondition);

}
