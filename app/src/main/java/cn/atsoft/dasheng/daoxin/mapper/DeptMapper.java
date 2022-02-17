package cn.atsoft.dasheng.daoxin.mapper;

import cn.atsoft.dasheng.daoxin.entity.Dept;
import cn.atsoft.dasheng.daoxin.model.params.DeptParam;
import cn.atsoft.dasheng.daoxin.model.result.DeptResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * daoxin部门表 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-02-17
 */
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    List<DeptResult> customList(@Param("paramCondition") DeptParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") DeptParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    Page<DeptResult> customPageList(@Param("page") Page page, @Param("paramCondition") DeptParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") DeptParam paramCondition);

}
