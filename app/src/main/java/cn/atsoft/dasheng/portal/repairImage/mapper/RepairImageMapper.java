package cn.atsoft.dasheng.portal.repairImage.mapper;

import cn.atsoft.dasheng.portal.repairImage.entity.RepairImage;
import cn.atsoft.dasheng.portal.repairImage.model.params.RepairImageParam;
import cn.atsoft.dasheng.portal.repairImage.model.result.RepairImageResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报修图片 Mapper 接口
 * </p>
 *
 * @author 1
 * @since 2021-09-01
 */
public interface RepairImageMapper extends BaseMapper<RepairImage> {

    /**
     * 获取列表
     *
     * @author 1
     * @Date 2021-09-01
     */
    List<RepairImageResult> customList(@Param("paramCondition") RepairImageParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 1
     * @Date 2021-09-01
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") RepairImageParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 1
     * @Date 2021-09-01
     */
    Page<RepairImageResult> customPageList(@Param("page") Page page, @Param("paramCondition") RepairImageParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 1
     * @Date 2021-09-01
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") RepairImageParam paramCondition);

}
