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
public abstract class FuncMapping {

    public abstract void pack(Info info, Object imap, Buffer buffer);

    public abstract Object unpack(Info info, Buffer buffer);

    public abstract Object look(Dsp vd, InfoMode vm, Object m);

    public abstract void free_info(Object imap);

    public abstract void free_look(Object imap);

    public abstract int inverse(Block vd, Object lm);

    // Impl

    public static class FuncMapping0 extends FuncMapping {

        static FuncMapping0 INSTANCE = new FuncMapping0();

        private FuncMapping0() {}

        @Override
        public void pack(Info info, Object imap, Buffer buffer) {

        }

        @Override
        public Object unpack(Info info, Buffer buffer) {
            return null;
        }

        @Override
        public Object look(Dsp vd, InfoMode vm, Object m) {
            Info vi = vd.getInfo();
            LookMapping0 look = new LookMapping0();
            InfoMapping0 info = look.map = (InfoMapping0) m; // FIXME

            look.mode = vm;
            look.timeLook    = new Object[info.subMaps];
            look.floorLook   = new Object[info.subMaps];
            look.residueLook = new Object[info.subMaps];
            look.timeFunc    = new FuncTime[info.subMaps];
            look.floorFunc   = new FuncFloor[info.subMaps];
            look.residueFunc = new FuncResidue[info.subMaps];

            for (int i = 0; i < info.subMaps; i++) {
                int timeNum    = info.timeSubMap[i];
                int floorNum   = info.floorSubMap[i];
                int residueNum = info.residueSubMap[i];

                // look.timeFunc[i] = Registry.times[vi]; // FIXME
            }


            return null;
        }

        @Override
        public void free_info(Object imap) {

        }

        @Override
        public void free_look(Object imap) {

        }

        @Override
        public int inverse(Block vd, Object lm) {
            return 0;
        }

        // Inner Class

        static class InfoMapping0 {
            int subMaps; // <= 16
            int[] chmuxList     = new int[256]; // up to 256 channels in a Vorbis stream

            int[] timeSubMap    = new int[16]; // [mux]
            int[] floorSubMap   = new int[16]; // [mux] submap to floors
            int[] residueSubMap = new int[16];// [mux] submap to residue
            int[] psySubMap     = new int[16]; // [mux]; encode only

            int couplingSteps;
            int[] couplingMag   = new int[256];
            int[] couplingAng   = new int[256];

            void free(){
                chmuxList     = null;
                timeSubMap    = null;
                floorSubMap   = null;
                residueSubMap = null;
                psySubMap     = null;

                couplingMag   = null;
                couplingAng   = null;
            }
        }

        static class LookMapping0{
            InfoMode mode;
            InfoMapping0 map;
            Object[] timeLook;
            Object[] floorLook;
            Object[] floorState;
            Object[] residueLook;
            PsyLook[] psyLook;

            FuncTime[] timeFunc;
            FuncFloor[] floorFunc;
            FuncResidue[] residueFunc;

            int ch;
            float[][] decay;
            int lastFrame; // if a different mode is called, we need to
            // invalidate decay and floor state
        }

        static class PsyLook{
            int n;
            PsyInfo vi;

            float[][]   peakAtt;
            float[][][] toneCurves;
            float[][][] noiseCurves;

            float[] ath;
            int[] octave;

            void init(PsyInfo vi, int n, int rate) {}
        }

        /**
         * psycho acoustic setup
         */
        static class PsyInfo {

            float athAtt;

            int   athPointer;
            int   decayPointer;
            int   smoothPointer;
            int   noiseFitPointer;
            int   noiseFitSubBlock;
            float noiseFitThreshDB;

            int     toneMaskPointer;
            float[] toneAtt125Hz   = new float[5];
            float[] toneAtt250Hz   = new float[5];
            float[] toneAtt500Hz   = new float[5];
            float[] toneAtt1000Hz  = new float[5];
            float[] toneAtt2000Hz  = new float[5];
            float[] toneAtt4000Hz  = new float[5];
            float[] toneAtt8000Hz  = new float[5];

            int     peakAttPointer;
            float[] peakAtt125Hz   = new float[5];
            float[] peakAtt250Hz   = new float[5];
            float[] peakAtt500Hz   = new float[5];
            float[] peakAtt1000Hz  = new float[5];
            float[] peakAtt2000Hz  = new float[5];
            float[] peakAtt4000Hz  = new float[5];
            float[] peakAtt8000Hz  = new float[5];

            int     noiseMask;
            float[] noiseAtt125Hz  = new float[5];
            float[] noiseAtt250Hz  = new float[5];
            float[] noiseAtt500Hz  = new float[5];
            float[] noiseAtt1000Hz = new float[5];
            float[] noiseAtt2000Hz = new float[5];
            float[] noiseAtt4000Hz = new float[5];
            float[] noiseAtt8000Hz = new float[5];

            float maxCurveDB;

            float attackCoEff;
            float decayCoEff;

            void free() {}
        }

    }

}
