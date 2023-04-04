package cn.atsoft.dasheng.wedrive.space.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.wedrive.space.entity.WxWedriveSpace;
import cn.atsoft.dasheng.wedrive.space.model.params.WxWedriveSpaceParam;
import cn.atsoft.dasheng.wedrive.space.model.result.WxWedriveSpaceResult;
import com.baomidou.mybatisplus.extension.service.IService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.WxCpBaseResp;

import java.util.List;

/**
 * <p>
 * 微信微盘空间 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-31
 */
public interface WxWedriveSpaceService extends IService<WxWedriveSpace> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-03-31
     */
    void add(WxWedriveSpaceParam param) throws WxErrorException;

    WxWedriveSpaceResult detail(String spaceId) throws WxErrorException;

    WxCpBaseResp spaceAclAdd(WxWedriveSpaceParam param) throws WxErrorException;

    WxCpBaseResp spaceAclDelete(WxWedriveSpaceParam param) throws WxErrorException;

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-03-31
     */
    void delete(WxWedriveSpaceParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-03-31
     */
    void update(WxWedriveSpaceParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-03-31
     */
    WxWedriveSpaceResult findBySpec(WxWedriveSpaceParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-03-31
     */
    List<WxWedriveSpaceResult> findListBySpec(WxWedriveSpaceParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-03-31
     */
     PageInfo<WxWedriveSpaceResult> findPageBySpec(WxWedriveSpaceParam param);

}
