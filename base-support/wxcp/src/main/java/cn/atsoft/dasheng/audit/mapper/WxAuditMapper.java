package cn.atsoft.dasheng.audit.mapper;

import cn.atsoft.dasheng.audit.entity.WxAudit;
import cn.atsoft.dasheng.audit.model.params.WxAuditParam;
import cn.atsoft.dasheng.audit.model.result.WxAuditResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-18
 */
public interface WxAuditMapper extends BaseMapper<WxAudit> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    List<WxAuditResult> customList(@Param("paramCondition") WxAuditParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") WxAuditParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    Page<WxAuditResult> customPageList(@Param("page") Page page, @Param("paramCondition") WxAuditParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") WxAuditParam paramCondition);
    Long unpaidMoney(@Param("orderId") Long orderId);

}
