package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Contacts;
import cn.atsoft.dasheng.app.model.params.ContactsParam;
import cn.atsoft.dasheng.app.model.result.ContactsResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.crm.entity.Data;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.jna.platform.win32.WinDef;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 联系人表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2021-07-23
 */
public interface ContactsMapper extends BaseMapper<Contacts> {

    /**
     * 获取列表
     *
     * @author
     * @Date 2021-07-23
     */
    List<ContactsResult> customList(@Param("paramCondition") ContactsParam paramCondition);

    /**
     * 通过绑定表查询联系人id
     *
     * @param id
     * @return
     */
    List<Long> queryContactsId(Long id);

    /**
     * 获取map列表
     *
     * @author
     * @Date 2021-07-23
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ContactsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author
     * @Date 2021-07-23
     */
    Page<ContactsResult> customPageList(@Param("dataScope") DataScope dataScope, @Param("page") Page page, @Param("paramCondition") ContactsParam paramCondition, @Param("ids") List<Long> ids);

    /**
     * 获取分页map列表
     *
     * @author
     * @Date 2021-07-23
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ContactsParam paramCondition);

}
