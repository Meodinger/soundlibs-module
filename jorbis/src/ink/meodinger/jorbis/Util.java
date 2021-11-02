package ink.meodinger.jorbis;

/**
 * Author: Meodinger
 * Date: 2021/10/30
 * Have fun with my code!
 */

public class Util {

    public static int i_log(int v){
        int ret = 0;
        while (v != 0) {
            ret++;
            v >>>= 1;
        }
        return ret;
    }

    /**
     * @return log2(v) as int
     */
    public static int i_log2(int v){
        int ret = 0;
        while (v > 1) {
            ret++;
            v >>>= 1;
        }
        return ret;
    }

    /**
     * count valid bits
     */
    public static int i_count(int v){
        int ret = 0;
        while (v != 0) {
            ret += (v & 1);
            v >>>= 1;
        }
        return ret;
    }
}
