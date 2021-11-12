package ink.meodinger.jogg;


/**
 * Author: Meodinger
 * Date: 2021/10/27
 * Have fun with my code!
 */

public class Page {

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
        return (this.headerBase[this.headerPointer + 4] & 0xff);
    }

    /**
     * Indicate whether this page contains packet data
     * which has been continued from the previous page.
     * @return 0x1 for true, 0x0 for false
     */
    public int continued() {
        return (this.headerBase[this.headerPointer + 5] & 0b0001);
    }

    /**
     * Whether this page is at the beginning of the logical bitstream
     * @return > 0 for true, 0x0 for false
     */
    public int bos() {
        return (this.headerBase[this.headerPointer + 5] & 0b0010);
    }

    /**
     * Whether this page is at the end of the logical bitstream
     * @return > 0 for true, 0x0 for false
     */
    public int eos() {
        return (this.headerBase[this.headerPointer + 5] & 0b0100);
    }

    /**
     * Return the exact granular position of the packet data contained at the end of this page.
     * This is useful for tracking location when seeking or decoding.
     * For example, in audio codecs this position is the pcm sample number and in video this is the frame number.
     * @return The specific last granular position of the decoded data contained in the page
     */
    public long granulePos() {
        long ret =         (this.headerBase[this.headerPointer + 13] & 0xff);
        ret = (ret << 8) | (this.headerBase[this.headerPointer + 12] & 0xff);
        ret = (ret << 8) | (this.headerBase[this.headerPointer + 11] & 0xff);
        ret = (ret << 8) | (this.headerBase[this.headerPointer + 10] & 0xff);
        ret = (ret << 8) | (this.headerBase[this.headerPointer +  9] & 0xff);
        ret = (ret << 8) | (this.headerBase[this.headerPointer +  8] & 0xff);
        ret = (ret << 8) | (this.headerBase[this.headerPointer +  7] & 0xff);
        ret = (ret << 8) | (this.headerBase[this.headerPointer +  6] & 0xff);

        return ret;
    }

    /**
     * Return the unique serial number for the logical bitstream of this page.
     * Each page contains the serial number for the logical bitstream that it belongs to.
     * @return The serial number for this page.
     */
    public int serialNo() {
        return     (this.headerBase[this.headerPointer + 14] & 0xff)
                | ((this.headerBase[this.headerPointer + 15] & 0xff) << 8)
                | ((this.headerBase[this.headerPointer + 16] & 0xff) << 16)
                | ((this.headerBase[this.headerPointer + 17] & 0xff) << 24);
    }

    /**
     * Return the sequential page number
     * This is useful for ordering pages or determining when pages have been lost.
     * @return The page number for this page
     */
    public int pageNo() {
        return     (this.headerBase[this.headerPointer + 18] & 0xff)
                | ((this.headerBase[this.headerPointer + 19] & 0xff) << 8)
                | ((this.headerBase[this.headerPointer + 20] & 0xff) << 16)
                | ((this.headerBase[this.headerPointer + 21] & 0xff) << 24);
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
        final int n = this.headerBase[this.headerPointer + 26];
        int count = 0;

        for (int i = 0; i < n; i++)
            if ((this.headerBase[this.headerPointer + 27 + i] & 0xff) < 255)
                count++;

        return count;
    }

    /**
     * Checksum an ogg page
     */
    public void checksum() {
        checksum(false);
    }

    /**
     * Checksum an ogg page
     * @param useOriCRCAlgo Use original libogg CRC algorithm, or JOgg CRC algorithm
     */
    public void checksum(boolean useOriCRCAlgo) {
        int crc_reg = 0;

        // safety; needed for API behavior, but not framing code
        this.headerBase[this.headerPointer + 22] = 0;
        this.headerBase[this.headerPointer + 23] = 0;
        this.headerBase[this.headerPointer + 24] = 0;
        this.headerBase[this.headerPointer + 25] = 0;

        if (useOriCRCAlgo) {
            crc_reg = _CRC_.updateCRC(crc_reg, this.headerBase, this.headerPointer, this.headerBytes);
            crc_reg = _CRC_.updateCRC(crc_reg, this.bodyBase, this.bodyPointer, this.bodyBytes);
        } else {
            // CRC code copied from `com.jcraft.jogg.Page`
            // Code in libogg is so hard to read
            for (int i = 0; i < this.headerBytes; i++) {
                crc_reg = (crc_reg << 8)
                        ^ _CRC_.CRC_LOOKUP[((crc_reg >>> 24) & 0xff)
                        ^ (this.headerBase[this.headerPointer + i] & 0xff)];
            }
            for (int i = 0; i < this.bodyBytes; i++) {
                crc_reg = (crc_reg << 8)
                        ^ _CRC_.CRC_LOOKUP[((crc_reg >>> 24) & 0xff)
                        ^ (this.bodyBase[this.bodyPointer + i] & 0xff)];
            }
        }

        this.headerBase[this.headerPointer + 22] = (byte) ((crc_reg       ) & 0xff);
        this.headerBase[this.headerPointer + 23] = (byte) ((crc_reg >>>  8) & 0xff);
        this.headerBase[this.headerPointer + 24] = (byte) ((crc_reg >>> 16) & 0xff);
        this.headerBase[this.headerPointer + 25] = (byte) ((crc_reg >>> 24) & 0xff);
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
    public Page copy(final Page p) {
        final byte[] header = new byte[this.headerBytes];
        System.arraycopy(this.headerBase, this.headerPointer, header, 0, this.headerBytes);
        p.headerBase = header;
        p.headerPointer = 0;
        p.headerBytes = this.headerBytes;

        final byte[] body = new byte[this.bodyBytes];
        System.arraycopy(this.bodyBase, this.bodyPointer, body, 0, this.bodyBytes);
        p.bodyBase = body;
        p.bodyPointer = 0;
        p.bodyBytes = this.bodyBytes;

        return p;
    }
}
