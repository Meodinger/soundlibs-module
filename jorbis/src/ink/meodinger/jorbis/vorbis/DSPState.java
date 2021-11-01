package ink.meodinger.jorbis.vorbis;

import ink.meodinger.jorbis.codec.CodeBook;

/**
 * Author: Meodinger
 * Date: 2021/11/1
 * Have fun with my code!
 */

public class DSPState {

    private static final float PI = 3.1415926539f; // Use float, not double
    private static final int VI_TRANSFORM_B = 1;
    private static final int VI_WINDOW_B = 1;

    private int analysisP = 0;
    private Info vInfo = null;
    private int modeBits = 0;

    private float[][] pcm = null;
    private int pcmStorage = 0;
    private int pcmCurrent = 0;
    private int pcmReturned = 0;

    private float[] multipliers = null;
    private int envelopeStorage = 0;
    private int envelopeCurrent = 0;

    private int eof = 0;

    private int lW, w, nW, centerW;

    private long granulePos = 0;
    private long sequence = 0;

    private long glueBits = 0;
    private long timeBits = 0;
    private long floorBits = 0;
    private long resBits = 0;

    // Local lookup storage

    // block, lead_in, lead_out, type
    float[][][][][] window = null;
    Object[][] transform;
    CodeBook[] fullBooks;
    // backend lookups are tied to the mode, not the backend or naked mapping
    Object[] mode;

    // Local storage, only used on the encoding side. This way the
    // application does not need to worry about freeing some packets'
    // memory and not others'; packet storage is always tracked.
    // Cleared next call to a _dsp_ function
    byte[] header0 = null;
    byte[] header1 = null;
    byte[] header2 = null;

}
