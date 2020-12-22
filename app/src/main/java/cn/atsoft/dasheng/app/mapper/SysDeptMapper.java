package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.SysDept;
import cn.atsoft.dasheng.app.model.params.SysDeptParam;
import cn.atsoft.dasheng.app.model.result.SysDeptResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2020-12-22
 */
public interface SysDeptMapper extends BaseMapper<SysDept> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2020-12-22
     */
    List<SysDeptResult> customList(@Param("paramCondition") SysDeptParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2020-12-22
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SysDeptParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2020-12-22
     */
    Page<SysDeptResult> customPageList(@Param("page") Page page, @Param("paramCondition") SysDeptParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2020-12-22
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SysDeptParam paramCondition);

}
