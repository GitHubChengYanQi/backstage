package cn.atsoft.dasheng.supplier.mapper;

import cn.atsoft.dasheng.supplier.entity.SupplierBlacklist;
import cn.atsoft.dasheng.supplier.model.params.SupplierBlacklistParam;
import cn.atsoft.dasheng.supplier.model.result.SupplierBlacklistResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 供应商黑名单 Mapper 接口
 * </p>
 *
 * @author Captian_Jazz
 * @since 2021-12-20
 */
public interface SupplierBlacklistMapper extends BaseMapper<SupplierBlacklist> {

    /**
     * 获取列表
     *
     * @author Captian_Jazz
     * @Date 2021-12-20
     */
    List<SupplierBlacklistResult> customList(@Param("paramCondition") SupplierBlacklistParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captian_Jazz
     * @Date 2021-12-20
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SupplierBlacklistParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captian_Jazz
     * @Date 2021-12-20
     */
    Page<SupplierBlacklistResult> customPageList(@Param("page") Page page, @Param("paramCondition") SupplierBlacklistParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captian_Jazz
     * @Date 2021-12-20
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SupplierBlacklistParam paramCondition);

}
