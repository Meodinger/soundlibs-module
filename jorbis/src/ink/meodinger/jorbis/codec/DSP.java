package ink.meodinger.jorbis.codec;

import ink.meodinger.jogg.Packet;
import ink.meodinger.jorbis.codec.internal.DSPBackend;

/**
 * Author: Meodinger
 * Date: 2021/11/13
 * Have fun with my code!
 */

public class DSP {

    private int analysis = 0;
    private Info info = null;

    private float[][] pcm = null;
    private float[][] pcmRet = null;
    private int pcmStorage = 0;
    private int pcmCurrent = 0;
    private int pcmReturned = 0;

    private int preExtraPolate = 0;
    private int eofFlag = 0;

    private long lW = 0;
    private long W = 0;
    private long nW = 0;
    private long centerW = 0;

    private long granulePos = 0;
    private long sequence = 0;

    private long glueBits = 0;
    private long timeBits = 0;
    private long floorBits = 0;
    private long residueBits = 0;

    private DSPBackend backend = null;

    public void clear() {

    }

    public double granuleTime(long granulePos) {
        return 0;
    }

    public int analysisInit(Info info) {
        return 0;
    }

    public float[][] analysisBuffer(int vals) {
        return null;
    }

    public int analysisWrote(int vals) {
        return 0;
    }

    public int analysisHeaderOut(Comment comment, Packet packet, Packet common, Packet code) {
        return 0;
    }

    public int analysisBlockOut(Block block) {
        return 0;
    }

    public int synthesisInit(Info info) {
        return 0;
    }

    public int synthesisRestart() {
        return 0;
    }

    public int synthesisBlockIn(Block block) {
        return 0;
    }

    public int synthesisPcmOut(float[][][] pcm) {
        return 0;
    }

    public int synthesisLapOut(float[][][] pcm) {
        return 0;
    }

    public int synthesisRead(int samples) {
        return 0;
    }

}
