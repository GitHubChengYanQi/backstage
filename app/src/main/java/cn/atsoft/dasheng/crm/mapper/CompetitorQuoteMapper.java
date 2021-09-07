package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.CompetitorQuote;
import cn.atsoft.dasheng.crm.model.params.CompetitorQuoteParam;
import cn.atsoft.dasheng.crm.model.result.CompetitorQuoteResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 竞争对手报价 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-09-07
 */
public interface CompetitorQuoteMapper extends BaseMapper<CompetitorQuote> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-09-07
     */
    List<CompetitorQuoteResult> customList(@Param("paramCondition") CompetitorQuoteParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-09-07
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CompetitorQuoteParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-09-07
     */
    Page<CompetitorQuoteResult> customPageList(@Param("page") Page page, @Param("paramCondition") CompetitorQuoteParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-09-07
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CompetitorQuoteParam paramCondition);

}
