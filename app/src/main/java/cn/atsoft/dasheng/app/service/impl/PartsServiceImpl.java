package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.model.params.PartRequest;
import cn.atsoft.dasheng.app.model.params.PartsAttribute;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.mapper.PartsMapper;
import cn.atsoft.dasheng.app.model.params.PartsParam;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.JsonArray;
import org.beetl.ext.fn.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 清单 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-21
 */
@Service
public class PartsServiceImpl extends ServiceImpl<PartsMapper, Parts> implements PartsService {

    @Autowired
    private UserService userService;

    @Override
    public void add(PartRequest partRequest) {
        QueryWrapper<Parts> partsQueryWrapper = new QueryWrapper<>();
        partsQueryWrapper.in("pid", partRequest.getPid());
        this.remove(partsQueryWrapper);
        List<Parts> partsList = new ArrayList<>();
        for (PartsParam part : partRequest.getParts()) {
            Parts parts = new Parts();
            parts.setPid(partRequest.getPid());
            parts.setSpuId(part.getSpuId());
            parts.setNote(part.getNote());
            parts.setNumber(part.getNumber());
            String toJsonStr = JSONUtil.toJsonStr(part.getPartsAttributes());
            parts.setAttribute(toJsonStr);
            partsList.add(parts);
        }
        this.saveBatch(partsList);
    }

    @Override
    public void delete(PartsParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(PartsParam param) {
        Parts oldEntity = getOldEntity(param);
        Parts newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PartsResult findBySpec(PartsParam param) {
        return null;
    }

    @Override
    public List<PartsResult> findListBySpec(PartsParam param) {
        return null;
    }

    @Override
    public PageInfo<PartsResult> findPageBySpec(PartsParam param) {
        Page<PartsResult> pageContext = getPageContext();
        IPage<PartsResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PartsParam param) {
        return param.getPartsId();
    }

    private Page<PartsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Parts getOldEntity(PartsParam param) {
        return this.getById(getKey(param));
    }

    private Parts getEntity(PartsParam param) {
        Parts entity = new Parts();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<PartsResult> data) {
        List<Long> pids = new ArrayList<>();
        List<Long> userIds = new ArrayList();
        for (PartsResult datum : data) {
            pids.add(datum.getPartsId());
            userIds.add(datum.getCreateUser());
        }
        List<Parts> parts = pids.size() == 0 ? new ArrayList<>() : this.lambdaQuery().in(Parts::getPartsId, pids).list();
        List<User> users =userIds.size()==0? new ArrayList<>(): userService.query().in("user_id", userIds).list();

        for (PartsResult datum : data) {
            List<PartsResult> partsResults = new ArrayList<>();
            for (Parts part : parts) {
                if (datum.getPartsId() != null && datum.getPartsId().equals(part.getPid())) {
                    PartsResult partsResult = new PartsResult();
                    ToolUtil.copyProperties(part, partsResult);
                    partsResults.add(partsResult);
                }
            }
            datum.setPartsResults(partsResults);
            if (ToolUtil.isNotEmpty(datum.getAttribute())) {
                datum.setPartsAttributes(datum.getAttribute());
            }
            for (User user : users) {
                if (user.getUserId().equals(datum.getCreateUser())) {
                    UserResult userResult = new UserResult();
                    ToolUtil.copyProperties(user, userResult);
                    datum.setUserResult(userResult);
                    break;
                }
            }
        }
    }
}
