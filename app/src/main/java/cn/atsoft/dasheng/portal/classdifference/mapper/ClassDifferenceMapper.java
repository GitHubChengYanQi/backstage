package cn.atsoft.dasheng.portal.classdifference.mapper;

import cn.atsoft.dasheng.portal.classdifference.entity.ClassDifference;
import cn.atsoft.dasheng.portal.classdifference.model.params.ClassDifferenceParam;
import cn.atsoft.dasheng.portal.classdifference.model.result.ClassDifferenceResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 分类明细 Mapper 接口
 * </p>
 *
 * @author siqiang
 * @since 2021-08-18
 */
public interface ClassDifferenceMapper extends BaseMapper<ClassDifference> {

    /**
     * 获取列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    List<ClassDifferenceResult> customList(@Param("paramCondition") ClassDifferenceParam paramCondition);

    /**
     * 获取map列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ClassDifferenceParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    Page<ClassDifferenceResult> customPageList(@Param("page") Page page, @Param("paramCondition") ClassDifferenceParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ClassDifferenceParam paramCondition);

}
