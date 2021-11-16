package ink.meodinger.jorbis;

import ink.meodinger.jogg.Packet;

/**
 * Author: Meodinger
 * Date: 2021/11/2
 * Have fun with my code!
 */

public final class Vorbis {

    private Vorbis() {}

    public static final byte[] VORBIS = "vorbis".getBytes();
    public static final byte[] VENDOR = "Meodinger Jorbis I 20220101".getBytes();

    public static final int FALSE = -1;
    public static final int EOF   = -2;
    public static final int HOLE  = -3;

    public static final int E_READ       = -128;
    public static final int E_FAULT      = -129;
    public static final int E_IMPL       = -130;
    public static final int E_INVALID    = -131;
    public static final int E_NOT_VORBIS = -132;
    public static final int E_BAD_HEADER = -133;
    public static final int E_VERSION    = -134;
    public static final int E_NOT_AUDIO  = -135;
    public static final int E_BAD_PACKET = -136;
    public static final int E_BAD_LINK   = -137;
    public static final int E_NOS_EEK    = -138;

    public char[] version() {
        return null;
    }

    public int vorbis_bitrate_addblock(Block block) {
        return 0;
    }
    public int vorbis_bitrate_flushpacket(DSP dsp, Packet packet) {
        return 0;
    }
    public int synthesis_idheader(Packet packet) {
        return 0;
    }

}
