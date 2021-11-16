package ink.meodinger.jorbis.codec.internal;

import ink.meodinger.jogg.Buffer;
import ink.meodinger.jorbis.Vorbis;

/**
 * Author: Meodinger
 * Date: 2021/11/16
 * Have fun with my code!
 */

public class BlockInternal {

    private static final int PACKET_BLOBS = 15;

    private float[][] pcmDelay;
    private float ampMax;
    private int blockType;

    private Buffer[][] packetBlob = new Buffer[][PACKET_BLOBS];
}
