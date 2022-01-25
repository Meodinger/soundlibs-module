package ink.meodinger.jorbis.codec;

import ink.meodinger.jogg.Packet;
import ink.meodinger.jorbis.internal.CodeBookStatic;


/**
 * Author: Meodinger
 * Date: 2021/11/13
 * Have fun with my code!
 */

public class Info {

    private int  version  = 0;
    private int  channels = 0;
    private long rate     = 0;

    private long bitrateUpper   = 0;
    private long bitrateNominal = 0;
    private long bitrateLower   = 0;
    private long bitrateWindow  = 0;

    int[] blockSizes = new int[2];

    int modes = 0;
    InfoMode[] modeParam = null;

    int floors = 0;
    int[] floorType = null;
    Object[] floorParam = null;

    int maps = 0;
    int[] mapType = null;
    Object[] mapParam = null;

    int residues = 0;
    int[] residueType = null;
    Object[] residueParam = null;

    int times = 0;
    int[] timeType = null;
    Object[] timeParam = null;

    int books = 0;
    CodeBookStatic[] bookParam = null; // CodeBook

    int psys = 0; // encode only
    Object[] psyParam = null; // PsyInfo

    public void init() {
        rate = 0;
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
