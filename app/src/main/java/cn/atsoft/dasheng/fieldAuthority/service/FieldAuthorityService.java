package cn.atsoft.dasheng.fieldAuthority.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.fieldAuthority.entity.FieldAuthority;
import cn.atsoft.dasheng.fieldAuthority.model.params.FieldAuthorityParam;
import cn.atsoft.dasheng.fieldAuthority.model.result.FieldAuthorityResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字段操作权限表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-05-17
 */
public interface FieldAuthorityService extends IService<FieldAuthority> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-05-17
     */
    void add(FieldAuthorityParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-05-17
     */
    void delete(FieldAuthorityParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-05-17
     */
    void update(FieldAuthorityParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-05-17
     */
    FieldAuthorityResult findBySpec(FieldAuthorityParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-05-17
     */
    List<FieldAuthorityResult> findListBySpec(FieldAuthorityParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-05-17
     */
     PageInfo<FieldAuthorityResult> findPageBySpec(FieldAuthorityParam param);

}
