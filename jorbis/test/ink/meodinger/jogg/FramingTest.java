package ink.meodinger.jogg;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Author: Meodinger
 * Date: 2021/11/9
 * Have fun with my code!
 */

public class FramingTest {

    // 17 only
    private static final int[] header1_0 = {
            0x4f, 0x67, 0x67, 0x53, 0x00, 0x06,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0x00, 0x00, 0x00, 0x00,
            0x15, 0xed, 0xec, 0x91,
            1,
            17
    };

    // 17, 254, 255, 256, 500, 510, 600 byte, pad
    private static final int[] header1_1 = {
            0x4f, 0x67, 0x67, 0x53, 0x00, 0x02,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0x00, 0x00, 0x00, 0x00,
            0x59, 0x10, 0x6c, 0x2c,
            1,
            17
    };
    private static final int[] header2_1 = {
            0x4f, 0x67, 0x67, 0x53, 0x00, 0x04,
            0x07, 0x18, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0x01, 0x00, 0x00, 0x00,
            0x89, 0x33, 0x85, 0xce,
            13,
            254, 255, 0, 255, 1, 255, 245, 255,
            255, 0, 255, 255, 90
    };

    // nil packets; beginning, middle, end
    private static final int[] header1_2 = {
            0x4f, 0x67, 0x67, 0x53, 0x00, 0x02,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0x00, 0x00, 0x00, 0x00,
            0xff, 0x7b, 0x23, 0x17,
            1,
            0
    };
    private static final int[] header2_2 = {
            0x4f, 0x67, 0x67, 0x53, 0x00, 0x04,
            0x07, 0x28, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0x01, 0x00, 0x00, 0x00,
            0x5c, 0x3f, 0x66, 0xcb,
            17,
            17, 254, 255, 0, 0, 255, 1, 0,
            255, 245, 255, 255, 0, 255, 255, 90,
            0
    };

    // large initial packet
    private static final int[] header1_3 = {
            0x4f, 0x67, 0x67, 0x53, 0x00, 0x02,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x27, 0x31, 0xaa,
            18,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 10
    };
    private static final int[] header2_3 = {
            0x4f, 0x67, 0x67, 0x53, 0x00, 0x04,
            0x07, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0x01, 0x00, 0x00, 0x00,
            0x7f, 0x4e, 0x8a, 0xd2,
            4,
            255, 4, 255, 0
    };

    // continuing packet test
    private static final int[] header1_4 = {
            0x4f, 0x67, 0x67, 0x53, 0x00, 0x02,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0x00, 0x00, 0x00, 0x00,
            0xff, 0x7b, 0x23, 0x17,
            1,
            0
    };
    private static final int[] header2_4 = {
            0x4f, 0x67, 0x67, 0x53, 0x00, 0x00,
            0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF,
            0x01, 0x02, 0x03, 0x04, 0x01, 0x00, 0x00, 0x00,
            0xf8, 0x3c, 0x19, 0x79,
            255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255
    };
    private static final int[] header3_4 = {
            0x4f, 0x67, 0x67, 0x53, 0x00, 0x05,
            0x07, 0x0c, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0x02, 0x00, 0x00, 0x00,
            0x38, 0xe6, 0xb6, 0x28,
            6,
            255, 220, 255, 4, 255, 0
    };

    // spill expansion test
    private static final int[] header1_4b = {
            0x4f, 0x67, 0x67, 0x53, 0x00, 0x02,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0x00, 0x00, 0x00, 0x00,
            0xff, 0x7b, 0x23, 0x17,
            1,
            0
    };
    private static final int[] header2_4b = {
            0x4f, 0x67, 0x67, 0x53, 0x00, 0x00,
            0x07, 0x10, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0x01, 0x00, 0x00, 0x00,
            0xce, 0x8f, 0x17, 0x1a,
            23,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 10, 255, 4, 255, 0, 0
    };
    private static final int[] header3_4b = {
            0x4f, 0x67, 0x67, 0x53, 0x00, 0x04,
            0x07, 0x14, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0x02, 0x00, 0x00, 0x00,
            0x9b, 0xb2, 0x50, 0xa1,
            1,
            0
    };

    // page with the 255 segment limit
    private static final int[] header1_5 = {
            0x4f, 0x67, 0x67, 0x53, 0x00, 0x02,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0x00, 0x00, 0x00, 0x00,
            0xff, 0x7b, 0x23, 0x17,
            1,
            0
    };
    private static final int[] header2_5 = {
            0x4f, 0x67, 0x67, 0x53, 0x00, 0x00,
            0x07, 0xfc, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0x01, 0x00, 0x00, 0x00,
            0xed, 0x2a, 0x2e, 0xa7,
            255,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10, 10,
            10, 10, 10, 10, 10, 10, 10
    };
    private static final int[] header3_5 = {
            0x4f, 0x67, 0x67, 0x53, 0x00, 0x04,
            0x07, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0x02, 0x00, 0x00, 0x00,
            0x6c, 0x3b, 0x82, 0x3d,
            1,
            50
    };

    // packet that over-spans over an entire page
    private static final int[] header1_6 = {
            0x4f, 0x67, 0x67, 0x53, 0, 0x02,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0, 0, 0, 0,
            0xff, 0x7b, 0x23, 0x17,
            1,
            0
    };
    private static final int[] header2_6 = {
            0x4f, 0x67, 0x67, 0x53, 0, 0x00,
            0x07, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 1, 0, 0, 0,
            0x68, 0x22, 0x7c, 0x3d,
            255,
            100,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255
    };
    private static final int[] header3_6 = {
            0x4f, 0x67, 0x67, 0x53, 0, 0x01,
            0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF,
            0x01, 0x02, 0x03, 0x04, 2, 0, 0, 0,
            0xf4, 0x87, 0xba, 0xf3,
            255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255
    };
    private static final int[] header4_6 = {
            0x4f, 0x67, 0x67, 0x53, 0, 0x05,
            0x07, 0x10, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 3, 0, 0, 0,
            0xf7, 0x2f, 0x6c, 0x60,
            5,
            254, 255, 4, 255, 0
    };

    // packet that over-spans over an entire page
    private static final int[] header1_7 = {
            0x4f, 0x67, 0x67, 0x53, 0, 0x02,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0, 0, 0, 0,
            0xff, 0x7b, 0x23, 0x17,
            1,
            0
    };
    private static final int[] header2_7 = {
            0x4f, 0x67, 0x67, 0x53, 0, 0x00,
            0x07, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 1, 0, 0, 0,
            0x68, 0x22, 0x7c, 0x3d,
            255,
            100,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255, 255, 255,
            255, 255, 255, 255, 255, 255
    };
    private static final int[] header3_7 = {
            0x4f, 0x67, 0x67, 0x53, 0, 0x05,
            0x07, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 2, 0, 0, 0,
            0xd4, 0xe0, 0x60, 0xe5,
            1,
            0
    };

    // -----  ----- //

    private final Stream encode = new Stream();
    private final Stream decode = new Stream();
    private final Sync sync = new Sync();

    private static int comparePacket(Packet packet1, Packet packet2) {
        if (packet1.data != packet2.data) {
            System.out.print("data!\n");
            return -1;
        }
        if (packet1.pointer != packet2.pointer) {
            System.out.printf("pointer! %d != %d\n", packet1.pointer, packet2.pointer);
            return -1;
        }
        if (packet1.bytes != packet2.bytes) {
            System.out.printf("bytes! %d != %d\n", packet1.bytes, packet2.bytes);
            return -1;
        }
        if (packet1.bos != packet2.bos) {
            System.out.printf("bos! %d != %d\n", packet1.bos, packet2.bos);
            return -1;
        }
        if (packet1.eos != packet2.eos) {
            System.out.printf("eos! %d != %d\n", packet1.eos, packet2.eos);
            return -1;
        }
        if (packet1.granulePos != packet2.granulePos) {
            System.out.printf("granulePos! %d != %d\n", packet1.granulePos, packet2.granulePos);
            return -1;
        }
        if (packet1.packetNo != packet2.packetNo) {
            System.out.printf("packetNo! %d != %d\n", packet1.packetNo, packet2.packetNo);
            return -1;
        }
        return 0;
    }

    private static int sequence = 0;
    private static int lastNo = 0;
    private static void checkPacket(Packet packet, int len, int no, long pos) {
        assertEquals("Incorrect packet length", len, packet.bytes);
        assertEquals("Incorrect packet granulePos", pos, packet.granulePos);

        // Packet number just follows sequence/gap, adjust the input number for that
        if (no == 0) {
            sequence = 0;
        } else {
            sequence++;
            if (no > lastNo + 1) sequence++;
        }
        lastNo = no;
        assertEquals("Incorrect packet sequence", sequence, packet.packetNo);

        // Test data
        final int packetBytes = packet.bytes;
        for (int i = 0; i < packetBytes; i++) {
            assertEquals("Body data mismatch (1) at pos " + i, (i + no) & 0xff, packet.data[packet.pointer + i] & 0xff);
        }
    }

    private static void checkPage(Page page, byte[] bodyBase, int bodyPointer, int[] header) {
        // Test data
        final int bodyBytes = page.bodyBytes;
        for (int i = 0; i < bodyBytes; i++) {
            assertEquals("Body data mismatch (2) at pos " + i, bodyBase[bodyPointer + i] & 0xff, page.bodyBase[page.bodyPointer + i] & 0xff);
        }

        // Test header
        final int headerBytes = page.headerBytes;
        for (int i = 0; i < headerBytes; i++) {
            assertEquals("Header content mismatch at pos " + i, header[i] & 0xff, page.headerBase[page.headerPointer + i] & 0xff);
        }
        assertEquals("Header length incorrect", header[26] + 27, page.headerBytes);
    }

    private static void copyPage(Page page) {
        byte[] temp1 = new byte[page.headerBytes];
        System.arraycopy(page.headerBase, page.headerPointer, temp1, 0, page.headerBytes);
        page.headerBase = temp1;
        page.headerPointer = 0;

        byte[] temp2 = new byte[page.bodyBytes];
        System.arraycopy(page.bodyBase, page.bodyPointer, temp2, 0, page.bodyBytes);
        page.bodyBase = temp2;
        page.bodyPointer = 0;
    }

    private void testPack(int[] pl, int[][] headers, int byteSkip, int pageSkip, int packetSkip) {
        byte[] data = new byte[1024 * 1024];
        int inputPointer = 0;
        int outputPointer = 0;
        int decodePointer = 0;
        int decodePacket = 0;
        int granulePos = 7;
        int pageNo = 0;
        int packets;
        int pageOut = pageSkip;
        int bosFlag = 0;
        int esoFlag = 0;

        int byteSkipCount = 0;

        encode.reset();
        decode.reset();
        sync.reset();

        for (packets = 0; packets < packetSkip; packets++) decodePacket += pl[packets];
        for (packets = 0; ; packets++) if (pl[packets] == -1) break;

        for (int i = 0; i < packets; i++) {
            // Construct a test packet
            Packet packet = new Packet();
            final int len = pl[i];

            packet.data = data;
            packet.pointer = inputPointer;
            packet.bytes = len;
            packet.eos = pl[i + 1] < 0 ? 1 : 0;
            packet.granulePos = granulePos;

            granulePos += 1024;

            for (int j = 0; j < len; j++) data[inputPointer++] = (byte) ((i + j) & 0xff);

            // Submit the test packet
            encode.packetIn(packet);

            // Retrieve any finished pages
            Page page = new Page();
            while (encode.pageOut(page) != 0) {
                // We have a page. Check it carefully
                System.out.printf("%d, ", pageNo);
                assertNotEquals("Coded too many pages!", null, headers[pageNo]);
                checkPage(page, data, outputPointer, headers[pageNo]);

                outputPointer += page.bodyBytes;
                pageNo++;
                if (pageSkip != 0) {
                    bosFlag = 1;
                    pageSkip--;
                    decodePointer += page.bodyBytes;
                }

                // Have a complete page; submit it to sync/decode
                Page pageDecode = new Page();
                Packet packetDecode1 = new Packet();
                Packet packetDecode2 = new Packet();

                int bufferPointer = sync.buffer(page.headerBytes + page.bodyBytes);
                int nextPointer = bufferPointer;

                byteSkipCount += page.headerBytes;
                if (byteSkipCount > byteSkip) {
                    System.arraycopy(page.headerBase, page.headerPointer, sync.data, nextPointer, byteSkipCount - byteSkip);
                    nextPointer += (byteSkipCount - byteSkip);
                    byteSkipCount = byteSkip;
                }

                byteSkipCount += page.bodyBytes;
                if (byteSkipCount > byteSkip) {
                    System.arraycopy(page.bodyBase, page.bodyPointer, sync.data, nextPointer, byteSkipCount - byteSkip);
                    nextPointer += (byteSkipCount - byteSkip);
                    byteSkipCount = byteSkip;
                }

                sync.wrote(nextPointer - bufferPointer);

                while (true) {
                    int ret = sync.pageOut(pageDecode);
                    if (ret == 0) break;
                    if (ret < 0) continue;

                    // Got a page. Verify that it's good
                    System.out.printf("(%d) ", pageOut);

                    checkPage(pageDecode, data, decodePointer, headers[pageOut]);
                    decodePointer += pageDecode.bodyBytes;
                    pageOut++;

                    // Submit it to de-constitution
                    decode.pageIn(pageDecode);

                    // Packets out?
                    while (decode.packetPeek(packetDecode2) > 0) {
                        decode.packetPeek(null);
                        decode.packetOut(packetDecode1); // Just catching them all

                        // Verify peek and out match
                        assertEquals("packet out != peek, pos=" + decodePacket, 0, comparePacket(packetDecode1, packetDecode2));

                        // Verify the packet
                        // Check data
                        for (int k = 0; k < packetDecode1.bytes; k++) {
                            assertEquals("packet data mismatch in decode! pos=" + decodePacket, data[decodePacket + k], packetDecode1.data[packetDecode1.pointer + k]);
                        }
                        // Check bos flag
                        if (bosFlag == 0) {
                            assertNotEquals("b_o_s flag not set on packet!", 0, packetDecode1.bos);
                        } else {
                            assertEquals("b_o_s flag incorrectly set on packet", 0, packetDecode1.bos);
                        }
                        bosFlag = 1;
                        decodePacket += packetDecode1.bytes;
                        // Check eos flag
                        assertEquals("Multiple decoded packets with eos flag!", 0, esoFlag);
                        if (packetDecode1.eos != 0) esoFlag = 1;
                        // Check granulePos flag
                        if (packetDecode1.granulePos != -1) System.out.printf(" granule:%d ", packetDecode1.granulePos);
                    }
                }
            }
        }
        assertNull("did not write last page!", headers[pageNo]);
        assertNull("did not decode last page!", headers[pageOut]);
        assertEquals("encoded page data incomplete!", inputPointer, outputPointer);
        assertEquals("decoded page data incomplete!", inputPointer, decodePointer);
        assertEquals("decoded packet data incomplete!", inputPointer, decodePacket);
        assertNotEquals("Never got a packet with EOS set!", 0, esoFlag);
        System.out.println("ok");
    }

    // Exercise each code path in the framing code.
    // Also verify that the checksums are working.
    public void initPage() {
        encode.init(0x04030201);
        decode.init(0x04030201);
        sync.init();
    }

    @Test
    public void singlePageEncoding() {
        initPage();

        // 17 only
        int[] packets = {17, -1};
        int[][] headers = {header1_0, null};

        System.out.print("Testing single page encoding... ");
        testPack(packets, headers, 0, 0, 0);
    }

    @Test
    public void basicPageEncoding() {
        initPage();

        // 17, 254, 255, 256, 500, 510, 600 byte, pad
        int[] packets = {17, 254, 255, 256, 500, 510, 600, -1};
        int[][] headers = {header1_1, header2_1, null};

        System.out.print("Testing basic page encoding... ");
        testPack(packets, headers, 0, 0, 0);
    }

    @Test
    public void basicNilPackets() {
        initPage();

        // nil packets; beginning, middle, end
        int[] packets = {0, 17, 254, 255, 0, 256, 0, 500, 510, 600, 0, -1};
        int[][] headers = {header1_2, header2_2, null};

        System.out.print("Testing basic nil packets... ");
        testPack(packets, headers, 0, 0, 0);
    }

    @Test
    public void largeInitialPacket() {
        initPage();

        // large initial packet
        int[] packets = {4345, 259, 255, -1};
        int[][] headers = {header1_3, header2_3, null};

        System.out.print("Testing initial-packet lacing > 4k... ");
        testPack(packets, headers, 0, 0, 0);
    }

    @Test
    public void singlePacketPageSpan() {
        initPage();

        // continuing packet test; with page spill expansion, we have to overflow the lacing table.
        int[] packets = {0, 65500, 259, 255, -1};
        int[][] headers = {header1_4, header2_4, header3_4, null};

        System.out.print("Testing single packet page span... ");
        testPack(packets, headers, 0, 0, 0);
    }

    @Test
    public void pageSpillExpansion() {
        initPage();

        // spill expand packet test
        int[] packets = {0, 4345, 259, 255, 0, 0, -1};
        int[][] headers = {header1_4b, header2_4b, header3_4b, null};

        System.out.print("Testing page spill expansion... ");
        testPack(packets, headers, 0, 0, 0);
    }

    @Test
    public void maxPacketSegments() {
        initPage();

        // page with the 255 segment limit
        int[] packets = {0,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 50, -1
        };
        int[][] headers = {header1_5, header2_5, header3_5, null};

        System.out.print("Testing max packet segments... ");
        testPack(packets, headers, 0, 0, 0);
    }

    @Test
    public void veryLargePackets() {
        initPage();

        // packet that over-spans over an entire page
        int[] packets = {0, 100, 130049, 259, 255, -1};
        int[][] headers = {header1_6, header2_6, header3_6, header4_6, null};

        System.out.print("Testing very large packets... ");
        testPack(packets, headers, 0, 0, 0);
    }

    @Test
    public void veryLargePacketsRsync() {
        initPage();

        // test for the libogg 1.1.1 re-sync in large continuation bug
        // found by Josh Coalson
        int[] packets = {0, 100, 130049, 259, 255, -1};
        int[][] headers = {header1_6, header2_6, header3_6, header4_6, null};

        System.out.print("Testing continuation re-sync in very large packets... ");
        testPack(packets, headers, 100, 2, 3);
    }

    @Test
    public void zeroDataPage() {
        initPage();

        // term only page.  why not?
        int[] packets = {0, 100, 64770, -1};
        int[][] headers = {header1_7, header2_7, header3_7, null};

        System.out.print("Testing zero data page (1 nil packet)... ");
        testPack(packets, headers, 0, 0, 0);
    }


    // Pages test
    private final Page[] pages = new Page[5];
    private void initPages() {
        byte[] data = new byte[1024 * 1024];
        int[] pl = {0, 1, 1, 98, 4079, 1, 1, 2954, 2057, 76, 34, 912, 0, 234, 1000, 1000, 1000, 300, -1};
        int inputPointer = 0;
        for (int i = 0; i < 5; i++) pages[i] = new Page();

        encode.reset();

        for (int i = 0; pl[i] != -1; i++) {
            Packet packet = new Packet();
            int len = pl[i];

            packet.data = data;
            packet.pointer = inputPointer;
            packet.bytes = len;
            packet.eos = pl[i + 1] < 0 ? 1 : 0;
            packet.granulePos = (i + 1) * 1000;

            for (int j = 0; j < len; j++) data[inputPointer++] = (byte) (i + j);
            encode.packetIn(packet);
        }

        // retrieve finished pages
        for (int i = 0; i < 5; i++) {
            assertNotEquals("Too few pages output building sync tests!", 0, encode.pageOut(pages[i]));
            copyPage(pages[i]);
        }
    }

    @Test
    public void lossOfPages() {
        initPages();

        // Test lost pages on pageIn/packetOut: no rollback
        Page temp = new Page();
        Packet test = new Packet();

        System.out.print("Testing loss of pages... ");

        sync.reset();
        decode.reset();
        int pointer;
        for (int i = 0; i < 5; i++) {
            pointer = sync.buffer(pages[i].headerBytes);
            System.arraycopy(pages[i].headerBase, pages[i].headerPointer, sync.data, pointer, pages[i].headerBytes);
            sync.wrote(pages[i].headerBytes);

            pointer = sync.buffer(pages[i].bodyBytes);
            System.arraycopy(pages[i].bodyBase, pages[i].bodyPointer, sync.data, pointer, pages[i].bodyBytes);
            sync.wrote(pages[i].bodyBytes);
        }

        sync.pageOut(temp);
        decode.pageIn(temp);
        sync.pageOut(temp);
        decode.pageIn(temp);
        sync.pageOut(temp);
        // skip
        sync.pageOut(temp);
        decode.pageIn(temp);

        // do we get the expected results/packets?
        assertEquals(1, decode.packetOut(test));
        checkPacket(test, 0, 0, 0);
        assertEquals(1, decode.packetOut(test));
        checkPacket(test, 1, 1, -1);
        assertEquals(1, decode.packetOut(test));
        checkPacket(test, 1, 2, -1);
        assertEquals(1, decode.packetOut(test));
        checkPacket(test, 98, 3, -1);
        assertEquals(1, decode.packetOut(test));
        checkPacket(test, 4079, 4, 5000);
        assertEquals("loss of page did not return error", -1, decode.packetOut(test));

        assertEquals(1, decode.packetOut(test));
        checkPacket(test, 76, 9, -1);
        assertEquals(1, decode.packetOut(test));
        checkPacket(test, 34, 10, -1);

        System.out.println("ok");
    }

    @Test
    public void lossOfPagesRollback() {
        initPages();

        // Test lost pages on pageIn/packetOut: rollback with continuation
        Page temp = new Page();
        Packet test = new Packet();

        System.out.print("Testing loss of pages (rollback required)... ");

        sync.reset();
        decode.reset();

        int pointer;
        for (int i = 0; i < 5; i++) {
            pointer = sync.buffer(pages[i].headerBytes);
            System.arraycopy(pages[i].headerBase, pages[i].headerPointer, sync.data, pointer, pages[i].headerBytes);
            sync.wrote(pages[i].headerBytes);

            pointer = sync.buffer(pages[i].bodyBytes);
            System.arraycopy(pages[i].bodyBase, pages[i].bodyPointer, sync.data, pointer, pages[i].bodyBytes);
            sync.wrote(pages[i].bodyBytes);
        }

        sync.pageOut(temp);
        decode.pageIn(temp);
        sync.pageOut(temp);
        decode.pageIn(temp);
        sync.pageOut(temp);
        decode.pageIn(temp);
        sync.pageOut(temp);
        // skip
        sync.pageOut(temp);
        decode.pageIn(temp);

        // do we get the expected results/packets?
        assertEquals(1, decode.packetOut(test));
        checkPacket(test, 0, 0, 0);
        assertEquals(1, decode.packetOut(test));
        checkPacket(test, 1, 1, -1);
        assertEquals(1, decode.packetOut(test));
        checkPacket(test, 1, 2, -1);
        assertEquals(1, decode.packetOut(test));
        checkPacket(test, 98, 3, -1);
        assertEquals(1, decode.packetOut(test));
        checkPacket(test, 4079, 4, 5000);
        assertEquals(1, decode.packetOut(test));
        checkPacket(test, 1, 5, -1);
        assertEquals(1, decode.packetOut(test));
        checkPacket(test, 1, 6, -1);
        assertEquals(1, decode.packetOut(test));
        checkPacket(test, 2954, 7, -1);
        assertEquals(1, decode.packetOut(test));
        checkPacket(test, 2057, 8, 9000);

        assertEquals("loss of page did not return error", -1, decode.packetOut(test));

        assertEquals(1, decode.packetOut(test));
        checkPacket(test, 300, 17, 18000);

        System.out.println("ok");
    }


    // Sync test
    private void initSync() {
        initPages();
    }

    @Test
    public void syncOnPartialInputs() {
        initSync();

        System.out.print("Testing sync on partial inputs... ");
        Page decodePage = new Page();
        int pointer;

        sync.reset();

        // Test fractional page inputs: incomplete capture
        pointer = sync.buffer(pages[1].headerBytes);
        System.arraycopy(pages[1].headerBase, pages[1].headerPointer, sync.data, pointer, 3);
        sync.wrote(3);
        assertFalse(sync.pageOut(decodePage) > 0);

        // Test fractional page inputs: incomplete fixed header
        pointer = sync.buffer(pages[1].headerBytes);
        System.arraycopy(pages[1].headerBase, pages[1].headerPointer + 3, sync.data, pointer, 20);
        sync.wrote(20);
        assertFalse(sync.pageOut(decodePage) > 0);

        // Test fractional page inputs: incomplete header
        pointer = sync.buffer(pages[1].headerBytes);
        System.arraycopy(pages[1].headerBase, pages[1].headerPointer + 23, sync.data, pointer, 5);
        sync.wrote(5);
        assertFalse(sync.pageOut(decodePage) > 0);

        // Test fractional page inputs: incomplete body
        pointer = sync.buffer(pages[1].headerBytes);
        System.arraycopy(pages[1].headerBase, pages[1].headerPointer + 28, sync.data, pointer, pages[1].headerBytes - 28);
        sync.wrote(pages[1].headerBytes - 28);
        assertFalse(sync.pageOut(decodePage) > 0);

        pointer = sync.buffer(pages[1].bodyBytes);
        System.arraycopy(pages[1].bodyBase, pages[1].bodyPointer, sync.data, pointer, 1000);
        sync.wrote(1000);
        assertFalse(sync.pageOut(decodePage) > 0);

        pointer = sync.buffer(pages[1].bodyBytes);
        System.arraycopy(pages[1].bodyBase, pages[1].bodyPointer + 1000, sync.data, pointer, pages[1].bodyBytes - 1000);
        sync.wrote(pages[1].bodyBytes - 1000);
        assertFalse(sync.pageOut(decodePage) <= 0);

        System.out.println("ok");
    }

    @Test
    public void syncOn1PlusPartialInputs() {
        initSync();

        System.out.print("Testing sync on 1+partial inputs... ");
        Page decodePage = new Page();
        int pointer;

        sync.reset();

        pointer = sync.buffer(pages[1].headerBytes);
        System.arraycopy(pages[1].headerBase, pages[1].headerPointer, sync.data, pointer, pages[1].headerBytes);
        sync.wrote(pages[1].headerBytes);

        pointer = sync.buffer(pages[1].bodyBytes);
        System.arraycopy(pages[1].bodyBase, pages[1].bodyPointer, sync.data, pointer, pages[1].bodyBytes);
        sync.wrote(pages[1].bodyBytes);

        pointer = sync.buffer(pages[1].headerBytes);
        System.arraycopy(pages[1].headerBase, pages[1].headerPointer, sync.data, pointer, 20);
        sync.wrote(20);
        assertFalse(sync.pageOut(decodePage) <= 0);
        assertFalse(sync.pageOut(decodePage) > 0);

        pointer = sync.buffer(pages[1].headerBytes);
        System.arraycopy(pages[1].headerBase, pages[1].headerPointer + 20, sync.data, pointer, pages[1].headerBytes - 20);
        sync.wrote(pages[1].headerBytes - 20);

        pointer = sync.buffer(pages[1].bodyBytes);
        System.arraycopy(pages[1].bodyBase, pages[1].bodyPointer, sync.data, pointer, pages[1].bodyBytes);
        sync.wrote(pages[1].bodyBytes);
        assertFalse(sync.pageOut(decodePage) <= 0);

        System.out.println("ok");
    }

    @Test
    public void syncRecapture() {
        initSync();

        System.out.print("Testing search for capture... ");
        Page decodePage = new Page();
        int pointer;

        sync.reset();

        // garbage
        pointer = sync.buffer(pages[1].bodyBytes);
        System.arraycopy(pages[1].bodyBase, pages[1].bodyPointer, sync.data, pointer, pages[1].bodyBytes);
        sync.wrote(pages[1].bodyBytes);

        pointer = sync.buffer(pages[1].headerBytes);
        System.arraycopy(pages[1].headerBase, pages[1].headerPointer, sync.data, pointer, pages[1].headerBytes);
        sync.wrote(pages[1].headerBytes);

        pointer = sync.buffer(pages[1].bodyBytes);
        System.arraycopy(pages[1].bodyBase, pages[1].bodyPointer, sync.data, pointer, pages[1].bodyBytes);
        sync.wrote(pages[1].bodyBytes);

        pointer = sync.buffer(pages[2].headerBytes);
        System.arraycopy(pages[2].headerBase, pages[2].headerPointer, sync.data, pointer, 20);
        sync.wrote(20);
        assertFalse(sync.pageOut(decodePage) > 0);
        assertFalse(sync.pageOut(decodePage) <= 0);
        assertFalse(sync.pageOut(decodePage) > 0);

        pointer = sync.buffer(pages[2].headerBytes);
        System.arraycopy(pages[2].headerBase, pages[2].headerPointer + 20, sync.data, pointer, pages[2].headerBytes - 20);
        sync.wrote(pages[2].headerBytes - 20);

        pointer = sync.buffer(pages[2].bodyBytes);
        System.arraycopy(pages[2].bodyBase, pages[2].bodyPointer, sync.data, pointer, pages[2].bodyBytes);
        sync.wrote(pages[2].bodyBytes);
        assertFalse(sync.pageOut(decodePage) <= 0);

        System.out.println("ok");
    }

    @Test
    public void syncRecapture2() {
        initSync();

        System.out.print("Testing capture... ");
        Page decodePage = new Page();
        int pointer;

        sync.reset();

        // page
        pointer = sync.buffer(pages[1].headerBytes);
        System.arraycopy(pages[1].headerBase, pages[1].headerPointer, sync.data, pointer, pages[1].headerBytes);
        sync.wrote(pages[1].headerBytes);

        pointer = sync.buffer(pages[1].bodyBytes);
        System.arraycopy(pages[1].bodyBase, pages[1].bodyPointer, sync.data, pointer, pages[1].bodyBytes);
        sync.wrote(pages[1].bodyBytes);

        // Garbage
        pointer = sync.buffer(pages[2].headerBytes);
        System.arraycopy(pages[2].headerBase, pages[2].headerPointer, sync.data, pointer, pages[2].headerBytes);
        sync.wrote(pages[2].headerBytes);

        pointer = sync.buffer(pages[2].headerBytes);
        System.arraycopy(pages[2].headerBase, pages[2].headerPointer, sync.data, pointer, pages[2].headerBytes);
        sync.wrote(pages[2].headerBytes);

        assertFalse(sync.pageOut(decodePage) <= 0);

        pointer = sync.buffer(pages[2].bodyBytes);
        System.arraycopy(pages[2].bodyBase, pages[2].bodyPointer, sync.data, pointer, pages[2].bodyBytes - 5);
        sync.wrote(pages[2].bodyBytes - 5);

        // page
        pointer = sync.buffer(pages[3].headerBytes);
        System.arraycopy(pages[3].headerBase, pages[3].headerPointer, sync.data, pointer, pages[3].headerBytes);
        sync.wrote(pages[3].headerBytes);

        pointer = sync.buffer(pages[3].bodyBytes);
        System.arraycopy(pages[3].bodyBase, pages[3].bodyPointer, sync.data, pointer, pages[3].bodyBytes);
        sync.wrote(pages[3].bodyBytes);

        assertFalse(sync.pageOut(decodePage) > 0);
        assertFalse(sync.pageOut(decodePage) <= 0);

        System.out.println("ok");
    }

}
