package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.AnomalyBind;
import cn.atsoft.dasheng.erp.model.params.AnomalyBindParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyBindResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 异常生成的实物 绑定 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-05-28
 */
public interface AnomalyBindMapper extends BaseMapper<AnomalyBind> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-05-28
     */
    List<AnomalyBindResult> customList(@Param("paramCondition") AnomalyBindParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-05-28
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") AnomalyBindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-05-28
     */
    Page<AnomalyBindResult> customPageList(@Param("page") Page page, @Param("paramCondition") AnomalyBindParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-05-28
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") AnomalyBindParam paramCondition);

}
