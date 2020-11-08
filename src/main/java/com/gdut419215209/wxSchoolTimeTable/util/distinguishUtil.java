package com.example.demo.util;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class distinguishUtil {

    //识别验证码
    public static String distinguishImg(String fileName) throws TesseractException, IOException {
        //首先把验证码二值化，保存在D:/trans/ 下
        binaryzationImg(fileName);

        // 识别图片的路径（修改为自己的图片路径）
        String path = "D:/trans/"+fileName;

        // 语言库位置（修改为跟自己语言库文件夹的路径）
        String lagnguagePath = "D:/tessdata";

        File file = new File(path);
        ITesseract instance = new Tesseract();

        //设置训练库的位置
        instance.setDatapath(lagnguagePath);
        instance.setOcrEngineMode(2);
        //chi_sim ：简体中文， eng    根据需求选择语言库
        instance.setLanguage("eng");
        instance.setTessVariable("user_defined_dpi", "300");

        String result = instance.doOCR(file).replace(" ","");
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(result);
        String message = m.replaceAll("");
        return message;
    }

    //二值化验证码
    private static void binaryzationImg(String fileName) throws IOException {
        String filename ="D:/img/"+fileName;
        File file = new File(filename);
        BufferedImage bi = ImageIO.read(file);
        // 获取当前图片的高,宽,ARGB
        int h = bi.getHeight();
        int w = bi.getWidth();
        int rgb = bi.getRGB(0, 0);
        int arr[][] = new int[w][h];

        // 获取图片每一像素点的灰度值
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                // getRGB()返回默认的RGB颜色模型(十进制)
                arr[i][j] = getImageRgb(bi.getRGB(i, j));//该点的灰度值
            }

        }

        BufferedImage bufferedImage=new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);//  构造一个类型为预定义图像类型之一的 BufferedImage，TYPE_BYTE_BINARY（表示一个不透明的以字节打包的 1、2 或 4 位图像。）
        int FZ=130;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if(getGray(arr,i,j,w,h)>FZ){
                    int black=new Color(255,255,255).getRGB();
                    bufferedImage.setRGB(i, j, black);
                }else{
                    int white=new Color(0,0,0).getRGB();
                    bufferedImage.setRGB(i, j, white);
                }
            }

        }
        try {
            ImageIO.write(bufferedImage, "jpg", new File("D:/trans/"+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getImageRgb(int i) {
        String argb = Integer.toHexString(i);// 将十进制的颜色值转为十六进制
        // argb分别代表透明,红,绿,蓝 分别占16进制2位
        int r = Integer.parseInt(argb.substring(2, 4),16);//后面参数为使用进制
        int g = Integer.parseInt(argb.substring(4, 6),16);
        int b = Integer.parseInt(argb.substring(6, 8),16);
        int result=(int)((r+g+b)/3);
        return result;
    }


    //自己加周围8个灰度值再除以9，算出其相对灰度值
    private static int getGray(int gray[][], int x, int y, int w, int h)
    {
        int rs = gray[x][y]
                + (x == 0 ? 255 : gray[x - 1][y])
                + (x == 0 || y == 0 ? 255 : gray[x - 1][y - 1])
                + (x == 0 || y == h - 1 ? 255 : gray[x - 1][y + 1])
                + (y == 0 ? 255 : gray[x][y - 1])
                + (y == h - 1 ? 255 : gray[x][y + 1])
                + (x == w - 1 ? 255 : gray[x + 1][ y])
                + (x == w - 1 || y == 0 ? 255 : gray[x + 1][y - 1])
                + (x == w - 1 || y == h - 1 ? 255 : gray[x + 1][y + 1]);
        return rs / 9;
    }
}
