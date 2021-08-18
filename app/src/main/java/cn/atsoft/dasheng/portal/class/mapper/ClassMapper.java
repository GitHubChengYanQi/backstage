package cn.atsoft.dasheng.portal.class.mapper;

import cn.atsoft.dasheng.portal.class.entity.Class;
import cn.atsoft.dasheng.portal.class.model.params.ClassParam;
import cn.atsoft.dasheng.portal.class.model.result.ClassResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 分类导航 Mapper 接口
 * </p>
 *
 * @author siqiang
 * @since 2021-08-18
 */
public interface ClassMapper extends BaseMapper<Class> {

    /**
     * 获取列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    List<ClassResult> customList(@Param("paramCondition") ClassParam paramCondition);

    /**
     * 获取map列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ClassParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    Page<ClassResult> customPageList(@Param("page") Page page, @Param("paramCondition") ClassParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ClassParam paramCondition);

}
