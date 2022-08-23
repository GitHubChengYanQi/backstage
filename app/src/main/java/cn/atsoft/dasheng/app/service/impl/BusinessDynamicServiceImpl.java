package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.BusinessDynamic;
import cn.atsoft.dasheng.app.mapper.BusinessDynamicMapper;
import cn.atsoft.dasheng.app.model.params.BusinessDynamicParam;
import cn.atsoft.dasheng.app.model.result.BusinessDynamicResult;
import cn.atsoft.dasheng.app.service.BusinessDynamicService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
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
 * 商机动态表 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-08-10
 */
@Service
public class BusinessDynamicServiceImpl extends ServiceImpl<BusinessDynamicMapper, BusinessDynamic> implements BusinessDynamicService {
    @Autowired
    private UserService userService;

    @Override
    public void add(BusinessDynamicParam param) {
        BusinessDynamic entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(BusinessDynamicParam param) {
        BusinessDynamic byId = this.getById(param.getDynamicId());
        if (ToolUtil.isEmpty(byId)) {
            throw new ServiceException(500, "所删除目标不存在");
        } else {
            param.setDisplay(0);
            this.update(param);
        }
    }

    @Override
    public void update(BusinessDynamicParam param) {
        BusinessDynamic oldEntity = getOldEntity(param);
        BusinessDynamic newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public BusinessDynamicResult findBySpec(BusinessDynamicParam param) {
        return null;
    }

    @Override
    public List<BusinessDynamicResult> findListBySpec(BusinessDynamicParam param) {
        return null;
    }

    @Override
    public PageInfo findPageBySpec(BusinessDynamicParam param, DataScope dataScope) {
        Page<BusinessDynamicResult> pageContext = getPageContext();
        IPage<BusinessDynamicResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(BusinessDynamicParam param) {
        return param.getDynamicId();
    }

    private Page<BusinessDynamicResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private BusinessDynamic getOldEntity(BusinessDynamicParam param) {
        return this.getById(getKey(param));
    }

    private BusinessDynamic getEntity(BusinessDynamicParam param) {
        BusinessDynamic entity = new BusinessDynamic();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<BusinessDynamicResult> data) {
        List<Long> Ids = new ArrayList<>();
        for (BusinessDynamicResult datum : data) {
            Ids.add(datum.getCreateUser());
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("user_id", Ids);
        List<User> userList = Ids.size() == 0 ? new ArrayList<>() : userService.list(userQueryWrapper);

        for (BusinessDynamicResult datum : data) {
            for (User user : userList) {
                if (datum.getCreateUser().equals(user.getUserId())) {
                    UserResult userResult = new UserResult();
                    ToolUtil.copyProperties(user, userResult);
                    datum.setUserResult(userResult);
                    break;
                }
            }
        }
    }
}
