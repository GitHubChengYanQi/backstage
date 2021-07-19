package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Business;
import cn.atsoft.dasheng.app.model.params.BusinessParam;
import cn.atsoft.dasheng.app.model.result.BusinessResult;
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
 * @author cheng
 * @since 2021-07-19
 */
public interface BusinessMapper extends BaseMapper<Business> {

    /**
     * 获取列表
     *
     * @author cheng
     * @Date 2021-07-19
     */
    List<BusinessResult> customList(@Param("paramCondition") BusinessParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cheng
     * @Date 2021-07-19
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") BusinessParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cheng
     * @Date 2021-07-19
     */
    Page<BusinessResult> customPageList(@Param("page") Page page, @Param("paramCondition") BusinessParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author cheng
     * @Date 2021-07-19
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") BusinessParam paramCondition);

}
