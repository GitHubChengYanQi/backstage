package cn.atsoft.dasheng.appBase.service.impl;


import cn.atsoft.dasheng.appBase.config.AliConfiguration;
import cn.atsoft.dasheng.appBase.entity.Media;
import cn.atsoft.dasheng.appBase.mapper.MediaMapper;
import cn.atsoft.dasheng.appBase.model.params.MediaParam;
import cn.atsoft.dasheng.appBase.model.result.MediaResult;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.appBase.config.AliyunService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.net.URL;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Sing
 * @since 2021-04-21
 */
@Service
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements MediaService {

    @Autowired
    private AliyunService aliyunService;

    @Override
    public void add(MediaParam param) {
        Media entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(MediaParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(MediaParam param) {
        Media oldEntity = getOldEntity(param);
        Media newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public MediaResult findBySpec(MediaParam param) {
        return null;
    }

    @Override
    public List<MediaResult> findListBySpec(MediaParam param) {
        return null;
    }

    @Override
    public PageInfo<MediaResult> findPageBySpec(MediaParam param) {
        Page<MediaResult> pageContext = getPageContext();
        IPage<MediaResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public PageInfo<MediaResult> findPageBySpecMyself(MediaParam param) {
        Page<MediaResult> pageContext = getPageContext();
        IPage<MediaResult> page = this.baseMapper.customPageList(pageContext, param);
        this.formatUrl(page.getRecords());
        return PageFactory.createPageInfo(page);
    }
    private void formatUrl(List<MediaResult> param){
        for (MediaResult mediaResult : param) {
            Long mediaId = mediaResult.getMediaId();
            String mediaUrl = this.getMediaUrl(mediaId, 1L);
            mediaResult.setUrl(mediaUrl);
        }
    }

    @Override
    public Media getMediaId(String type) {
        return getMediaId(type, 0L);
    }

    @Override
    public Media getMediaId(String type, Long userId) {
        if (!userId.equals(0L)) {
            List<String> types = Arrays.asList("png", "jpg", "jpeg", "gif", "mp4", "mp3", "flac", "aac");
            if (!types.contains(type)) {
                throw new ServiceException(500, "数据类型错误");
            }
        }

        String date = DateUtil.format(DateUtil.date(), "yyyyMMdd");
        String path = aliyunService.getConfig().getOss().getPath() + type + "/" + date + "/" + date + RandomUtil.randomNumbers(6) + "." + type;
        String endpoint = aliyunService.getConfig().getOss().getEndpoint();
        String bucket = aliyunService.getConfig().getOss().getBucket();

        Media media = new Media();
        media.setPath(path);
        media.setEndpoint(endpoint);
        media.setBucket(bucket);
        media.setStatus(0);
        media.setUserId(userId);

        this.getBaseMapper().insert(media);
        return media;
    }

    @Override
    public Map<String, Object> getOssToken(Media media) {

        AliConfiguration aliConfiguration = aliyunService.getConfiguration();
        OSS ossClient = aliyunService.getOssClient();
        try {

            String accessId = aliConfiguration.getAccessId();
            String host = "https://" + aliConfiguration.getOss().getBucket() + "." + aliConfiguration.getOss().getEndpoint();
            String dir = media.getPath();
            String callBackUrl = aliConfiguration.getOss().getCallbackUrl() + "/media/callback";


            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
//            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            policyConds.addConditionItem(MatchMode.Exact, PolicyConditions.COND_KEY, dir);
//            policyConds.addConditionItem(MatchMode.Exact, PolicyConditions.COND_KEY, dir);
//            policyConds.addConditionItem(MatchMode.Range, PolicyConditions.COND_KEY, dir);
//            policyConds.addConditionItem(PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            Map<String, Object> respMap = new LinkedHashMap<String, Object>();
            respMap.put("OSSAccessKeyId", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("Signature", postSignature);
            respMap.put("key", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            respMap.put("mediaId", media.getMediaId());
//             respMap.put("expire", formatISO8601Date(expiration));

            JSONObject jasonCallback = new JSONObject();
            jasonCallback.put("callbackUrl", callBackUrl);
            jasonCallback.put("callbackBody",
                    "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
            jasonCallback.put("callbackBodyType", "application/json");
            String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
//            respMap.put("callback", base64CallbackBody);
            respMap.put("mediaId", media.getMediaId().toString());

//            JSONObject ja1 = JSONObject.fromObject(respMap);
            // System.out.println(ja1.toString());
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Methods", "GET, POST");
//            response(request, response, ja1.toString());
            return respMap;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return null;
    }

    @Override
    public String getMediaUrl(Long mediaId, Long userId) {

        if (ToolUtil.isEmpty(mediaId) || mediaId <= 0) {
            return null;
        }
        Media result = this.getById(mediaId);
        if (ToolUtil.isEmpty(result)) {
            throw new ServiceException(500, "媒体不存在");
        }
//        if (ToolUtil.isNotEmpty(result.getUserId()) && !result.getUserId().equals(userId)) {
//            throw new ServiceException(500, "媒体信息错误");
//        }
        OSS ossClient = aliyunService.getOssClient();
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(result.getBucket(), result.getPath(), HttpMethod.GET);
        long expireTime = 86400 * 15;
        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
        Date expiration = new Date(expireEndTime);
        generatePresignedUrlRequest.setExpiration(expiration);
        URL url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    private Serializable getKey(MediaParam param) {
        return param.getMediaId();
    }

    private Page<MediaResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Media getOldEntity(MediaParam param) {
        return this.getById(getKey(param));
    }

    private Media getEntity(MediaParam param) {
        Media entity = new Media();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
