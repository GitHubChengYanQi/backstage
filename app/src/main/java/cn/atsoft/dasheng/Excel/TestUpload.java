package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.Excel.pojo.SpuExcel;
import cn.atsoft.dasheng.appBase.service.WxCpService;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.FileInfo;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.bean.taskcard.TaskCardButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping(value = "/TestUpload")
public class TestUpload {

    @Autowired
    private WxCpService wxCpService;

    /**
     * 企业微信上传临时文件并推送
     *
     * @param file
     * @return
     * @throws WxErrorException
     */
    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData upload(@RequestParam MultipartFile file) throws WxErrorException {

        String name = file.getOriginalFilename();
        String fileSavePath = ConstantsContext.getFileUploadPath();

        try {
            File excelFile = new File(fileSavePath + name);
            file.transferTo(excelFile);
            WxMediaUploadResult upload = wxCpService.getWxCpClient().getMediaService().upload("file", excelFile);
            String mediaId = upload.getMediaId();

            File download = wxCpService.getWxCpClient().getMediaService().download(mediaId);

            WxCpMessage wxCpMessage = new WxCpMessage();
            wxCpMessage.setToUser("9a95a561fa96d4a7af2ad653e9a5e9d4|ChengYanqi|XuYiNing|RenYiTaiYu");
            wxCpMessage.setMsgType("file");
            wxCpMessage.setMediaId(mediaId);

            wxCpService.getWxCpClient().getMessageService().send(wxCpMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseData.success();
    }



}
