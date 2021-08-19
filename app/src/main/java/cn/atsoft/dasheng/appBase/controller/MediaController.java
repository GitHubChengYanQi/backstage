package cn.atsoft.dasheng.appBase.controller;

import cn.atsoft.dasheng.appBase.config.AliConfiguration;
import cn.atsoft.dasheng.appBase.config.AliyunService;
import cn.atsoft.dasheng.appBase.entity.Media;
import cn.atsoft.dasheng.appBase.model.params.MediaParam;
import cn.atsoft.dasheng.appBase.model.result.MediaObjectResult;
import cn.atsoft.dasheng.appBase.model.result.MediaResult;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PolicyConditions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 控制器
 *
 * @author Sing
 * @Date 2021-04-21 07:43:33
 */
@RestController
@RequestMapping("/media")
@Api(tags = "")
public class MediaController extends BaseController {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private AliyunService aliyunService;
    /**
     * 编辑接口
     *
     * @author Sing
     * @Date 2021-04-21
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody MediaParam mediaParam) {

        this.mediaService.update(mediaParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Sing
     * @Date 2021-04-21
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody MediaParam mediaParam)  {
        this.mediaService.delete(mediaParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Sing
     * @Date 2021-04-21
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<MediaResult> detail(@RequestBody MediaParam mediaParam) {
        Media detail = this.mediaService.getById(mediaParam.getMediaId());
        MediaResult result = new MediaResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Sing
     * @Date 2021-04-21
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<MediaResult> list(@RequestBody(required = false) MediaParam mediaParam) {
        if(ToolUtil.isEmpty(mediaParam)){
            mediaParam = new MediaParam();
        }
        return this.mediaService.findPageBySpec(mediaParam);
    }


    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    @ApiOperation("获取阿里云OSS临时上传token")
    public ResponseData getToken(@Param("type") String type) {


        Media media = mediaService.getMediaId(type);


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

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("OSSAccessKeyId", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("Signature", postSignature);
            respMap.put("key", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
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
            return ResponseData.success(respMap);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }

        return ResponseData.error("错误");
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    @ApiOperation("OSS异步通知")
    public String callback() {
        return null;
    }

    @RequestMapping("/getObject")
    @ApiOperation("获取浏览地址")
    public ResponseData getObject(@Param("mediaId") Long mediaId) {
        String url = mediaService.getMediaUrl(mediaId, 0L);
        return ResponseData.success(url);
    }

    @RequestMapping("/getObjectMeta")
    @ApiOperation("获取资源元信息")
    public ResponseData getObjectMeta(@Param("mediaId") Long mediaId) {
        Media result = mediaService.getById(mediaId);
        if (ToolUtil.isEmpty(result)) {
            throw new ServiceException(500, "媒体不存在");
        }
        OSS ossClient = aliyunService.getOssClient();
        ObjectMetadata objectMetadata = ossClient.headObject(result.getBucket(), result.getPath());
        MediaObjectResult mediaObjectResult = new MediaObjectResult();
        ToolUtil.copyProperties(objectMetadata, mediaObjectResult);
        return ResponseData.success(mediaObjectResult);
    }
}


