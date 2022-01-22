package ink.meodinger.jorbis.internal;

/**
 * Author: Meodinger
 * Date: 2022/1/22
 * Have fun with my code!
 */
public final class Utils {

    private Utils() {}

    public static int iLog(int v) {
        int ret;
        for (ret = 0; v != 0; ret++) v >>>= 1;
        return ret;
    }

    public static int iLog2(int v) {
        int ret;
        for (ret = 0; v > 1; ret++) v >>>= 1;
        return ret;
    }

    public static int iCount(int v) {
        int ret;
        for (ret = 0; v != 0; ret += (v & 0x01)) v >>>= 1;
        return ret;
    }

}
