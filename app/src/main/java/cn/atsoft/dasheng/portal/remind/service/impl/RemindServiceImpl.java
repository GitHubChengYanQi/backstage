package cn.atsoft.dasheng.portal.remind.service.impl;


import cn.atsoft.dasheng.app.model.result.DeliveryDetailsResult;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.common_area.entity.CommonArea;
import cn.atsoft.dasheng.portal.banner.entity.Banner;
import cn.atsoft.dasheng.portal.banner.model.result.BannerResult;
import cn.atsoft.dasheng.portal.bannerdifference.entity.BannerDifference;
import cn.atsoft.dasheng.portal.bannerdifference.model.result.BannerDifferenceResult;
import cn.atsoft.dasheng.portal.remind.entity.Remind;
import cn.atsoft.dasheng.portal.remind.mapper.RemindMapper;
import cn.atsoft.dasheng.portal.remind.model.params.RemindParam;
import cn.atsoft.dasheng.portal.remind.model.result.RemindResult;
import  cn.atsoft.dasheng.portal.remind.service.RemindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.repair.model.result.RepairResult;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * 提醒表 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-08-26
 */
@Service
public class RemindServiceImpl extends ServiceImpl<RemindMapper, Remind> implements RemindService {

    @Autowired
    private UserService userService;

    @Override
    public void add(RemindParam param){

        List<Long> users = param.getUsers();
        List<Remind> list = new ArrayList<>();
        for (Long user : users) {
            Remind result = new Remind();
            result.setType(param.getType());
            result.setUserId(user);
            list.add(result);
        }
        this.saveBatch(list);
    }

    @Override
    public void delete(RemindParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(RemindParam param){
        Remind oldEntity = getOldEntity(param);
        Remind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RemindResult findBySpec(RemindParam param){
        return null;
    }

    @Override
    public List<RemindResult> findListBySpec(RemindParam param){
        return null;
    }

    @Override
    public PageInfo<RemindResult> findPageBySpec(RemindParam param){
        Page<RemindResult> pageContext = getPageContext();
        IPage<RemindResult> page = this.baseMapper.customPageList(pageContext, param);


        for (RemindResult record : page.getRecords()) {
            QueryWrapper<Remind> remindQueryWrapper = new QueryWrapper<>();
            remindQueryWrapper.in("type",record.getType());
             List<Remind> list = this.list(remindQueryWrapper);
            List<Long> longs = new ArrayList<>();
            for (Remind remind : list) {
                longs.add(remind.getUserId());
            }
            QueryWrapper<User> differenceQueryWrapper = new QueryWrapper<>();
            differenceQueryWrapper.in("user_id", longs);
            List<User> lists =longs.size() == 0 ? new ArrayList<>() :  userService.list(differenceQueryWrapper);
            record.setUsers(lists);
        }


        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RemindParam param){
        return param.getRemindId();
    }

    private Page<RemindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Remind getOldEntity(RemindParam param) {
        return this.getById(getKey(param));
    }

    private Remind getEntity(RemindParam param) {
        Remind entity = new Remind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
