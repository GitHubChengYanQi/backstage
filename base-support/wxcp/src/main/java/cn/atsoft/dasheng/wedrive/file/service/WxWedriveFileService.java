package cn.atsoft.dasheng.wedrive.file.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.wedrive.file.entity.WxWedriveFile;
import cn.atsoft.dasheng.wedrive.file.model.params.WxWedriveFileParam;
import cn.atsoft.dasheng.wedrive.file.model.result.WxWedriveFileResult;
import com.baomidou.mybatisplus.extension.service.IService;
import me.chanjar.weixin.common.error.WxErrorException;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 微信微盘文件管理 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-01
 */
public interface WxWedriveFileService extends IService<WxWedriveFile> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
    void add(WxWedriveFileParam param) throws WxErrorException, IOException;

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
    void delete(WxWedriveFileParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
    void update(WxWedriveFileParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
    WxWedriveFileResult findBySpec(WxWedriveFileParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
    List<WxWedriveFileResult> findListBySpec(WxWedriveFileParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
     PageInfo<WxWedriveFileResult> findPageBySpec(WxWedriveFileParam param);

}
