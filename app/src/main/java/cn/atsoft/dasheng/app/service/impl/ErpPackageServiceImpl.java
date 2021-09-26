package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.entity.ErpPackageTable;
import cn.atsoft.dasheng.app.model.params.ErpPackageTableParam;
import cn.atsoft.dasheng.app.model.result.ErpPackageTableResult;
import cn.atsoft.dasheng.app.service.ErpPackageTableService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ErpPackage;
import cn.atsoft.dasheng.app.mapper.ErpPackageMapper;
import cn.atsoft.dasheng.app.model.params.ErpPackageParam;
import cn.atsoft.dasheng.app.model.result.ErpPackageResult;
import  cn.atsoft.dasheng.app.service.ErpPackageService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
 * 套餐表	 服务实现类
 * </p>
 *
 * @author qr
 * @since 2021-08-04
 */

@Service
public class ErpPackageServiceImpl extends ServiceImpl<ErpPackageMapper, ErpPackage> implements ErpPackageService {

    @Autowired
    private ErpPackageTableService erpPackageTableService;

    @Override
    public Long add(ErpPackageParam param){
        ErpPackage entity = getEntity(param);
        this.save(entity);
        return entity.getPackageId();
    }

    @Override
    public void delete(ErpPackageParam param){

        ErpPackageTableParam erpPackageTableParam = new ErpPackageTableParam();
        erpPackageTableParam.setPackageId(param.getPackageId());
        DataScope dataScope = new DataScope();
        PageInfo<ErpPackageTableResult> pageBySpec = erpPackageTableService.findPageBySpec(erpPackageTableParam,dataScope);
        for (int i =0 ; i < pageBySpec.getData().size(); i++){
            erpPackageTableParam.setId(pageBySpec.getData().get(i).getId());
            erpPackageTableService.delete(erpPackageTableParam);
        }
        this.removeById(getKey(param));
    }

    @Override
    public void batchDelete(List<Long> packageIds) {
        ErpPackage erpPackage = new ErpPackage();
        erpPackage.setDisplay(0);
        UpdateWrapper<ErpPackage> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("package_id", packageIds);
        this.update(erpPackage, updateWrapper);

        //查询绑定表数据
        QueryWrapper<ErpPackageTable> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(ErpPackageTable::getPackageId,packageIds);
        List<ErpPackageTable> list = erpPackageTableService.list(queryWrapper);
        List<ErpPackageTable>newEntity = new ArrayList<>();
        for (ErpPackageTable packageTable : list) {
            //绑定表数据逻辑删除赋值
            packageTable.setDisplay(0);
            newEntity.add(packageTable);
        }
        //绑定表更新逻辑删除
        erpPackageTableService.updateBatchById(newEntity);
    }


    @Override
    public void update(ErpPackageParam param){
        ErpPackage oldEntity = getOldEntity(param);
        ErpPackage newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ErpPackageResult findBySpec(ErpPackageParam param){
        return null;
    }

    @Override
    public List<ErpPackageResult> findListBySpec(ErpPackageParam param){
        return null;
    }

    @Override
    public PageInfo<ErpPackageResult> findPageBySpec(ErpPackageParam param, DataScope dataScope ){
        Page<ErpPackageResult> pageContext = getPageContext();
        IPage<ErpPackageResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ErpPackageParam param){
        return param.getPackageId();
    }

    private Page<ErpPackageResult> getPageContext() {
        List<String> fields = new ArrayList<String>(){{
            add("productName");
        }};
        return PageFactory.defaultPage(fields);
    }

    private ErpPackage getOldEntity(ErpPackageParam param) {
        return this.getById(getKey(param));
    }

    private ErpPackage getEntity(ErpPackageParam param) {
        ErpPackage entity = new ErpPackage();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
