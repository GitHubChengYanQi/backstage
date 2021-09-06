package cn.atsoft.dasheng.binding.wxUser.mapper;

import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.model.params.WxuserInfoParam;
import cn.atsoft.dasheng.binding.wxUser.model.result.WxuserInfoResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户 小程序 关联 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-24
 */
public interface WxuserInfoMapper extends BaseMapper<WxuserInfo> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2021-08-24
     */
    List<WxuserInfoResult> customList(@Param("paramCondition") WxuserInfoParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2021-08-24
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") WxuserInfoParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2021-08-24
     */
    Page<WxuserInfoResult> customPageList(@Param("page") Page page, @Param("paramCondition") WxuserInfoParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2021-08-24
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") WxuserInfoParam paramCondition);

}
