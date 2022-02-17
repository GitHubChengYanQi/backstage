package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ShipSetp;
import cn.atsoft.dasheng.production.entity.Sop;
import cn.atsoft.dasheng.production.entity.SopBind;
import cn.atsoft.dasheng.production.mapper.SopBindMapper;
import cn.atsoft.dasheng.production.model.params.SopBindParam;
import cn.atsoft.dasheng.production.model.result.ShipSetpResult;
import cn.atsoft.dasheng.production.model.result.SopBindResult;
import cn.atsoft.dasheng.production.model.result.SopResult;
import cn.atsoft.dasheng.production.service.ShipSetpService;
import cn.atsoft.dasheng.production.service.SopBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.production.service.SopService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * sop 绑定 工序 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-17
 */
@Service
public class SopBindServiceImpl extends ServiceImpl<SopBindMapper, SopBind> implements SopBindService {
    @Autowired
    private ShipSetpService shipSetpService;

    @Autowired
    private SopService sopService;


    @Override
    public void add(SopBindParam param) {
        SopBind entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(SopBindParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(SopBindParam param) {
        SopBind sopBind = this.query().eq("ship_setp_id", param.getShipSetpId()).one();
        if (ToolUtil.isNotEmpty(sopBind)) {
            this.removeById(sopBind);
        }
        add(param);
    }

    @Override
    public SopBindResult findBySpec(SopBindParam param) {
        return null;
    }

    @Override
    public List<SopBindResult> findListBySpec(SopBindParam param) {
        return null;
    }

    @Override
    public PageInfo<SopBindResult> findPageBySpec(SopBindParam param) {
        Page<SopBindResult> pageContext = getPageContext();
        IPage<SopBindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SopBindParam param) {
        return param.getBindId();
    }

    private Page<SopBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private SopBind getOldEntity(SopBindParam param) {
        return this.getById(getKey(param));
    }

    private SopBind getEntity(SopBindParam param) {
        SopBind entity = new SopBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public Map<Long, SopResult> getSop(List<Long> shipId) {
        if (ToolUtil.isEmpty(shipId)) {
            return new HashMap<>();
        }
        List<SopBind> sopBinds = this.query().in("ship_setp_id", shipId).list();
        if (ToolUtil.isEmpty(sopBinds)) {
            return new HashMap<>();
        }

        List<Sop> sopList = sopService.query().in("sop_id", new ArrayList<Long>() {{
            for (SopBind sopBind : sopBinds) {
                add(sopBind.getSopId());
            }
        }}).list();

        List<SopResult> results = BeanUtil.copyToList(sopList, SopResult.class, new CopyOptions());
        sopService.format(results);
        Map<Long, SopResult> sopResultMap = new HashMap<>();
        for (SopBind sopBind : sopBinds) {
            for (SopResult result : results) {
                if (sopBind.getSopId().equals(result.getSopId())) {
                    sopResultMap.put(sopBind.getShipSetpId(), result);
                    break;
                }
            }
        }
        return sopResultMap;
    }

    @Override
    public Map<Long, List<ShipSetpResult>> getShipSetp(List<Long> sopId) {
        if (ToolUtil.isEmpty(sopId)) {
            return new HashMap<>();
        }
        List<SopBind> bindList = this.query().in("sop_id", sopId).list();
        if (ToolUtil.isEmpty(bindList)) {
            return new HashMap<>();
        }
        List<ShipSetp> shipSetps = shipSetpService.listByIds(new ArrayList<Long>() {{
            for (SopBind sopBind : bindList) {
                add(sopBind.getShipSetpId());
            }
        }});
        List<ShipSetpResult> setpResults = BeanUtil.copyToList(shipSetps, ShipSetpResult.class, new CopyOptions());

        Map<Long, List<ShipSetpResult>> map = new HashMap<>();
        for (SopBind sopBind : bindList) {
            List<ShipSetpResult> shipSetpResults = new ArrayList<>();
            for (ShipSetpResult setpResult : setpResults) {
                if (sopBind.getShipSetpId().equals(setpResult.getShipSetpId())) {
                    shipSetpResults.add(setpResult);
                }
            }
            map.put(sopBind.getSopId(), shipSetpResults);
        }
        return map;
    }
}
