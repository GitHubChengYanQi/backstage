package cn.atsoft.dasheng.wedrive.file.service.impl;


import cn.atsoft.dasheng.audit.service.RestWxCpService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.media.config.RestAliConfiguration;
import cn.atsoft.dasheng.media.config.RestAliyunService;
import cn.atsoft.dasheng.media.entity.Media;
import cn.atsoft.dasheng.media.service.RestMediaService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.wedrive.file.entity.WxWedriveFile;
import cn.atsoft.dasheng.wedrive.file.mapper.WxWedriveFileMapper;
import cn.atsoft.dasheng.wedrive.file.model.params.WxWedriveFileParam;
import cn.atsoft.dasheng.wedrive.file.model.result.WxWedriveFileResult;
import  cn.atsoft.dasheng.wedrive.file.service.WxWedriveFileService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.wedrive.space.model.result.WxWedriveSpaceResult;
import cn.atsoft.dasheng.wedrive.space.service.WxWedriveSpaceService;
import com.aliyun.oss.model.OSSObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.oa.wedrive.WxCpFileUpload;
import me.chanjar.weixin.cp.bean.oa.wedrive.WxCpFileUploadRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Base64;
import java.util.List;

/**
 * <p>
 * 微信微盘文件管理 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-01
 */
@Service
public class WxWedriveFileServiceImpl extends ServiceImpl<WxWedriveFileMapper, WxWedriveFile> implements WxWedriveFileService {

    @Autowired
    private RestWxCpService wxCpService;

    @Autowired
    private WxWedriveSpaceService wedriveSpaceService;

    @Autowired
    private RestMediaService mediaService;

    @Autowired
    private RestAliyunService aliyunService;

    @Override
    public void add(WxWedriveFileParam param) throws WxErrorException, IOException {
        WxWedriveSpaceResult detail = wedriveSpaceService.detail(param.getSpaceId());
        if (ToolUtil.isEmpty(detail.getWxCpSpaceInfo())) {
            throw new ServiceException(500 ,"空间未找到");
        }
        if (ToolUtil.isEmpty(param.getMediaId())) {
            throw new ServiceException(500 ,"请传媒体id");
        }
        WxCpFileUploadRequest uploadRequest = new WxCpFileUploadRequest();
        uploadRequest.setSpaceId(param.getSpaceId());
        uploadRequest.setFileName(param.getFileName());

        Media media = mediaService.getById(param.getMediaId());
        RestAliConfiguration.OSS oss = aliyunService.getConfig().getOss2();
        OSSObject object = aliyunService.getPrivateOssClient().getObject(oss.getBucket(), media.getPath());

        InputStream field = object.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(field);
        String encoded = Base64.getEncoder().encodeToString(bytes);
        uploadRequest.setFileBase64Content(encoded);



        WxCpFileUpload wxCpFileUpload = wxCpService.getWxCpClient().getOaWeDriveService().fileUpload(uploadRequest);


        WxWedriveFile entity = getEntity(param);
        entity.setFileId(wxCpFileUpload.getFileId());
        this.save(entity);
    }

    @Override
    public void delete(WxWedriveFileParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(WxWedriveFileParam param){
        WxWedriveFile oldEntity = getOldEntity(param);
        WxWedriveFile newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public WxWedriveFileResult findBySpec(WxWedriveFileParam param){
        return null;
    }

    @Override
    public List<WxWedriveFileResult> findListBySpec(WxWedriveFileParam param){
        return null;
    }

    @Override
    public PageInfo<WxWedriveFileResult> findPageBySpec(WxWedriveFileParam param){
        Page<WxWedriveFileResult> pageContext = getPageContext();
        IPage<WxWedriveFileResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(WxWedriveFileParam param){
        return param.getFileId();
    }

    private Page<WxWedriveFileResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private WxWedriveFile getOldEntity(WxWedriveFileParam param) {
        return this.getById(getKey(param));
    }

    private WxWedriveFile getEntity(WxWedriveFileParam param) {
        WxWedriveFile entity = new WxWedriveFile();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
