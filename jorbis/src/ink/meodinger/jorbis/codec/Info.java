package ink.meodinger.jorbis.codec;

import ink.meodinger.jogg.Packet;
import ink.meodinger.jorbis.codec.internal.CodecSetupInfo;

/**
 * Author: Meodinger
 * Date: 2021/11/13
 * Have fun with my code!
 */

public class Info {

    private int version;
    private int channels;
    private long rate;

    private long bitrateUpper;
    private long bitrateNominal;
    private long bitrateLower;
    private long bitrateWindow;

    private CodecSetupInfo codecSetupInfo;

    public void init() {

    }

    public void clear() {

    }

    public int blockSize(int zo) {
        return 0;
    }

    public long packetBlockSize(Packet packet) {
        return 0;
    }

    public int synthesisHeaderIn(Comment comment, Packet packet) {
        return 0;
    }

    public int synthesisHalfRate(int flag) {
        return 0;
    }

    public int synthesisHalfRate() {
        return 0;
    }

}
