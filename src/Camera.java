import java.security.PublicKey;

public class Camera {
    // 摄像机的位置矢量
    public static Vector3D position;
    // 摄像机的朝向矢量
    public static Vector3D direction;
    // 摄像机的移动和转向
    public static boolean MOVE_FORWARD, MOVE_BACKWARD, SLIDE_LEFT, SLIDE_RIGHT, LOOK_UP, LOOK_DOWN, LOOK_RIGHT, LOOK_LEFT;
    // 摄像机在 Y 轴的旋转 左右观看
    public static int Y_angle;
    // 摄像机在 X 轴的旋转 上下观看
    public static int X_angle;
    // 移动摄像机朝向的速度 每帧 2 度
    public static int turnRate = 2;
    // 移动摄像机位置的速度 每帧 0.03 个单位长度
    public static float moveSpeed = 0.03f;
    // 初始化方法
    public static void init(float x, float y, float z) {
        position = new Vector3D(x ,y, z);
        direction = new Vector3D(0, 0, 1);
    }
    // 更新摄像机的状态
    public static void update() {
        // 处理向上看， 并保证仰角不大于等于90度
        if (LOOK_UP) {
            X_angle += turnRate;
            if (X_angle > 89 && X_angle < 180)
                X_angle = 89;
        }

        //处理向下看， 并保证俯角不大于等于90度
        if (LOOK_DOWN) {
            X_angle -= turnRate;
            if (X_angle < 271 && X_angle > 180)
                X_angle = -89;
        }

        // 处理向右看
        if (LOOK_RIGHT)
            Y_angle += turnRate;

        // 处理向左看
        if (LOOK_LEFT) {
            Y_angle -= turnRate;
        }

        //将 Y_angle 和 X_angle 的值限制在 0-359 的范围内
        Y_angle = (Y_angle + 360) % 360;
        X_angle = (X_angle + 360) % 360;

        //更新视角的方向
        direction.set(0,0,1);
        direction.rotate(Vector3D.Axis.X, X_angle);
        direction.rotate(Vector3D.Axis.Y, Y_angle);
        direction.unit();

        //处理向前移动
        if (MOVE_FORWARD)
            position.add(direction, moveSpeed);

        //处理后前移动
        if (MOVE_BACKWARD)
            position.sub(direction, moveSpeed);

        //视角方向与一个向下的矢量的叉积结果为视角需要向左移动的方向
        if (SLIDE_LEFT){
            Vector3D left = direction.cross(new Vector3D(0, -1, 0));
            left.unit();
            position.sub(left, moveSpeed);
        }

        //视角方向与一个向上的矢量的叉积结果为视角需要向右移动的方向
        if (SLIDE_RIGHT) {
            Vector3D right = direction.cross(new Vector3D(0, 1, 0));
            right.unit();
            position.sub(right, moveSpeed);
        }
    }
}
