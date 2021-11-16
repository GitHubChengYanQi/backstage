package cn.atsoft.dasheng.form.mapper;

import cn.atsoft.dasheng.form.entity.DataValue;
import cn.atsoft.dasheng.form.model.params.DataValueParam;
import cn.atsoft.dasheng.form.model.result.DataValueResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自定义表单各个字段数据	 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-11-16
 */
public interface DataValueMapper extends BaseMapper<DataValue> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-11-16
     */
    List<DataValueResult> customList(@Param("paramCondition") DataValueParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-11-16
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") DataValueParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-11-16
     */
    Page<DataValueResult> customPageList(@Param("page") Page page, @Param("paramCondition") DataValueParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-11-16
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") DataValueParam paramCondition);

}
