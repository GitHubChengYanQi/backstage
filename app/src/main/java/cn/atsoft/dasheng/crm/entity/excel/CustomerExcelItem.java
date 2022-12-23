package cn.atsoft.dasheng.crm.entity.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 客户管理表
 * </p>
 *
 * @author
 * @since 2021-07-23
 */

@Data
public class CustomerExcelItem implements Serializable {

    @Excel(name = "供应商名称")
    private String customerName;

    @Excel(name = "联系人姓名")
    private String userName;

    @Excel(name = "联系人电话")
    private String  phone;

    @Excel(name = "职务")
    private String role;
}
