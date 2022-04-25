package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.DocumentsStatus;
import cn.atsoft.dasheng.crm.model.params.DocumentsStatusParam;
import cn.atsoft.dasheng.crm.model.result.DocumentsStatusResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 单据状态 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-04-23
 */
public interface DocumentsStatusMapper extends BaseMapper<DocumentsStatus> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-04-23
     */
    List<DocumentsStatusResult> customList(@Param("paramCondition") DocumentsStatusParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-04-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") DocumentsStatusParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-04-23
     */
    Page<DocumentsStatusResult> customPageList(@Param("page") Page page, @Param("paramCondition") DocumentsStatusParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-04-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") DocumentsStatusParam paramCondition);

}
