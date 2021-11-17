package ink.meodinger.jorbis.codec;

import ink.meodinger.jogg.Buffer;
import ink.meodinger.jogg.Packet;
import ink.meodinger.jorbis.codec.internal.AllocChain;
import ink.meodinger.jorbis.codec.internal.BlockInternal;

/**
 * Author: Meodinger
 * Date: 2021/11/13
 * Have fun with my code!
 */

public class Block {

    private float[][] pcm = null;
    private Buffer opb = null;

    private long lW = 0;
    private long W = 0;
    private long nW = 0;
    private int pcmEnd = 0;
    private int mode = 0;

    private int eofFlag = 0;
    private long granulePos = 0;
    private long sequence = 0;
    private Dsp dsp = null;

    private long glueBits = 0;
    private long timeBits = 0;
    private long floorBits = 0;
    private long residueBits = 0;

    private Object[] localStore;
    private long localTop;
    private long localAlloc;
    private long totalUes;
    private AllocChain[] reap;

    private BlockInternal internal;

    public int init(Dsp dspState) {
        this.pcm = null;
        this.opb = null;

        this.lW = 0;
        this.W = 0;
        this.nW = 0;
        this.pcmEnd = 0;
        this.mode = 0;

        this.eofFlag = 0;
        this.granulePos = 0;
        this.sequence = 0;
        this.dsp = dspState;

        this.glueBits = 0;
        this.timeBits = 0;
        this.floorBits = 0;
        this.residueBits = 0;

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

}
