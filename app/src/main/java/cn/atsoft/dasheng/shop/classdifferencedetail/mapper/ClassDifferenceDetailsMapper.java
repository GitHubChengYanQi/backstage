package cn.atsoft.dasheng.shop.classdifferencedetail.mapper;

import cn.atsoft.dasheng.shop.classdifferencedetail.entity.ClassDifferenceDetails;
import cn.atsoft.dasheng.shop.classdifferencedetail.model.params.ClassDifferenceDetailsParam;
import cn.atsoft.dasheng.shop.classdifferencedetail.model.result.ClassDifferenceDetailsResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 分类明细内容 Mapper 接口
 * </p>
 *
 * @author siqiang
 * @since 2021-08-18
 */
public interface ClassDifferenceDetailsMapper extends BaseMapper<ClassDifferenceDetails> {

    /**
     * 获取列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    List<ClassDifferenceDetailsResult> customList(@Param("paramCondition") ClassDifferenceDetailsParam paramCondition);

    /**
     * 获取map列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ClassDifferenceDetailsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    Page<ClassDifferenceDetailsResult> customPageList(@Param("page") Page page, @Param("paramCondition") ClassDifferenceDetailsParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ClassDifferenceDetailsParam paramCondition);

}
