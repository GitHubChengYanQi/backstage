package cn.atsoft.dasheng.view.mapper;

import cn.atsoft.dasheng.view.entity.TableView;
import cn.atsoft.dasheng.view.model.params.TableViewParam;
import cn.atsoft.dasheng.view.model.result.TableViewResult;
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
 * @author 
 * @since 2021-11-04
 */
public interface TableViewMapper extends BaseMapper<TableView> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-11-04
     */
    List<TableViewResult> customList(@Param("paramCondition") TableViewParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-11-04
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") TableViewParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-11-04
     */
    Page<TableViewResult> customPageList(@Param("page") Page page, @Param("paramCondition") TableViewParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-11-04
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") TableViewParam paramCondition);

}
