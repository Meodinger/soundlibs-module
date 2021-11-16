package ink.meodinger.jorbis.codec.internal;

/**
 * Author: Meodinger
 * Date: 2021/11/16
 * Have fun with my code!
 */

public class CodecSetupInfo {

    private long[] blockSizes = new long[2];

    private int modes;
    private int maps;
    private int floors;
    private int residues;
    private int books;
    private int psys;

    private InfoMode[] modeParam = new InfoMode[64];

    private int[] mapType = new int[64];
    private Object[][] mapParam; //= new Object[][64];     // vorbis_info_mapping
    private int[] floorType = new int[64];
    private Object[][] floorParam; //= new Object[][64];   // vorbis_info_floor
    private int[] residueType = new int[64];
    private Object[][] residueParam; //= new Object[][64]; // vorbis_info_residue
    private Object[] fullBooks; //= new Object[];          // CodeBook
    private Object[][] bookParam; //= new Object[][64];    // StaticCodeBook

    private Object[][] psyParam; //= new Object[][4];      // vorbis_info_psy
    private Object psyGlobalParam; //= new Object();       // vorbis_info_psy_global

    private Object bi = new Object();                   // bitrate_manager_info
    private Object hi = new Object();                   // highlevel_encode_setup

    private int halfRateFlag;

}
