package cn.atsoft.dasheng.uc.service;

import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.model.params.UcOpenUserInfoParam;
import cn.atsoft.dasheng.uc.model.result.UcOpenUserInfoResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Sing
 * @since 2021-03-17
 */
public interface UcOpenUserInfoService extends IService<UcOpenUserInfo> {

    /**
     * 新增
     *
     * @author Sing
     * @Date 2021-03-17
     */
    UcOpenUserInfo add(UcOpenUserInfoParam param);

    /**
     * 删除
     *
     * @author Sing
     * @Date 2021-03-17
     */
    void delete(UcOpenUserInfoParam param);

    /**
     * 更新
     *
     * @author Sing
     * @Date 2021-03-17
     */
    UcOpenUserInfo update(UcOpenUserInfoParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Sing
     * @Date 2021-03-17
     */
    UcOpenUserInfoResult findBySpec(UcOpenUserInfoParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Sing
     * @Date 2021-03-17
     */
    List<UcOpenUserInfoResult> findListBySpec(UcOpenUserInfoParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Sing
     * @Date 2021-03-17
     */
     PageInfo findPageBySpec(UcOpenUserInfoParam param);
    UcOpenUserInfoResult findByOne(UcOpenUserInfoParam param);

}
