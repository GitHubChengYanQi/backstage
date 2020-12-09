package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.SysLoginLog;
import cn.atsoft.dasheng.app.model.params.SysLoginLogParam;
import cn.atsoft.dasheng.app.model.result.SysLoginLogResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 登录记录 Mapper 接口
 * </p>
 *
 * @author sing
 * @since 2020-12-09
 */
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

    /**
     * 获取列表
     *
     * @author sing
     * @Date 2020-12-09
     */
    List<SysLoginLogResult> customList(@Param("paramCondition") SysLoginLogParam paramCondition);

    /**
     * 获取map列表
     *
     * @author sing
     * @Date 2020-12-09
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SysLoginLogParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author sing
     * @Date 2020-12-09
     */
    Page<SysLoginLogResult> customPageList(@Param("page") Page page, @Param("paramCondition") SysLoginLogParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author sing
     * @Date 2020-12-09
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SysLoginLogParam paramCondition);

}
