package ink.meodinger.jorbis.codec.internal;

import ink.meodinger.jogg.Buffer;


/**
 * Author: Meodinger
 * Date: 2021/11/16
 * Have fun with my code!
 */

public class BlockInternal {

    public static final int TYPE_IMPULSE = 0;
    public static final int TYPE_PADDING = 1;
    public static final int TYPE_TRANSITION = 0;
    public static final int TYPE_LONG = 1;

    private static final int PACKET_BLOBS = 15;

    private float[][] pcmDelay;
    private float ampMax;
    private int blockType;

    private Buffer[][] packetBlob; // = new Buffer[][PACKET_BLOBS];
}
