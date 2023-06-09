package cn.atsoft.dasheng.media.controller;

import cn.atsoft.dasheng.media.config.RestAliyunService;
import cn.atsoft.dasheng.media.entity.Media;
import cn.atsoft.dasheng.media.model.enums.OssEnums;
import cn.atsoft.dasheng.media.model.params.MediaParam;
import cn.atsoft.dasheng.media.model.result.MediaObjectResult;
import cn.atsoft.dasheng.media.model.result.MediaResult;
import cn.atsoft.dasheng.media.service.RestMediaService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 控制器
 *
 * @author Sing
 * @Date 2021-04-21 07:43:33
 */
@RestController
@RequestMapping("/cn/atsoft/dasheng/media/{version}")
@ApiVersion("2.0")
@Api(tags = "")
public class RestMediaController extends BaseController {

    @Autowired
    private RestMediaService restMediaService;

    @Autowired
    private RestAliyunService aliyunService;
//    /**
//     * 编辑接口
//     *
//     * @author Sing
//     * @Date 2021-04-21
//     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody MediaParam mediaParam) {
//
//        this.mediaService.update(mediaParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 删除接口
//     *
//     * @author Sing
//     * @Date 2021-04-21
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody MediaParam mediaParam)  {
//        this.mediaService.delete(mediaParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 查看详情接口
//     *
//     * @author Sing
//     * @Date 2021-04-21
//     */
//    @RequestMapping(value = "/detail", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData<MediaResult> detail(@RequestBody MediaParam mediaParam) {
//        Media detail = this.mediaService.getById(mediaParam.getMediaId());
//        MediaResult result = new MediaResult();
//        ToolUtil.copyProperties(detail, result);
//
////        result.setValue(parentValue);
//        return ResponseData.success(result);
//    }
//
//    /**
//     * 查询列表
//     *
//     * @author Sing
//     * @Date 2021-04-21
//     */
//    @RequestMapping(value = "/list", method = RequestMethod.POST)
//    @ApiOperation("列表")
//    public PageInfo<MediaResult> list(@RequestBody(required = false) MediaParam mediaParam) {
//        if(ToolUtil.isEmpty(mediaParam)){
//            mediaParam = new MediaParam();
//        }
//        return this.mediaService.findPageBySpec(mediaParam);
//    }


    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    @ApiOperation("获取阿里云OSS临时上传token")
    public ResponseData getToken(@Param("type") String type) {


        Media media = restMediaService.getMediaId(type);

        return ResponseData.success(restMediaService.getOssToken(media));
    }
    @RequestMapping(value = "/{version}/getToken", method = RequestMethod.GET)
    @ApiOperation("获取阿里云OSS临时上传token")
    @ApiVersion("1.2")
    public ResponseData getToken(@Param("type") String type, @Param("model") OssEnums model) {
        if(ToolUtil.isEmpty(type)){
            throw new ServiceException(500,"请传入文件名称");
        }
        Media media = restMediaService.getMediaId(type,model);

        return ResponseData.success(restMediaService.getOssToken(media,model));
    }
    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    @ApiOperation("OSS异步通知")
    public String callback() {
        return null;
    }


    @RequestMapping("/getObject")
    @ApiOperation("获取浏览地址")
    public ResponseData getObject(@Param("mediaId") Long mediaId) {
        String url = restMediaService.getMediaUrl(mediaId, 0L);
        return ResponseData.success(url);
    }
    @RequestMapping("/getMediaUrl")
    @ApiOperation("获取浏览地址")
    public ResponseData getMediaUrl(@Param("mediaId") Long mediaId,@Param("option") String option) {
//        String url = mediaService.getMediaUrl(mediaId, 0L);
        String url = restMediaService.getMediaUrlAddUseData(mediaId, 0L, option);
        return ResponseData.success(url);
    }
    @RequestMapping(value = "/getMediaUrls" , method = RequestMethod.POST)
    @ApiOperation("获取浏览地址")
    public ResponseData getMediaUrls(@RequestBody MediaParam param) {
//        String url = mediaService.getMediaUrl(mediaId, 0L);
        if (ToolUtil.isEmpty(param.getMediaIds())){
            return ResponseData.success(new ArrayList<>());
        }
        List<MediaResult> results = new ArrayList<>();
        for (Long mediaId : param.getMediaIds()) {
            Media media = restMediaService.getById(mediaId);
            if (ToolUtil.isEmpty(media)){
                continue;
            }
            MediaResult mediaResult = new MediaResult();
            ToolUtil.copyProperties(media,mediaResult);
            mediaResult.setMediaId(mediaId);
            mediaResult.setUrl(restMediaService.getMediaUrlAddUseData(mediaId, 0L, null));
            if (ToolUtil.isNotEmpty(param.getOption())) {
                mediaResult.setThumbUrl(restMediaService.getMediaUrlAddUseData(mediaId, 0L,param.getOption()));
            }
            results.add(mediaResult);
        }
        return ResponseData.success(results);
    }
    @RequestMapping(value = "{version}/getMediaUrls" , method = RequestMethod.POST)
    @ApiOperation("获取浏览地址")
    @ApiVersion("1.2")
    public ResponseData getMediaUrlsV12(@RequestBody MediaParam param) {
//        String url = mediaService.getMediaUrl(mediaId, 0L);
        if (ToolUtil.isEmpty(param.getMediaIds())){
            return ResponseData.success(new ArrayList<>());
        }
        List<MediaResult> results = new ArrayList<>();
        for (Long mediaId : param.getMediaIds()) {
            Media media = restMediaService.getById(mediaId);
            MediaResult mediaResult = new MediaResult();
            ToolUtil.copyProperties(media,mediaResult);
            mediaResult.setMediaId(mediaId);
            switch (param.getModel()){
                case PRI:
                    mediaResult.setUrl(restMediaService.getPrivateMediaUrlAddUseData(mediaId, 0L, null));
                    if (ToolUtil.isNotEmpty(param.getOption())) {
                        mediaResult.setThumbUrl(restMediaService.getPrivateMediaUrlAddUseData(mediaId, 0L,param.getOption()));
                    }
                    break;
                case PUB:
                    mediaResult.setUrl(restMediaService.getMediaUrlAddUseData(mediaId, 0L, null));
                    if (ToolUtil.isNotEmpty(param.getOption())) {
                        mediaResult.setThumbUrl(restMediaService.getMediaUrlAddUseData(mediaId, 0L,param.getOption()));
                    }
                    break;
            }
            results.add(mediaResult);
        }
        return ResponseData.success(results);
    }

    @RequestMapping("/getObjectMeta")
    @ApiOperation("获取资源元信息")
    public ResponseData getObjectMeta(@Param("mediaId") Long mediaId) {
        Media result = restMediaService.getById(mediaId);
        if (ToolUtil.isEmpty(result)) {
            throw new ServiceException(500, "媒体不存在");
        }
        OSS ossClient = aliyunService.getOssClient();
        ObjectMetadata objectMetadata = ossClient.headObject(result.getBucket(), result.getPath());
        MediaObjectResult mediaObjectResult = new MediaObjectResult();
        ToolUtil.copyProperties(objectMetadata, mediaObjectResult);
        ossClient.shutdown();
        return ResponseData.success(mediaObjectResult);
    }

    /**
     * 查询列表
     *
     * @author Sing
     * @Date 2021-04-21
     */
    @RequestMapping(value = "/sortPagelist", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<MediaResult> sortPagelist(@RequestBody(required = false) MediaParam mediaParam) {
        if(ToolUtil.isEmpty(mediaParam)){
            mediaParam = new MediaParam();
        }
        return this.restMediaService.findPageBySpecMyself(mediaParam);
    }
     /**
     * 查询列表
     *
     * @author Sing
     * @Date 2021-04-21
     */
    @RequestMapping(value = "/getUrlByMediaId", method = RequestMethod.GET)
    @ApiOperation("单个媒体地址301跳转By媒体Id")
    public void getUrlByMediaId(HttpServletRequest request, HttpServletResponse response, @RequestParam Long mediaId,@RequestParam(required = false) String xOssProcess) throws IOException {
        if(ToolUtil.isEmpty(mediaId)){
            throw new ServiceException(500,"媒体id不能为空");
        }
        String url = this.restMediaService.getMediaUrlAddUseData(mediaId,0L,xOssProcess);
        response.sendRedirect(url);

    }



    /**
     * 查询列表
     *
     * @author Sing
     * @Date 2021-04-21
     */
    @RequestMapping(value = "/getMediaPathById", method = RequestMethod.GET)
    @ApiOperation("单个媒体地址301跳转By媒体Id")
    public String getMediaPathById( @RequestParam Long mediaId){
        if(ToolUtil.isEmpty(mediaId)){
            throw new ServiceException(500,"媒体id不能为空");
        }
        return this.restMediaService.getMediaPathPublic(mediaId,0L);
    }

//    /**
//     * 阿里云上传
//     * @param mediaId
//     * @return
//     */
//    @RequestMapping("/getTemporaryFile")
//    public ResponseData getTemporaryFile(@RequestParam String mediaId) {
//        Long id = mediaService.getTemporaryFile(mediaId);
//        return ResponseData.success(id);
//    }

}


