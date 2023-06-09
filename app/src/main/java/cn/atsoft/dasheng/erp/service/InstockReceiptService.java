package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockLogDetail;
import cn.atsoft.dasheng.erp.entity.InstockReceipt;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.model.params.InstockReceiptParam;
import cn.atsoft.dasheng.erp.model.result.InstockReceiptResult;
import cn.atsoft.dasheng.erp.pojo.ReplaceSku;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 入库记录单 服务类
 * </p>
 *
 * @author song
 * @since 2022-08-11
 */
public interface InstockReceiptService extends IService<InstockReceipt> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-08-11
     */
    void add(InstockReceiptParam param);

    void addReceipt(InstockOrderParam param, List<InstockLogDetail> instockLogDetails);

    XWPFDocument createWord(String module, Map<String, Object> map, Map<String, List<ReplaceSku>> skuMap);

    Map<String, Object> detailBackMap(Long id);

    Map<String, List<ReplaceSku>> detailBackSkuMap(Long id);

    InstockReceiptResult detail(Long receiptId);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-08-11
     */
    void delete(InstockReceiptParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-08-11
     */
    void update(InstockReceiptParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-08-11
     */
    InstockReceiptResult findBySpec(InstockReceiptParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-08-11
     */
    List<InstockReceiptResult> findListBySpec(InstockReceiptParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-08-11
     */
    PageInfo<InstockReceiptResult> findPageBySpec(InstockReceiptParam param);

}
