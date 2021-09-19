package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.crm.entity.Data;
import cn.atsoft.dasheng.crm.model.params.DataParam;
import cn.atsoft.dasheng.crm.model.result.DataResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资料 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-09-11
 */
public interface DataMapper extends BaseMapper<Data> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-09-11
     */
    List<DataResult> customList(@Param("paramCondition") DataParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-09-11
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") DataParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-09-11
     */
    Page<DataResult> customPageList(@Param("dataScope") DataScope dataScope, @Param("page") Page page, @Param("paramCondition") DataParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-09-11
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") DataParam paramCondition);

}
