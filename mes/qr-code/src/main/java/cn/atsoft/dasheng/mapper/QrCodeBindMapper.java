package cn.atsoft.dasheng.mapper;

import cn.atsoft.dasheng.entity.QrCodeBind;
import cn.atsoft.dasheng.model.params.QrCodeBindParam;
import cn.atsoft.dasheng.model.result.QrCodeBindResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 二维码绑定 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
public interface QrCodeBindMapper extends BaseMapper<QrCodeBind> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-29
     */
    List<QrCodeBindResult> customList(@Param("paramCondition") QrCodeBindParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-29
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") QrCodeBindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-29
     */
    Page<QrCodeBindResult> customPageList(@Param("page") Page page, @Param("paramCondition") QrCodeBindParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-29
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") QrCodeBindParam paramCondition);

}
