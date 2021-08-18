package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.ErpPackageTable;
import cn.atsoft.dasheng.app.model.params.ErpPackageTableParam;
import cn.atsoft.dasheng.app.model.result.ErpPackageTableResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 套餐分表 Mapper 接口
 * </p>
 *
 * @author qr
 * @since 2021-08-04
 */
public interface ErpPackageTableMapper extends BaseMapper<ErpPackageTable> {

    /**
     * 获取列表
     *
     * @author qr
     * @Date 2021-08-04
     */
    List<ErpPackageTableResult> customList(@Param("paramCondition") ErpPackageTableParam paramCondition);

    /**
     * 获取map列表
     *
     * @author qr
     * @Date 2021-08-04
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ErpPackageTableParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author qr
     * @Date 2021-08-04
     */
    Page<ErpPackageTableResult> customPageList(@Param("page") Page page, @Param("paramCondition") ErpPackageTableParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author qr
     * @Date 2021-08-04
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ErpPackageTableParam paramCondition);

}
