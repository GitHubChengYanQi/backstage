package cn.atsoft.dasheng.view.mapper;

import cn.atsoft.dasheng.view.entity.MobelTableView;
import cn.atsoft.dasheng.view.model.params.MobelTableViewParam;
import cn.atsoft.dasheng.view.model.result.MobelTableViewResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 移动端菜单 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-05-09
 */
public interface MobelTableViewMapper extends BaseMapper<MobelTableView> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-05-09
     */
    List<MobelTableViewResult> customList(@Param("paramCondition") MobelTableViewParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-05-09
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") MobelTableViewParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-05-09
     */
    Page<MobelTableViewResult> customPageList(@Param("page") Page page, @Param("paramCondition") MobelTableViewParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-05-09
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") MobelTableViewParam paramCondition);

}
