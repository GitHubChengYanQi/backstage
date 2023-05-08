package cn.atsoft.dasheng.serial.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.serial.entity.SerialNumber;
import cn.atsoft.dasheng.serial.mapper.SerialNumberMapper;
import cn.atsoft.dasheng.serial.model.params.SerialNumberParam;
import cn.atsoft.dasheng.serial.model.result.SerialNumberResult;
import cn.atsoft.dasheng.serial.service.SerialNumberService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 流水号 服务实现类
 * </p>
 *
 * @author
 * @since 2021-11-04
 */
@Transactional
@Service
public class SerialNumberServiceImpl extends ServiceImpl<SerialNumberMapper, SerialNumber> implements SerialNumberService {
    @Autowired
    private SerialNumberService serialNumberService;

    @Override
    public String add(SerialNumberParam param) {
        SerialNumber entity = getEntity(param);
        SerialNumber num = this.getSerial(param);
        if (ToolUtil.isEmpty(num)) {
            entity.setNum(1L);
        } else {
            entity.setNum(num.getNum()+1L);

        }
        entity.setDate(new Date());
        this.save(entity);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        int max = entity.getSerialLength().intValue();

        nf.setMaximumIntegerDigits(max);
        nf.setMinimumIntegerDigits(max);
        Long snum = entity.getNum();
        return nf.format(snum);
    }
    @Override
    public String genNumber(){
       return this.add(new SerialNumberParam());
    }

    @Override
    public List<String> addBatch(SerialNumberParam param) {
        SerialNumber entity = getEntity(param);
        SerialNumber num = this.getSerial(param);
        Long number = 0L;
        if (ToolUtil.isEmpty(num)) {
            number = 1L;
        } else {
            number = num.getNum() + 1;
        }
        List<SerialNumber> serialNumbers = new ArrayList<>();
        if (ToolUtil.isEmpty(param.getCont())){
            param.setCont(0L);
        }
        for (int i = 0; i < param.getCont(); i++) {
            SerialNumber serialNumber = new SerialNumber();
            ToolUtil.copyProperties(param, serialNumber);
            serialNumber.setNum(number + i);
            serialNumber.setDate(new Date());
            serialNumbers.add(serialNumber);
        }
        serialNumberService.saveBatch(serialNumbers);
        List<String> serialStr = new ArrayList<>();
        for (SerialNumber serialNumber : serialNumbers) {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            int max = entity.getSerialLength().intValue();

            nf.setMaximumIntegerDigits(max);
            nf.setMinimumIntegerDigits(max);
            Long snum = serialNumber.getNum();
            serialStr.add(nf.format(snum));
        }
        return serialStr;
    }

    public SerialNumber getSerial(SerialNumberParam param) {
        QueryWrapper<SerialNumber> queryWrapper = new QueryWrapper<>();
        if(ToolUtil.isNotEmpty(param.getDate())){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = simpleDateFormat.format(param.getDate());
            queryWrapper.apply("date_format(create_time,'%Y-%m-%d') = date_format('"+date+"','%Y-%m-%d')").orderByDesc("num");
        }
        if (ToolUtil.isNotEmpty(param.getMd5())) {
            queryWrapper.eq("md5",param.getMd5());
        }
        SerialNumber num = this.baseMapper.selectOne(queryWrapper.orderByDesc("num").last("limit 1"));
        return num;
    }

    @Override
    public void delete(SerialNumberParam param) {
//        this.removeById(getKey(param));
    }

    @Override
    public void update(SerialNumberParam param) {
//        SerialNumber oldEntity = getOldEntity(param);
//        SerialNumber newEntity = getEntity(param);
//        ToolUtil.copyProperties(newEntity, oldEntity);
//        this.updateById(newEntity);
    }

    @Override
    public SerialNumberResult findBySpec(SerialNumberParam param) {
        return null;
    }

    @Override
    public List<SerialNumberResult> findListBySpec(SerialNumberParam param) {
        return null;
    }

    @Override
    public PageInfo<SerialNumberResult> findPageBySpec(SerialNumberParam param) {
        Page<SerialNumberResult> pageContext = getPageContext();
        IPage<SerialNumberResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SerialNumberParam param) {
        return param.getSerialId();
    }

    private Page<SerialNumberResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private SerialNumber getOldEntity(SerialNumberParam param) {
        return this.getById(getKey(param));
    }

    private SerialNumber getEntity(SerialNumberParam param) {
        SerialNumber entity = new SerialNumber();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
