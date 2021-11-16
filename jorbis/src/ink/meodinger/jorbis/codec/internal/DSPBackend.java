package ink.meodinger.jorbis.codec.internal;

import ink.meodinger.jorbis.Drft;
import ink.meodinger.jorbis.Envelope;

/**
 * Author: Meodinger
 * Date: 2021/11/13
 * Have fun with my code!
 */

public class DSPBackend {

    private Envelope.Lookup envelope = null;
    private int[] window = new int[2];
    private Object[][][] transforms = new Objec[][][2]; // vorbis_look_transform
    private Drft.Lookup[] fftLook = new Drft.Lookup[2];

    private int modeBits = 0;
    private Object[][] flr = new Object[][]; // vorbis_look_floor
    private Object[][] residue = new Object[][]; // vorbis_look_residue
    private Object[][] psy = new Object[]; // vorbis_look_psy
    private Object psyGlobal = new Object; // vorbis_look_psy_global

    private byte[] header;
    private byte[] header1;
    private byte[] header2;

    private Object bms; // bitrate_manager_state

    private long sampleCount;

}
