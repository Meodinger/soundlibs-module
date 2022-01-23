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
public abstract class FuncMapping {

    public abstract void pack(Info info, Object imap, Buffer buffer);

    public abstract Object unpack(Info info, Buffer buffer);

    public abstract Object look(Dsp vd, InfoMode vm, Object m);

    public abstract void free_info(Object imap);

    public abstract void free_look(Object imap);

    public abstract int inverse(Block vd, Object lm);

}
