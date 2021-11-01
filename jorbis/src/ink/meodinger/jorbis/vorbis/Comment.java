package ink.meodinger.jorbis.vorbis;

import ink.meodinger.jogg.*;
import ink.meodinger.jorbis.Util;

/**
 * Author: Meodinger
 * Date: 2021/11/1
 * Have fun with my code!
 */

public class Comment {

    private static final int E_IMPLANTATION = -130;

    // unlimited user comment fields.
    private byte[] vendor = Util.VENDOR;
    private byte[][] userComments = null;
    private int[] commentLengths = null;
    private int commentCount = 0;

}
