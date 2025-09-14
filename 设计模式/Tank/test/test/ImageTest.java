package test;

import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;//表示内存中的图像
import java.io.File;
import java.io.IOException;

import static org.testng.AssertJUnit.assertNotNull;//静态导入断言方法，用于验证图像是否加载成功

public class ImageTest {

    @Test
    public void testLoadImage() {
        try{
            BufferedImage image = ImageIO.read(new File("D:\\A-For-Study\\project\\设计模式\\Tank\\src\\images\\bulletD.gif"));
            assertNotNull(image);
            //通过类加载器，从类路径中来加载图像
            BufferedImage image2 = ImageIO.read(ImageTest.class.getClassLoader().getResourceAsStream("images\\bulletD.gif"));
            assertNotNull(image2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
