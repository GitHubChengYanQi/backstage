package cn.atsoft.dasheng.erp.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.erp.entity.Spu;
import cn.atsoft.dasheng.erp.model.params.SpuParam;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
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
 * @author 
 * @since 2021-10-18
 */
public interface SpuMapper extends BaseMapper<Spu> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-10-18
     */
    List<SpuResult> customList(@Param("paramCondition") SpuParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-10-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SpuParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-10-18
     */
    Page<SpuResult> customPageList(@Param("page") Page page, @Param("paramCondition") SpuParam paramCondition,@Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-10-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SpuParam paramCondition);

}
