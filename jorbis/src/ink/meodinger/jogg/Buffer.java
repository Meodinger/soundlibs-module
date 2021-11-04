package ink.meodinger.jogg;

/**
 * Author: Meodinger
 * Date: 2021/10/26
 * Have fun with my code!
 */

public class Buffer {

    /*
     * Something about MSb, LSb, Big-endian and Little-endian
     *
     * Big- and little-endian is about *byte* order in memory
     * For example: an Integer 0x0066CCFF
     *           Higher  ----->  Lower  (The lowest is the start byte)
     *   Little-: 0xFF 0xCC 0x66 0x00   or  1111 1111 1100 1100 0110 0110 0000 0000
     *   Big-   : 0x00 0x66 0xCC 0xFF   or  0000 0000 0110 0110 1100 1100 1111 1111
     *
     * MSb and LSb is about *bit* in bytes
     * For example: bytes 0xFF 0xCC 0x66 0x00
     *        0b0000_0000
     *   LSb:           ^ of 0x00
     *        0b1111_1111
     *   MSb:   ^         of 0xFF
     *
     * In java, we don't need to care about byte order in memory.
     * Java always use Little-endian, so we only need LSb methods of libogg.
     */

    private static final int BUFFER_INCREMENT = 256;

    /**
     * For example: MASK[7] = 0x7f -> 0b0111_1111
     */
    private static final int[] MASK = { 0x00000000,
            0x00000001, 0x00000003, 0x00000007, 0x0000000f,
            0x0000001f, 0x0000003f, 0x0000007f, 0x000000ff,
            0x000001ff, 0x000003ff, 0x000007ff, 0x00000fff,
            0x00001fff, 0x00003fff, 0x00007fff, 0x0000ffff,
            0x0001ffff, 0x0003ffff, 0x0007ffff, 0x000fffff,
            0x001fffff, 0x003fffff, 0x007fffff, 0x00ffffff,
            0x01ffffff, 0x03ffffff, 0x07ffffff, 0x0fffffff,
            0x1fffffff, 0x3fffffff, 0x7fffffff, 0xffffffff
    };

    private int endByte = 0;
    private int endBit = 0;

    private byte[] buffer = null;
    private int pointer = 0;
    private int storage = 0;

    // ----- Write ----- //

    /**
     * Initialize buffer
     */
    public void writeInit() {
        buffer = new byte[BUFFER_INCREMENT];
        buffer[0] = 0x0;

        pointer = 0;
        storage = BUFFER_INCREMENT;
    }

    public void writeTrunc(final int bits) {
        final int _endBytes = bits >> 3;
        final int _endBit = bits - _endBytes * 8;

        buffer[_endBit] &= MASK[_endBit];
        pointer = _endBytes;
        endByte = _endBytes;
        endBit  = _endBit;
    }

    public void writeAlign() {
        final int bits = 8 - endBit;
        if (bits < 8) write(0, bits);
    }

    /**
     * Clear buffer
     */
    public void writeClear() {
        buffer = null;
    }

    /**
     * Write bytes until 0 encountered
     * @param s bytes to write
     */
    public void write(final byte[] s) {
        for (final byte b : s) {
            if (b == 0) break;
            write(b, 8);
        }
    }

    /**
     * Write bits to buffer
     * @param value bits to write as int
     * @param bits  length of value
     */
    public void write(final int value, final int bits) {
        // Mask is set here in case of bits changes
        final int mask = MASK[bits];

        // Buffer cannot contain all bits, increase buffer length
        // +4 because length of int is 32 (4 * 8 byte -> 32 bits)
        if (endByte + 4 >= storage) {
            final int newStorage = storage + BUFFER_INCREMENT;
            final byte[] newBuffer = new byte[newStorage];

            System.arraycopy(buffer, 0, newBuffer, 0, storage);
            buffer = newBuffer;
            storage = newStorage;
        }

        // use mask to take valid bits
        final int validValue = value & mask;
        // move to current bit index (how many bits will be actually write)
        final int actualBits = bits + endBit;

        // write to byte at the end
        buffer[pointer] |= (byte) (validValue << endBit);
        // write to following bytes if value contains more than one byte
        if (actualBits >= 8) {
            buffer[pointer + 1] = (byte) (validValue >>> (8 - endBit));
            if (actualBits >= 16) {
                buffer[pointer + 2] = (byte) (validValue >>> (16 - endBit));
                if (actualBits >= 24) {
                    buffer[pointer + 3] = (byte) (validValue >>> (24 - endBit));
                    if (actualBits >= 32) {
                        if (endBit > 0) buffer[pointer + 4] = (byte) (validValue >>> (32 - endBit));
                        else buffer[pointer + 4] = 0;
                    }
                }
            }
        }

        // update state
        pointer += actualBits / 8; // equal to endByte
        endByte += actualBits / 8; // endByte index
        endBit   = actualBits & 7; // endBit range is 0 -> 7
    }

    // ----- Read ----- //

    /**
     * Initialize read
     * @param buf   init buffer
     * @param bytes buffer length
     */
    public void readInit(final byte[] buf, final int bytes) {
        readInit(buf, 0, bytes);
    }

    /**
     * Initialize read
     * @param buf   init buffer
     * @param start start index
     * @param bytes buffer length
     */
    public void readInit(final byte[] buf, final int start, final int bytes) {
        buffer = buf;
        pointer = start;
        endByte = 0;
        endBit = 0;
        storage = bytes;
    }

    /**
     * Read bytes to a byte array
     * @param s     which array will write to
     * @param bytes length of bytes
     */
    public void read(final byte[] s, final int bytes) {
        int i = 0;
        int left = bytes;
        while (left-- != 0) {
            s[i++] = (byte) read(8);
        }
    }

    /**
     * Read bits
     * @param bits length of bits to read
     * @return bits as int
     */
    public int read(final int bits) {
        // Mask must be set here because bits will change
        final int mask = MASK[bits];
        // Move to current bit index (how many bits will be actually read)
        final int actualBits = bits + endBit;

        // End of buffer encountered
        if (endByte + 4 >= storage) {
            if (endByte + ((actualBits + 7) >> 3) > storage) {
                // If really read out of range, return -1
                pointer = storage - 1; // ?? pointer = NULL
                endByte = storage;
                endBit  = 1;
                return -1;
            } else if (actualBits == 0) return 0;
            // special case to avoid reading b->ptr[0],
            // which might be past the end of the buffer;
            // also skips some useless accounting
        }

        // Read current byte
        int ret = (buffer[pointer] & 0xff) >>> endBit;
        // Read more bytes if needed
        if (actualBits > 8) {
            ret |= (buffer[pointer + 1] & 0xff) << (8 - endBit);
            if (actualBits > 16) {
                ret |= (buffer[pointer + 2] & 0xff) << (16 - endBit);
                if (actualBits > 24) {
                    ret |= (buffer[pointer + 3] & 0xff) << (24 - endBit);
                    if (actualBits > 32 && endBit != 0) {
                        ret |= (buffer[pointer + 4] & 0xff) << (32 - endBit);
                    }
                }
            }
        }
        ret &= mask;

        // Update state
        pointer += actualBits / 8;
        endByte += actualBits / 8;
        endBit   = actualBits & 7;

        return ret;
    }

    /**
     * Read one bit
     * @return bit as int
     */
    public int readOne() {
        if (endByte >= storage) {
            // If really read out of range, return -1
            pointer = storage - 1; // ?? pointer = NULL
            endByte = storage;
            endBit  = 1;
            return -1;
        }

        int ret = (buffer[pointer] >>> endBit) & 0x1;

        if (++endBit > 7) {
            pointer++;
            endByte++;
            endBit = 0;
        }
        return ret;
    }

    // ----- Other ----- //

    /**
     * reset buffer
     */
    public void reset() {
        buffer[0] = 0x0;

        pointer = 0;
        endByte = 0;
        endBit = 0;
    }

    /**
     * Look some bits ahead
     * @param bits length of bits to look
     * @return bits as int
     */
    public int look(final int bits) {
        final int mask = MASK[bits];
        final int actualBits = bits + endBit;

        if (endByte + 4 >= storage) {
            if (endByte + ((actualBits + 7) >> 3) > storage) return -1;
            // special case to avoid reading b->ptr[0],
            // which might be past the end of the buffer;
             // also skips some useless accounting
            else if (actualBits == 0) return 0;
        }

        // Look current byte
        int ret = (buffer[pointer] & 0xff) >>> endBit;
        // Look more bytes if needed
        if (actualBits > 8) {
            ret |= (buffer[pointer + 1] & 0xff) << (8 - endBit);
            if (actualBits > 16) {
                ret |= (buffer[pointer + 2] & 0xff) << (16 - endBit);
                if (actualBits > 24) {
                    ret |= (buffer[pointer + 3] & 0xff) << (24 - endBit);
                    if (actualBits > 32 && endBit != 0) {
                        ret |= (buffer[pointer + 4] & 0xff) << (32 - endBit);
                    }
                }
            }
        }
        // Use mask to get rid of invalid bits
        return ret & mask;
    }

    /**
     * Look one bit ahead
     * @return one bit as int
     */
    public int lookOne() {
        if (endByte >= storage) return -1;
        return ((buffer[pointer] >> endBit) & 0x1);
    }

    /**
     * Skip some bits
     * @param bits length of bits to skip
     */
    public void advance(final int bits) {
        final int actualBits = bits + endBit;

        pointer += actualBits / 8;
        endByte += actualBits / 8;
        endBit   = actualBits & 7;
    }

    /**
     * Skip one bit
     */
    public void advanceOne() {
        if (++endBit > 7) {
            pointer++;
            endByte++;
            endBit = 0;
        }
    }

    // ----- Accessors ----- //

    /**
     * Bytes count in buffer
     * @return bytes count
     */
    public int bytes() {
        return (endByte + (endBit + 7) / 8);
    }

    /**
     * Bits count in buffer
     * @return bits count
     */
    public int bits() {
        return (endByte * 8 + endBit);
    }

    /**
     * Get internal buffer
     */
    public byte[] buffer() {
        return buffer;
    }

    // ----- Static Write Copy ----- //

    public static void writeCopy(final Buffer target, final byte[] source, final int bits) {
        final int validBytes = bits / 8;
        final int actualBits = bits - validBytes * 8;
        final int pBytes = (target.endBit + bits) / 8;

        // Expand storage up-front
        if (target.endByte + pBytes >= target.storage) {
            final int newStorage = target.endByte + pBytes + BUFFER_INCREMENT;
            final byte[] temp = new byte[newStorage];

            System.arraycopy(target.buffer, 0, temp, 0, target.storage);
            target.storage = newStorage;
            target.buffer = temp;
        }

        // copy whole octets
        if (target.endBit > 0) {
            // unaligned copy, do it
            for (int i = 0; i < validBytes; i++) target.write(source[i], 8);
        } else {
            // aligned block copy
            System.arraycopy(source, 0, target.buffer, target.pointer, validBytes);
            target.pointer += validBytes;
            target.endByte += validBytes;
            target.buffer[target.pointer] = 0x0;
        }

        // copy trailing bits
        if (actualBits > 0) target.write(source[validBytes], actualBits);
    }
}
