package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.ContactsBind;
import cn.atsoft.dasheng.crm.model.params.ContactsBindParam;
import cn.atsoft.dasheng.crm.model.result.ContactsBindResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 联系人绑定表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2021-09-22
 */
public interface ContactsBindMapper extends BaseMapper<ContactsBind> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2021-09-22
     */
    List<ContactsBindResult> customList(@Param("paramCondition") ContactsBindParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2021-09-22
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ContactsBindParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2021-09-22
     */
    Page<ContactsBindResult> customPageList(@Param("page") Page page, @Param("paramCondition") ContactsBindParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2021-09-22
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ContactsBindParam paramCondition);

}
