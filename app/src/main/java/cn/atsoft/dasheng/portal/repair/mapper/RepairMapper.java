package cn.atsoft.dasheng.portal.repair.mapper;

import cn.atsoft.dasheng.portal.repair.entity.Repair;
import cn.atsoft.dasheng.portal.repair.model.params.RepairParam;
import cn.atsoft.dasheng.portal.repair.model.result.RepairResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报修 Mapper 接口
 * </p>
 *
 * @author siqiang
 * @since 2021-08-20
 */
public interface RepairMapper extends BaseMapper<Repair> {

    /**
     * 获取列表
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    List<RepairResult> customList(@Param("paramCondition") RepairParam paramCondition);

    /**
     * 获取map列表
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RepairParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    Page<RepairResult> customPageList(@Param("page") Page page, @Param("paramCondition") RepairParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RepairParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    Page<RepairResult> customMyPageList(@Param("page") Page page, @Param("paramCondition") RepairParam paramCondition);



}
