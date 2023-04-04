package cn.atsoft.dasheng.wedrive.file.mapper;

import cn.atsoft.dasheng.wedrive.file.entity.WedriveFile;
import cn.atsoft.dasheng.wedrive.file.model.params.WedriveFileParam;
import cn.atsoft.dasheng.wedrive.file.model.result.WedriveFileResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 微信微盘文件管理 Mapper 接口
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-01
 */
public interface WedriveFileMapper extends BaseMapper<WedriveFile> {

    /**
     * 获取列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
    List<WedriveFileResult> customList(@Param("paramCondition") WedriveFileParam paramCondition);

    /**
     * 获取map列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") WedriveFileParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
    Page<WedriveFileResult> customPageList(@Param("page") Page page, @Param("paramCondition") WedriveFileParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") WedriveFileParam paramCondition);

}
