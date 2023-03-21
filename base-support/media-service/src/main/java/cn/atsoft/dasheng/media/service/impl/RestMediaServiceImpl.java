package cn.atsoft.dasheng.media.service.impl;


import cn.atsoft.dasheng.media.config.RestAliConfiguration;
import cn.atsoft.dasheng.media.config.RestAliyunService;
import cn.atsoft.dasheng.media.entity.Media;
import cn.atsoft.dasheng.media.mapper.RestMediaMapper;
import cn.atsoft.dasheng.media.model.enums.OssEnums;
import cn.atsoft.dasheng.media.model.params.MediaParam;
import cn.atsoft.dasheng.media.model.result.MediaResult;
import cn.atsoft.dasheng.media.model.result.MediaUrlResult;
import cn.atsoft.dasheng.media.service.RestMediaService;

import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.CannedAccessControlList;
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
import java.util.stream.Collectors;

import static cn.atsoft.dasheng.media.model.enums.OssEnums.PRI;
import static cn.atsoft.dasheng.media.model.enums.OssEnums.PUB;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Sing
 * @since 2021-04-21
 */
@Service
public class RestMediaServiceImpl extends ServiceImpl<RestMediaMapper, Media> implements RestMediaService {

    @Autowired
    private RestAliyunService aliyunService;

    @Override
    public void add(MediaParam param) {
        Media entity = getEntity(param);
        this.save(entity);
    }
    private final static String THUMB_URL_PARAM = "image/resize,m_fill,h_200,w_200";
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

    private void formatUrl(List<MediaResult> param) {
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
    public Media getMediaId(String type, OssEnums model) {
        return getMediaId(type, 0L,model);
    }

    @Override
    public Media getMediaId(String type, Long userId) {

        List<String> collect = Arrays.stream(type.split("\\.(?=[^\\.]+$)")).collect(Collectors.toList());
        String fileName = type;
        if (ToolUtil.isEmpty(collect.get(1))){
            throw new ServiceException(500,"传入参数格式错误");
        }
        String sname = collect.get(1);

        if (!userId.equals(0L) && ToolUtil.isNotEmpty(sname)) {
            List<String> types = Arrays.asList("png", "jpg", "jpeg", "gif", "mp4", "mp3", "flac", "aac");
            if (!types.contains(sname)) {
                throw new ServiceException(500, "数据类型错误");
            }
        }

        String date = DateUtil.format(DateUtil.date(), "yyyyMMdd");
        String path = aliyunService.getConfig().getOss().getPath() + sname + "/" + date + "/" + date + RandomUtil.randomNumbers(6) + "." + sname;
        String endpoint = aliyunService.getConfig().getOss().getEndpoint();
        String bucket = aliyunService.getConfig().getOss().getBucket();

        Media media = new Media();
        media.setFiledName(fileName);
        media.setType(sname);
        media.setPath(path);
        media.setFiledName(fileName);
        media.setEndpoint(endpoint);
        media.setBucket(bucket);
        media.setStatus(0);
        media.setUserId(userId);

        this.getBaseMapper().insert(media);
        return media;
    }
    @Override
    public Media getMediaId(String type, Long userId, OssEnums model) {

        List<String> collect = Arrays.stream(type.split("\\.(?=[^\\.]+$)")).collect(Collectors.toList());
        String fileName = type;
        if (ToolUtil.isEmpty(collect.get(1))){
            throw new ServiceException(500,"传入参数格式错误");
        }
        String sname = collect.get(1);

        if (!userId.equals(0L) && ToolUtil.isNotEmpty(sname)) {
            List<String> types = Arrays.asList("png", "jpg", "jpeg", "gif", "mp4", "mp3", "flac", "aac");
            if (!types.contains(sname)) {
                throw new ServiceException(500, "数据类型错误");
            }
        }

        String date = DateUtil.format(DateUtil.date(), "yyyyMMdd");
        String path = "";
        String endpoint = "";
        String bucket = "";
        switch (model){
            case PUB:
                 path = aliyunService.getConfig().getOss().getPath() + sname + "/" + date + "/" + date + RandomUtil.randomNumbers(6) + "." + sname;
                 endpoint = aliyunService.getConfig().getOss().getEndpoint();
                 bucket = aliyunService.getConfig().getOss().getBucket();
                break;
            case PRI:
                path = aliyunService.getConfig().getOss2().getPath() + sname + "/" + date + "/" + date + RandomUtil.randomNumbers(6) + "." + sname;
                endpoint = aliyunService.getConfig().getOss2().getEndpoint();
                bucket = aliyunService.getConfig().getOss2().getBucket();
                break;
        }
        Media media = new Media();
        media.setFiledName(fileName);
        media.setType(sname);
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
         return this.getOssToken(media,PUB);
    }
    @Override
    public Map<String, Object> getOssToken(Media media, OssEnums enums) {

        RestAliConfiguration restAliConfiguration = aliyunService.getConfiguration();
        OSS ossClient = null;
        if (enums.equals(PUB)){
            ossClient = aliyunService.getOssClient();
        }else if (enums.equals(PRI)){
            ossClient = aliyunService.getPrivateOssClient();
        }

        try {

            String accessId = restAliConfiguration.getAccessId();
            String host = "";
            String callBackUrl = "";
                    switch (enums){
                case PUB:
                    host = "https://" + restAliConfiguration.getOss().getBucket() + "." + restAliConfiguration.getOss().getEndpoint();
                    callBackUrl = restAliConfiguration.getOss().getCallbackUrl() + "/cn/atsoft/dasheng/media/callback";
                    break;
                case PRI:
                    host = "https://" + restAliConfiguration.getOss2().getBucket() + "." + restAliConfiguration.getOss2().getEndpoint();
                    callBackUrl = restAliConfiguration.getOss2().getCallbackUrl() + "/cn/atsoft/dasheng/media/callback";
                    break;
            }
            String dir = media.getPath();



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
    public List<String> getMediaUrls(List<Long> mediaIds, Long userId) {
        List<String> urls = new ArrayList<>();
        for (Long mediaId : mediaIds) {
            String url = getMediaUrlAddUseData(mediaId, userId, null);
            urls.add(url);
        }
        return urls;
    }
    @Override
    public List<MediaUrlResult> getMediaUrlResults(List<Long> mediaIds) {
        List<MediaUrlResult> results = BeanUtil.copyToList(this.listByIds(mediaIds), MediaUrlResult.class);
        for (MediaUrlResult result : results) {
            String url = getMediaUrlAddUseData(result.getMediaId(), null, null);
            String ThumbUrl = getMediaUrlAddUseData(result.getMediaId(), null, THUMB_URL_PARAM);
            result.setUrl(url);
            result.setThumbUrl(ThumbUrl);
        }
        return results;
    }


    @Override
    public String getMediaUrl(Long mediaId, Long userId) {
        return getMediaUrlAddUseData(mediaId, userId, null);
    }

    @Override
    public String getMediaUrlAddUseData(Long mediaId, Long userId, String useData) {
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
        if (ToolUtil.isNotEmpty(useData)) {
            generatePresignedUrlRequest.setQueryParameter(new HashMap<String, String>() {{
                put("x-oss-process", useData);
            }});
        }

        URL url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
        ossClient.shutdown();
        return url.toString();
    }
    @Override
    public String getPrivateMediaUrlAddUseData(Long mediaId, Long userId, String useData) {
        if (ToolUtil.isEmpty(mediaId) || mediaId <= 0) {
            return null;
        }
        Media result = this.getById(mediaId);
        if (ToolUtil.isEmpty(result)) {
            throw new ServiceException(500, "媒体不存在");
        }

        OSS ossClient = aliyunService.getPrivateOssClient();
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(result.getBucket(), result.getPath(), HttpMethod.GET);
        long expireTime = 86400 * 15;
        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
        Date expiration = new Date(expireEndTime);
        generatePresignedUrlRequest.setExpiration(expiration);
        if (ToolUtil.isNotEmpty(useData)) {
            generatePresignedUrlRequest.setQueryParameter(new HashMap<String, String>() {{
                put("x-oss-process", useData);
            }});
        }

        URL url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
        ossClient.shutdown();
        return url.toString();
    }

    @Override
    public String getMediaPathPublic(Long mediaId, Long userId) {
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
        ossClient.setObjectAcl(result.getBucket(), result.getPath(), CannedAccessControlList.PublicRead);

//        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(result.getBucket(), result.getPath(), HttpMethod.GET);
//        long expireTime = 86400 * 15;
//        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
//        Date expiration = new Date(expireEndTime);
//        generatePresignedUrlRequest.setExpiration(expiration);
//        if (ToolUtil.isNotEmpty(useData)){
//            generatePresignedUrlRequest.setQueryParameter(new HashMap<String,String>(){{
//                put("x-oss-process",useData);
//            }});
//        }
//        URL url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
        ossClient.shutdown();
        return "https://" + result.getBucket() + "." + result.getEndpoint() + "/" + result.getPath();
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

//    /**
//     * 获取临时文件  上传阿里云
//     *
//     * @param
//     * @param mediaId
//     * @return
//     */
//    @Override
//    public Long getTemporaryFile(String mediaId) {
//        if (ToolUtil.isEmpty(mediaId)) {
//            return null;
//        }
//        try {
//
//            WxCpMediaService mediaService = wxCpService.getWxCpClient().getMediaService();
//            File download = mediaService.download(mediaId);
//            String bucket = aliyunService.getConfig().getOss().getBucket();
//            OSS ossClient = aliyunService.getOssClient();
//
//            Media media = getMediaId(download.getName());
//            ossClient.putObject(bucket, media.getPath(), download);
//
//            String mediaUrlAddUseData = getMediaUrlAddUseData(media.getMediaId(), 0L, null);
//            ossClient.shutdown();
//            return media.getMediaId();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
    @Override
    public List<MediaResult> listByIds(List<Long> ids) {
        if (ToolUtil.isEmpty(ids) || ids.size() == 0) {
            return new ArrayList<>();
        }
        List<MediaResult> results = BeanUtil.copyToList(this.lambdaQuery().in(Media::getMediaId, ids).list(), MediaResult.class);

        for (MediaResult result : results) {
            String url = getMediaUrlAddUseData(result.getMediaId(), null, null);
            result.setUrl(url);
            //为以后更多类型预留
            switch (result.getType()) {
                case "jpeg":
                case "png":
                case "jpg":
                    String thumbUrl = getMediaUrlAddUseData(result.getMediaId(), null, THUMB_URL_PARAM);
                    result.setThumbUrl(thumbUrl);
                    break;
                default:
                    break;
            }
        }
        return results;
    }

}
