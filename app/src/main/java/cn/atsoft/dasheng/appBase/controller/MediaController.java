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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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


        Media media = mediaService.getMediaId(type);

        return ResponseData.success(mediaService.getOssToken(media));
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
        return this.mediaService.findPageBySpecMyself(mediaParam);
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
        String url = this.mediaService.getMediaUrlAddUseData(mediaId,0L,xOssProcess);
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
        return this.mediaService.getMediaPathPublic(mediaId,0L);
    }


}


