package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Phone;
import cn.atsoft.dasheng.app.model.params.PhoneParam;
import cn.atsoft.dasheng.app.model.result.PhoneResult;
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
 * @author cheng
 * @since 2021-08-12
 */
public interface PhoneMapper extends BaseMapper<Phone> {

    /**
     * 获取列表
     *
     * @author cheng
     * @Date 2021-08-12
     */
    List<PhoneResult> customList(@Param("paramCondition") PhoneParam paramCondition);

    /**
     * 获取map列表
     *
     * @author cheng
     * @Date 2021-08-12
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") PhoneParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author cheng
     * @Date 2021-08-12
     */
    Page<PhoneResult> customPageList(@Param("page") Page page, @Param("paramCondition") PhoneParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author cheng
     * @Date 2021-08-12
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PhoneParam paramCondition);

}
