package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ShipSetp;
import cn.atsoft.dasheng.production.entity.ShipSetpClass;
import cn.atsoft.dasheng.production.mapper.ShipSetpMapper;
import cn.atsoft.dasheng.production.model.params.ShipSetpParam;
import cn.atsoft.dasheng.production.model.result.ShipSetpClassResult;
import cn.atsoft.dasheng.production.model.result.ShipSetpResult;
import cn.atsoft.dasheng.production.service.ShipSetpClassService;
import cn.atsoft.dasheng.production.service.ShipSetpService;
import cn.atsoft.dasheng.core.util.ToolUtil;
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
    private ShipSetpClassService shipSetpClassService;
    @Override
    public void add(ShipSetpParam param) {
        ShipSetp entity = getEntity(param);
        this.save(entity);
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
    public PageInfo<ShipSetpResult> findPageBySpec(ShipSetpParam param) {
        Page<ShipSetpResult> pageContext = getPageContext();
        IPage<ShipSetpResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private void format(List<ShipSetpResult> param) {
        List<Long> createUserIds = new ArrayList<>();
        List<Long> shipSetpClassIds = new ArrayList<>();
        for (ShipSetpResult shipSetpResult : param) {
            createUserIds.add(shipSetpResult.getCreateUser());
            shipSetpClassIds.add(shipSetpResult .getShipSetpClassId());
        }
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
               if (shipSetpResult.getCreateUser().equals(user.getUserId())){
                   UserResult userResult = new UserResult();
                   ToolUtil.copyProperties(user,userResult);
                   shipSetpResult.setUserResult(userResult);
               }
            }
            //返回工序分类
            for (ShipSetpClass shipSetpClass : shipSetpClassList) {
                if (shipSetpResult.getShipSetpClassId().equals(shipSetpClass.getShipSetpClassId())) {
                    ShipSetpClassResult shipSetpClassResult = new ShipSetpClassResult() ;
                    ToolUtil.copyProperties(shipSetpClass,shipSetpClassResult);
                    shipSetpResult.setShipSetpClassResult(shipSetpClassResult);
                }
            }
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
