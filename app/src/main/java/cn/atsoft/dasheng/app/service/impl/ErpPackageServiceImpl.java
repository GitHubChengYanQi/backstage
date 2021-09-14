package cn.atsoft.dasheng.app.service.impl;


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
import cn.atsoft.dasheng.core.util.ToolUtil;
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
        PageInfo<ErpPackageTableResult> pageBySpec = erpPackageTableService.findPageBySpec(erpPackageTableParam);
        for (int i =0 ; i < pageBySpec.getData().size(); i++){
            erpPackageTableParam.setId(pageBySpec.getData().get(i).getId());
            erpPackageTableService.delete(erpPackageTableParam);
        }
        this.removeById(getKey(param));
    }

    @Override
    public void batchDelete(ErpPackageParam param) {
        //根据传入数组 查询相关数据
        QueryWrapper<ErpPackage> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().in(ErpPackage::getPackageId,param.getPackageIds());
        //查询结果装入List
        List<ErpPackage> list1 = baseMapper.selectList(queryWrapper1);
        for (ErpPackage erpPackage : list1) {
            //逻辑删除赋值
            erpPackage.setDisplay(0);
        }
        //更新本表逻辑删除
        this.updateBatchById(list1);

        //查询绑定表数据
        QueryWrapper<ErpPackageTable> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(ErpPackageTable::getPackageId,param.getPackageIds());
        List<ErpPackageTable> list = erpPackageTableService.list(queryWrapper);

        List<ErpPackageTable>newEntity = new ArrayList<>();
        for (ErpPackageTable packageTable : list) {
            ErpPackageTable erpPackageTable = new ErpPackageTable();
            ToolUtil.copyProperties(packageTable,erpPackageTable);
            //绑定表数据逻辑删除赋值
           erpPackageTable.setDisplay(0);
           newEntity.add(erpPackageTable);
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
    public PageInfo<ErpPackageResult> findPageBySpec(ErpPackageParam param){
        Page<ErpPackageResult> pageContext = getPageContext();
        IPage<ErpPackageResult> page = this.baseMapper.customPageList(pageContext, param);
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
