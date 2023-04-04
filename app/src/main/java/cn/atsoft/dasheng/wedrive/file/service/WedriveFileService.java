package cn.atsoft.dasheng.wedrive.file.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.wedrive.file.entity.WedriveFile;
import cn.atsoft.dasheng.wedrive.file.model.params.WedriveFileParam;
import cn.atsoft.dasheng.wedrive.file.model.result.WedriveFileResult;
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
public interface WedriveFileService extends IService<WedriveFile> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
    void add(WedriveFileParam param) throws WxErrorException, IOException;

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
    void delete(WedriveFileParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
    void update(WedriveFileParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
    WedriveFileResult findBySpec(WedriveFileParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
    List<WedriveFileResult> findListBySpec(WedriveFileParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-04-01
     */
     PageInfo<WedriveFileResult> findPageBySpec(WedriveFileParam param);

}
