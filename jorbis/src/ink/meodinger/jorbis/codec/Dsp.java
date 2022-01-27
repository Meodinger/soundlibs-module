package ink.meodinger.jorbis.codec;

import ink.meodinger.jogg.Packet;
import ink.meodinger.jorbis.internal.CodeBook;

/**
 * Author: Meodinger
 * Date: 2021/11/13
 * Have fun with my code!
 */

public class Dsp {

    int analysisPointer = 0;
    Info info = null;
    int modeBits = 0;

    float[][] pcm    = null;
    float[][] pcmRet = null;
    int pcmStorage   = 0;
    int pcmCurrent   = 0;
    int pcmReturned  = 0;

    int preExtraPolate = 0;
    int eofFlag = 0;

    long lW      = 0;
    long W       = 0;
    long nW      = 0;
    long centerW = 0;

    long granulePos = 0;
    long sequence = 0;

    long glueBits    = 0;
    long timeBits    = 0;
    long floorBits   = 0;
    long residueBits = 0;

    float[][][][][] window    = null; // block, leadin, leadout, type
    Object[][]      transform = null;
    CodeBook[]      fullBooks = null;
    Object[]        mode      = null;

    private byte[] header  = null;
    private byte[] header1 = null;
    private byte[] header2 = null;


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

    // Getters

    public Info getInfo() {
        return info;
    }
}
