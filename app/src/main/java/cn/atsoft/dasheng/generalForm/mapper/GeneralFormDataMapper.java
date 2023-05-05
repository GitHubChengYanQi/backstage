package cn.atsoft.dasheng.generalForm.mapper;

import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.generalForm.entity.GeneralFormData;
import cn.atsoft.dasheng.generalForm.model.params.GeneralFormDataParam;
import cn.atsoft.dasheng.generalForm.model.result.GeneralFormDataResult;
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
 * @author Captain_Jazz
 * @since 2022-09-08
 */
public interface GeneralFormDataMapper extends BaseMapper<GeneralFormData> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    List<GeneralFormDataResult> customList(@Param("paramCondition") GeneralFormDataParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") GeneralFormDataParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    Page<GeneralFormDataResult> customPageList(@Param("page") Page page, @Param("paramCondition") GeneralFormDataParam paramCondition,@Param("dataScope") DataScope dataScope);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2022-09-08
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") GeneralFormDataParam paramCondition);

}
