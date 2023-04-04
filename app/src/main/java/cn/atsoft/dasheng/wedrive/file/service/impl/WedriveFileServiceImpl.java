package cn.atsoft.dasheng.wedrive.file.service.impl;


import cn.atsoft.dasheng.appBase.config.AliConfiguration;
import cn.atsoft.dasheng.appBase.config.AliyunService;
import cn.atsoft.dasheng.appBase.entity.Media;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.appBase.service.WxCpService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Order;
import cn.atsoft.dasheng.crm.service.OrderService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.wedrive.file.entity.WedriveFile;
import cn.atsoft.dasheng.wedrive.file.mapper.WedriveFileMapper;
import cn.atsoft.dasheng.wedrive.file.model.params.WedriveFileParam;
import cn.atsoft.dasheng.wedrive.file.model.result.WedriveFileResult;
import cn.atsoft.dasheng.wedrive.file.service.WedriveFileService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.wedrive.space.entity.WedriveSpace;
import cn.atsoft.dasheng.wedrive.space.model.enums.TypeEnum;
import cn.atsoft.dasheng.wedrive.space.model.result.WxWedriveSpaceResult;
import cn.atsoft.dasheng.wedrive.space.service.WedriveSpaceService;
import com.aliyun.oss.model.OSSObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.JsonObject;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.WxCpBaseResp;
import me.chanjar.weixin.cp.bean.oa.wedrive.WxCpFileCreate;
import me.chanjar.weixin.cp.bean.oa.wedrive.WxCpFileDeleteRequest;
import me.chanjar.weixin.cp.bean.oa.wedrive.WxCpFileUpload;
import me.chanjar.weixin.cp.bean.oa.wedrive.WxCpFileUploadRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import static cn.atsoft.dasheng.wedrive.file.model.enums.FileType.file_folder;
import static me.chanjar.weixin.cp.constant.WxCpApiPathConsts.Oa.FILE_CREATE;
import static me.chanjar.weixin.cp.constant.WxCpApiPathConsts.Oa.FILE_DELETE;

/**
 * <p>
 * 微信微盘文件管理 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-01
 */
@Service
public class WedriveFileServiceImpl extends ServiceImpl<WedriveFileMapper, WedriveFile> implements WedriveFileService {
    @Autowired
    private WxCpService wxCpService;

    @Autowired
    private WedriveSpaceService wedriveSpaceService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private AliyunService aliyunService;
    @Autowired
    private OrderService orderService;

    @Override
    public void add(WedriveFileParam param) throws WxErrorException, IOException {
        Order order = orderService.getById(param.getOrderId());
        if (ToolUtil.isNotEmpty(param.getSpaceId())) {
            WxWedriveSpaceResult detail = wedriveSpaceService.detail(param.getSpaceId());
            if (ToolUtil.isEmpty(detail.getWxCpSpaceInfo())) {
                throw new ServiceException(500, "空间未找到");
            }
            if (ToolUtil.isEmpty(param.getMediaId())) {
                throw new ServiceException(500, "请传媒体id");
            }
        }


        WedriveSpace wedriveSpace = null;
        switch (param.getSpaceType()) {
            case order:
                wedriveSpace = wedriveSpaceService.lambdaQuery().eq(WedriveSpace::getType, TypeEnum.order).eq(WedriveSpace::getDisplay, 1).one();
                break;
            case invoiceBill:
                wedriveSpace = wedriveSpaceService.lambdaQuery().eq(WedriveSpace::getType, TypeEnum.invoiceBill).eq(WedriveSpace::getDisplay, 1).one();
                break;
            case paymentRecord:
                wedriveSpace = wedriveSpaceService.lambdaQuery().eq(WedriveSpace::getType, TypeEnum.paymentRecord).eq(WedriveSpace::getDisplay, 1).one();
                break;
        }
        if (ToolUtil.isEmpty(wedriveSpace)) {
            throw new ServiceException(500, "请先在系统中创建 对应类型的 企业微信-微盘空间");
        }
        param.setSpaceId(wedriveSpace.getSpaceId());

        /**
         * 查询、创建文件夹
         */
        WedriveFile file = this.lambdaQuery().eq(WedriveFile::getFileType, file_folder.getValue()).eq(WedriveFile::getFileName, order.getCoding()).one();
        if (ToolUtil.isEmpty(file)) {
            String fileId = null;

           try{
               String apiUrl = this.wxCpService.getWxCpClient().getWxCpConfigStorage().getApiUrl(FILE_CREATE);
               JsonObject jsonObject = new JsonObject();
               jsonObject.addProperty("spaceid", wedriveSpace.getSpaceId());
               jsonObject.addProperty("fatherid", wedriveSpace.getSpaceId());
               jsonObject.addProperty("file_type", file_folder.getValue());
               jsonObject.addProperty("file_name", order.getCoding());
               String responseContent = this.wxCpService.getWxCpClient().post(apiUrl, jsonObject.toString());
               WxCpFileCreate wxCpFileCreate = WxCpFileCreate.fromJson(responseContent);
               fileId = wxCpFileCreate.getFileId();
               file = new WedriveFile();
               file.setFileId(fileId);
               file.setFileName(order.getCoding());
               file.setSpaceId(wedriveSpace.getSpaceId());
               file.setFileType(file_folder.getValue());
               this.save(file);


           }catch (Exception e){
               /**
                * 如果出现异常 删除微盘文件夹
                */
               if (ToolUtil.isNotEmpty(fileId)){
                   this.baseMapper.deleteById(fileId);
                   String apiUrl = this.wxCpService.getWxCpClient().getWxCpConfigStorage().getApiUrl(FILE_DELETE);
                   WxCpFileDeleteRequest request = new WxCpFileDeleteRequest(null, Collections.singletonList(fileId));
                   this.wxCpService.getWxCpClient().post(apiUrl, request.toJson());
               }
           }
        }
        List<WedriveFile> saveList = new ArrayList<>();
        /**
         * 上传微盘文件
         */
        for (Long mediaId : param.getMediaIds()) {
            Media media = mediaService.getById(mediaId);
            AliConfiguration.OSS oss = aliyunService.getConfig().getOss2();
            OSSObject object = aliyunService.getPrivateOssClient().getObject(oss.getBucket(), media.getPath());

            InputStream field = object.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(field);

            String encoded = Base64.getEncoder().encodeToString(bytes);
            WxCpFileUploadRequest uploadRequest = new WxCpFileUploadRequest();
            uploadRequest.setSpaceId(param.getSpaceId());
            uploadRequest.setFileName(media.getFiledName());
            uploadRequest.setFatherId(file.getFileId());
            uploadRequest.setFileBase64Content(encoded);

            WxCpFileUpload wxCpFileUpload = wxCpService.getWxCpClient().getOaWeDriveService().fileUpload(uploadRequest);

            WedriveFile entity = getEntity(param);
            entity.setFileName(media.getFiledName());
            entity.setType(media.getType());
            entity.setFileId(wxCpFileUpload.getFileId());
            saveList.add(entity);

        }
        this.saveBatch(saveList);

    }

    @Override
    public void delete(WedriveFileParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(WedriveFileParam param) {
        WedriveFile oldEntity = getOldEntity(param);
        WedriveFile newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public WedriveFileResult findBySpec(WedriveFileParam param) {
        return null;
    }

    @Override
    public List<WedriveFileResult> findListBySpec(WedriveFileParam param) {
        return null;
    }

    @Override
    public PageInfo<WedriveFileResult> findPageBySpec(WedriveFileParam param) {
        Page<WedriveFileResult> pageContext = getPageContext();
        IPage<WedriveFileResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(WedriveFileParam param) {
        return param.getFileId();
    }

    private Page<WedriveFileResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private WedriveFile getOldEntity(WedriveFileParam param) {
        return this.getById(getKey(param));
    }

    private WedriveFile getEntity(WedriveFileParam param) {
        WedriveFile entity = new WedriveFile();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
