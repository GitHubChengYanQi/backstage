package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.appBase.entity.Media;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ShipSetp;
import cn.atsoft.dasheng.production.entity.Sop;
import cn.atsoft.dasheng.production.entity.SopDetail;
import cn.atsoft.dasheng.production.mapper.SopMapper;
import cn.atsoft.dasheng.production.model.params.SopDetailParam;
import cn.atsoft.dasheng.production.model.params.SopParam;
import cn.atsoft.dasheng.production.model.result.ShipSetpResult;
import cn.atsoft.dasheng.production.model.result.SopDetailResult;
import cn.atsoft.dasheng.production.model.result.SopResult;
import cn.atsoft.dasheng.production.service.ShipSetpService;
import cn.atsoft.dasheng.production.service.SopBindService;
import cn.atsoft.dasheng.production.service.SopDetailService;
import cn.atsoft.dasheng.production.service.SopService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * sop 主表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
@Service
public class SopServiceImpl extends ServiceImpl<SopMapper, Sop> implements SopService {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private SopDetailService sopDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShipSetpService shipSetpService;

    @Autowired
    private SopBindService sopBindService;

    @Override
    public Long add(SopParam param) {
        Sop entity = getEntity(param);
        this.save(entity);

        for (SopDetailParam sopDetailParam : param.getSopDetails()) {
            sopDetailParam.setSopId(entity.getSopId());
        }

        List<SopDetail> sopDetails = BeanUtil.copyToList(param.getSopDetails(), SopDetail.class, new CopyOptions());
        sopDetailService.saveBatch(sopDetails);
        return entity.getSopId();
    }

    @Override
    public void delete(SopParam param) {
        if (ToolUtil.isEmpty(param.getSopId())) {
            throw new ServiceException(500,"删除数据不存在");
        }else {
            param.setDisplay(0);
            this.update(param);
        }
    }

    @Override
    public void update(SopParam param) {
        Long oldId = param.getSopId();
        String alter = param.getAlterWhy();

        for (SopDetailParam sopDetail : param.getSopDetails()) {
            sopDetail.setSopDetailId(null);
        }

        param.setAlterWhy(null);
        param.setSopId(null);
        Long newId = add(param);

        List<SopResult> oldSopByPid = this.getOldSopByPid(oldId);   //修改当前下 所有的修改记录
        if (ToolUtil.isNotEmpty(oldSopByPid)) {
            List<Sop> sops = BeanUtil.copyToList(oldSopByPid, Sop.class, new CopyOptions());
            for (Sop sop : sops) {
                sop.setPid(newId);
            }
            this.updateBatchById(sops);
        }

        Sop sop = new Sop();  //修改记录
        sop.setSopId(oldId);
        sop.setPid(newId);
        sop.setDisplay(0);
        sop.setAlterWhy(alter);
        this.updateById(sop);
    }

    @Override
    public SopResult detail(Long sopId) {
        Sop sop = this.getById(sopId);
        SopResult sopResult = new SopResult();
        ToolUtil.copyProperties(sop, sopResult);

        List<SopDetailResult> resultBySopId = sopDetailService.getResultBySopId(sop.getSopId()); //获取详情

        List<SopResult> oldSop = getOldSopByPid(sop.getSopId());  //获取修改记录
        List<Long> imgId = new ArrayList<>();
        if (ToolUtil.isNotEmpty(sopResult.getFinishedPicture())) {
            imgId = Arrays.stream(sopResult.getFinishedPicture().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        }
        List<String> mediaUrls = new ArrayList<>();
        for (Long aLong : imgId) {
            String mediaUrl = mediaService.getMediaUrl(aLong, 0L); //成品图
            mediaUrls.add(mediaUrl);
        }
        sopResult.setMediaUrls(mediaUrls);
        sopResult.setOldSop(oldSop);
        sopResult.setSopDetails(resultBySopId);
        return sopResult;
    }

    /**
     * 获取多个图片路径
     *
     * @param ids
     * @return
     */
    @Override
    public List<String> getImgUrls(List<Long> ids) {
        List<String> mediaUrls = new ArrayList<>();
        for (Long id : ids) {
            String mediaUrl = mediaService.getMediaUrl(id, 0L); //成品图
            mediaUrls.add(mediaUrl);
        }
        return mediaUrls;
    }

    /**
     * 添加工序
     *
     * @param sopId
     * @param shipId
     */
    @Override
    public void addShip(Long sopId, Long shipId) {
        List<Sop> sops = this.query().eq("ship_setp_id", shipId).eq("display", 1).list();
        if (ToolUtil.isNotEmpty(sops)) {    //sop 更换绑定工序
            for (Sop sop : sops) {
                sop.setShipSetpId(null);
            }
            this.updateBatchById(sops);
        }
        Sop sop = new Sop();
        sop.setSopId(sopId);
        sop.setShipSetpId(shipId);
        this.updateById(sop);
    }

    @Override
    public SopResult findBySpec(SopParam param) {
        return null;
    }

    @Override
    public List<SopResult> findListBySpec(SopParam param) {
        List<SopResult> sopResults = this.baseMapper.customList(param);
        format(sopResults);
        return sopResults;
    }

    @Override
    public PageInfo<SopResult> findPageBySpec(SopParam param) {
        Page<SopResult> pageContext = getPageContext();
        IPage<SopResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SopParam param) {
        return param.getSopId();
    }

    private Page<SopResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    /**
     * 获取修改记录
     *
     * @param pid
     * @return
     */
    private List<SopResult> getOldSopByPid(Long pid) {
        if (ToolUtil.isEmpty(pid)) {
            return new ArrayList<>();
        }
        List<Sop> sops = this.query().eq("pid", pid).list();
        if (ToolUtil.isEmpty(sops)) {
            return new ArrayList<>();
        }
        List<SopResult> sopResults = BeanUtil.copyToList(sops, SopResult.class, new CopyOptions());

        List<Long> userIds = new ArrayList<>();
        for (SopResult sopResult : sopResults) {
            userIds.add(sopResult.getCreateUser());
        }
        List<UserResult> users =userIds.size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(userIds);

        for (SopResult sopResult : sopResults) {
            for (UserResult user : users) {
                if (user.getUserId().equals(sopResult.getCreateUser())) {
                    sopResult.setUser(user);
                    break;
                }
            }
        }

        return sopResults;
    }

    private Sop getOldEntity(SopParam param) {
        return this.getById(getKey(param));
    }

    private Sop getEntity(SopParam param) {
        Sop entity = new Sop();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void format(List<SopResult> data) {

        List<Long> userIds = new ArrayList<>();
        List<Long> sopIds = new ArrayList<>();
        for (SopResult datum : data) {
            userIds.add(datum.getCreateUser());
            sopIds.add(datum.getSopId());
        }
        Map<Long, List<ShipSetpResult>> shipMap = sopBindService.getShipSetp(sopIds);
        List<UserResult> users = (userIds.size() == 0) ? new ArrayList<>() : userService.getUserResultsByIds(userIds);

        for (SopResult datum : data) {

            for (UserResult user : users) {
                if (user.getUserId().equals(datum.getCreateUser())) {
                    datum.setUser(user);
                    break;
                }
            }

            List<ShipSetpResult> shipSetpResults = shipMap.get(datum.getSopId());
            datum.setShipSetpResults(shipSetpResults);
        }

    }

}
