package cn.atsoft.dasheng.wedrive.space.mapper;

import cn.atsoft.dasheng.wedrive.space.entity.WxWedriveSpace;
import cn.atsoft.dasheng.wedrive.space.model.params.WxWedriveSpaceParam;
import cn.atsoft.dasheng.wedrive.space.model.result.WxWedriveSpaceResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 微信微盘空间 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-31
 */
public interface WxWedriveSpaceMapper extends BaseMapper<WxWedriveSpace> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-31
     */
    List<WxWedriveSpaceResult> customList(@Param("paramCondition") WxWedriveSpaceParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-31
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") WxWedriveSpaceParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-31
     */
    Page<WxWedriveSpaceResult> customPageList(@Param("page") Page page, @Param("paramCondition") WxWedriveSpaceParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-31
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") WxWedriveSpaceParam paramCondition);

}
