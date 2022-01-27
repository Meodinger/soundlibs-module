package ink.meodinger.jorbis.codec;

import ink.meodinger.jogg.Packet;

/**
 * Author: Meodinger
 * Date: 2021/11/16
 * Have fun with my code!
 */

public class Comment {

    byte[]   vendor         = null;

    char[][] userComments   = null;
    int[]    commentLengths = null;
    int      commentCount   = 0;

    public void init() {

    }

    public void clear() {

    }

    public void add(final char[] comment) {

    }

    public void addTag(final char[] tag, final char[] contents) {

    }

    public char[] query(final char[] tag, final int count) {
        return null;
    }

    public int queryCount(final char[] tag) {
        return 0;
    }

    public int headerOut(Packet packet) {
        return 0;
    }

}
