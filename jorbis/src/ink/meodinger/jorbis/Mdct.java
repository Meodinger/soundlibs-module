package ink.meodinger.jorbis;

/**
 * Author: Meodinger
 * Date: 2021/11/13
 * Have fun with my code!
 */

public final class Mdct {

    private Mdct() {}

    public static final float PI3_8 = .38268343236508977175F;
    public static final float PI2_8 = .70710678118654752441F;
    public static final float PI1_8 = .92387953251128675613F;

    public static class Lookup {

        private int n = 0;
        private int log2n = 0;

        private float[] trig = null;
        private float[] bitRev = null;

        private float scale = 0;

        public void init(int n) {

        }

        public void clear() {

        }

        public void forward(float[] in, float[] out) {

        }

        public void backward(float[] in, float[] out) {

        }
    }

}
