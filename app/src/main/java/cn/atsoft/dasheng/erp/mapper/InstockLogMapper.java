package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.InstockLog;
import cn.atsoft.dasheng.erp.model.params.InstockLogParam;
import cn.atsoft.dasheng.erp.model.result.InstockLogResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 入库记录表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-14
 */
public interface InstockLogMapper extends BaseMapper<InstockLog> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    List<InstockLogResult> customList(@Param("paramCondition") InstockLogParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") InstockLogParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    Page<InstockLogResult> customPageList(@Param("page") Page page, @Param("paramCondition") InstockLogParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") InstockLogParam paramCondition);

}
