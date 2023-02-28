package cn.atsoft.dasheng.purchase.service.impl;



import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;

import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.model.exception.ServiceException;

import cn.atsoft.dasheng.purchase.entity.RestOrderDetail;
import cn.atsoft.dasheng.purchase.mapper.RestOrderDetailMapper;
import cn.atsoft.dasheng.purchase.model.params.RestOrderDetailParam;
import cn.atsoft.dasheng.purchase.model.result.RestOrderDetailResult;
import cn.atsoft.dasheng.purchase.service.RestOrderDetailService;
import cn.atsoft.dasheng.service.IErpBase;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * 订单明细表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@Service("RestOrderDetailService")
public class RestOrderDetailServiceImpl extends ServiceImpl<RestOrderDetailMapper, RestOrderDetail> implements RestOrderDetailService, IErpBase {

    @Autowired
    private ActivitiProcessService processService;
    @Autowired
    private RestOrderDetailService detailService;

    @Override
    public void add(RestOrderDetailParam param) {
        RestOrderDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(RestOrderDetailParam param) {
        param.setDisplay(0);
        this.updateById(this.getEntity(param));
    }

    @Override
    public void update(RestOrderDetailParam param) {
        RestOrderDetail oldEntity = getOldEntity(param);
        RestOrderDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RestOrderDetailResult findBySpec(RestOrderDetailParam param) {
        return null;
    }

    @Override
    public List<RestOrderDetailResult> findListBySpec(RestOrderDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<RestOrderDetailResult> findPageBySpec(RestOrderDetailParam param) {
        Page<RestOrderDetailResult> pageContext = getPageContext();
        IPage<RestOrderDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void format(List<RestOrderDetailResult> param) {

    }

    private Serializable getKey(RestOrderDetailParam param) {
        return param.getDetailId();
    }

    private Page<RestOrderDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestOrderDetail getOldEntity(RestOrderDetailParam param) {
        return this.getById(getKey(param));
    }

    private RestOrderDetail getEntity(RestOrderDetailParam param) {
        RestOrderDetail entity = new RestOrderDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }


    @Override
    public PageInfo getOrderList(Map<String, Object> param) {
        return null;
    }

    @Override
    public PageInfo getOrderDetailList(Map<String, Object> param) {
        RestOrderDetailParam restOrderDetailParam = BeanUtil.mapToBean(param, RestOrderDetailParam.class, true);

        return this.findPageBySpec(restOrderDetailParam);
    }
}
