package ink.meodinger.jogg;

/**
 * Author: Meodinger
 * Date: 2021/10/26
 * Have fun with my code!
 */

public class Buffer {

    private static final int BUFFER_INCREMENT = 256;

    // For example: MASK[7] = 0x7f -> 0b01111111
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

    private enum IOState {
        WRITE("Write"), READ("Read");

        String description;

        IOState(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }
    private IOState state = null;
    private void check(IOState expected) {
        if (state != expected) throw new IllegalStateException("Buffer state should be" + expected);
    }

    byte[] buffer = null;

    private int pointer = 0;
    private int endByte = 0;
    private int endBit = 0;
    private int storage = 0;

    /**
     * Initialize buffer
     */
    public void writeInit() {
        state = IOState.WRITE;

        buffer = new byte[BUFFER_INCREMENT];
        buffer[0] = 0x0;

        pointer = 0;
        storage = BUFFER_INCREMENT;
    }

    /**
     * Clear buffer
     */
    public void writeClear() {
        check(IOState.WRITE);

        buffer = null;
    }

    /**
     * Write bytes until 0 encountered
     * @param s bytes to write
     */
    public void write(byte[] s) {
        for (byte b : s) {
            if (b == 0) break;
            write(b, 8);
        }
    }

    /**
     * Write bits to buffer
     * @param value bits to write as int
     * @param bits  length of value
     */
    public void write(int value, int bits) {
        check(IOState.WRITE);

        // Mask is set here in case of bits changes
        int mask = MASK[bits];

        // Buffer cannot contain all bits, increase buffer length
        // +4 because length of int is 32 (4 * 8 byte -> 32 bits)
        if (endByte + 4 >= storage) {
            byte[] temp = new byte[storage + BUFFER_INCREMENT];

            System.arraycopy(buffer, 0, temp, 0, storage);
            buffer = temp;
            storage += BUFFER_INCREMENT;
        }

        // use mask to take valid bits
        value &= mask;
        // move to current bit index
        bits += endBit;

        // write to byte at the end
        buffer[pointer] |= (byte) (value << endBit);
        // write to following bytes if value contains more than one byte
        if (bits >= 8) {
            buffer[pointer + 1] = (byte) (value >>> (8 - endBit));
            if (bits >= 16) {
                buffer[pointer + 2] = (byte) (value >>> (16 - endBit));
                if (bits >= 24) {
                    buffer[pointer + 3] = (byte) (value >>> (24 - endBit));
                    if (bits >= 32) {
                        if (endBit > 0) buffer[pointer + 4] = (byte) (value >>> (32 - endBit));
                        else buffer[pointer + 4] = 0;
                    }
                }
            }
        }

        // update state
        pointer += bits / 8; // equal to endByte
        endByte += bits / 8; // endByte index
        endBit = bits & 7;   // endBit range is 0 -> 7
    }

    /**
     * Initialize read
     * @param buf   init buffer
     * @param bytes buffer length
     */
    public void readInit(byte[] buf, int bytes) {
        readInit(buf, 0, bytes);
    }

    /**
     * Initialize read
     * @param buf   init buffer
     * @param start start index
     * @param bytes buffer length
     */
    public void readInit(byte[] buf, int start, int bytes) {
        state = IOState.READ;

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
    public void read(byte[] s, int bytes) {
        int i = 0;
        while (bytes-- != 0) {
            s[i++] = (byte) read(8);
        }
    }

    /**
     * Read bits
     * @param bits length of bits to read
     * @return bits as int
     */
    public int read(int bits) {
        check(IOState.READ);

        // Mask must be set here because bits will change
        int mask = MASK[bits];
        int ret;

        // Move to current bit index
        bits += endBit;

        // End of buffer encountered
        if (endByte + 4 >= storage) {
            ret = -1;
            if (endByte + (bits - 1) / 8 >= storage) {
                // If really read out of range, return -1
                pointer += bits / 8;
                endByte += bits / 8;
                endBit = bits & 7;
                return ret;
            }
        }

        // Read current byte
        ret = (buffer[pointer] & 0xff) >>> endBit;
        // Read more bytes if needed
        if (bits > 8) {
            ret |= (buffer[pointer + 1] & 0xff) << (8 - endBit);
            if (bits > 16) {
                ret |= (buffer[pointer + 2] & 0xff) << (16 - endBit);
                if (bits > 24) {
                    ret |= (buffer[pointer + 3] & 0xff) << (24 - endBit);
                    if (bits > 32 && endBit != 0) {
                        ret |= (buffer[pointer + 4] & 0xff) << (32 - endBit);
                    }
                }
            }
        }
        ret &= mask;

        // Update state
        pointer += bits / 8;
        endByte += bits / 8;
        endBit = bits & 7;

        return ret;
    }

    /**
     * not clear its function
     * @param bits length of bits to read
     * @return bits as Int
     */
    public int readB(int bits) {
        check(IOState.READ);

        int mask = 32 - bits;
        int ret;

        bits += endBit;

        if (endByte + 4 >= storage) {
            ret = -1;
            if (endByte * 8 + bits > storage * 8) {
                pointer += bits / 8;
                endByte += bits / 8;
                endBit = bits & 7;
                return ret;
            }
        }

        ret = (buffer[pointer] & 0xff) << (24 + endBit);
        if (bits > 8) {
            ret |= (buffer[pointer+1] & 0xff) << (16 + endBit);
            if (bits > 16) {
                ret |= (buffer[pointer + 2] & 0xff) << (8 + endBit);
                if (bits > 24) {
                    ret |= (buffer[pointer + 3] & 0xff) << (endBit);
                    if (bits > 32 && endBit != 0) {
                        ret |= (buffer[pointer + 4] & 0xff) >> (8 - endBit);
                    }
                }
            }
        }
        ret = (ret >>> (mask >> 1)) >>> ((mask + 1) >> 1);

        pointer += bits / 8;
        endByte += bits / 8;
        endBit = bits & 7;

        return ret;
    }

    /**
     * Read one bit
     * @return bit as int
     */
    public int readOne() {
        check(IOState.READ);

        int ret;

        if (endByte >= storage) {
            ret = -1;
            endBit++;
            if (endBit > 7) {
                pointer++;
                endByte++;
                endBit = 0;
            }
            return ret;
        }

        ret = (buffer[pointer] >> endBit) & 0x1;

        endBit++;
        if (endBit > 7) {
            pointer++;
            endByte++;
            endBit = 0;
        }
        return ret;
    }

    /**
     * reset buffer
     */
    void reset() {
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
    public int look(int bits) {
        int mask = MASK[bits];
        int ret;

        bits += endBit;

        if (endByte + 4 >= storage) {
            ret = -1;
            if (endByte + (bits - 1) / 8 >= storage) return ret;
        }

        // Look current byte
        ret = (buffer[pointer] & 0xff) >>> endBit;
        // Look more bytes if needed
        if (bits > 8) {
            ret |= (buffer[pointer + 1] & 0xff) << (8 - endBit);
            if (bits > 16) {
                ret |= (buffer[pointer + 2] & 0xff) << (16 - endBit);
                if (bits > 24) {
                    ret |= (buffer[pointer + 3] & 0xff) << (24 - endBit);
                    if (bits > 32 && endBit != 0) {
                        ret |= (buffer[pointer + 4] & 0xff) << (32 - endBit);
                    }
                }
            }
        }
        // Use mask to take off invalid bits
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
    public void skip(int bits) {
        bits += endBit;

        pointer += bits / 8;
        endByte += bits / 8;
        endBit = bits & 7;
    }

    /**
     * Skip one bit
     */
    public void skipOne() {
        endBit++;
        if (endBit > 7) {
            pointer++;
            endByte++;
            endBit = 0;
        }
    }

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

    // ----- static method ----- //

    /**
     * log2(v) as int + 1
     */
    public static int ilog(int v) {
        int ret = 0;
        while (v > 0) {
            ret++;
            v >>>= 1;
        }
        return ret;
    }
}
