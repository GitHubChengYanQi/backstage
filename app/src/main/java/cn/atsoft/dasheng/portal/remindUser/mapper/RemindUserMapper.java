package cn.atsoft.dasheng.portal.remindUser.mapper;

import cn.atsoft.dasheng.portal.remindUser.entity.RemindUser;
import cn.atsoft.dasheng.portal.remindUser.model.params.RemindUserParam;
import cn.atsoft.dasheng.portal.remindUser.model.result.RemindUserResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author c
 * @since 2021-08-27
 */
public interface RemindUserMapper extends BaseMapper<RemindUser> {

    /**
     * 获取列表
     *
     * @author c
     * @Date 2021-08-27
     */
    List<RemindUserResult> customList(@Param("paramCondition") RemindUserParam paramCondition);

    /**
     * 获取map列表
     *
     * @author c
     * @Date 2021-08-27
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RemindUserParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author c
     * @Date 2021-08-27
     */
    Page<RemindUserResult> customPageList(@Param("page") Page page, @Param("paramCondition") RemindUserParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author c
     * @Date 2021-08-27
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RemindUserParam paramCondition);

}
