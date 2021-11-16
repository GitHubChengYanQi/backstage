package cn.atsoft.dasheng.form.mapper;

import cn.atsoft.dasheng.form.entity.Data;
import cn.atsoft.dasheng.form.model.params.DataParam;
import cn.atsoft.dasheng.form.model.result.DataResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自定义表单主表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-11-16
 */
public interface DataMapper extends BaseMapper<Data> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-11-16
     */
    List<DataResult> customList(@Param("paramCondition") DataParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-11-16
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") DataParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-11-16
     */
    Page<DataResult> customPageList(@Param("page") Page page, @Param("paramCondition") DataParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-11-16
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") DataParam paramCondition);

}
