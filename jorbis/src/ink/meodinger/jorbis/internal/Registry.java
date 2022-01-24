package ink.meodinger.jorbis.internal;


import ink.meodinger.jogg.Buffer;
import ink.meodinger.jorbis.codec.Block;
import ink.meodinger.jorbis.codec.Dsp;
import ink.meodinger.jorbis.codec.Info;
import ink.meodinger.jorbis.codec.internal.InfoMode;

/**
 * Author: Meodinger
 * Date: 2022/1/22
 * Have fun with my code!
 */
public final class Registry {

    private Registry() {}

    public static final int VI_TRANSFORM_B = 1;
    public static final int VI_WINDOW_B    = 1;
    public static final int VI_TIME_B      = 1;

    public static final int VI_FLOOR_B     = 2;
    public static final int VI_RES_B       = 3;
    public static final int VI_MAP_B       = 1;

    public static FuncFloor[] floors = new FuncFloor[] {
        // Floor0,1
    };

    public static FuncResidue[] residues = new FuncResidue[] {
        // Residue0,1,2
    };

    public static FuncMapping[] mappings = new FuncMapping[] {
            FuncMapping.FuncMapping0.INSTANCE
    };

    public static FuncTime[] times = new FuncTime[] {
            FuncTime.FuncTime0.INSTANCE
    };

}
