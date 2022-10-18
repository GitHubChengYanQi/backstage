package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.InstockLogDetail;
import cn.atsoft.dasheng.erp.model.params.InstockLogDetailParam;
import cn.atsoft.dasheng.erp.model.result.InstockLogDetailResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-14
 */
public interface InstockLogDetailMapper extends BaseMapper<InstockLogDetail> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    List<InstockLogDetailResult> customList(@Param("paramCondition") InstockLogDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") InstockLogDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    Page<InstockLogDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") InstockLogDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") InstockLogDetailParam paramCondition);

    /**
     * 通过物料查询记录
     *
     * @param paramCondition
     * @return
     */
    List<InstockLogDetailResult> skuLogDetail(@Param("paramCondition") InstockLogDetailParam paramCondition);

}
