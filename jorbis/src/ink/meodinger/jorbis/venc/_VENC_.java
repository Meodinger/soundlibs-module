package ink.meodinger.jorbis.venc;

import ink.meodinger.jorbis.codec.Info;

/**
 * Author: Meodinger
 * Date: 2021/11/16
 * Have fun with my code!
 */

public final class _VENC_ {

    private _VENC_() {
    }

    /**
     * Query the current encoder bitrate management setting.
     *
     * Argument: `RateManage._2`
     *
     * Used to query the current encoder bitrate management setting.
     * Also used to initialize fields of a `RateManage._2` for use with
     */
    public static final int RATE_MANAGE_2_GET = 0x14;

    /**
     * Set the current encoder bitrate management settings.
     *
     * Argument: `RateManage._2`
     *
     * Used to set the current encoder bitrate management settings
     * to the values listed in the `RateManage._2`.
     * Passing a NULL pointer will disable bitrate management.
     */
    public static final int RATE_MANAGE_2_SET = 0x15;

    /**
     * Return the current encoder hardLowPass setting (kHz)
     * in the double pointed to by arg.
     *
     * Argument: double[]
     */
    public static final int LOW_PASS_GET = 0x20;

    /**
     * Set the encoder hardLowPass to the value (kHz) pointed to by arg.
     * Valid lowPass settings range from 2 to 99.
     *
     * Argument: double[]
     */
    public static final int LOW_PASS_SET = 0x21;

    /**
     * Return the current encoder impulse block setting
     * in the double pointed to by arg.
     *
     * Argument: double[]
     */
    private static final int I_BLOCK_GET = 0x30;

    /**
     * Set the impulse block bias to the value pointed to by arg.
     *
     * Argument: double[]
     *
     * Valid range is -15.0 to 0.0 [default].
     * A negative impulse block bias will direct to encoder to use more bits
     * when encoding short blocks that contain strong impulses,
     * thus improving the accuracy of impulse encoding.
     */
    public static final int I_BLOCK_SET = 0x31;

    /**
     * Return the current encoder coupling setting
     * in the int pointed to by arg.
     *
     * Argument: int[]
     */
    public static final int COUPLING_GET = 0x40;

    /**
     * Enable/disable channel coupling in multichannel encoding according to arg.
     *
     * Argument: int[]
     *
     * Zero disables channel coupling for multichannel inputs, nonzero enables channel coupling.
     * Setting has no effect on monophonic encoding or multichannel counts that do not offer coupling.
     * At present, coupling is available for stereo and 5.1 encoding.
     */
    public static final int COUPLING_SET = 0x41;

    /* deprecated rate management supported only for compatibility */

    /**
     * Old interface to querying bitrate management settings.
     *
     * Deprecated after move to bit-reservoir style management in 1.1 rendered
     * this interface partially obsolete.
     * Please use `RATE_MANAGE_2_GET` instead.
     *
     * Argument: `RateManage._1`
     */
    @Deprecated(since = "1.1")
    public static final int RATE_MANAGE_GET = 0x10;

    /**
     * Old interface to modifying bitrate management settings.
     *
     * Deprecated after move to bit-reservoir style management in 1.1 rendered
     * this interface partially obsolete.
     * Please use `RATE_MANAGE_2_SET` instead.
     *
     * Argument: `RateManage._1`
     */
    @Deprecated(since = "1.1")
    public static final int RATE_MANAGE_SET = 0x11;

    /**
     * Old interface to setting average-bitrate encoding mode.
     *
     * Deprecated after move to bit-reservoir style management in 1.1 rendered
     * this interface partially obsolete.
     *
     * Please use `RATE_MANAGE_2_SET` instead.
     *
     * Argument: `RateManage._1`
     */
    @Deprecated(since = "1.1")
    public static final int RATE_MANAGE_AVG = 0x12;
    /**
     * Old interface to setting bounded-bitrate encoding modes.
     *
     * Deprecated after move to bit-reservoir style management in 1.1 rendered
     * this interface partially obsolete.
     *
     * Please use `RATE_MANAGE_2_SET` instead.
     *
     * Argument: `RateManage._1`
     */
    @Deprecated(since = "1.1")
    public static final int RATE_MANAGE_HARD = 0x13;


    /**
     * This is the primary function within `jorbis.venc` for setting up managed bitrate modes.
     *
     * Before this function is called, the `jorbis.codec.Info` class should be initialized by `init()`.
     * After encoding, `clear()` should be called.
     *
     * The max_bitrate, nominal_bitrate, and min_bitrate settings are used to set constraints for the encoded file.
     * This function uses these settings to select the appropriate encoding mode and set it up.
     *
     * @param info            Pointer to an initialized `jorbis.codec.Info`.
     * @param channels        The number of channels to be encoded.
     * @param rate            The sampling rate of the source audio.
     * @param maxBitrate      Desired maximum bitrate (limit). -1 indicates unset.
     * @param nominalBitrate  Desired average, or central, bitrate. -1 indicates unset.
     * @param minBitrate      Desired minimum bitrate. -1 indicates unset.
     *
     * @return Zero for success, and negative values for failure.
     *
     * \returnValue 0          Success.
     * \returnValue E_FAULT    Internal logic fault; indicates a bug or heap/stack corruption.
     * \returnValue E_INVALID  Invalid setup request, eg, out of range argument.
     * \returnValue E_IMPL     Unimplemented mode; unable to comply with bitrate request.
     */
    public static int vorbisEncodeInit(Info info, long channels, long rate, long maxBitrate, long nominalBitrate, long minBitrate) {
        return 0;
    }

    /**
     * This function performs step-one of a three-step bitrate-managed encode setup.
     * It functions similarly to the one-step setup performed by `vorbisEncodeInit()`
     * but allows an application to make further encode setup tweaks using `vorbisEncodeCtl()`
     * before finally calling `vorbisEncodeSetupInit()` to complete the setup process.
     *
     * Before this function is called, the `jorbis.codec.Info` should be initialized by `init()`.
     * After encoding, `clear()` should be called.
     *
     * The max_bitrate, nominal_bitrate, and min_bitrate settings are used to set constraints for the encoded file.
     * This function uses these settings to select the appropriate encoding mode and set it up.
     *
     * @param info             Pointer to an initialized `jorbis.codec.Info`.
     * @param channels         The number of channels to be encoded.
     * @param rate             The sampling rate of the source audio.
     * @param maxBitrate       Desired maximum bitrate (limit). -1 indicates unset.
     * @param nominalBitrate   Desired average, or central, bitrate. -1 indicates unset.
     * @param minBitrate       Desired minimum bitrate. -1 indicates unset.
     *
     * @return Zero for success, and negative for failure.
     *
     * \returnValue 0           Success
     * \returnValue E_FAULT     Internal logic fault; indicates a bug or heap/stack corruption.
     * \returnValue E_INVALID   Invalid setup request, eg, out of range argument.
     * \returnValue E_IMPL      Unimplemented mode; unable to comply with bitrate request.
     */
    public static int vorbisEncodeSetupManaged(Info info, long channels, long rate, long maxBitrate, long nominalBitrate, long minBitrate) {
        return 0;
    }

    /**
     * This function performs step-one of a three-step variable bitrate (quality-based) encode setup.
     * It functions similarly to the one-step setup performed by `vorbisEncodeInitVbr()`
     * but allows an application to make further encode setup tweaks using `vorbisEncodeCtl()`
     * before finally calling `vorbisEncodeSetupInit()` to complete the setup process.
     *
     * Before this function is called, the `jorbis.codec.Info` should be initialized by `init()`.
     * After encoding, `clear()` should be called.
     *
     * @param info      Pointer to an initialized `jorbis.codec.Info`.
     * @param channels  The number of channels to be encoded.
     * @param rate      The sampling rate of the source audio.
     * @param quality   Desired quality level, currently from -0.1 to 1.0 (lo to hi).
     *
     * @return Zero for success, and negative values for failure.
     *
     * \returnValue  0          Success
     * \returnValue  E_FAULT    Internal logic fault; indicates a bug or heap/stack corruption.
     * \returnValue  E_INVALID  Invalid setup request, eg, out of range argument.
     * \returnValue  E_IMPL     Unimplemented mode; unable to comply with quality level request.
     */
    public static int vorbisEncodeSetupVbr(Info info, long channels, long rate, float quality) {
        return 0;
    }

    /**
     * This is the primary function within `jorbis.venc` for setting up variable bitrate ("quality" based) modes.
     *
     * Before this function is called, the `jorbis.codec.Info` should be initialized by `init()`.
     * After encoding, `clear()` should be called.
     *
     * @param info         Pointer to an initialized `jorbis.codec.Info`.
     * @param channels     The number of channels to be encoded.
     * @param rate         The sampling rate of the source audio.
     * @param baseQuality  Desired quality level, currently from -0.1 to 1.0 (lo to hi).
     *
     *
     * @return Zero for success, or a negative number for failure.
     *
     * \returnValue 0           Success
     * \returnValue E_FAULT     Internal logic fault; indicates a bug or heap/stack corruption.
     * \returnValue E_INVALID   Invalid setup request, eg, out of range argument.
     * \returnValue E_IMPL      Unimplemented mode; unable to comply with quality level request.
     */
    public static int vorbisEncodeInitVbr(Info info, long channels, long rate, float baseQuality) {
        return 0;
    }

    /**
     * This function performs the last stage of three-step encoding setup,
     * as described in the API overview under managed bitrate modes.
     *
     * Before this function is called, the `jorbis.coedc.Info` should be initialized by `init()`,
     * one of `vorbisEncodeSetupManaged()` or `vorbisEncodeSetupVbr()` called to initialize the
     * high-level encoding setup, and `vorbisEncodeCtl()` called if necessary to make encoding setup changes.
     * `vorbisEncodeSetupInit()` finalizes the high-level encoding structure into a complete encoding
     * setup after which the application may make no further setup changes.
     * After encoding, `clear()` should be called.
     *
     * @param info Pointer to an initialized `jorbis.codec.Info`.
     *
     * @return Zero for success, and negative values for failure.
     *
     * \returnValue 0          Success.
     * \returnValue E_FAULT    Internal logic fault; indicates a bug or heap/stack corruption.
     * \returnValue E_INVALID  Attempt to use `vorbisEncodeSetupInit()` without first calling
     *                         one of `vorbisEncodeSetupManaged()` or `vorbisEncodeSetupVbr()`
     *                         to initialize the high-level encoding setup.
     */
    public static int vorbisEncodeSetupInit(Info info) {
        return 0;
    }

    /**
     * This function implements a generic interface to miscellaneous encoder settings
     * similar to the classic UNIX 'ioctl()' system call.
     * Applications may use `vorbisEncodeCtl()` to query or set bitrate management
     * or quality mode details by using one of several request arguments detailed below.
     * `vorbisEncodeCtl()` must be called after one of `vorbisEncodeSetupManaged()`
     * or `vorbisEncodeSetupVbr()`. When used to modify settings, `vorbisEncodeCtl()`
     * must be called before `vorbisEncodeSetupInit()`.
     *
     * @param info   Pointer to an initialized `jorbis.codec.Info`.
     * @param number Specifies the desired action; See `enc-ctl-codes` "the list of available requests".
     * @param args   int[] pointing to a data structure matching the request argument.
     *
     * \returnValue 0          Success. Any further return information (such as the result of a query)
     *                         is placed into the storage pointed to by args.
     * \returnValue E_INVALID  Invalid argument, or an attempt to modify a setting after
     *                         calling `vorbisEncodeSetupInit()`.
     * \returnValue E_IMPL     Unimplemented or unknown request
     */
    public static int vorbisEncodeCtl(Info info, int number, int... args) {
        return 0;
    }

}
