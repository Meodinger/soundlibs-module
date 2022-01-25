package ink.meodinger.jorbis.internal;

import ink.meodinger.jogg.Buffer;
import ink.meodinger.jorbis.codec.Block;
import ink.meodinger.jorbis.codec.Dsp;
import ink.meodinger.jorbis.codec.Info;
import ink.meodinger.jorbis.codec.InfoMode;

/**
 * Author: Meodinger
 * Date: 2022/1/22
 * Have fun with my code!
 */
public abstract class FuncTime {

    public abstract void pack(Object i, Buffer opb);

    public abstract Object unpack(Info vi, Buffer opb);

    public abstract Object look(Dsp vd, InfoMode vm, Object i);

    public abstract void free_info(Object i);

    public abstract void free_look(Object i);

    public abstract int inverse(Block vb, Object i, float[] in, float[] out);

    // Impl

    public static class FuncTime0 extends FuncTime {

        static FuncTime0 INSTANCE = new FuncTime0();

        private FuncTime0() {}

        @Override
        public void pack(Object i, Buffer opb) {

        }

        @Override
        public Object unpack(Info vi, Buffer opb) {
            return "";
        }

        @Override
        public Object look(Dsp vd, InfoMode vm, Object i) {
            return "";
        }

        @Override
        public void free_info(Object i) {

        }

        @Override
        public void free_look(Object i) {

        }

        @Override
        public int inverse(Block vb, Object i, float[] in, float[] out) {
            return 0;
        }
    }

}
