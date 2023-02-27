package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.purchase.entity.RestOrder;
import cn.atsoft.dasheng.purchase.mapper.RestOrderMapper;
import cn.atsoft.dasheng.purchase.model.params.RestOrderParam;
import cn.atsoft.dasheng.purchase.model.result.RestOrderResult;
import cn.atsoft.dasheng.purchase.model.result.RestOrderSimpleResult;
import cn.atsoft.dasheng.purchase.service.RestOrderService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.NumberChineseFormatter;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@Service
public class RestOrderServiceImpl extends ServiceImpl<RestOrderMapper, RestOrder> implements RestOrderService {


    @Override
    @Transactional
    public RestOrder add(RestOrderParam param) {
        return null ;
    }

    @Override
    public void delete(RestOrderParam param) {
        param.setDisplay(0);
        this.updateById(this.getEntity(param));

    }

    @Override
    public void update(RestOrderParam param) {
        RestOrder oldEntity = getOldEntity(param);
        RestOrder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RestOrderResult findBySpec(RestOrderParam param) {
        return null;
    }

    @Override
    public List<RestOrderResult> findListBySpec(RestOrderParam param) {
        List<RestOrderResult> orderResults = this.baseMapper.customList(param);
        format(orderResults);
        return orderResults;
    }

    @Override
    public PageInfo<RestOrderResult> findPageBySpec(RestOrderParam param) {
        Page<RestOrderResult> pageContext = getPageContext();
        IPage<RestOrderResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }
    @Override
    public PageInfo<RestOrderSimpleResult> simpleFindPageBySpec(RestOrderParam param) {
        Page<RestOrderResult> pageContext = getPageContext();
        IPage<RestOrderSimpleResult> page = this.baseMapper.simpleCustomPageList(pageContext, param);
//        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void format(List<RestOrderResult> data) {

    }

    private Serializable getKey(RestOrderParam param) {
        return param.getOrderId();
    }

    private Page<RestOrderResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestOrder getOldEntity(RestOrderParam param) {
        return this.getById(getKey(param));
    }

    private RestOrder getEntity(RestOrderParam param) {
        RestOrder entity = new RestOrder();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
}
