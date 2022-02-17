package cn.atsoft.dasheng.production.mapper;

import cn.atsoft.dasheng.production.entity.SopBind;
import cn.atsoft.dasheng.production.model.params.SopBindParam;
import cn.atsoft.dasheng.production.model.result.SopBindResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * sop 绑定 工序 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-02-17
 */
public interface SopBindMapper extends BaseMapper<SopBind> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-02-17
     */
    List<SopBindResult> customList(@Param("paramCondition") SopBindParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-02-17
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") SopBindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-02-17
     */
    Page<SopBindResult> customPageList(@Param("page") Page page, @Param("paramCondition") SopBindParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-02-17
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SopBindParam paramCondition);

}
