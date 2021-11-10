package ink.meodinger.jogg;

import static ink.meodinger.jogg._CRC_.updateCRC;

/**
 * Author: Meodinger
 * Date: 2021/10/27
 * Have fun with my code!
 */

public class Page {

    // ----- Static Fields & Methods ----- //

    private static final int[] CRC_LOOKUP = new int[256];
    private static int crcEntry(int index) {
        int r = index << 24;
        for (int i = 0; i < 8; i++) {
            if ((r & 0x8000_0000) != 0) {
                // CRC-32-IEEE 802.3
                r = (r << 1) ^ 0x04c1_1db7;
                /*
                   The same as the ethernet generator polynomial,
                   although we use an unreflected alg and an
                   init/final of 0, not 0xffffffff.
               	 */
            } else {
                r <<= 1;
            }
        }
        // r & 0xffff_ffff;
        return r;
    }
    static {
        for (int i = 0; i < CRC_LOOKUP.length; i++) {
            CRC_LOOKUP[i] = crcEntry(i);
        }
    }

    // ----- Instance Fields & Methods ----- //

    public byte[] headerBase = null;
    public int headerPointer = 0;
    public int headerBytes = 0;

    public byte[] bodyBase = null;
    public int bodyPointer = 0;
    public int bodyBytes = 0;

    /**
     * The version number
     * In the current version of Ogg, the version number is always 0.
     * @return 0; other numbers indicate an error in page encoding
     */
    public int version() {
        return (headerBase[headerPointer + 4] & 0xff);
    }

    /**
     * Indicate whether this page contains packet data
     * which has been continued from the previous page.
     * @return 0x1 for true, 0x0 for false
     */
    public int continued() {
        return (headerBase[headerPointer + 5] & 0b0001);
    }

    /**
     * Whether this page is at the beginning of the logical bitstream
     * @return > 0 for true, 0x0 for false
     */
    public int bos() {
        return (headerBase[headerPointer + 5] & 0b0010);
    }

    /**
     * Whether this page is at the end of the logical bitstream
     * @return > 0 for true, 0x0 for false
     */
    public int eos() {
        return (headerBase[headerPointer + 5] & 0b0100);
    }

    /**
     * Return the exact granular position of the packet data contained at the end of this page.
     * This is useful for tracking location when seeking or decoding.
     * For example, in audio codecs this position is the pcm sample number and in video this is the frame number.
     * @return The specific last granular position of the decoded data contained in the page
     */
    public long granulePos() {
        long ret =         (headerBase[headerPointer + 13] & 0xff);
        ret = (ret << 8) | (headerBase[headerPointer + 12] & 0xff);
        ret = (ret << 8) | (headerBase[headerPointer + 11] & 0xff);
        ret = (ret << 8) | (headerBase[headerPointer + 10] & 0xff);
        ret = (ret << 8) | (headerBase[headerPointer +  9] & 0xff);
        ret = (ret << 8) | (headerBase[headerPointer +  8] & 0xff);
        ret = (ret << 8) | (headerBase[headerPointer +  7] & 0xff);
        ret = (ret << 8) | (headerBase[headerPointer +  6] & 0xff);

        return ret;
    }

    /**
     * Return the unique serial number for the logical bitstream of this page.
     * Each page contains the serial number for the logical bitstream that it belongs to.
     * @return The serial number for this page.
     */
    public int serialNo() {
        return     (headerBase[headerPointer + 14] & 0xff)
                | ((headerBase[headerPointer + 15] & 0xff) << 8)
                | ((headerBase[headerPointer + 16] & 0xff) << 16)
                | ((headerBase[headerPointer + 17] & 0xff) << 24);
    }

    /**
     * Return the sequential page number
     * This is useful for ordering pages or determining when pages have been lost.
     * @return The page number for this page
     */
    public int pageNo() {
        return     (headerBase[headerPointer + 18] & 0xff)
                | ((headerBase[headerPointer + 19] & 0xff) << 8)
                | ((headerBase[headerPointer + 20] & 0xff) << 16)
                | ((headerBase[headerPointer + 21] & 0xff) << 24);
    }

    /* NOTE:
       If a page consists of a packet begun on a previous page, and a new
       packet begun (but not completed) on this page, the return will be:
         ogg_page_packets(page)   ==1,
         ogg_page_continued(page) !=0

       If a page happens to be a single packet that was begun on a
       previous page, and spans to the next page (in the case of a three or
       more page packet), the return will be:
         ogg_page_packets(page)   ==0,
         ogg_page_continued(page) !=0
    */

    /**
     * Return the number of packets that are completed on this page.
     * If the leading packet is begun on a previous page, but ends on
     * this page, it's counted.
     */
    public int packets() {
        int count = 0, n = headerBase[headerPointer + 26];

        for (int i = 0; i < n; i++)
            if ((headerBase[headerPointer + 27 + i] & 0xff) < 255)
                count++;

        return count;
    }

    /**
     * Checksum an ogg page
     */
    public void checksum() {
        int crc_reg = 0;

        // safety; needed for API behavior, but not framing code
        headerBase[headerPointer + 22] = 0;
        headerBase[headerPointer + 23] = 0;
        headerBase[headerPointer + 24] = 0;
        headerBase[headerPointer + 25] = 0;

        /*

        // CRC code copied from `com.jcraft.jogg.Page`
        // Code in libogg is so hard to read
        for (int i = 0; i < headerBytes; i++) {
            crc_reg = (crc_reg << 8) ^ CRC_LOOKUP[((crc_reg >>> 24) & 0xff) ^ (headerBase[headerPointer + i] & 0xff)];
        }
        for (int i = 0; i < bodyBytes; i++) {
            crc_reg = (crc_reg << 8) ^ CRC_LOOKUP[((crc_reg >>> 24) & 0xff) ^ (bodyBase[bodyPointer + i] & 0xff)];
        }

         */

        crc_reg = updateCRC(crc_reg, headerBase, headerPointer, headerBytes);
        crc_reg = updateCRC(crc_reg, bodyBase, bodyPointer, bodyBytes);

        headerBase[headerPointer + 22] = (byte) ((crc_reg       ) & 0xff);
        headerBase[headerPointer + 23] = (byte) ((crc_reg >>>  8) & 0xff);
        headerBase[headerPointer + 24] = (byte) ((crc_reg >>> 16) & 0xff);
        headerBase[headerPointer + 25] = (byte) ((crc_reg >>> 24) & 0xff);
    }

    /**
     * @return A copy of current page
     */
    public Page copy() {
        return copy(new Page());
    }

    /**
     * @param p Destination page which will copy to
     * @return p
     */
    public Page copy(Page p) {
        byte[] temp;

        temp = new byte[headerBytes];
        System.arraycopy(headerBase, headerPointer, temp, 0, headerBytes);
        p.headerBase = temp;
        p.headerPointer = 0;
        p.headerBytes = headerBytes;

        temp = new byte[bodyBytes];
        System.arraycopy(bodyBase, bodyPointer, temp, 0, bodyBytes);
        p.bodyBase = temp;
        p.bodyPointer = 0;
        p.bodyBytes = bodyBytes;

        return p;
    }
}
