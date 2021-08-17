package cn.atsoft.dasheng.uc.mapper;

import cn.atsoft.dasheng.uc.entity.UcPayOrder;
import cn.atsoft.dasheng.uc.model.params.UcPayOrderParam;
import cn.atsoft.dasheng.uc.model.result.UcPayOrderResult;
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
 * @author Sing
 * @since 2021-03-21
 */
public interface UcPayOrderMapper extends BaseMapper<UcPayOrder> {

    /**
     * 获取列表
     *
     * @author Sing
     * @Date 2021-03-21
     */
    List<UcPayOrderResult> customList(@Param("paramCondition") UcPayOrderParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Sing
     * @Date 2021-03-21
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") UcPayOrderParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Sing
     * @Date 2021-03-21
     */
    Page<UcPayOrderResult> customPageList(@Param("page") Page page, @Param("paramCondition") UcPayOrderParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Sing
     * @Date 2021-03-21
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") UcPayOrderParam paramCondition);

}
