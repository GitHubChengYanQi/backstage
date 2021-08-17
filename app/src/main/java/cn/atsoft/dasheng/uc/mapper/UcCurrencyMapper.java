package cn.atsoft.dasheng.uc.mapper;

import cn.atsoft.dasheng.uc.entity.UcCurrency;
import cn.atsoft.dasheng.uc.model.params.UcCurrencyParam;
import cn.atsoft.dasheng.uc.model.result.UcCurrencyResult;
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
 * @since 2021-03-22
 */
public interface UcCurrencyMapper extends BaseMapper<UcCurrency> {

    /**
     * 获取列表
     *
     * @author Sing
     * @Date 2021-03-22
     */
    List<UcCurrencyResult> customList(@Param("paramCondition") UcCurrencyParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Sing
     * @Date 2021-03-22
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") UcCurrencyParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Sing
     * @Date 2021-03-22
     */
    Page<UcCurrencyResult> customPageList(@Param("page") Page page, @Param("paramCondition") UcCurrencyParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Sing
     * @Date 2021-03-22
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") UcCurrencyParam paramCondition);

}
