package cn.atsoft.dasheng.service;


import org.springframework.stereotype.Service;

@Service
public class RestQrCodeCreateService {

    public String createQrCode(String content) {
//        if (ToolUtil.isNotEmpty(content)) {
//
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            @SuppressWarnings("rawtypes")
//            HashMap<EncodeHintType, Comparable> hints = new HashMap<>();
//            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
//            hints.put(EncodeHintType.MARGIN, 0);
//
//            try {
//                QRCodeWriter writer = new QRCodeWriter();
//                BitMatrix bitMatrix = writer.encode(String.valueOf(content), BarcodeFormat.QR_CODE, 120, 120, hints);
//                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
//                ImageIO.write(bufferedImage, "png", os);
//                return new String("<img width='120' height='120' src='data:image/png;base64," + Base64.encode(os.toByteArray()) + "' />");
//            } catch (WriterException | IOException e) {
//                e.printStackTrace();
//            }
//
//
//        }
        return null;
    }
}
