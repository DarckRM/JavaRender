public class Vector3D {
    // 矢量在X Y Z轴上的分量
    public float x, y, z;

    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vector3D v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    // 矢量的加法
    public void add(Vector3D v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
    }
    public void add(Vector3D v, float scalar) {
        this.x += v.x * scalar;
        this.y += v.y * scalar;
        this.z += v.z * scalar;
    }
    // 矢量的减法
    public void sub(Vector3D v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
    }
    public void sub(Vector3D v, float scalar) {
        this.x -= v.x * scalar;
        this.y -= v.y * scalar;
        this.z -= v.z * scalar;
    }
    // 矢量的点积 可看作两个向量的相似度
    public float dot(Vector3D v1, Vector3D v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }
    // 矢量的叉积 求与两个向量垂直的向量
    public void cross(Vector3D v1, Vector3D v2) {
        x = v1.y * v2.z - v1.z * v2.y;
        y = v1.z * v2.x - v1.x * v2.z;
        z = v1.x * v2.y - v1.y * v2.x;
    }
    public  Vector3D cross(Vector3D v){
        return new Vector3D(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
    }
    // 矢量的长度
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }
    // 矢量单位化
    public void unit() {
        float length = length();
        x = x / length;
        y = y / length;
        z = z / length;
    }
    // 矢量放大缩小 (矢量乘以一个标量)
    public void scale(float scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;
    }
    // 矢量绕 X Y Z 轴旋转的方法 顺时针旋转指定角度
    public void rotate(Axis axis, int angle) {
        float sin = LookupTables.sin[angle];
        float cos = LookupTables.cos[angle];
        switch (axis) {
            case X -> {
                float old_Y = y;
                float old_Z = z;
                y = cos * old_Y - sin * old_Z;
                z = sin * old_Y - cos * old_Z;
            }
            case Y -> {
                float old_X = x;
                float old_Z = z;
                y = cos * old_X - sin * old_Z;
                z = sin * old_X - cos * old_Z;
            }
            case Z -> {
                float old_X = x;
                float old_Y = y;
                y = cos * old_X - sin * old_Y;
                z = sin * old_X - cos * old_Y;
            }
        }
    }
    public enum Axis {
        X,
        Y,
        Z
    }

}
