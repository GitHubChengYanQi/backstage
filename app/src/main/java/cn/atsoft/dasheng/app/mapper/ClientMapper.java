package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Client;
import cn.atsoft.dasheng.app.model.params.ClientParam;
import cn.atsoft.dasheng.app.model.result.ClientResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户管理表 Mapper 接口
 * </p>
 *
 * @author ta
 * @since 2021-07-19
 */
public interface ClientMapper extends BaseMapper<Client> {

    /**
     * 获取列表
     *
     * @author ta
     * @Date 2021-07-19
     */
    List<ClientResult> customList(@Param("paramCondition") ClientParam paramCondition);

    /**
     * 获取map列表
     *
     * @author ta
     * @Date 2021-07-19
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ClientParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author ta
     * @Date 2021-07-19
     */
    Page<ClientResult> customPageList(@Param("page") Page page, @Param("paramCondition") ClientParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author ta
     * @Date 2021-07-19
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ClientParam paramCondition);

}
