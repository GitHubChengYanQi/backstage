package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Message;
import cn.atsoft.dasheng.app.model.params.MessageParam;
import cn.atsoft.dasheng.app.model.result.MessageResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息提醒 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-03
 */
public interface MessageMapper extends BaseMapper<Message> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-08-03
     */
    List<MessageResult> customList(@Param("paramCondition") MessageParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-08-03
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") MessageParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-08-03
     */
    Page<MessageResult> customPageList(@Param("page") Page page, @Param("paramCondition") MessageParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-08-03
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") MessageParam paramCondition);

}
