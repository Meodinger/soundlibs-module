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
    private static final int[] head1_5 = {
            0x4f, 0x67, 0x67, 0x53, 0x00, 0x02,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0x00, 0x00, 0x00, 0x00,
            0xff, 0x7b, 0x23, 0x17,
            1,
            0
    };
    private static final int[] head2_5 = {
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
    private static final int[] head3_5 = {
            0x4f, 0x67, 0x67, 0x53, 0x00, 0x04,
            0x07, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0x02, 0x00, 0x00, 0x00,
            0x6c, 0x3b, 0x82, 0x3d,
            1,
            50
    };

    // packet that over-spans over an entire page
    private static final int[] head1_6 = {
            0x4f, 0x67, 0x67, 0x53, 0, 0x02,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0, 0, 0, 0,
            0xff, 0x7b, 0x23, 0x17,
            1,
            0
    };
    private static final int[] head2_6 = {
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
    private static final int[] head3_6 = {
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
    private static final int[] head4_6 = {
            0x4f, 0x67, 0x67, 0x53, 0, 0x05,
            0x07, 0x10, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 3, 0, 0, 0,
            0xf7, 0x2f, 0x6c, 0x60,
            5,
            254, 255, 4, 255, 0
    };

    // packet that over-spans over an entire page
    private static final int[] head1_7 = {
            0x4f, 0x67, 0x67, 0x53, 0, 0x02,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x02, 0x03, 0x04, 0, 0, 0, 0,
            0xff, 0x7b, 0x23, 0x17,
            1,
            0
    };
    private static final int[] head2_7 = {
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
    private static final int[] head3_7 = {
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

    private static void checkPacket(Packet packet, int len, int no, long pos) {
        int sequence = 0;
        int lastNo = 0;

        assertEquals("Incorrect packet length", len, packet.bytes);
        assertEquals("Incorrect packet granulePos", pos, packet.granulePos);

        // Packet number just follows sequence/gap, adjust the input number for that
        if (no == 0) {
            //noinspection ConstantConditions
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
            assertEquals("Body data mismatch (1) at pos " + i, (i + no) & 0xff, packet.data[i] & 0xff);
        }
    }

    private static void checkPage(Page page, byte[] bodyBase, int bodyPointer, int[] headerBase, int headerPointer) {
        // Test data
        final int bodyBytes = page.bodyBytes;
        for (int i = 0; i < bodyBytes; i++) {
            assertEquals("Body data mismatch (2) at pos " + i, bodyBase[bodyPointer + i], page.bodyBase[page.bodyPointer + i] & 0xff);
        }

        // Test header
        final int headerBytes = page.headerBytes;
        for (int i = 0; i < headerBytes; i++) {
            assertEquals("Header content mismatch at pos " + i, headerBase[headerPointer + i], page.headerBase[page.headerPointer + i] & 0xff);
        }
        assertEquals("Header length incorrect", headerBase[headerPointer + 26] + 27, page.headerBytes);
    }

    private static void printHeader(Page page) {
        final int headerBytes = page.headerBytes;

        System.out.println("HEADER:");
        System.out.printf("  capture: %c %c %c %c  version: %d  flags: %x%n\n",
                page.headerBase[page.headerPointer],
                page.headerBase[page.headerPointer + 1],
                page.headerBase[page.headerPointer + 2],
                page.headerBase[page.headerPointer + 3],
                page.headerBase[page.headerPointer + 4],
                page.headerBase[page.headerPointer + 5]
        );
        System.out.printf("  granulePos: %d  serialNo: %d  pageNo: %d\n",
                ((long) (page.headerBase[page.headerPointer + 13] << 24)) | (page.headerBase[page.headerPointer + 12] << 16) |
                        (page.headerBase[page.headerPointer + 11] << 8) | (page.headerBase[page.headerPointer + 10]) |
                        (page.headerBase[page.headerPointer + 9] << 24) | (page.headerBase[page.headerPointer + 8] << 16) |
                        (page.headerBase[page.headerPointer + 7] << 8) | (page.headerBase[page.headerPointer + 6]),
                (page.headerBase[page.headerPointer + 17] << 24) | (page.headerBase[page.headerPointer + 16] << 16) |
                        (page.headerBase[page.headerPointer + 15] << 8) | (page.headerBase[page.headerPointer + 14]),
                (page.headerBase[page.headerPointer + 21] << 24) | (page.headerBase[page.headerPointer + 20] << 16) |
                        (page.headerBase[page.headerPointer + 19] << 8) | (page.headerBase[page.headerPointer + 18])
        );
        System.out.printf("  checksum: %02x:%02x:%02x:%02x\n  segments: %d (",
                page.headerBase[page.headerPointer + 22], page.headerBase[page.headerPointer + 23],
                page.headerBase[page.headerPointer + 24], page.headerBase[page.headerPointer + 25],
                page.headerBase[page.headerPointer + 26]
        );
        for (int i = 27; i < headerBytes; i++) System.out.printf("%d ", page.headerBase[page.headerPointer + i]);
        System.out.println(")");
    }

    private static void copyPage(Page page) {
        byte[] temp;

        temp = new byte[page.headerBytes];
        System.arraycopy(page.headerBase, page.headerBytes, temp, 0, page.headerBytes);
        page.headerBase = temp;
        page.headerPointer = 0;

        temp = new byte[page.bodyBytes];
        System.arraycopy(page.bodyBase, page.bodyBytes, temp, 0, page.bodyBytes);
        page.bodyBase = temp;
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
        int packets = 0;
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
            int len = pl[i];

            packet.data = data;
            packet.pointer = inputPointer;
            packet.bytes = len;
            packet.eos = pl[i + 1] < 0 ? 1 : 0;
            packet.granulePos = granulePos;

            granulePos += 1024;

            for (int j = 0; j < len; j++) data[inputPointer++] = (byte) (i + j);

            // Submit the test packet
            encode.packetIn(packet);

            // Retrieve any finished pages
            Page page = new Page();
            while (encode.pageOut(page) != 0) {
                // We have a page. Check it carefully
                System.out.printf("%d, ", pageNo);
                assertNotEquals("Coded too many pages!", null, headers[pageNo]);
                checkPage(page, data, outputPointer, headers[pageNo], 0);

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

                sync.buffer(page.headerBytes + page.bodyBytes);
                byte[] buffer = sync.data;
                int bufferPointer = 0;
                int nextPointer = 0;
                byteSkipCount += page.headerBytes;
                if (byteSkipCount > byteSkip) {
                    System.arraycopy(page.headerBase, page.headerPointer, buffer, 0, byteSkipCount - byteSkip);
                    nextPointer += byteSkipCount - byteSkip;
                }

                byteSkipCount += page.bodyBytes;
                if (byteSkipCount > byteSkip) {
                    System.arraycopy(page.bodyBase, page.bodyPointer, buffer, nextPointer, byteSkipCount - byteSkip);
                    nextPointer += byteSkipCount - byteSkip;
                    byteSkipCount = byteSkip;
                }

                sync.wrote(nextPointer - bufferPointer);

                while (true) {
                    int ret = sync.pageOut(pageDecode);
                    if (ret == 0) break;
                    if (ret < 0) continue;

                    // Got a page. Verify that it's good
                    System.out.printf("(%d) ", pageOut);

                    checkPage(pageDecode, data, decodePointer, headers[pageOut], 0);
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
                            assertEquals("packet data mismatch in decode! pos=" + decodePacket, data[decodePacket + i], packetDecode1.data[i]);
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

    @Test
    public void main() {
        encode.init(0x04030201);
        decode.init(0x04030201);
        sync.init();


        // Exercise each code path in the framing code.
        // Also verify that the checksums are working.
        {
            // 17 only
            int[] packets = {17, -1};
            int[][] headers = {header1_0, null};

            System.out.print("Testing single page encoding... ");
            testPack(packets, headers, 0, 0, 0);
        }

        {
            // 17, 254, 255, 256, 500, 510, 600 byte, pad
            int[] packets = {17, 254, 255, 256, 500, 510, 600, -1};
            int[][] headers = {header1_1, header2_1, null};

            System.out.print("Testing basic page encoding... ");
            testPack(packets, headers, 0, 0, 0);
        }

        {
            // nil packets; beginning, middle, end
            int[] packets = {0, 17, 254, 255, 0, 256, 0, 500, 510, 600, 0, -1};
            int[][] headers = {header1_2, header2_2, null};

            System.out.print("Testing basic nil packets... ");
            testPack(packets, headers, 0, 0, 0);
        }

        {
            // large initial packet
            int[] packets = {4345, 259, 255, -1};
            int[][] headers = {header1_3, header2_3, null};

            System.out.print("Testing initial-packet lacing > 4k... ");
            testPack(packets, headers, 0, 0, 0);
        }

        {
            // continuing packet test; with page spill expansion, we have to overflow the lacing table.
            int[] packets = {0, 65500, 259, 255, -1};
            int[][] headers = {header1_4, header2_4, header3_4, null};

            System.out.print("Testing single packet page span... ");
            testPack(packets, headers, 0, 0, 0);
        }

        {
            // spill expand packet test
            int[] packets = {0, 4345, 259, 255, 0, 0, -1};
            int[][] headers = {header1_4b, header2_4b, header3_4b, null};

            System.out.print("Testing page spill expansion... ");
            testPack(packets, headers, 0, 0, 0);
        }

    }


}
