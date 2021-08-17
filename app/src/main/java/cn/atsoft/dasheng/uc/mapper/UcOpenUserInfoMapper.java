package cn.atsoft.dasheng.uc.mapper;

import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.model.params.UcOpenUserInfoParam;
import cn.atsoft.dasheng.uc.model.result.UcOpenUserInfoResult;
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
 * @author Sing
 * @since 2021-03-17
 */
public interface UcOpenUserInfoMapper extends BaseMapper<UcOpenUserInfo> {

    /**
     * 获取列表
     *
     * @author Sing
     * @Date 2021-03-17
     */
    List<UcOpenUserInfoResult> customList(@Param("paramCondition") UcOpenUserInfoParam paramCondition);
    UcOpenUserInfoResult customListFindOne(@Param("paramCondition") UcOpenUserInfoParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Sing
     * @Date 2021-03-17
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") UcOpenUserInfoParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Sing
     * @Date 2021-03-17
     */
    Page<UcOpenUserInfoResult> customPageList(@Param("page") Page page, @Param("paramCondition") UcOpenUserInfoParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Sing
     * @Date 2021-03-17
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") UcOpenUserInfoParam paramCondition);

}
