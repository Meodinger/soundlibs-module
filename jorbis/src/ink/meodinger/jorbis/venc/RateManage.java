package ink.meodinger.jorbis.venc;

/**
 * Author: Meodinger
 * Date: 2021/11/17
 * Have fun with my code!
 */

public abstract class RateManage {

    /**
     * This is a deprecated interface.
     * Please use vorbis_encode_ctl() with the RateManage._2
     * and `RATE_MANAGE_2_GET`, `RATE_MANAGE_2_SET` calls in new code.
     *
     * The RateManage._1 is used with vorbis_encode_ctl()
     * and `RATE_MANAGE_GET`, `RATE_MANAGE_SET`, `RATE_MANAGE_AVG`, `RATE_MANAGE_HARD` calls
     * in order to query and modify specifics of the encoder's
     * bitrate management configuration.
     */
    @Deprecated(since = "1.1")
    public final static class _1 extends RateManage {

        /**
         * Nonzero if bitrate management is active
         */
        int managementActive;

        /**
         * Hard lower limit (in kilobits per second) below which
         * the stream bitrate will never be allowed for any given
         * bitrate_hard_window seconds of time.
         */
        long bitrateHardMin;

        /**
         * Hard upper limit (in kilobits per second) above which
         * the stream bitrate will never be allowed for any given
         * bitrate_hard_window seconds of time.
         */
        long bitrateHardMax;

        /**
         * The window period (in seconds) used to regulate
         * the hard bitrate minimum and maximum
         */
        double bitrateHardWindow;

        /**
         * Soft lower limit (in kilobits per second) below which
         * the average bitrate tracker will start nudging the bitrate higher.
         */
        long bitrateAverageLow;

        /**
         * Soft upper limit (in kilobits per second) above which
         * the average bitrate tracker will start nudging the bitrate lower.
         */
        long bitrateAverageHigh;

        /**
         * The window period (in seconds) used to regulate
         * the average bitrate minimum and maximum.
         */
        double bitrateAverageWindow;

        /**
         * Regulates the relative centering of the average and hard windows.
         * In libvorbis 1.0 and 1.0.1, the hard window regulation overlapped
         * but followed the average window regulation.
         * In libvorbis 1.1 a bit-reservoir interface replaces the old windowing interface;
         * the older windowing interface is simulated and this field has no effect.
         */
        double bitrateAverageWindowCenter;

    }

    /**
     * The `RateManage._2` is used with vorbis_encode_ctl()
     * and the `RATE_MANAGE_2_GET`, `RATE_MANAGE_2_SET` calls
     * in order to query and modify specifics of the encoder's
     * bitrate management configuration.
     */
    public final static class _2 extends RateManage {

        /**
         * Nonzero if bitrate management is active
         */
        int managementActive;

        /**
         * Lower allowed bitrate limit in kilobits per second
         */
        long bitrateLimitMinKbps;

        /**
         * Upper allowed bitrate limit in kilobits per second
         */
        long bitrateLimitMaxKbps;

        /**
         * Size of the bitrate reservoir in bits
         */
        long bitrateLimitReservoirBits;

        /**
         * Regulates the bitrate reservoir's preferred fill level in a range from 0.0 to 1.0;
         * 0.0 tries to bank bits to buffer against future bitrate spikes,
         * 1.0 buffers against future sudden drops in instantaneous bitrate.
         * Default is 0.1
         */
        double bitrateLimitReservoirBias;

        /**
         * Average bitrate setting in kilobits per second
         */
        long bitrateAverageKbps;

        /**
         * Slew rate limit setting for average bitrate adjustment;
         * sets the minimum time in seconds the bitrate tracker
         * may swing from one extreme to the other when boosting
         * or damping average bitrate.
         */
        double bitrateAverageDamping;
    }
}
