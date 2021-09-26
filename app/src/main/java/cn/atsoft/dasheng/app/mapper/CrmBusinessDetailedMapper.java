package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.CrmBusinessDetailed;
import cn.atsoft.dasheng.app.model.params.CrmBusinessDetailedParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessDetailedResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商机明细表 Mapper 接口
 * </p>
 *
 * @author qr
 * @since 2021-08-04
 */
public interface CrmBusinessDetailedMapper extends BaseMapper<CrmBusinessDetailed> {

    /**
     * 获取列表
     *
     * @author qr
     * @Date 2021-08-04
     */
    List<CrmBusinessDetailedResult> customList(@Param("paramCondition") CrmBusinessDetailedParam paramCondition);

    /**
     * 获取map列表
     *
     * @author qr
     * @Date 2021-08-04
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CrmBusinessDetailedParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author qr
     * @Date 2021-08-04
     */
    Page<CrmBusinessDetailedResult> customPageList(@Param("page") Page page, @Param("paramCondition") CrmBusinessDetailedParam paramCondition,@Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author qr
     * @Date 2021-08-04
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CrmBusinessDetailedParam paramCondition);

}
