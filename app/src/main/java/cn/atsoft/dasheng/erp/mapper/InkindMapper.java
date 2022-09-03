package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.model.params.InkindParam;
import cn.atsoft.dasheng.erp.model.result.InkindResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 实物表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-11-01
 */
public interface InkindMapper extends BaseMapper<Inkind> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-11-01
     */
    List<InkindResult> customList(@Param("paramCondition") InkindParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-11-01
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") InkindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-11-01
     */
    Page<InkindResult> customPageList(@Param("page") Page page, @Param("paramCondition") InkindParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-11-01
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") InkindParam paramCondition);


    /**
     * 库存实物
     *
     * @author song
     * @Date 2021-11-01
     */
    Page<InkindResult> stockInkindList(@Param("page") Page page, @Param("paramCondition") InkindParam paramCondition);

}
