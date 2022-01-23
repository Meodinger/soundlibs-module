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
public abstract class FuncFloor {

    public abstract void pack(Object i, Buffer opb);

    public abstract Object unpack(Info vi, Buffer opb);

    public abstract Object look(Dsp vd, InfoMode mi, Object i);

    public abstract void free_info(Object i);

    public abstract void free_look(Object i);

    public abstract void free_state(Object vs);

    public abstract int forward(Block vb, Object i, float[] in, float[] out, Object vs);

    public abstract Object inverse1(Block vb, Object i, Object memo);

    public abstract int inverse2(Block vb, Object i, Object memo, float[] out);

}
