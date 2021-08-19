package cn.atsoft.dasheng.shop.classpage.mapper;

import cn.atsoft.dasheng.shop.classpage.entity.Classpojo;
import cn.atsoft.dasheng.shop.classpage.model.params.ClassParam;
import cn.atsoft.dasheng.shop.classpage.model.result.ClassResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 一级分类导航 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-19
 */
public interface ClassMapper extends BaseMapper<Classpojo> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-08-19
     */
    List<ClassResult> customList(@Param("paramCondition") ClassParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-08-19
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ClassParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-08-19
     */
    Page<ClassResult> customPageList(@Param("page") Page page, @Param("paramCondition") ClassParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-08-19
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ClassParam paramCondition);

}
