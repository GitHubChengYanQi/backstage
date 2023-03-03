package cn.atsoft.dasheng.mapper;

import cn.atsoft.dasheng.entity.QrCode;
import cn.atsoft.dasheng.model.params.QrCodeParam;
import cn.atsoft.dasheng.model.result.QrCodeResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 二维码 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
public interface QrCodeMapper extends BaseMapper<QrCode> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-10-29
     */
    List<QrCodeResult> customList(@Param("paramCondition") QrCodeParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-10-29
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") QrCodeParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-10-29
     */
    Page<QrCodeResult> customPageList(@Param("page") Page page, @Param("paramCondition") QrCodeParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-10-29
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") QrCodeParam paramCondition);



}
