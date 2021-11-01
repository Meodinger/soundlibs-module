package ink.meodinger.jorbis.vorbis;

import ink.meodinger.jorbis.*;

/**
 * Author: Meodinger
 * Date: 2021/11/1
 * Have fun with my code!
 */

public class Info {

    private static final int E_BAD_PACKET=-136;
    private static final int E_NOT_AUDIO=-135;

    private static final int VI_TIME_B=1;
    private static final int VI_FLOOR_B=2;
    private static final int VI_RES_B=3;
    private static final int VI_MAP_B=1;
    private static final int VI_WINDOW_B=1;

    private int version = 0;
    private int channels = 0;
    private int rate = 0;

    // The below bitrate declarations are *hints*
    // Combinations of the three values carry the following implications:
    //
    // all three set to the same value:
    //     Imply a fixed rate bitstream
    // only nominal set:
    //     Imply a VBR stream that averages the nominal bitrate
    //     No hard upper/lower limit
    // upper and or lower set:
    //     Imply a VBR bitstream that obeys the bitrate limits
    //     Nominal may also be set to give a nominal rate
    // none set:
    //     The coder does not care to speculate
    private int bitrateUpper = 0;
    private int bitrateNominal = 0;
    private int bitrateLower = 0;

    // Vorbis supports only short and long blocks,
    // but allows the encoder to choose the sizes
    private int[] blockSizes = new int[2];

    // Modes are the primary means of supporting on-the-fly different
    // blockSizes, different channel mappings (LR or mid-side),
    // different residue backends, etc. Each mode consists of a
    // blockSize flag and a mapping (along with the mapping setup)
    private int modes = 0;
    private int maps = 0;
    private int times = 0;
    private int floors = 0;
    private int residues = 0;
    private int books = 0;
    private int pSys = 0; // encode only

    // private InfoMode[] mode_param = null;

    private int[] mapType = null;
    private Object[] mapParam = null;


    private int[] timeType = null;
    private Object[] timeParam = null;

    private int[] floorType = null;
    private Object[] floorParam = null;

    private int[] residueType = null;
    private Object[] residueParam = null;

    // private StaticCodeBook[] book_param=null;

    // private PsyInfo[] psy_param=new PsyInfo[64]; // encode only

    // for block long/sort tuning; encode only
    private int envelopeSa = 0;
    private float preEchoThresh = 0f;
    private float preEchoClamp = 0f;
}
