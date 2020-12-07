package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Info;
import cn.atsoft.dasheng.app.model.params.InfoParam;
import cn.atsoft.dasheng.app.model.result.InfoResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据库信息表 Mapper 接口
 * </p>
 *
 * @author sing
 * @since 2020-12-07
 */
public interface InfoMapper extends BaseMapper<Info> {

    /**
     * 获取列表
     *
     * @author sing
     * @Date 2020-12-07
     */
    List<InfoResult> customList(@Param("paramCondition") InfoParam paramCondition);

    /**
     * 获取map列表
     *
     * @author sing
     * @Date 2020-12-07
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") InfoParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author sing
     * @Date 2020-12-07
     */
    Page<InfoResult> customPageList(@Param("page") Page page, @Param("paramCondition") InfoParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author sing
     * @Date 2020-12-07
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") InfoParam paramCondition);

}
