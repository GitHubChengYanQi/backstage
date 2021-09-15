package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.ApplyDetails;
import cn.atsoft.dasheng.erp.model.params.ApplyDetailsParam;
import cn.atsoft.dasheng.erp.model.result.ApplyDetailsResult;
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
 * @author song
 * @since 2021-09-15
 */
public interface ApplyDetailsMapper extends BaseMapper<ApplyDetails> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-09-15
     */
    List<ApplyDetailsResult> customList(@Param("paramCondition") ApplyDetailsParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-09-15
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ApplyDetailsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-09-15
     */
    Page<ApplyDetailsResult> customPageList(@Param("page") Page page, @Param("paramCondition") ApplyDetailsParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-09-15
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ApplyDetailsParam paramCondition);

}
