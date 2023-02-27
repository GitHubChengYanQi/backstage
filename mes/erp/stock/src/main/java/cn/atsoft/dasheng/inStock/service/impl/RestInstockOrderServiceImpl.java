package cn.atsoft.dasheng.inStock.service.impl;


import cn.atsoft.dasheng.audit.service.ActivitiProcessFormLogService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;

import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.entity.DocumentsAction;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;

import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.model.result.DocumentsStatusResult;
import cn.atsoft.dasheng.form.pojo.ProcessType;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.form.service.DocumentStatusService;
import cn.atsoft.dasheng.form.service.DocumentsActionService;
import cn.atsoft.dasheng.inStock.entity.RestInstockOrder;
import cn.atsoft.dasheng.inStock.mapper.RestInstockOrderMapper;
import cn.atsoft.dasheng.inStock.model.result.OrderResult;
import cn.atsoft.dasheng.inStock.service.RestInstockOrderService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


/**
 * <p>
 * 入库单 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
@Service
public class RestInstockOrderServiceImpl extends ServiceImpl<RestInstockOrderMapper, RestInstockOrder> implements RestInstockOrderService {

    @Autowired
    private UserService userService;
//    @Autowired
//    private StorehouseService storehouseService;
//    @Autowired
//    private InstockListService instockListService;
//    @Autowired
//    private InstockSendTemplate instockSendTemplate;
//    @Autowired
//    private OrCodeService orCodeService;
//    @Autowired
//    private CodingRulesService codingRulesService;
//    @Autowired
//    private InkindService inkindService;
//    @Autowired
//    private StockService stockService;
//    @Autowired
//    private StockDetailsService stockDetailsService;
//    @Autowired
//    private OrCodeBindService bindService;
//    @Autowired
//    private WxCpSendTemplate wxCpSendTemplate;
//    @Autowired
//    private GetOrigin getOrigin;
//    @Autowired
//    private StorehousePositionsService positionsService;
//    @Autowired
//    private StorehousePositionsBindService positionsBindService;
//    @Autowired
//    private MessageProducer messageProducer;
//
//    @Autowired
//    private StepsService stepsService;
//
//    @Autowired
//    private SkuService skuService;
//    @Autowired
//    private InstockLogService instockLogService;
//    @Autowired
//    private InstockLogDetailService instockLogDetailService;
    @Autowired
    private ActivitiProcessService activitiProcessService;
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;
    @Autowired
    private ActivitiProcessFormLogService activitiProcessLogService;
    @Autowired
    private DocumentStatusService documentStatusService;
    @Autowired
    private DocumentsActionService documentsActionService;

    @Override
    public List<OrderResult> showOrderList() {
//        return this.baseMapper.customList();
        return null;
    }
//    @Autowired
//    private AnnouncementsService announcementsService;
//    @Autowired
//    private RemarksService remarksService;
//    @Autowired
//    private MediaService mediaService;
//    @Autowired
//    private ShopCartService cartService;
//    @Autowired
//    private OrCodeBindService orCodeBindService;
//    @Autowired
//    private AnomalyDetailService anomalyDetailService;
//    @Autowired
//    private AnomalyService anomalyService;
//    @Autowired
//    private ShopCartService shopCartService;
//    @Autowired
//    private InstockHandleService instockHandleService;
//    @Autowired
//    private InventoryService inventoryService;
//    @Autowired
//    private AnomalyOrderService anomalyOrderService;
//    @Autowired
//    private ProductionPickListsService pickListsService;
//    @Autowired
//    private AllocationService allocationService;
//    @Autowired
//    private AllocationDetailService allocationDetailService;
//    @Autowired
//    private AllocationCartService allocationCartService;
//    @Autowired
//    private InstockReceiptService instockReceiptService;
//    @Autowired
//    private CustomerService customerService;
//
//    @Autowired
//    private SkuHandleRecordService skuHandleRecordService;
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private OrderDetailService orderDetailService;
//    @Autowired
//    private StockLogService stockLogService;
//    @Autowired
//    private StockLogDetailService stockLogDetailService;



}
