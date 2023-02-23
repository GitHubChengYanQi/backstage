package cn.atsoft.dasheng.uc.service;

import cn.atsoft.dasheng.uc.entity.UcMember;
import cn.atsoft.dasheng.uc.model.params.UcMemberParam;
import cn.atsoft.dasheng.uc.model.result.UcMemberResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Sing
 * @since 2021-03-16
 */
public interface UcMemberService extends IService<UcMember> {

    /**
     * 新增
     *
     * @author Sing
     * @Date 2021-03-16
     */
    void add(UcMemberParam param);

    /**
     * 删除
     *
     * @author Sing
     * @Date 2021-03-16
     */
    void delete(UcMemberParam param);

    /**
     * 更新
     *
     * @author Sing
     * @Date 2021-03-16
     */
    void update(UcMemberParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Sing
     * @Date 2021-03-16
     */
    UcMemberResult findBySpec(UcMemberParam param);
    UcMemberResult findOne(UcMemberParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Sing
     * @Date 2021-03-16
     */
    List<UcMemberResult> findListBySpec(UcMemberParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Sing
     * @Date 2021-03-16
     */
     PageInfo findPageBySpec(UcMemberParam param);

    UcMember getByMemberId(Long memberId);
}
