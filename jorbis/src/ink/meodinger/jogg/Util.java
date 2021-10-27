package ink.meodinger.jogg;

/**
 * Author: Meodinger
 * Date: 2021/10/27
 * Have fun with my code!
 */

public class Util {

    /**
     * log2(v) as int + 1
     */
    public static int ilog(int v) {
        int ret = 0;
        while (v > 0) {
            ret++;
            v >>>= 1;
        }
        return ret;
    }
}
