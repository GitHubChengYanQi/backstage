package cn.atsoft.dasheng.miniapp.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.miniapp.entity.WxMaMessage;
import cn.atsoft.dasheng.miniapp.model.params.WxMaMessageParam;
import cn.atsoft.dasheng.miniapp.model.result.WxMaMessageResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 小程序订阅消息 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-11
 */
public interface WxMaMessageMapper extends BaseMapper<WxMaMessage> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-11
     */
    List<WxMaMessageResult> customList(@Param("paramCondition") WxMaMessageParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-11
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") WxMaMessageParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-11
     */
    Page<WxMaMessageResult> customPageList(@Param("page") Page page, @Param("paramCondition") WxMaMessageParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-11
     */
    Page<WxMaMessageResult> customPageList(@Param("page") Page page, @Param("paramCondition") WxMaMessageParam paramCondition,@Param("dataScope")DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-11
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") WxMaMessageParam paramCondition);

}
