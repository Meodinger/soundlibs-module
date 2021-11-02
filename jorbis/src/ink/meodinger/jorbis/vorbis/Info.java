package ink.meodinger.jorbis.vorbis;

import ink.meodinger.jorbis.*;
import ink.meodinger.jorbis.codec.*;

/**
 * Author: Meodinger
 * Date: 2021/11/1
 * Have fun with my code!
 */

public class Info {

    private static final int VI_TIME_B   = 1;
    private static final int VI_FLOOR_B  = 2;
    private static final int VI_RES_B    = 3;
    private static final int VI_MAP_B    = 1;
    private static final int VI_WINDOW_B = 1;

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
    //private InfoMode[] modeParam = null;

    private int maps = 0;
    private int[] mapType = null;
    private Object[] mapParam = null;

    private int times = 0;
    private int[] timeType = null;
    private Object[] timeParam = null;

    private int floors = 0;
    private int[] floorType = null;
    private Object[] floorParam = null;

    private int residues = 0;
    private int[] residueType = null;
    private Object[] residueParam = null;

    private int books = 0;
    private CodeBookStatic[] bookParam = null;

    private int pSys = 0; // encode only
    //private PsyInfo[] psyParam = new PsyInfo[64]; // encode only

    // for block long/sort tuning; encode only
    private int envelopeSa = 0;
    private float preEchoThresh = 0f;
    private float preEchoClamp = 0f;

    // -----  ----- //

    public void init() {
        rate = 0;
    }

    public void clear() {
        int i;

        //for (i = 0; i < modes; i++) modeParam[i] = null;
        //modeParam = null;
        //for (i = 0; i < maps; i++) AbstractMapping.mappingP[mapType[i].freeInfo(mapParam[i])];
        mapParam = null;
        //for (i = 0; i < times; i++) AbstractTime.timeP[timeType[i].freeInfo(timeParam[i])];
        timeParam = null;
        //for (i = 0; i < floors; i++) AbstractFloor.floorP[floorType[i].freeInfo(floorParam[i])];
        floorParam = null;
        //for (i = 0; i < residues; i++) AbstractResidue.residueP[residueType[i].freeInfo(residueParam[i])];
        residueParam = null;

        // the static codebooks *are* freed if you call info_clear(),
        // because decode side does alloc a 'static' codebook.
        // Calling clear on the full codebook does not clear the
        // static codebook (that's our responsibility)
        for(i = 0; i < books; i++) {
            // just in case the decoder pre-cleared to save space
            if(bookParam[i] != null) {
                //bookParam[i].clear();
                bookParam[i] = null;
            }
        }
        //if (vi->bookParam) free(vi->bookParam);
        bookParam = null;

        //for(i = 0; i < pSys; i++) psyParam[i].free();

        throw new IllegalStateException("todo: re-write");
    }
}
