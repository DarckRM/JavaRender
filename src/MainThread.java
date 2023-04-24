import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;

public class MainThread extends JFrame {
    // 屏幕的分辨率
    public static int screen_w = 720;
    public static int screen_h = 720;
    public static int screenSize = screen_w * screen_h;
    public static int half_screen_w = screen_w / 2;
    public static int half_screen_h = screen_h / 2;
    // 当前渲染帧序号
    public static int frameIndex;
    // 帧间隔毫秒
    public static int frameInterval = 16;
    // CPU 空闲时间越低说明效率越高
    public static int sleepTime;
    // 刷新率以及计算刷新率所用的参数
    public static int fps;
    public static long lastDraw;
    public static double thisTime, lastTime;

    // JPanel 作为画板
    public static JPanel panel;
    // 屏幕像素数组
    public static int[] screen;
    // 屏幕图像缓冲区 提供了在内存中操作屏幕的方法
    public static BufferedImage screenBuffer;
    public static void main(String[] args) {
        new MainThread();
    }

    public MainThread() {
        setTitle("Java 软光栅学习 Demo");
        panel = (JPanel) this.getContentPane();
        panel.setPreferredSize(new Dimension(screen_w, screen_h));
        panel.setMinimumSize(new Dimension(screen_w, screen_h));
        panel.setLayout(null);

        setResizable(false);
        pack();
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        screenBuffer = new BufferedImage(screen_w, screen_h, BufferedImage.TYPE_INT_RGB);
        DataBuffer dest = screenBuffer.getRaster().getDataBuffer();
        screen = ((DataBufferInt)dest).getData();
        int r_tianyi = 0x66, g_tianyi = 0xcc, b_tianyi = 0xff;
        int r_moe = 255, g_moe = 180, b_moe = 160;
        int tianyiLan = (r_tianyi << 16) | (g_tianyi << 8) | b_tianyi;
        int moe = (r_moe << 16) | (g_moe << 8) | b_moe;
        screen[0] = tianyiLan;
        // 初始化光栅渲染器
        LookupTables.init();
        Rasterizer.init();
        // 一个简单的三角形
        Vector3D[] myTriangle = new Vector3D[]{
                new Vector3D(0, 1, 2),
                new Vector3D(1, -1, 2),
                new Vector3D(-1, -1, 2)
        };
        while (true) {
            // 画面渲染天依蓝
            for (int i = 1; i < screenSize; i += i)
                System.arraycopy(screen, 0, screen, i, Math.min(screenSize - i, i));

            // 渲染三角形
            Rasterizer.triangleVertices = myTriangle;
            Rasterizer.color = (255 << 16) | (128 << 8);
            Rasterizer.type = 0;
            Rasterizer.rasterize();

            frameIndex++;
            // 计算当前的刷新率 并尽量保持刷新率
            if (frameIndex % 30 == 0) {
                double thisTime = System.currentTimeMillis();
                fps = (int) (1000 / ((thisTime - lastTime) / 30));
                lastTime = thisTime;
            }
            sleepTime = 0;
            while (System.currentTimeMillis() - lastDraw < frameInterval) {
                try {
                    Thread.sleep(1);
                    sleepTime++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lastDraw = System.currentTimeMillis();
            // 左上角显示刷新率
            Graphics2D g2 = (Graphics2D) screenBuffer.getGraphics();
            g2.setColor(Color.BLACK);
            g2.drawString("FPS: " + fps + " " + "Thread Sleep: " + sleepTime + " ms", 5, 15);

            // 把图像画到显存里
            panel.getGraphics().drawImage(screenBuffer, 0, 0, this);
        }

    }
}