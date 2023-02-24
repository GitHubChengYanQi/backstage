package cn.atsoft.dasheng.binding.wxUser.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.model.params.WxuserInfoParam;
import cn.atsoft.dasheng.binding.wxUser.model.result.WxuserInfoResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户 小程序 关联 服务类
 * </p>
 *
 * @author
 * @since 2021-08-24
 */
public interface WxuserInfoService extends IService<WxuserInfo> {

    /**
     * 新增
     *
     * @author
     * @Date 2021-08-24
     */
    void add(WxuserInfoParam param);

    /**
     * 删除
     *
     * @author
     * @Date 2021-08-24
     */
    void delete(WxuserInfoParam param);

    /**
     * 更新
     *
     * @author
     * @Date 2021-08-24
     */
    void update(WxuserInfoParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author
     * @Date 2021-08-24
     */
    WxuserInfoResult findBySpec(WxuserInfoParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author
     * @Date 2021-08-24
     */
    List<WxuserInfoResult> findListBySpec(WxuserInfoParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author
     * @Date 2021-08-24
     */
    PageInfo<WxuserInfoResult> findPageBySpec(WxuserInfoParam param);

    /**
     * 分配权限
     *
     * @param
     * @return
     */
    Boolean sendPermissions(Long type,Long userId);

    WxuserInfo getByMemberId(Long memberId);
}
