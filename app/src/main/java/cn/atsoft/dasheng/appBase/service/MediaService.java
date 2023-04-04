package cn.atsoft.dasheng.appBase.service;

import cn.atsoft.dasheng.appBase.entity.Media;
import cn.atsoft.dasheng.appBase.model.enums.OssEnums;
import cn.atsoft.dasheng.appBase.model.params.MediaParam;
import cn.atsoft.dasheng.appBase.model.result.MediaResult;
import cn.atsoft.dasheng.appBase.model.result.MediaUrlResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Sing
 * @since 2021-04-21
 */
public interface MediaService extends IService<Media> {


    Media getMediaId(String type, Long userId, OssEnums model);

    Map<String,Object> getOssToken(Media media);
    /**
     * 新增
     *
     * @author Sing
     * @Date 2021-04-21
     */
    void add(MediaParam param);

    /**
     * 删除
     *
     * @author Sing
     * @Date 2021-04-21
     */
    void delete(MediaParam param);

    /**
     * 更新
     *
     * @author Sing
     * @Date 2021-04-21
     */
    void update(MediaParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Sing
     * @Date 2021-04-21
     */
    MediaResult findBySpec(MediaParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Sing
     * @Date 2021-04-21
     */
    List<MediaResult> findListBySpec(MediaParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Sing
     * @Date 2021-04-21
     */
     PageInfo<MediaResult> findPageBySpec(MediaParam param);

    PageInfo<MediaResult> findPageBySpecMyself(MediaParam param);

    Media getMediaId(String type);

    Media getMediaId(String type, OssEnums model);

    Media getMediaId(String type , Long userId);

    Map<String, Object> getOssToken(Media media, OssEnums enums);

    List<String> getMediaUrls(List<Long> mediaIds, Long userId);

    List<MediaUrlResult> getMediaUrlResults(List<Long> mediaIds);

    String getMediaUrl(Long mediaId, Long userId);

    String getMediaUrlAddUseData(Long mediaId, Long userId, String useData);


    String getPrivateMediaUrlAddUseData(Long mediaId, Long userId, String useData);

    String getMediaPathPublic(Long mediaId, Long userId);

   Long getTemporaryFile(String mediaId);

    List<MediaResult> listByIds(List<Long> ids);

    Long uploadPrivateFile(File file);
}
