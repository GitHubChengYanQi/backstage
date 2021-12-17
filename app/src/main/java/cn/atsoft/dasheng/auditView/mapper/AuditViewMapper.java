package cn.atsoft.dasheng.auditView.mapper;

import cn.atsoft.dasheng.auditView.entity.AuditView;
import cn.atsoft.dasheng.auditView.model.params.AuditViewParam;
import cn.atsoft.dasheng.auditView.model.result.AuditViewResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 所有审核 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-12-16
 */
public interface AuditViewMapper extends BaseMapper<AuditView> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-12-16
     */
    List<AuditViewResult> customList(@Param("paramCondition") AuditViewParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-12-16
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") AuditViewParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-12-16
     */
    Page<AuditViewResult> customPageList(@Param("page") Page page, @Param("paramCondition") AuditViewParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-12-16
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") AuditViewParam paramCondition);

}
