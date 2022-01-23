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
public abstract class FuncResidue {

    public abstract void pack(Object vr, Buffer opb);

    public abstract Object unpack(Info vi, Buffer opb);

    public abstract Object look(Dsp vd, InfoMode vm, Object vr);

    public abstract void free_info(Object i);

    public abstract void free_look(Object i);

    public abstract int inverse(Block vb, Object vl, float[][] in, int[] nonzero, int ch);

}
