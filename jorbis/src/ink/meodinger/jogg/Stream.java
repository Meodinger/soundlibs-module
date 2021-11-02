package ink.meodinger.jogg;

import java.util.Arrays;

/**
 * Author: Meodinger
 * Date: 2021/10/27
 * Have fun with my code!
 */

public class Stream {

    // ----- Constants ----- //

    private static final int INIT_DATA_STORAGE = 16 * 1024;
    private static final int INIT_VALUES_STORAGE = 1024;
    private static final int HEADER_WORKSPACE = 282;

    // ----- Fields ----- //

    /**
     * Data from packet bodies
     */
    private byte[] bodyData = new byte[INIT_DATA_STORAGE];
    /**
     * Storage allocated for bodies in bytes (filled or unfilled)
     */
    private int bodyStorage = INIT_DATA_STORAGE;
    /**
     * Amount of storage filled with stored packet bodies
     */
    private int bodyFill = 0;
    /**
     * Number of elements returned from storage
     */
    private int bodyReturned = 0;

    /**
     * String of lacing values for the packet segments within the current page
     * Each value is a byte, indicating packet segment length.
     */
    private int[] lacingValues = new int[INIT_VALUES_STORAGE];
    /**
     * Lacing values for the packet segments within the current page
     * Not compact this way, but it is simply coupled to the lacing fifo
     */
    private long[] granuleValues = new long[INIT_VALUES_STORAGE];
    /**
     * Total amount of storage (in bytes) allocated for storing lacing values
     */
    private int lacingStorage = INIT_VALUES_STORAGE;
    /**
     * Fill marker for the current vs. total allocated storage of lacing values for the page
     */
    private int lacingFill = 0;
    /**
     * Number of lacing values returned from lacing_storage
     */
    private int lacingReturned = 0;
    /**
     * Lacing value for current packet segment
     */
    private int lacingPacket = 0;

    /**
     * Temporary storage for page header during encode process
     * while the header is being created
     */
    private final byte[] headerData = new byte[HEADER_WORKSPACE];
    /**
     * Fill marker for header storage allocation
     * Used during the header creation process
     */
    private int headerFill = 0;

    /**
     * Marker set when the last packet of the logical bitstream has been buffered
     */
    public int eos = 0;
    /**
     * Marker set after we have written the first page in the logical bitstream
     */
    public int bos = 0;
    /**
     * Serial number of this logical bitstream
     */
    public int serialNo = 0;
    /**
     * Number of the current page within the stream
     */
    public int pageNo = 0;
    /**
     * Number of the current packet
     */
    public long packetNo = 0;
    /**
     * Exact position of decoding/encoding process
     */
    public long granulePos = 0;

    // ----- Constructors ----- //

    public Stream() {}

    public Stream(int serialNo) {
        init(serialNo);
    }

    // ----- Stream API ----- //

    /**
     * Initialize the StreamState and assign the stream a given serial number
     * @param serialNo Stream serial number
     */
    public void init(int serialNo) {
        Arrays.fill(bodyData, (byte) 0);
        Arrays.fill(lacingValues, 0);
        Arrays.fill(granuleValues, 0);

        this.serialNo = serialNo;
    }

    /**
     * Clear and free the internal memory used by StreamState,
     * but does not free the structure itself
     * It is safe to call clear() on the same structure more than once
     */
    public void clear() {
        bodyData = null;
        lacingValues = null;
        granuleValues = null;
    }

    public void reset() {
        bodyFill = 0;
        bodyReturned = 0;

        lacingFill = 0;
        lacingReturned = 0;
        lacingPacket = 0;

        headerFill = 0;

        eos = 0;
        bos = 0;
        pageNo = -1;
        packetNo = 0;
        granulePos = 0;

    }

    public void reset(int serialNo) {
        // todo
    }

    // destroy() is not needed (same as clear())

    public int check() {
        // todo
    }

    public int eof() {
        return eos;
    }

    private void bodyExpand(int needed) {
        if (bodyStorage <= bodyFill + needed) {
            bodyStorage += (needed + 1024);
            byte[] temp = new byte[bodyStorage];
            System.arraycopy(bodyData, 0, temp, 0, bodyData.length);
            bodyData = temp;
        }
    }

    private void lacingExpand(int needed) {
        if (lacingStorage <= lacingFill + needed) {
            lacingStorage += (needed + 32);

            int[] tempLacing = new int[lacingStorage];
            System.arraycopy(lacingValues, 0, tempLacing, 0, lacingValues.length);
            lacingValues = tempLacing;

            long[] tempGranules = new long[lacingStorage];
            System.arraycopy(granuleValues, 0, tempGranules, 0, granuleValues.length);
            granuleValues = tempGranules;
        }
    }

    // ----- Encode & Decode ----- //

    public int bytesIn(byte[] inputs, int eos, long granulePos) {
        // todo
    }

    /**
     * Submit a packet to the bitstream for page encapsulation
     * After this is called, more packets can be submitted, or pages can be written out
     *
     * In a typical encoding situation, this method should be used after filling a packet with data
     * The data in the packet is copied into the internal storage managed by StreamState,
     * so the caller is free to alter the contents of packet after this call has returned
     *
     * @param packet Packet will be copied to StreamState
     * @return 0: success
     *        -1: error (actually in java we never return -1)
     */
    public int packetIn(Packet packet) {
        final int lacingValue = packet.bytes / 255 + 1;

        if (bodyReturned != 0) {
            // Advance packet data according to the bodyReturned pointer
            // We had to keep it around to return a pointer into the buffer last call

            bodyFill -= bodyReturned;
            if (bodyFill != 0) {
                System.arraycopy(bodyData, bodyReturned, bodyData, 0, bodyFill);
            }
            bodyReturned = 0;
        }

        // Make sure we have the buffer storage
        bodyExpand(packet.bytes);
        lacingExpand(lacingValue);

        // Copy in the submitted packet
        // Yes, the copy is a waste; this is the liability of overly clean abstraction for the time being
        // It will actually be fairly easy to eliminate the extra copy in the future
        //
        // Note (Meodinger 2021/10/28) : I'll work on this in some time
        System.arraycopy(packet.data, packet.pointer, bodyData, bodyFill, packet.bytes);
        bodyFill += packet.bytes;

        // Store lacing values for this packet
        int i;
        for (i = 0; i < lacingValue - 1; i++) {
            lacingValues[lacingFill + i] = 255;
            granuleValues[lacingFill + i] = granulePos;
        }
        lacingValues[lacingFill + i] = (packet.bytes) % 255;
        granulePos = granuleValues[lacingFill + i] = packet.granulePos;

        // Flag the first segment as the beginning of the packet
        lacingValues[lacingFill] |= 0x0000_0100;
        lacingFill += lacingValue;

        // For the sake of completeness
        packetNo++;

        if (packet.eos != 0) eos = 1;

        return 0;
    }

    /**
     * Form packets into pages
     *
     * In a typical encoding situation, this would be called after using packetIn() to
     * submit data packets to the bitstream Internally, this method assembles
     * the accumulated packet bodies into an Ogg page suitable for writing to a stream
     * The method is typically called in a loop until there are no more pages ready for output
     *
     * This method will only return a page when a "reasonable" amount of packet data is available
     * Normally this is appropriate since it limits the overhead of the Ogg page headers in the bitstream,
     * and so calling pageOut() after packetIn() should be the common case. Call flush() if
     * immediate page generation is desired. This may be occasionally necessary, for example,
     * to limit the temporal latency of a variable bitrate stream.
     *
     * @param packet Destination packet
     * @return 0: Insufficient data has accumulated to fill a page;
     *        -1: An internal error occurred, in this case og is not modified
     *         1: A page has been completed and returned
     */
    public int packetOut(Packet packet) {
        // The last part of decode. We have the stream broken into packet segments
        // Now we need to group them into packets (or return the out of sync markers)

        int pointer = lacingReturned;
        if (lacingPacket <= pointer) return 0;

        if ((lacingValues[pointer] & 0x0000_0400) != 0) {
            // We lost sync here, let the app know
            lacingReturned++;

            // We need to tell the codec there's a gap
            // It might need to handle previous packet dependencies
            packetNo++;

            return -1;
        }

        // Gather the whole packet
        // We'll have no holes or a partial packet
        int size = lacingValues[pointer] & 0xff;
        int bytes = 0;

        packet.data = bodyData;
        packet.pointer = bodyReturned;
        packet.eos = lacingValues[pointer] & 0x0000_0200;
        packet.bos = lacingValues[pointer] & 0x0000_0100;
        bytes += size;

        while (size == 255) {
            int val = lacingValues[++pointer];
            size = val & 0xff;
            if ((val & 0x0000_0200) != 0) packet.eos = 0x0000_0200;
            bytes += size;
        }

        packet.packetNo = packetNo;
        packet.granulePos = granuleValues[pointer];
        packet.bytes = bytes;

        bodyReturned += bytes;
        lacingReturned = pointer + 1;
        // Gathering end here

        packetNo++;
        return 1;
    }

    public int packetPeek(Packet packet) {
        // todo
    }

    /**
     * Add a complete page to the bitstream
     *
     * In a typical decoding situation, this method would be called after using pageOut() to create a valid Page
     *
     * Internally, this method breaks the page into packet segments in preparation for
     * outputting a valid packet to the codec decoding layer
     * @param page Page to be submitted to bitstream
     * @return 0: success
     *        -1: error
     */
    public int pageIn(Page page) {
        final byte[] headerBase = page.headerBase;
        int headerPointer = page.headerPointer;

        final byte[] bodyBase = page.bodyBase;
        int bodyPointer = page.bodyPointer;
        int bodyBytes = page.bodyBytes;

        final int _segments = headerBase[headerPointer + 26] & 0xff;
        int segmentPointer = 0;

        final int _version = page.version();
        final int _continued = page.continued();
        final long _granulePos = page.granulePos();
        final int _serialNo = page.serialNo();
        final int _pageNo = page.pageNo();
        int _bos = page.bos();
        int _eos = page.eos();

        // Cleanup 'returned data'
        if (bodyReturned != 0) { // body data
            bodyFill -= bodyReturned;
            if (bodyFill != 0) {
                System.arraycopy(bodyData, bodyReturned, bodyData, 0, bodyFill);
            }
            bodyReturned = 0;
        }
        if (lacingReturned != 0) { // segment table
            if (lacingFill - lacingReturned != 0) {
                System.arraycopy(lacingValues, lacingReturned, lacingValues , 0, lacingFill - lacingReturned);
                System.arraycopy(granuleValues, lacingReturned, granuleValues, 0, lacingFill - lacingReturned);
            }
            lacingFill -= lacingReturned;
            lacingPacket -= lacingReturned;
            lacingReturned = 0;
        }

        // check the serial number
        if (_serialNo != serialNo) return -1;
        if (_version > 0) return -1;

        lacingExpand(_segments + 1);

        // Are we in sequence?
        if (_pageNo != pageNo) {
            // Unroll previous partial packet (if any)
            for (int i = lacingPacket; i < lacingFill; i++) {
                bodyFill -= lacingValues[i] & 0xff;
            }
            lacingFill = lacingPacket;

            // Make a note of dropped data in segment table
            if (pageNo != -1) {
                lacingValues[lacingFill++] = 0x0000_0400;
                lacingPacket++;
            }

            // Are we a 'continued packet' page?
            // If so, we'll need to skip some segments
            if (_continued != 0) {
                _bos = 0;
                for (; segmentPointer < _segments; segmentPointer++) {
                    int val = (headerBase[headerPointer + 27 + segmentPointer] & 0xff);
                    bodyPointer += val;
                    bodyBytes -= val;
                    if (val < 255) {
                        segmentPointer++;
                        break;
                    }
                }
            }
        }

        if (bodyBytes != 0) {
            bodyExpand(bodyBytes);
            System.arraycopy(bodyBase, bodyPointer, bodyData, bodyFill, bodyBytes);
            bodyFill += bodyBytes;
        }

        int saved = -1;
        while (segmentPointer < _segments) {
            int val = (headerBase[headerPointer + 27 + segmentPointer] & 0xff);
            lacingValues[lacingFill] = val;
            granuleValues[lacingFill] = -1;

            if (_bos != 0) {
                lacingValues[lacingFill] |= 0x0000_0100;
                _bos = 0;
            }

            if (val < 255) saved = lacingFill;

            lacingFill++;
            segmentPointer++;

            if (val < 255) lacingPacket = lacingFill;
        }
        // Set the granulePos on the last pcm val of the last full packet
        if (saved != -1) granuleValues[saved] = _granulePos;

        if (_eos != 0) {
            eos = 1;
            if (lacingFill > 0) lacingValues[lacingFill - 1] |= 0x0000_0200;
        }

        pageNo = _pageNo + 1;
        return 0;
    }

    /**
     * Form packets into pages.
     *
     * In a typical encoding situation, this would be called after using
     * packetIn() to submit data packets to the bitstream. Internally,
     * this method assembles the accumulated packet bodies into an Ogg page
     * suitable for writing to a stream. The method is typically called
     * in a loop until there are no more pages ready for output
     *
     * This method will only return a page when a "reasonable" amount of
     * packet data is available. Normally this is appropriate since it
     * limits the overhead of the Ogg page headers in the bitstream,
     * and so calling pageOut() after packetIn() should be the common case.
     * Call flush() if immediate page generation is desired. This may be
     * occasionally necessary, for example, to limit the temporal latency
     * of a variable bitrate stream.
     *
     * @param page Destination page
     * @return 0: success
     *        -1: error (actually in java we never return -1)
     */
    public int pageOut(Page page) {
        if ((eos != 0 && lacingFill != 0)    // Done, now flush
         || (bodyFill - bodyReturned > 4096) // Page nominal size
         || (lacingFill >= 255)              // Segment table full
         || (bos == 0 && lacingFill != 0)    // Initial header page
        ) {
            return flush(page);
        }
        return 0;
    }

    public int pageOutFill(Page page, int fill) {
        // todo
    }

    /**
     * This will flush remaining packets into a page (returning nonzero),
     * even if there is not enough data to trigger a flush normally
     * (undersized page). If there are no packets or partial packets to
     * flush, flush() returns 0.  Note that flush() will try to flush a
     * normal-sized page like pageOut(); a call to flush() does not guarantee
     * that all packets have flushed. Only a return value of 0 from flush()
     * indicates all packet data is flushed into pages
     *
     * Page will flush the last page in a stream even if it's undersized;
     * you almost certainly want to use pageOut() (and *not* flush())
     * unless you need to flush an undersized page in the middle of
     * a stream for some reason
     *
     * @return 0: all packet data has already been flushed into pages, and there are no packets to put into the page.
     *          | an StreamState has been cleared explicitly or implicitly due to an internal error
     *      else: remaining packets have successfully been flushed into the page
     */
    public int flush(Page page) {
        final int maxValues = Math.min(lacingFill, 255);
        int i, values;

        int bytes = 0;
        int acc = 0;
        long _granulePos = granuleValues[0];

        if (maxValues == 0) return 0;

        // Construct a page
        // Decide how many segments to include

        // If this is the initial header case, the first page must only include
        // the initial header packet
        if (bos == 0) {
            _granulePos = 0;
            for (values = 0; values < maxValues; values++) {
                if ((lacingValues[values] & 0xff) < 255) {
                    values++;
                    break;
                }
            }
        } else {
            for (values = 0; values < maxValues; values++) {
                if (acc > 4096) break;
                acc += (lacingValues[values] & 0xff);
                _granulePos = granuleValues[values];
            }
        }

        // Construct the header in temp storage

        // Magic number
        System.arraycopy("OggS".getBytes(), 0, headerData, 0, 4);

        // Stream structure version
        headerData[4] = 0x00;

        headerData[5] = 0x00;
        // Continued packet flag
        if ((lacingValues[0] & 0x0100) == 0) headerData[5] |= 0b0001;
        // First page flag
        if (bos == 0) headerData[5] |= 0b0010;
        // Last page flag
        if (eos != 0 && lacingFill == values) headerData[5] |= 0b0100;

        bos = 1;

        // 64 bits of PCM position
        for (i = 6; i < 14; i++) {
            headerData[i] = (byte) _granulePos;
            _granulePos >>>= 8;
        }

        // 32 bits of stream serial number
        int _serialNo = serialNo;
        for (i = 14; i < 18; i++) {
            headerData[i] = (byte) _serialNo;
            _serialNo >>>= 8;
        }

        // 32 bits of page counter
        // (we have both counter and page header because this val can roll over)
        if (pageNo == -1) pageNo = 0; /* because someone called
                                         reset(); this would be a
                                         strange thing to do in an
                                         encoding stream, but it has
                                         plausible uses */
        int _pageNo = pageNo++;
        for (i = 18; i < 22; i++) {
            headerData[i] = (byte) _pageNo;
            _pageNo >>>= 8;
        }

        // zero for computation, filled in later
        headerData[22] = 0;
        headerData[23] = 0;
        headerData[24] = 0;
        headerData[25] = 0;

        // segment table
        headerData[26] = (byte) values;
        for (i = 0; i < values; i++) {
            headerData[27 + i] = (byte) lacingValues[i];
            bytes += (headerData[i + 27] & 0xff);
        }

        // set pointers in the Page
        page.headerBase = headerData;
        page.headerPointer = 0;
        page.headerBytes = headerFill = values + 27;
        page.bodyBase = bodyData;
        page.bodyPointer = bodyReturned;
        page.bodyBytes = bytes;

        // Advance the lacing data and set the bodyReturned pointer

        lacingFill -= values;
        System.arraycopy(lacingValues, values, lacingValues, 0, lacingFill * 4);
        System.arraycopy(granuleValues, values, granuleValues, 0, lacingFill * 8);
        bodyReturned += bytes;

        // calculate the checksum
        page.checksum();

        // done
        return 1;
    }

    public int flushFill(Page page, int fill) {
        // todo
    }
}
