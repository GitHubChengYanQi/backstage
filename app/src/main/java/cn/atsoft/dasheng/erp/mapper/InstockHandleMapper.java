package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.InstockHandle;
import cn.atsoft.dasheng.erp.model.params.InstockHandleParam;
import cn.atsoft.dasheng.erp.model.result.InstockHandleResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 入库操作结果 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-07-08
 */
public interface InstockHandleMapper extends BaseMapper<InstockHandle> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-07-08
     */
    List<InstockHandleResult> customList(@Param("paramCondition") InstockHandleParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-07-08
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") InstockHandleParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-07-08
     */
    Page<InstockHandleResult> customPageList(@Param("page") Page page, @Param("paramCondition") InstockHandleParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-07-08
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") InstockHandleParam paramCondition);

}
