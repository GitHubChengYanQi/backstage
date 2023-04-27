package cn.atsoft.dasheng.miniapp.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.miniapp.entity.WxMaConfig;
import cn.atsoft.dasheng.miniapp.model.params.WxMaConfigParam;
import cn.atsoft.dasheng.miniapp.model.result.WxMaConfigResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 微信小程序配置表（对应租户） Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-25
 */
public interface WxMaConfigMapper extends BaseMapper<WxMaConfig> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-25
     */
    List<WxMaConfigResult> customList(@Param("paramCondition") WxMaConfigParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-25
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") WxMaConfigParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-25
     */
    Page<WxMaConfigResult> customPageList(@Param("page") Page page, @Param("paramCondition") WxMaConfigParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-25
     */
    Page<WxMaConfigResult> customPageList(@Param("page") Page page, @Param("paramCondition") WxMaConfigParam paramCondition,@Param("dataScope")DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-25
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") WxMaConfigParam paramCondition);

}
