package ink.meodinger.jogg;

/**
 * Author: Meodinger
 * Date: 2021/10/27
 * Have fun with my code!
 */

public class Packet {

    /**
     * The packet data.
     * This is treated as an opaque type by the ogg layer.
     */
    public byte[] data = null;

    /**
     * Pointer for packet data
     */
    public int pointer = 0;

    /**
     * Indicates the size of the packet data in bytes.
     * Packets can be of arbitrary size.
     */
    public int bytes = 0;

    /**
     * Flag indicating whether this packet begins a logical bitstream.
     * 1 indicates this is the first packet, 0 indicates any other position in the stream.
     */
    public int bos = 0;

    /**
     * Flag indicating whether this packet ends a bitstream.
     * 1 indicates the last packet, 0 indicates any other position in the stream.
     */
    public int eos = 0;

    /**
     * A number indicating the position of this packet in the decoded data.
     * This is the last sample, frame or other unit of information ('granule')
     * that can be completely decoded from this packet.
     */
    public long granulePos = 0;

    /**
     * Sequence number for decode.
     * The framing knows where there's a hole in the data, but we need coupling
     * so that the codec (which is in a separate abstraction layer) also knows
     * about the gap.
     */
    public long packetNo = 0;

    /**
     * Clear data in Packet
     */
    public void clear() {
        data = null;
        pointer = 0;
        bytes = 0;

        bos = 0;
        eos = 0;
        granulePos = 0;
        packetNo = 0;
    }
}
