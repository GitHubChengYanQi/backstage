package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Adress;
import cn.atsoft.dasheng.app.model.params.AdressParam;
import cn.atsoft.dasheng.app.model.result.AdressResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户地址表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-07-23
 */
public interface AdressMapper extends BaseMapper<Adress> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-07-23
     */
    List<AdressResult> customList(@Param("paramCondition") AdressParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-07-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") AdressParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-07-23
     */
    Page<AdressResult> customPageList(@Param("page") Page page, @Param("paramCondition") AdressParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-07-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") AdressParam paramCondition);

}
