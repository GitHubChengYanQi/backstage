package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.crm.entity.Data;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.erp.model.result.ToolResult;
import cn.atsoft.dasheng.erp.service.ToolService;
import cn.atsoft.dasheng.production.entity.*;
import cn.atsoft.dasheng.production.mapper.ShipSetpMapper;
import cn.atsoft.dasheng.production.model.params.ShipSetpBindParam;
import cn.atsoft.dasheng.production.model.params.ShipSetpParam;
import cn.atsoft.dasheng.production.model.params.SopBindParam;
import cn.atsoft.dasheng.production.model.result.ShipSetpBindResult;
import cn.atsoft.dasheng.production.model.result.ShipSetpClassResult;
import cn.atsoft.dasheng.production.model.result.ShipSetpResult;
import cn.atsoft.dasheng.production.model.result.SopResult;
import cn.atsoft.dasheng.production.service.ShipSetpBindService;
import cn.atsoft.dasheng.production.service.ShipSetpClassService;
import cn.atsoft.dasheng.production.service.ShipSetpService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.production.service.SopService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工序表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
@Service
public class ShipSetpServiceImpl extends ServiceImpl<ShipSetpMapper, ShipSetp> implements ShipSetpService {

    @Autowired
    private UserService userService;

    @Autowired
    private ShipSetpBindService shipSetpBindService;

    @Autowired
    private ToolService toolService;

    @Autowired
    private ShipSetpClassService shipSetpClassService;

    @Autowired
    private SopService sopService;
    @Autowired
    private SopBindServiceImpl sopBindService;

    @Override
    public void add(ShipSetpParam param) {
        ShipSetp entity = getEntity(param);
        this.save(entity);
        if (ToolUtil.isNotEmpty(param.getBinds())) {
            List<ShipSetpBind> bindEntityList = new ArrayList<>();
            for (ShipSetpBindParam bindParam : param.getBinds()) {
                bindParam.setShipSetpId(entity.getShipSetpId());
                ShipSetpBind bind = new ShipSetpBind();
                ToolUtil.copyProperties(bindParam, bind);
                bindEntityList.add(bind);
            }
            shipSetpBindService.saveBatch(bindEntityList);
        }
        if (ToolUtil.isNotEmpty(param.getSopId())) {
            SopBind sopBind = new SopBind();
            sopBind.setSopId(param.getSopId());
            sopBind.setShipSetpId(entity.getShipSetpId());
            sopBindService.save(sopBind);
//            sopService.addShip(param.getSopId(), entity.getShipSetpId());
        }
    }

    @Override
    public void delete(ShipSetpParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ShipSetpParam param) {
        ShipSetp oldEntity = getOldEntity(param);
        ShipSetp newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        if (ToolUtil.isNotEmpty(param.getBinds())) {
            List<ShipSetpBind> binds = shipSetpBindService.query().eq("ship_setp_id", oldEntity.getShipSetpId()).eq("display", 1).list();
            for (ShipSetpBind bind : binds) {
                bind.setDisplay(0);
            }
            shipSetpBindService.updateBatchById(binds);//删除老的绑定
            List<ShipSetpBind> bindEntityList = new ArrayList<>();
            for (ShipSetpBindParam bindParam : param.getBinds()) {
                bindParam.setShipSetpId(oldEntity.getShipSetpId());
                ShipSetpBind bind = new ShipSetpBind();
                ToolUtil.copyProperties(bindParam, bind);
                bind.setShipSetpBindId(null); //把传入数据主键滞空，不然保存报错 传入数据将之前的绑定表数据原封不动带入 带有主键无法保存
                bindEntityList.add(bind);
            }
            shipSetpBindService.saveBatch(bindEntityList);
        }
        if (ToolUtil.isNotEmpty(param.getSopId())) {
            sopBindService.update(new SopBindParam() {{
                setSopId(param.getSopId());
                setShipSetpId(param.getShipSetpId());
            }});
//            sopService.addShip(param.getSopId(), oldEntity.getShipSetpId());
        }
        this.updateById(newEntity);

    }

    @Override
    public ShipSetpResult findBySpec(ShipSetpParam param) {
        return null;
    }

    @Override
    public List<ShipSetpResult> findListBySpec(ShipSetpParam param) {
        return null;
    }

    @Override
    public PageInfo<ShipSetpResult> findPageBySpec(ShipSetpParam param, DataScope dataScope) {
        Page<ShipSetpResult> pageContext = getPageContext();
        IPage<ShipSetpResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void format(List<ShipSetpResult> param) {
        List<Long> createUserIds = new ArrayList<>();
        List<Long> shipSetpClassIds = new ArrayList<>();
        List<Long> shipSetpIds = new ArrayList<>();
        for (ShipSetpResult shipSetpResult : param) {
            createUserIds.add(shipSetpResult.getCreateUser());
            shipSetpClassIds.add(shipSetpResult.getShipSetpClassId());
            shipSetpIds.add(shipSetpResult.getShipSetpId());
        }


        //查询工具&设备绑定表
        List<ShipSetpBind> shipSetpBindList = shipSetpIds.size() == 0 ? new ArrayList<>() : shipSetpBindService.query().in("ship_setp_id", shipSetpIds).eq("display", 1).list();
        List<Long> toolIds = new ArrayList<>();
        List<ShipSetpBindResult> shipSetpBindResultList = new ArrayList<>();
        for (ShipSetpBind shipSetpBind : shipSetpBindList) {
            ShipSetpBindResult shipSetpBindResult = new ShipSetpBindResult();
            ToolUtil.copyProperties(shipSetpBind, shipSetpBindResult);
            shipSetpBindResultList.add(shipSetpBindResult);
            toolIds.add(shipSetpBind.getFromId());
        }
        List<Tool> tools = toolIds.size() == 0 ? new ArrayList<>() : toolService.listByIds(toolIds);
        List<ToolResult> toolResults = new ArrayList<>();
        for (Tool tool : tools) {
            ToolResult toolResult = new ToolResult();
            ToolUtil.copyProperties(tool, toolResult);
            toolResults.add(toolResult);
        }
        for (ShipSetpBindResult shipSetpBindResult : shipSetpBindResultList) {
            for (ToolResult toolResult : toolResults) {
                if (ToolUtil.isNotEmpty(shipSetpBindResult.getFromId()) && shipSetpBindResult.getFromId().equals(toolResult.getToolId())) {
                    shipSetpBindResult.setToolResult(toolResult);
                }
            }
        }

        //查询SOP
        Map<Long, SopResult> serviceSop = sopBindService.getSop(shipSetpIds);


        //查询创建人
        List<User> userList = createUserIds.size() == 0 ? new ArrayList<>() : userService.listByIds(createUserIds);

        //返回工序分类
        List<ShipSetpClass> shipSetpClassList = shipSetpClassIds.size() == 0 ? new ArrayList<>() : shipSetpClassService.listByIds(shipSetpClassIds);
        /**
         * 匹配需要返回的数据
         */
        for (ShipSetpResult shipSetpResult : param) {
            //返回创建人
            for (User user : userList) {
                if (shipSetpResult.getCreateUser().equals(user.getUserId())) {
                    UserResult userResult = new UserResult();
                    ToolUtil.copyProperties(user, userResult);
                    shipSetpResult.setUserResult(userResult);
                }
            }
            //返回工序分类
            for (ShipSetpClass shipSetpClass : shipSetpClassList) {
                if (shipSetpResult.getShipSetpClassId().equals(shipSetpClass.getShipSetpClassId())) {
                    ShipSetpClassResult shipSetpClassResult = new ShipSetpClassResult();
                    ToolUtil.copyProperties(shipSetpClass, shipSetpClassResult);
                    shipSetpResult.setShipSetpClassResult(shipSetpClassResult);
                }
            }
            //返回绑定工序
            List<ShipSetpBindResult> shipSetpBindResults = new ArrayList<>();
            for (ShipSetpBindResult shipSetpBindResult : shipSetpBindResultList) {
                if (shipSetpResult.getShipSetpId().equals(shipSetpBindResult.getShipSetpId())) {
                    shipSetpBindResults.add(shipSetpBindResult);
                }

                shipSetpResult.setBinds(shipSetpBindResults);
            }

            SopResult result = serviceSop.get(shipSetpResult.getShipSetpId());
            shipSetpResult.setSopResult(result);


        }


    }

    private Serializable getKey(ShipSetpParam param) {
        return param.getShipSetpId();
    }

    private Page<ShipSetpResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ShipSetp getOldEntity(ShipSetpParam param) {
        return this.getById(getKey(param));
    }

    private ShipSetp getEntity(ShipSetpParam param) {
        ShipSetp entity = new ShipSetp();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
