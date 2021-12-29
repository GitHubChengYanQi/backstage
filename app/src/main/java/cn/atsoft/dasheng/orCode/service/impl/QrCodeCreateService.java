package cn.atsoft.dasheng.orCode.service.impl;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.codec.Base64;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.tomcat.util.net.openssl.ciphers.EncryptionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

@Service
public class QrCodeCreateService {

    public String createQrCode(String content){
        if (ToolUtil.isNotEmpty(content)) {
//            ServletOutputStream stream = null;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            @SuppressWarnings("rawtypes")
            HashMap<EncodeHintType, Comparable> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, EncryptionLevel.MEDIUM);
            hints.put(EncodeHintType.MARGIN, 2);

            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix bitMatrix = writer.encode(String.valueOf(content), BarcodeFormat.QR_CODE, 200, 200, hints);
                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
                ImageIO.write(bufferedImage, "png", os);
                return new String("data:image/png;base64" + Base64.encode(os.toByteArray()));
            } catch (WriterException | IOException e) {
                e.printStackTrace();
            }
//
//            finally {
//                if (stream != null) {
//                    stream.flush();
//                    stream.close();
//                }
//            }
        }
        return null ;
    }
}
