package cn.atsoft.dasheng.api.uc.mapper;

import cn.atsoft.dasheng.api.uc.entity.OpenUserInfo;
import cn.atsoft.dasheng.api.uc.model.params.OpenUserInfoParam;
import cn.atsoft.dasheng.api.uc.model.result.OpenUserInfoResult;
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
 * @since 2021-08-25
 */
public interface OpenUserInfoMapper extends BaseMapper<OpenUserInfo> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-08-25
     */
    List<OpenUserInfoResult> customList(@Param("paramCondition") OpenUserInfoParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-08-25
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") OpenUserInfoParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-08-25
     */
    Page<OpenUserInfoResult> customPageList(@Param("page") Page page, @Param("paramCondition") OpenUserInfoParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-08-25
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") OpenUserInfoParam paramCondition);

}
