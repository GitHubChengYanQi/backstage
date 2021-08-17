package cn.atsoft.dasheng.uc.mapper;

import cn.atsoft.dasheng.uc.entity.UcMember;
import cn.atsoft.dasheng.uc.model.params.UcMemberParam;
import cn.atsoft.dasheng.uc.model.result.UcMemberResult;
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
 * @since 2021-03-16
 */
public interface UcMemberMapper extends BaseMapper<UcMember> {

    /**
     * 获取列表
     *
     * @author Sing
     * @Date 2021-03-16
     */
    List<UcMemberResult> customList(@Param("paramCondition") UcMemberParam paramCondition);
    UcMemberResult customListByOne(@Param("paramCondition") UcMemberParam paramCondition);
    /**
     * 获取map列表
     *
     * @author Sing
     * @Date 2021-03-16
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") UcMemberParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Sing
     * @Date 2021-03-16
     */
    Page<UcMemberResult> customPageList(@Param("page") Page page, @Param("paramCondition") UcMemberParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Sing
     * @Date 2021-03-16
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") UcMemberParam paramCondition);

}
