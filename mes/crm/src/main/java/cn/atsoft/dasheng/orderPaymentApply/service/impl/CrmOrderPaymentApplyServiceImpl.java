package cn.atsoft.dasheng.orderPaymentApply.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orderPaymentApply.entity.CrmOrderPaymentApply;
import cn.atsoft.dasheng.orderPaymentApply.mapper.CrmOrderPaymentApplyMapper;
import cn.atsoft.dasheng.orderPaymentApply.model.params.CrmOrderPaymentApplyParam;
import cn.atsoft.dasheng.orderPaymentApply.model.result.CrmOrderPaymentApplyResult;
import cn.atsoft.dasheng.orderPaymentApply.service.CrmOrderPaymentApplyService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.chanjar.weixin.cp.bean.oa.WxCpOaApplyEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-18
 */
@Service
public class CrmOrderPaymentApplyServiceImpl extends ServiceImpl<CrmOrderPaymentApplyMapper, CrmOrderPaymentApply> implements CrmOrderPaymentApplyService {

    @Autowired
    private UserService userService;


    @Override
    public void add(CrmOrderPaymentApplyParam param){
        CrmOrderPaymentApply entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CrmOrderPaymentApplyParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(CrmOrderPaymentApplyParam param){
        CrmOrderPaymentApply oldEntity = getOldEntity(param);
        CrmOrderPaymentApply newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CrmOrderPaymentApplyResult findBySpec(CrmOrderPaymentApplyParam param){
        return null;
    }
    @Override
    public CrmOrderPaymentApplyResult detail(CrmOrderPaymentApplyParam param){
        if (ToolUtil.isEmpty(param.getSpNo())){
            throw new ServiceException(500,"参数错误,请联系管理员");
        }



        CrmOrderPaymentApplyResult crmOrderPaymentApplyResult = this.baseMapper.getOneById(param.getSpNo());
        if (ToolUtil.isEmpty(crmOrderPaymentApplyResult)){
            throw new ServiceException(500,"参数错误,请联系管理员");
        }
        this.format(new ArrayList<CrmOrderPaymentApplyResult>(){{add(crmOrderPaymentApplyResult);}});
        crmOrderPaymentApplyResult.setApplyEventRequest(JSON.parseObject(crmOrderPaymentApplyResult.getMsg(),WxCpOaApplyEventRequest.class));
        return crmOrderPaymentApplyResult;
    }

    @Override
    public List<CrmOrderPaymentApplyResult> findListBySpec(CrmOrderPaymentApplyParam param){
        return null;
    }

    @Override
    public PageInfo<CrmOrderPaymentApplyResult> findPageBySpec(DataScope dataScope,CrmOrderPaymentApplyParam param){
        Page<CrmOrderPaymentApplyResult> pageContext = getPageContext();
        IPage<CrmOrderPaymentApplyResult> page = this.baseMapper.customPageList(dataScope,pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }
    public void format(List<CrmOrderPaymentApplyResult> dataList){
        List<Long> userIdList = dataList.stream().map(CrmOrderPaymentApplyResult::getCreatorUser).distinct().collect(Collectors.toList());
        List<Long> orderIdList = dataList.stream().map(CrmOrderPaymentApplyResult::getOrderId).distinct().collect(Collectors.toList());

        List<UserResult> userList = userIdList.size()== 0 ? new ArrayList<>() : userService.getUserResultsByIds(userIdList);
        for (CrmOrderPaymentApplyResult data : dataList) {
            for (UserResult  user : userList) {
                if (user.getUserId().equals(data.getCreatorUser())){
                    data.setUserResult(user);
                    break;
                }
            }
            data.setApplyEventRequest(JSON.parseObject(data.getMsg(), WxCpOaApplyEventRequest.class));
        }



    }
    private Serializable getKey(CrmOrderPaymentApplyParam param){
        return param.getSpNo();
    }

    private Page<CrmOrderPaymentApplyResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CrmOrderPaymentApply getOldEntity(CrmOrderPaymentApplyParam param) {
        return this.getById(getKey(param));
    }

    private CrmOrderPaymentApply getEntity(CrmOrderPaymentApplyParam param) {
        CrmOrderPaymentApply entity = new CrmOrderPaymentApply();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
