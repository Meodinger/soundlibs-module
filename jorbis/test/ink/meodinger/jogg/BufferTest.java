package ink.meodinger.jogg;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Author: Meodinger
 * Date: 2021/11/2
 * Have fun with my code!
 */

public class BufferTest {

    private static final int[] testBuffer1 = {
            18, 12, 103948, 4325, 543, 76, 432, 52, 3, 65, 4, 56, 32, 42, 34, 21, 1, 23, 32, 546, 456, 7,
            567, 56, 8, 8, 55, 3, 52, 342, 341, 4, 265, 7, 67, 86, 2199, 21, 7, 1, 5, 1, 4
    };
    private static final int test1Size = 43;

    private static final int[] testBuffer2 = {
            216531625, 1237861823, 56732452, 131, 3212421, 12325343, 34547562, 12313212,
            1233432, 534, 5, 346435231, 14436467, 7869299, 76326614, 167548585,
            85525151, 0, 12321, 1, 349528352
    };
    private static final int test2Size = 21;

    private static final int[] testBuffer3 = {
            1, 0, 14, 0, 1, 0, 12, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1,
            0, 1, 30, 1, 1, 1, 0, 0, 1, 0, 0, 0, 12, 0, 11, 0, 1, 0, 0, 1
    };
    private static final int test3Size = 56;

    private static final int[] large = {
            2136531625, 2137861823, 56732452, 131, 3212421, 12325343, 34547562, 12313212,
            1233432, 534, 5, 2146435231, 14436467, 7869299, 76326614, 167548585,
            85525151, 0, 12321, 1, 2146528352
    };


    private static final int oneSize = 33;
    private static final byte[] one = {
            -110, 25, 44, -105, -61, 15, -103, -80, -23, -125, -60,
            65, 85, -84, 47, 40, 34, -14, -33, -120, 35, -34, -45,
            86, -85, 50, -31, -121, -42, 75, -84, -33, 4
    };
    private static final byte[] oneB = {
            -106, 101, -125, 33, -53, 15, -52, -40, 105, -63, -100,
            65, 84, 85, -34, 8, -117, -111, -29, 126, 34, 55, -12,
            -85, 85, 100, 39, -61, -83, 18, -11, -5, -128
    };

    private static final int twoSize = 6;
    private static final byte[] two = { 61, -1, -1, -5, -25, 29 };
    private static final byte[] twoB = { -9, 63, -1, -3, -7, 120 };

    private static final int threeSize = 54;
    private static final byte[] three = {
            -87, 2, -24, -4, 91, -124, -100, 36, 89, 13, 123, -80,
            -112, 32, -2, -114, -32, 85, 59, 121, -112, 79, 124,
            23, 67, 90, 90, -40, 79, 23, 83, 58, -121, -60, 61, 55,
            -127, -73, 54, 101, 100, -86, 37, 127, 126, 10, 100, 52,
            4, 14, 18, 86, 77, 1
    };
    private static final byte[] threeB = {
            -50, -128, 42, -103, 57, 8, -73, -5, 13, 89, 36, 30, 32,
            -112, -73, -126, 59, -16, 121, 59, 85, -33, 19, -28, -76,
            -122, 33, 107, 74, 98, -23, -3, -60, -121, 63, 2, 110,
            114, 50, -101, 90, 127, 37, -86, 104, -56, 20, -2, 4, 58,
            106, -80, -112, 0
    };

    private static final int fourSize = 38;
    private static final byte[] four = {
            18, 6, -93, -4, 97, -62, 104, -125, 32, 1, 7, 82, -119,
            42, -127, 11, 72, -124, 60, -36, 112, 8, -60, 109, 64,
            -77, 86, 9, -119, -61, -48, 122, -87, 28, 2, -123, 0, 1
    };
    private static final byte[] fourB = {
            36, 48, 102, 83, -13, 24, 52, 7, 4, 35, -124, 10, -111,
            21, 2, 93, 2, 41, 1, -37, -72, 16, 33, -72, 54, -107,
            -86, -124, 18, 30, 29, 98, -27, 67, -127, 10, 4, 32
    };

    private static final int fiveSize = 45;
    private static final byte[] five = {
            -87, 2, 126, -117, -112, -84, 30, 4, 80, 72, -16, 59,
            -126, -38, 73, 62, -15, 24, -46, 44, 4, 20, 0, -8, 116,
            49, -121, 100, 110, -126, -75, -87, 84, 75, -97, 2, 1,
            0, -124, -64, 8, 0, 0, 18, 22
    };
    private static final byte[] fiveB = {
            1, 84, -111, 111, -11, 100, -128, 8, 56, 36, 40, 71,
            126, 78, -43, -30, 124, 105, 12, 0, -123, -128, 0, -94,
            -23, -14, 67, -104, 77, -51, 77, -84, -106, -87, -127,
            79, -128, 0, 6, 4, 32, 0, 27, 9, 0
    };

    private static final int sixSize = 7;
    private static final byte[] six = { 17, -79, -86, -14, -87, 19, -108 };
    private static final byte[] sixB = { -120, -115, 85, 79, -107, -56, 41 };


    private static final int[] MASK = {0x00000000,
            0x00000001, 0x00000003, 0x00000007, 0x0000000f,
            0x0000001f, 0x0000003f, 0x0000007f, 0x000000ff,
            0x000001ff, 0x000003ff, 0x000007ff, 0x00000fff,
            0x00001fff, 0x00003fff, 0x00007fff, 0x0000ffff,
            0x0001ffff, 0x0003ffff, 0x0007ffff, 0x000fffff,
            0x001fffff, 0x003fffff, 0x007fffff, 0x00ffffff,
            0x01ffffff, 0x03ffffff, 0x07ffffff, 0x0fffffff,
            0x1fffffff, 0x3fffffff, 0x7fffffff, 0xffffffff
    };

    private static int ilog(int v) {
        int ret = 0;
        while (v != 0) {
            ret++;
            v >>= 1;
        }
        return ret;
    }

    private final Buffer o = new Buffer();
    private final Buffer r = new Buffer();

    private void clipTest(int[] b, int vals, int bits, byte[] comp, int compSize) {
        int bytes, i;
        byte[] buffer;

        o.reset();
        for (i = 0; i < vals; i++) o.write(b[i], bits > 0 ? bits : ilog(b[i]));
        buffer = o.buffer();
        bytes = o.bytes();
        assertEquals("wrong number of bytes", compSize, bytes);
        for (i = 0; i < bytes; i++) {
            assertEquals("wrote incorrect value at" + i, comp[i], buffer[i]);
        }

        r.readInit(buffer, bytes);
        for (i = 0; i < vals; i++) {
            int tBit = bits > 0 ? bits : ilog(b[i]);
            assertNotEquals("out of data", -1, r.look(tBit));
            assertEquals("looked at incorrect value", b[i] & MASK[tBit], r.look(tBit));
            if (tBit == 1) assertEquals("looked at single bit incorrect value", b[i] & MASK[tBit], r.lookOne());

            if (tBit == 1) assertEquals("read incorrect single bit value", b[i] & MASK[tBit], r.readOne());
            else assertEquals("read incorrect value", b[i] & MASK[tBit], r.read(tBit));
        }
        assertEquals("leftover bytes after read", r.bytes(), bytes);
    }

    private void copyTest(int prefill, int copy) {
        Buffer srcWrite = new Buffer();
        Buffer dstWrite = new Buffer();
        Buffer srcRead = new Buffer();
        Buffer dstRead = new Buffer();
        byte[] srcBuf, dstBuf;
        int srcBytes, dstBytes;
        int i;

        srcWrite.writeInit();
        dstWrite.writeInit();

        for (i = 0; i < (prefill + copy + 7) / 8; i++) srcWrite.write((i ^ 0x5a) & 0xff, 8);
        srcBuf = srcWrite.buffer();
        srcBytes = srcWrite.bytes();

        // Prefill
        Buffer.writeCopy(dstWrite, srcBuf, prefill);

        // Check buffers; verify end byte masking
        dstBuf = dstWrite.buffer();
        dstBytes = dstWrite.bytes();
        assertEquals("wrong number of bytes after prefill", (prefill + 7) / 8, dstBytes);

        srcRead.readInit(srcBuf, srcBytes);
        dstRead.readInit(dstBuf, dstBytes);

        for (i = 0; i < prefill; i += 8) {
            int s = srcRead.read(Math.min(prefill - i, 8));
            int d = dstRead.read(Math.min(prefill - i, 8));
            assertEquals("prefill=" + i + "mismatch", s, d);
        }
        if (prefill < dstBytes) {
            int r = dstRead.read(dstBytes - prefill);
            assertEquals("prefill=" + prefill + "mismatch, trailing bits not zero", 0, r);
        }

        // Second copy
        Buffer.writeCopy(dstWrite, srcBuf, copy);

        // Check buffers; verify end byte masking
        dstBuf = dstWrite.buffer();
        dstBytes = dstWrite.bytes();
        assertEquals("wrong number of bytes after prefill + copy", (prefill + copy + 7) / 8, dstBytes);

        srcRead.readInit(srcBuf, srcBytes);
        dstRead.readInit(dstBuf, dstBytes);

        for (i = 0; i < prefill; i += 8) {
            int s = srcRead.read(Math.min(prefill - i, 8));
            int d = dstRead.read(Math.min(prefill - i, 8));
            assertEquals("prefill=" + i + "mismatch", s, d);
        }

        srcRead.readInit(srcBuf, srcBytes);
        for (i = 0; i < copy; i += 8) {
            int s = srcRead.read(Math.min(copy - i, 8));
            int d = dstRead.read(Math.min(copy - i, 8));
            assertEquals("prefill=" + prefill + " copy=" + copy + " mismatch! byte " + i / 8, s, d);
        }

        if (copy + prefill < dstBytes) {
            int r = dstRead.read(dstBytes - copy - prefill);
            assertEquals("prefill=" + prefill + " copy=" + copy + " mismatch, trailing bits not zero", 0, r);
        }

        srcWrite.writeClear();
        dstWrite.writeClear();
    }

    @Test
    public void smallPreClippedPacking() {
        o.writeInit();
        System.out.print("Small pre-clipped packing (LSb): ");
        clipTest(testBuffer1, test1Size, 0, one, oneSize);
        System.out.println("ok");
    }

    @Test
    public void largePreClippedPacking() {
        o.writeInit();
        System.out.print("Large pre-clipped packing (LSb): ");
        clipTest(testBuffer2, test2Size, 0, three, threeSize);
        System.out.println("ok");
    }

    @Test
    public void nullBitCall() {
        o.writeInit();
        System.out.print("Null bit call (LSb): ");
        clipTest(testBuffer3, test3Size, 0, two, twoSize);
        System.out.println("ok");
    }
}