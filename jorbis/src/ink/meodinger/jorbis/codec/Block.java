package ink.meodinger.jorbis.codec;

import ink.meodinger.jogg.Buffer;
import ink.meodinger.jogg.Packet;


/**
 * Author: Meodinger
 * Date: 2021/11/13
 * Have fun with my code!
 */

public class Block {

    float[][] pcm = new float[0][];
    Buffer opb = new Buffer();

    long lW = 0;
    long W  = 0;
    long nW = 0;
    int pcmEnd = 0;
    int mode = 0;

    int eofFlag = 0;
    long granulePos = 0;
    long sequence = 0;
    Dsp dsp = null;

    long glueBits    = 0;
    long timeBits    = 0;
    long floorBits   = 0;
    long residueBits = 0;

    public Block(Dsp dsp) {
        this.dsp = dsp;
        if (dsp.analysisPointer != 0) {
            opb.writeInit();
        }
    }

    public int init(Dsp dsp) {
        this.dsp = dsp;
        // todo
        return 0;
    }

    public int clear() {
        return 0;
    }

    public int analysis(Packet packet) {
        return 0;
    }

    public int synthesis(Packet packet) {
        return 0;
    }

    public int synthesisTrackOnly(Packet packet) {
        return 0;
    }

    public void alloc(long bytes) {

    }

    public void ripcord() {

    }

}
