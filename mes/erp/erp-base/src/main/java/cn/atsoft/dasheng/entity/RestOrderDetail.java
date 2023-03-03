package cn.atsoft.dasheng.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单明细表
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@Data
public class RestOrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private String remark;

    /**
     * 详情id
     */
    private Long detailId;

    /**
     * 订单id
     */
    private Long orderId;

    private Long skuId;

    private Long brandId;

    private Long customerId;

    /**
     * 预购数量
     */
    private Long preordeNumber;

    /**
     * 采购数量
     */
    private Long purchaseNumber;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 单价
     */
    private Integer onePrice;

    /**
     * 总价
     */
    private Integer totalPrice;

    /**
     * 票据类型
     */
    private Integer paperType;

    /**
     * 锐率
     */
    private Long rate;

    /**
     * 交货日期
     */
    private Integer deliveryDate;

    /**
     * 创建者
     */
    private Long createUser;

    /**
     * 修改者
     */
    private Long updateUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 状态
     */
    private Integer display;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 到货数量
     */
    private Integer arrivalNumber;

}
