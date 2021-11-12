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
     * Big- and little-endian is about *byte* order in memory.
     * For example: an Integer 0x0066CCFF
     *           Higher  ----->  Lower  (The lowest is the start byte)
     *   Little-: 0xFF 0xCC 0x66 0x00   or  1111 1111 1100 1100 0110 0110 0000 0000
     *   Big-   : 0x00 0x66 0xCC 0xFF   or  0000 0000 0110 0110 1100 1100 1111 1111
     *
     * MSb and LSb is about *bit* in bytes.
     * For example: bytes 0xFF 0xCC 0x66 0x00
     *        0b0000_0000
     *   LSb:           ^ of 0x00
     *        0b1111_1111
     *   MSb:   ^         of 0xFF
     *
     * In java, we don't need to care about byte order in memory.
     * Java always use Little-endian, so we only need LSb methods of libogg.
     */

    /*
     * Something about err handling when value > Int.MAX_VALUE
     *
     * Please see LINE:130
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
        this.endByte = 0;
        this.endBit = 0;

        this.buffer = new byte[BUFFER_INCREMENT];
        this.buffer[0] = 0x0;

        this.pointer = 0;
        this.storage = BUFFER_INCREMENT;
    }

    /**
     * Truncate an already written-to Buffer.
     * The Buffer must already be initialized for writing using `writeInit`.
     * @param bits Number of bits to keep in the buffer (size after truncation)
     */
    public void writeTrunc(final int bits) {
        final int _endBytes = bits >> 3;
        final int _endBit = bits - _endBytes * 8;

        this.pointer = _endBytes;
        this.endByte = _endBytes;
        this.endBit  = _endBit;
        this.buffer[this.pointer] &= MASK[_endBit];
    }

    /**
     * Pad the Buffer with zeros out to the next byte boundary.
     * The Buffer must already be initialized for writing using `writeInit`.
     * Only 32 bits can be written at a time
     */
    public void writeAlign() {
        final int bits = 8 - this.endBit;
        if (bits < 8) write(0, bits);
    }

    /**
     * Clear buffer
     */
    public void writeClear() {
        this.buffer = null;
    }

    @Deprecated(forRemoval = true)
    public int writeCheck() {
        return 0;
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
     * @param bits  length of value (max = 32)
     */
    public void write(final int value, final int bits) {
        // if (bits < 0 || bits > 32) { /* err */ }
        // We don't need to check this, err will occur when get mask.
        // The same to next storage check and others.
        // But its will be nice to preserve the `endByte >= storage - 4`
        // and others, make the code the same as the C code of libogg.

        // Mask is set here in case of bits changes
        final int mask = MASK[bits];

        // Buffer cannot contain all bits, increase buffer length.
        // enByte + 4 because length of int is 32 (4 * 8 byte -> 32 bits).
        // use -4 in case of endByte + 4 > Int.MAX_VALUE.
        if (this.endByte >= this.storage - 4) {
            // if (storage > Integer.MAX_VALUE - BUFFER_INCREMENT) { /* err */ }

            final int newStorage = this.storage + BUFFER_INCREMENT;
            final byte[] newBuffer = new byte[newStorage];

            System.arraycopy(this.buffer, 0, newBuffer, 0, this.storage);
            this.buffer = newBuffer;
            this.storage = newStorage;
        }

        // use mask to take valid bits
        final int validValue = value & mask;
        // move to current bit index (how many bits will be actually write)
        final int actualBits = bits + this.endBit;

        // write to byte at the end
        this.buffer[this.pointer] |= (byte) (validValue << this.endBit);
        // write to following bytes if value contains more than one byte
        if (actualBits >= 8) {
            this.buffer[this.pointer + 1] = (byte) (validValue >>> (8 - this.endBit));
            if (actualBits >= 16) {
                this.buffer[this.pointer + 2] = (byte) (validValue >>> (16 - this.endBit));
                if (actualBits >= 24) {
                    this.buffer[this.pointer + 3] = (byte) (validValue >>> (24 - this.endBit));
                    if (actualBits >= 32) {
                        if (this.endBit > 0) this.buffer[this.pointer + 4] = (byte) (validValue >>> (32 - this.endBit));
                        else this.buffer[this.pointer + 4] = 0;
                    }
                }
            }
        }

        // update state
        this.pointer += actualBits / 8; // equal to endByte
        this.endByte += actualBits / 8; // endByte index
        this.endBit   = actualBits & 7; // endBit range is 0 -> 7
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
        this.buffer = buf;
        this.pointer = start;
        this.endByte = 0;
        this.endBit = 0;
        this.storage = bytes;
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
        final int actualBits = bits + this.endBit;

        // End of buffer encountered
        if (this.endByte >= this.storage - 4) {
            if (this.endByte > (this.storage - ((actualBits + 7) >> 3))) {
                // If really read out of range, return -1
                this.pointer = this.storage - 1; // ?? pointer = NULL
                this.endByte = this.storage;
                this.endBit  = 1;
                return -1;
            } else if (actualBits == 0) return 0;
            // special case to avoid reading b->ptr[0],
            // which might be past the end of the buffer;
            // also skips some useless accounting.
        }

        // Read current byte
        int ret = (this.buffer[this.pointer] & 0xff) >>> this.endBit;
        // Read more bytes if needed
        if (actualBits > 8) {
            ret |= (this.buffer[this.pointer + 1] & 0xff) << (8 - this.endBit);
            if (actualBits > 16) {
                ret |= (this.buffer[this.pointer + 2] & 0xff) << (16 - this.endBit);
                if (actualBits > 24) {
                    ret |= (this.buffer[this.pointer + 3] & 0xff) << (24 - this.endBit);
                    if (actualBits > 32 && this.endBit != 0) {
                        ret |= (this.buffer[this.pointer + 4] & 0xff) << (32 - this.endBit);
                    }
                }
            }
        }
        ret &= mask;

        // Update state
        this.pointer += actualBits / 8;
        this.endByte += actualBits / 8;
        this.endBit   = actualBits & 7;

        return ret;
    }

    /**
     * Read one bit
     * @return bit as int
     */
    public int readOne() {
        if (this.endByte >= this.storage) {
            // If really read out of range, return -1
            this.pointer = this.storage - 1; // ?? pointer = NULL
            this.endByte = this.storage;
            this.endBit  = 1;
            return -1;
        }

        final int ret = (this.buffer[this.pointer] >>> this.endBit) & 0x1;

        if (++this.endBit > 7) {
            this.pointer++;
            this.endByte++;
            this.endBit = 0;
        }
        return ret;
    }

    // ----- Other ----- //

    /**
     * reset buffer
     */
    public void reset() {
        this.buffer[0] = 0x0;

        this.pointer = 0;
        this.endByte = 0;
        this.endBit = 0;
    }

    /**
     * Look some bits ahead
     * @param bits length of bits to look
     * @return bits as int
     */
    public int look(final int bits) {
        final int mask = MASK[bits];
        final int actualBits = bits + this.endBit;

        if (this.endByte >= this.storage - 4) {
            if (this.endByte > (this.storage - ((actualBits + 7) >> 3))) return -1;
            // special case to avoid reading b->ptr[0],
            // which might be past the end of the buffer;
            // also skips some useless accounting.
            else if (actualBits == 0) return 0;
        }

        // Look current byte
        int ret = (this.buffer[this.pointer] & 0xff) >>> this.endBit;
        // Look more bytes if needed
        if (actualBits > 8) {
            ret |= (this.buffer[this.pointer + 1] & 0xff) << (8 - this.endBit);
            if (actualBits > 16) {
                ret |= (this.buffer[this.pointer + 2] & 0xff) << (16 - this.endBit);
                if (actualBits > 24) {
                    ret |= (this.buffer[this.pointer + 3] & 0xff) << (24 - this.endBit);
                    if (actualBits > 32 && this.endBit != 0) {
                        ret |= (this.buffer[this.pointer + 4] & 0xff) << (32 - this.endBit);
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
        if (this.endByte >= this.storage) return -1;
        return ((this.buffer[this.pointer] >> this.endBit) & 0x1);
    }

    /**
     * Skip some bits
     * @param bits length of bits to skip
     */
    public void advance(final int bits) {
        final int actualBits = bits + this.endBit;

        this.pointer += actualBits / 8;
        this.endByte += actualBits / 8;
        this.endBit   = actualBits & 7;
    }

    /**
     * Skip one bit
     */
    public void advanceOne() {
        if (++this.endBit > 7) {
            this.pointer++;
            this.endByte++;
            this.endBit = 0;
        }
    }

    // ----- Accessors ----- //

    /**
     * Bytes count in buffer
     * @return bytes count
     */
    public int bytes() {
        return (this.endByte + (this.endBit + 7) / 8);
    }

    /**
     * Bits count in buffer
     * @return bits count
     */
    public int bits() {
        return (this.endByte * 8 + this.endBit);
    }

    /**
     * Get internal buffer
     */
    public byte[] buffer() {
        return this.buffer;
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
