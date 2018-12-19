package com.sian.translate.utlis;

import com.sian.translate.config.LoadConfig;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;

@Slf4j
@Component
public class ImageUtlis {

    @Autowired
    private LoadConfig loadConfigAutowired;
    private static LoadConfig loadConfig;

    @PostConstruct
    public void init() {
        loadConfig = this.loadConfigAutowired;
    }
    private final static String SUFFIXNAME ="IMG_";
    private final static String SMALL_SUFFIXNAME ="SMALLIMG_";
    private final static String BIG_SUFFIXNAME ="BIGIMG_";


    //获取图片链接
    private String imgPath;

    public static HashMap<String,String> loadImage(MultipartFile file) throws IOException {

        //获取本机IP
        String host = InetAddress.getLocalHost().getHostAddress();
        String port = loadConfig.getPort();
        String rootPath = loadConfig.getRootPath();
        String sonPath = loadConfig.getSonPath();

        // 获取文件名
//            String fileName = file.getOriginalFilename();

        // 解决中文问题，liunx下中文路径，图片显示问题
        String fileName = SUFFIXNAME + FileUtils.getUUID()+FileUtils.getSuffix(file.getOriginalFilename());
        String smallFileName = SMALL_SUFFIXNAME + FileUtils.getUUID()+FileUtils.getSuffix(file.getOriginalFilename());
        String bigFileName = BIG_SUFFIXNAME + FileUtils.getUUID()+FileUtils.getSuffix(file.getOriginalFilename());


        //logger.info("上传的文件名为：" + fileName);
        // 设置文件上传后的路径
        String filePath = rootPath + sonPath;
        log.info("上传的文件路径" + filePath);
        log.info("整个图片路径：" + host + ":" + port + sonPath + fileName);
        //创建文件路径
        String orignalFilePath = filePath + fileName;
        File dest = new File(orignalFilePath);
        file.transferTo(dest);


        String outSamllFile = filePath + smallFileName;
        String outBigFile = filePath + bigFileName;

        boolean translateSmall = compressImg(dest, outSamllFile, 200, 200,true );
        boolean translateBig = compressImg(dest, outBigFile, 1000, 1000,true );

        HashMap<String,String> map = new HashMap<>();

        if (translateSmall && translateBig){
            File smallFile = new File(outSamllFile);
            File bigFile = new File(outBigFile);

            log.info("小图：={}",outSamllFile);
            log.info("大图：={}",outBigFile);

            String orginalPath = ("http://"+host + ":" + port + sonPath + fileName).toString();
            String bigSmallPath = ("http://"+host + ":" + port + sonPath + bigFileName).toString();
            String imgSmallPath = ("http://"+host + ":" + port + sonPath + smallFileName).toString();

            // 检测是否存在目录
            if (!smallFile.getParentFile().exists()) {
                //假如文件不存在即重新创建新的文件已防止异常发生
                smallFile.getParentFile().mkdirs();
            }
            if (!bigFile.getParentFile().exists()) {
                //假如文件不存在即重新创建新的文件已防止异常发生
                bigFile.getParentFile().mkdirs();
            }
//            file.transferTo(smallFile);
//            file.transferTo(bigFile);
            map.put("smallFile", imgSmallPath);
            map.put("bigFile", bigSmallPath);
            map.put("orignalFile", orginalPath);

        }



        return  map;

    }

    /**
     * @param file  源文件
     * @param outFile    生成文件
     * @param width      指定宽度
     * @param height     指定高度
     * @param proportion 是否等比例操作
     * @描述 —— 是否等比例缩放图片
     */
    public static boolean compressImg(File file, String outFile,
                                      int width, int height, boolean proportion) {
        try {
            // 获得源文件
//            File file = new File(inputFile);
            if (!file.exists()) {
                return false;
            }
            Image img = ImageIO.read(file);
            // 判断图片格式是否正确
            if (img.getWidth(null) == -1) {
                return false;
            } else {
                int newWidth;
                int newHeight;
                // 判断是否是等比缩放
                if (proportion == true) {
                    // 为等比缩放计算输出的图片宽度及高度
                    double rate1 = ((double) img.getWidth(null))
                            / (double) width + 0.1;
                    double rate2 = ((double) img.getHeight(null))
                            / (double) height + 0.1;
                    // 根据缩放比率大的进行缩放控制
                    double rate = rate1 > rate2 ? rate1 : rate2;
                    newWidth = (int) (((double) img.getWidth(null)) / rate);
                    newHeight = (int) (((double) img.getHeight(null)) / rate);
                } else {
                    newWidth = width; // 输出的图片宽度
                    newHeight = height; // 输出的图片高度
                }

                BufferedImage tag = new BufferedImage((int) newWidth,
                        (int) newHeight, BufferedImage.TYPE_INT_RGB);

                /*
                 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的,优先级比速度高
                生成的图片质量比较好但速度慢
                 */
                tag.getGraphics().drawImage(
                        img.getScaledInstance(newWidth, newHeight,
                                Image.SCALE_SMOOTH), 0, 0, null);
                FileOutputStream out = new FileOutputStream(outFile);
                // JPEGImageEncoder可适用于其他图片类型的转换
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                encoder.encode(tag);
                out.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }
}
