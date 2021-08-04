package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.CrmBusiness;
import cn.atsoft.dasheng.app.model.params.CrmBusinessParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商机表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-03
 */
public interface CrmBusinessMapper extends BaseMapper<CrmBusiness> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-08-03
     */
    List<CrmBusinessResult> customList(@Param("paramCondition") CrmBusinessParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-08-03
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CrmBusinessParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-08-03
     */
    Page<CrmBusinessResult> customPageList(@Param("page") Page page, @Param("paramCondition") CrmBusinessParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-08-03
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CrmBusinessParam paramCondition);

}
