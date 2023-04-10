package cn.atsoft.dasheng.wedrive.space.service.impl;


import cn.atsoft.dasheng.audit.service.RestWxCpService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.atsoft.dasheng.wedrive.space.entity.WxWedriveSpace;
import cn.atsoft.dasheng.wedrive.space.mapper.WxWedriveSpaceMapper;
import cn.atsoft.dasheng.wedrive.space.model.params.WxWedriveSpaceParam;
import cn.atsoft.dasheng.wedrive.space.model.result.WxWedriveSpaceResult;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.wedrive.space.service.WxWedriveSpaceService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.JsonObject;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.WxCpBaseResp;
import me.chanjar.weixin.cp.bean.oa.wedrive.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.chanjar.weixin.cp.constant.WxCpApiPathConsts.Oa.SPACE_DISMISS;
import static me.chanjar.weixin.cp.constant.WxCpApiPathConsts.Oa.SPACE_INFO;

/**
 * <p>
 * 微信微盘空间 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-31
 */
@Service
public class WxWedriveSpaceServiceImpl extends ServiceImpl<WxWedriveSpaceMapper, WxWedriveSpace> implements WxWedriveSpaceService {

    @Autowired
    private RestWxCpService wxCpService;
    @Autowired
    private UserService userService;

    @Override
    public void add(WxWedriveSpaceParam param) throws WxErrorException {

        if (ToolUtil.isEmpty(param.getUserIds())) {
            throw new ServiceException(500, "请传入人员id");
        }
        if (ToolUtil.isEmpty(param.getSpaceName())) {
            throw new ServiceException(500, "请填写空间名称");
        }
        if (ToolUtil.isEmpty(param.getType())){
            throw new ServiceException(500,"类型格式错误");
        }
        Integer count = this.lambdaQuery().eq(WxWedriveSpace::getType, param.getType()).eq(WxWedriveSpace::getSpaceName, param.getSpaceName()).eq(WxWedriveSpace::getDisplay, 1).count();
        if (count>0){
            throw new ServiceException(500,"此分类此名称文件夹已经存在");
        }

        List<UserResult> userResultsByIds = userService.getUserResultsByIds(param.getUserIds());
        for (UserResult userResultsById : userResultsByIds) {
            if (ToolUtil.isEmpty(userResultsById.getOpenId())){
                throw new ServiceException(500,userResultsById.getName()+" 的账号没绑定微信");
            }
        }
//        配置
        List<WxCpSpaceCreateRequest.AuthInfo> infoList = new ArrayList<>();
        for (UserResult userResultsById : userResultsByIds) {

            if (ToolUtil.isNotEmpty(userResultsById.getOpenId())) {
                WxCpSpaceCreateRequest.AuthInfo user = new WxCpSpaceCreateRequest.AuthInfo();
                user.setAuth(1);
                user.setType(1);
                user.setUserId(userResultsById.getOpenId());
                infoList.add(user);
            }

        }

        WxCpSpaceCreateRequest wxCpSpaceCreateRequest = new WxCpSpaceCreateRequest(null, param.getSpaceName(), infoList);
        WxCpSpaceCreateData wxCpSpaceCreateData = wxCpService.getWxCpClient().getOaWeDriveService().spaceCreate(wxCpSpaceCreateRequest);
        WxWedriveSpace entity = getEntity(param);
        entity.setSpaceId(wxCpSpaceCreateData.getSpaceId());
        entity.setSpaceName(param.getSpaceName());
        this.save(entity);
    }

    @Override
    public WxWedriveSpaceResult detail(String spaceId) throws WxErrorException {
        Long userId = LoginContextHolder.getContext().getUserId();
        List<UserResult> userResultList = userService.getUserResultsByIds(Collections.singletonList(userId));
        if (ToolUtil.isEmpty(userResultList.get(0).getOpenId())) {
            throw new ServiceException(500, "请先绑定微信");
        }


        if (ToolUtil.isEmpty(spaceId)) {
            throw new ServiceException(500, "参数错误");
        }
        WxWedriveSpace entity = this.getById(spaceId);
        if (ToolUtil.isEmpty(entity)) {
            throw new ServiceException(500, "数据未找到");
        }
        String apiUrl = this.wxCpService.getWxCpClient().getWxCpConfigStorage().getApiUrl(SPACE_INFO);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("spaceid", spaceId);
        String responseContent = this.wxCpService.getWxCpClient().post(apiUrl, jsonObject.toString());
        WxCpSpaceInfo wxCpSpaceInfo = WxCpSpaceInfo.fromJson(responseContent);
        WxWedriveSpaceResult wxWedriveSpaceResult = BeanUtil.copyProperties(entity, WxWedriveSpaceResult.class);
        wxWedriveSpaceResult.setWxCpSpaceInfo(wxCpSpaceInfo);
        return wxWedriveSpaceResult;

    }

    @Override
    public WxCpBaseResp spaceAclAdd(WxWedriveSpaceParam param) throws WxErrorException {
        if (ToolUtil.isEmpty(param.getSpaceId())) {
            throw new ServiceException(500, "参数错误");
        }
        if (ToolUtil.isEmpty(param.getUserIds())){
            throw new ServiceException(500,"请选择人员");
        }
        WxCpSpaceAclAddRequest request = new WxCpSpaceAclAddRequest();

        List<UserResult> userResultsByIds = userService.getUserResultsByIds(param.getUserIds());
        for (UserResult userResultsById : userResultsByIds) {
            if (ToolUtil.isEmpty(userResultsById.getOpenId())){
                throw new ServiceException(500,userResultsById.getName()+" 的账号没绑定微信");
            }
        }
//        配置
        List<WxCpSpaceAclAddRequest.AuthInfo> infoList = new ArrayList<>();
        for (UserResult userResultsById : userResultsByIds) {

            if (ToolUtil.isNotEmpty(userResultsById.getOpenId())) {
                WxCpSpaceAclAddRequest.AuthInfo user = new WxCpSpaceAclAddRequest.AuthInfo();
                user.setAuth(1);
                user.setType(1);
                user.setUserId(userResultsById.getOpenId());
                infoList.add(user);
            }

        }
        request.setAuthInfo(infoList);
        request.setSpaceId(param.getSpaceId());

        return wxCpService.getWxCpClient().getOaWeDriveService().spaceAclAdd(request);

    }

    @Override
    public WxCpBaseResp spaceAclDelete(WxWedriveSpaceParam param) throws WxErrorException {
        if (ToolUtil.isEmpty(param.getSpaceId())) {
            throw new ServiceException(500, "参数错误");
        }
        if (ToolUtil.isEmpty(param.getUserIds())){
            throw new ServiceException(500,"请选择人员");
        }
        List<UserResult> userResultsByIds = userService.getUserResultsByIds(param.getUserIds());
        WxCpSpaceAclDelRequest request = new WxCpSpaceAclDelRequest();


        List<WxCpSpaceAclDelRequest.AuthInfo> infoList = new ArrayList<>();
        for (UserResult userResultsById : userResultsByIds) {

            if (ToolUtil.isNotEmpty(userResultsById.getOpenId())) {
                WxCpSpaceAclDelRequest.AuthInfo user = new WxCpSpaceAclDelRequest.AuthInfo();
                user.setType(1);
                user.setUserId(userResultsById.getOpenId());
                infoList.add(user);
            }

        }
        request.setAuthInfo(infoList);
        request.setSpaceId(param.getSpaceId());
        return wxCpService.getWxCpClient().getOaWeDriveService().spaceAclDel(request);

    }

    @Override
    public void delete(WxWedriveSpaceParam param) throws WxErrorException {
        WxWedriveSpace oldEntity = this.getOldEntity(param);
        if (ToolUtil.isEmpty(oldEntity)){
            throw new ServiceException(500,"参数错误");
        }
        oldEntity.setDisplay(0);


        String apiUrl = wxCpService.getWxCpClient().getWxCpConfigStorage().getApiUrl(SPACE_DISMISS);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("spaceid", oldEntity.getSpaceId());
        String responseContent = this. wxCpService.getWxCpClient().post(apiUrl, jsonObject.toString());
        this.updateById(oldEntity);
        WxCpBaseResp.fromJson(responseContent);
        log.debug("删除微盘空间" + responseContent);

    }

    @Override
    public void update(WxWedriveSpaceParam param) {
        WxWedriveSpace oldEntity = getOldEntity(param);
        WxWedriveSpace newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public WxWedriveSpaceResult findBySpec(WxWedriveSpaceParam param) {
        return null;
    }

    @Override
    public List<WxWedriveSpaceResult> findListBySpec(WxWedriveSpaceParam param) {
        return null;
    }

    @Override
    public PageInfo<WxWedriveSpaceResult> findPageBySpec(WxWedriveSpaceParam param) {
        Page<WxWedriveSpaceResult> pageContext = getPageContext();
        IPage<WxWedriveSpaceResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(WxWedriveSpaceParam param) {
        return param.getSpaceId();
    }

    private Page<WxWedriveSpaceResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private WxWedriveSpace getOldEntity(WxWedriveSpaceParam param) {
        return this.getById(getKey(param));
    }

    private WxWedriveSpace getEntity(WxWedriveSpaceParam param) {
        WxWedriveSpace entity = new WxWedriveSpace();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
    @Override
    public WxCpBaseResp rename(WxWedriveSpaceParam param) throws WxErrorException {
        WxWedriveSpace space = this.getById(param.getSpaceId());
        if (ToolUtil.isEmpty(space)){
            throw new ServiceException(500,"参数错误");
        }
        if (param.getSpaceName().length()>255){
            throw new ServiceException(500,"名字不能过长");
        }
        Integer count = this.lambdaQuery().eq(WxWedriveSpace::getType, param.getType()).eq(WxWedriveSpace::getSpaceName, param.getSpaceName()).eq(WxWedriveSpace::getDisplay, 1).count();
        if (count>0){
            throw new ServiceException(500,"此分类此名称文件夹已经存在");
        }
        space.setSpaceName(param.getSpaceName());

        WxCpSpaceRenameRequest wxCpSpaceRenameRequest = new WxCpSpaceRenameRequest();
        wxCpSpaceRenameRequest.setSpaceId(param.getSpaceId());
        wxCpSpaceRenameRequest.setSpaceName(space.getSpaceName());
        WxCpBaseResp wxCpBaseResp = wxCpService.getWxCpClient().getOaWeDriveService().spaceRename(wxCpSpaceRenameRequest);
        this.updateById(space);
        return wxCpBaseResp;
    }

}
