package ink.meodinger.jorbis.vorbis;

import ink.meodinger.jogg.*;

/**
 * Author: Meodinger
 * Date: 2021/11/1
 * Have fun with my code!
 */

public class Block {

    /// Necessary stream state for linking to the framing abstraction
    private float[][] pcm = null;
    private Buffer opb = new Buffer();

    private int lW, W, nW;
    private int pcmEnd;
    private int mode;
    private int eof;

    private long granulePos;
    private long sequence;

    DSPState vDSPState;

    // bit metric for the frame
    private int glue_bits;
    private int time_bits;
    private int floor_bits;
    private int res_bits;

}
