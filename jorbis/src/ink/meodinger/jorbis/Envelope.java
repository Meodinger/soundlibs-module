package ink.meodinger.jorbis;

import ink.meodinger.jorbis.codec.Info;

/**
 * Author: Meodinger
 * Date: 2021/11/13
 * Have fun with my code!
 */

public final class Envelope {

    private Envelope() {}

    public static final int PRE         = 16;
    public static final int WIN         = 4;
    public static final int POST        = 2;
    public static final int AMP         = PRE + POST - 1;

    public static final int BANDS       = 7;
    public static final int NEAR_DC     = 15;

    public static final int MIN_STRETCH = 2;
    public static final int MAX_STRETCH = 12;

    private static class Filter {
        public float[] ampBuffer = null;
        public int ampPointer = 0;

        public float[] nearDC = null;
        public float nearDCAcc = 0;
        public float nearDCPartialAcc = 0;
        public int nearDCPointer = 0;
    }
    private static class Band {
        public int ch = 0;
        public int winLength = 0;
        public int searchStep = 0;
        public int minEnergy = 0;
    }

    public static class Lookup {

        private Mdct.Lookup mdct = null;
        private float[] mdctWin = null;

        private Band[] bands = null;
        private Filter[] filters = null;
        private int stretch = 0;

        private int[] mark = null;

        private long storage = 0;
        private long current = 0;
        private long currentMark = 0;
        private long cursor = 0;

        public void init(Info info) {

        }

        public void clear() {

        }

        public long search() {
            return 0;
        }

        public void shift(long shift) {

        }

        public int mark() {
            return 0;
        }
    }
}
