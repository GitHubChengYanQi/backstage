package cn.atsoft.dasheng.uc.mapper;

import cn.atsoft.dasheng.uc.entity.UcSmsCode;
import cn.atsoft.dasheng.uc.model.params.UcSmsCodeParam;
import cn.atsoft.dasheng.uc.model.result.UcSmsCodeResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  -  Mapper 接口
 * </p>
 *
 * @author Sing
 * @since 2021-03-16
 */
public interface UcSmsCodeMapper extends BaseMapper<UcSmsCode> {

    /**
     * 获取列表
     *
     * @author Sing
     * @Date 2021-03-16
     */
    List<UcSmsCodeResult> customList(@Param("paramCondition") UcSmsCodeParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Sing
     * @Date 2021-03-16
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") UcSmsCodeParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Sing
     * @Date 2021-03-16
     */
    Page<UcSmsCodeResult> customPageList(@Param("page") Page page, @Param("paramCondition") UcSmsCodeParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Sing
     * @Date 2021-03-16
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") UcSmsCodeParam paramCondition);

}
