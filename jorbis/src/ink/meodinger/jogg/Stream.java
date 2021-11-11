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
    private byte[] bodyData = null;
    /**
     * Storage allocated for bodies in bytes (filled or unfilled)
     */
    private int bodyStorage = 0;
    /**
     * Amount of storage filled with stored packet bodies
     */
    private int bodyFill = 0;
    /**
     * Number of elements returned from storage
     */
    private int bodyReturned = 0;

    /**
     * Lacing values for the packet segments within the current page
     * Not compact this way, but it is simply coupled to the lacing fifo
     */
    private long[] granuleValues = null;
    /**
     * String of lacing values for the packet segments within the current page
     * Each value is a byte, indicating packet segment length.
     */
    private int[] lacingValues = null;
    /**
     * Total amount of storage (in bytes) allocated for storing lacing values
     */
    private int lacingStorage = 0;
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
    private byte[] headerData = null;
    /**
     * Fill marker for header storage allocation
     * Used during the header creation process
     */
    private int headerFill = 0;

    /**
     * Marker set after we have written the first page in the logical bitstream
     */
    public int bos = 0;
    /**
     * Marker set when the last packet of the logical bitstream has been buffered
     */
    public int eos = 0;
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

    public Stream() {
        init();
    }

    public Stream(int serialNo) {
        init(serialNo);
    }

    // ----- Stream API ----- //

    public void init() {
        this.bodyData = new byte[INIT_DATA_STORAGE];
        this.bodyStorage = INIT_DATA_STORAGE;
        this.bodyFill = 0;
        this.bodyReturned = 0;

        this.granuleValues = new long[INIT_VALUES_STORAGE];
        this.lacingValues = new int[INIT_VALUES_STORAGE];
        this.lacingStorage = INIT_VALUES_STORAGE;
        this.lacingFill = 0;
        this.lacingReturned = 0;
        this.lacingPacket = 0;

        this.headerData = new byte[HEADER_WORKSPACE];
        this.headerFill = 0;

        this.bos = 0;
        this.eos = 0;
        this.pageNo = 0;
        this.packetNo = 0;
        this.granulePos = 0;

        Arrays.fill(this.bodyData, (byte) 0);
        Arrays.fill(this.granuleValues, 0);
        Arrays.fill(this.lacingValues, 0);
        Arrays.fill(this.headerData, (byte) 0);
    }

    /**
     * Initialize a `Stream` and allocate appropriate memory in preparation for encoding or decoding.
     * Also assigns the stream a given serial number.
     * @param serialNo Stream serial number
     */
    public void init(int serialNo) {
        init();
        this.serialNo = serialNo;
    }

    /**
     * Clear and free the internal memory used by Stream,
     * but does not free the structure itself
     * It is safe to call clear() on the same structure more than once
     */
    public void clear() {
        this.bodyData = null;
        this.bodyStorage = 0;
        this.bodyFill = 0;
        this.bodyReturned = 0;

        this.headerData = null;
        this.headerFill = 0;

        this.granuleValues = null;
        this.lacingValues = null;
        this.lacingStorage = 0;
        this.lacingFill = 0;
        this.lacingReturned = 0;
        this.lacingPacket = 0;

        this.bos = 0;
        this.eos = 0;
        this.serialNo = 0;
        this.pageNo = 0;
        this.packetNo = 0;
        this.granulePos = 0;
    }

    /**
     * Set values in the `Stream` back to initial values.
     */
    public void reset() {
        // if (check() != 0) return -1;

        this.bodyFill = 0;
        this.bodyReturned = 0;

        this.lacingFill = 0;
        this.lacingReturned = 0;
        this.lacingPacket = 0;

        this.headerFill = 0;

        this.eos = 0;
        this.bos = 0;
        this.pageNo = -1;
        this.packetNo = 0;
        this.granulePos = 0;
    }

    /**
     * Set values in the `Stream` back to initial values.
     * Additionally, it sets the stream serial number to the given value.
     * @param serialNo New stream serial number to use
     */
    public void reset(int serialNo) {
        // if (check() != 0) return -1;

        reset();
        this.serialNo = serialNo;
    }

    /**
     * Frees the internal memory used by the `Stream` as well as itself.
     * This method should be called when you are done working with an ogg stream.
     * It can also be called to make sure that the struct does not exist.
     * It calls free() on its argument, so if the `Stream` is not malloc()'d
     * or will otherwise be freed by your own code, use ogg_stream_clear instead.
     */
    @Deprecated(forRemoval = true)
    public int destroy() {
        clear();
        return 0;
    }

    /**
     * Check the error or readiness condition of a `Stream`.
     * It is a safe practice to ignore unrecoverable errors (such as an internal error
     * caused by a malloc() failure) returned by ogg stream synchronization calls.
     * Should an internal error occur, the `Stream` will be cleared (equivalent to a call to `clear()`)
     * and subsequent calls using this `Stream` will be noops. Error detection is then handled via
     * a single call to `check()` at the end of the operational block.
     * @return 0 for good; else for error
     */
    @Deprecated(forRemoval = true)
    public int check() {
        return 0;
    }

    public int eos() {
        // if (check() != 0) return 1;

        return eos;
    }

    // ----- private api ----- //

    /**
     * Expand bodyData
     * @param needed bytes needed
     * @return 0 success; -1 error
     */
    private int bodyExpand(int needed) {
        if (this.bodyStorage - needed <= this.bodyFill) {
            if (this.bodyStorage > Integer.MAX_VALUE - needed) {
                clear();
                return -1;
            }

            int newStorage = this.bodyStorage + needed;
            if (newStorage < Integer.MAX_VALUE - 1024) newStorage += 1024;

            byte[] newData = new byte[newStorage];
            System.arraycopy(this.bodyData, 0, newData, 0, this.bodyData.length);
            this.bodyData = newData;
            this.bodyStorage = newStorage;
        }
        return 0;
    }

    /**
     * Expand lacingValues and granuleValues
     * @param needed bytes needed
     * @return 0 success; -1 error
     */
    private int lacingExpand(int needed) {
        if (this.lacingStorage - needed <= this.lacingFill) {
            if (this.lacingStorage > Integer.MAX_VALUE - needed) {
                clear();
                return -1;
            }

            int newStorage = this.lacingStorage + needed;
            if (newStorage < Integer.MAX_VALUE - 32) newStorage += 32;

            long[] newGranules = new long[newStorage];
            System.arraycopy(this.granuleValues, 0, newGranules, 0, this.granuleValues.length);
            this.granuleValues = newGranules;

            int[] newLacing = new int[newStorage];
            System.arraycopy(this.lacingValues, 0, newLacing, 0, this.lacingValues.length);
            this.lacingValues = newLacing;

            this.lacingStorage = newStorage;
        }
        return 0;
    }

    /**
     * Submit data to the internal buffer of the framing engine.
     * @param inputs Array of data will be copied to Stream
     * @return 0 success; -1 error
     */
    private int bytesIn(byte[][] inputs, int[] inputPointers, int[] inputBytes, int eos, long granulePos) {
        // if (check() != 0) return -1;

        if (inputs == null || inputs.length == 0) return 0;
        final int inputCount = inputs.length;

        int bytes = 0;
        for (int i = 0; i < inputCount; i++) {
            // if (_len > Integer.MAX_VALUE) return -1;
            if (bytes > Integer.MAX_VALUE - inputBytes[i]) return -1;
            bytes += inputBytes[i];
        }
        final int lacingValues = bytes / 255 + 1;

        if (this.bodyReturned != 0) {
            // Advance packet data according to the bodyReturned pointer
            // We had to keep it around to return a pointer into the buffer
            // last call
            this.bodyFill -= this.bodyReturned;
            if (this.bodyFill != 0) {
                System.arraycopy(
                        this.bodyData, this.bodyReturned,
                        this.bodyData, 0,
                        this.bodyFill
                );
            }
            this.bodyReturned = 0;
        }

        // Make sure we have the buffer storage
        if(bodyExpand(bytes) != 0 || lacingExpand(lacingValues) != 0) return -1;

        // Copy in the submitted packet
        // Yes, the copy is a waste; this is the liability of overly clean abstraction for the time being.
        // It will actually be fairly easy to eliminate the extra copy in the future
        for (int i = 0; i < inputCount; i++) {
            System.arraycopy(inputs[i], inputPointers[i], this.bodyData, this.bodyFill, inputBytes[i]);
            this.bodyFill += inputBytes[i];
            int c = 0;
        }

        // Store lacing values for this packet
        for (int i = 0; i < lacingValues - 1; i++) {
            this.lacingValues[this.lacingFill + i] = 255;
            this.granuleValues[this.lacingFill + i] = this.granulePos;
        }
        this.lacingValues[this.lacingFill + lacingValues - 1] = bytes % 255;
        this.granuleValues[this.lacingFill + lacingValues - 1] = granulePos;
        this.granulePos = granulePos;

        // Flag the first segment as the beginning of the packet
        this.lacingValues[this.lacingFill] |= 0x0100;
        this.lacingFill += lacingValues;

        // For the sake of completeness
        this.packetNo++;

        if (eos != 0) this.eos = 1;

        return 0;
    }

    /**
     * Conditionally flush a page
     * @param page Destination
     * @param force 0 will only flush nominal-size;
     *              1 forces us to flush a page regardless of page size
     *              so long as there's any data available at all
     * @param nFill Packet data watermark in bytes
     * @return   0 all packet data has already been flushed into pages, and there are no packets to put into the page
     *             A Stream has been cleared explicitly or implicitly due to an internal error;
     *        else remaining packets have successfully been flushed into the page
     */
    private int flushInternal(Page page, int force, int nFill) {
        // if (check() != 0) return 0;

        final int maxValues = Math.min(255, this.lacingFill);
        if (maxValues == 0) return 0;

        int values;
        int bytes = 0;
        long acc = 0;
        long granulePos = -1;

        // Construct a page
        // Decide how many segments to include

        // If this is the initial header case, the first page must
        // only include the initial header packet
        if (this.bos == 0) {
            granulePos = 0;
            for (values = 0; values < maxValues; values++) {
                if ((this.lacingValues[values] & 0xff) < 255) {
                    values++;
                    break;
                }
            }
        } else {
            // The extra packets_done, packet_just_done logic here attempts to do two things:
            //     1) Don't unnecessarily span pages
            //     2) Unless necessary, don't flush pages if there are less than four packets on them;
            //        this expands page size to reduce unnecessary overhead if incoming packets are large
            // These are not necessary behaviors, just 'always better than naive flushing' without
            // requiring an application to explicitly request a specific optimized behavior
            // We'll want an explicit behavior setup pathway eventually as well
            int packetsDone = 0;
            int packetsJustDone = 0;
            for (values = 0; values < maxValues; values++) {
                if (acc > nFill && packetsJustDone >= 4) {
                    force = 1;
                    break;
                }
                acc += this.lacingValues[values] & 0xff;
                if ((this.lacingValues[values] & 0xff) < 255) {
                    granulePos = this.granuleValues[values];
                    packetsJustDone = ++packetsDone;
                } else {
                    packetsJustDone = 0;
                }
            }
            if (values == 255) force = 1;
        }

        if (force == 0) return 0;

        // Construct the header in temp storage
        System.arraycopy("OggS".getBytes(), 0, this.headerData, 0, 4);

        // Stream structure version
        this.headerData[4] = 0x00;

        this.headerData[5] = 0x00;
        // Continued packet flag?
        if ((this.lacingValues[0] & 0x0100) == 0) this.headerData[5] |= 0b0001;
        // First page flag?
        if (this.bos == 0) headerData[5] |= 0b0010;
        // Last page flag?
        if (this.eos != 0 && this.lacingFill == values) this.headerData[5] |= 0b0100;
        this.bos = 1;

        // 64 bits of PCM position
        for (int i = 6; i < 14; i++) {
            this.headerData[i] = (byte) (granulePos & 0xff);
            granulePos >>= 8;
        }

        // 32 bits of stream serial number
        int serialNo = this.serialNo;
        for (int i = 14; i < 18; i++) {
            this.headerData[i] = (byte) (serialNo & 0xff);
            serialNo >>= 8;
        }

        // 32 bits of page counter
        // (we have both counter and page header because this val can roll over)
        /// because someone called reset();
        // this would be a strange thing to do in an encoding stream,
        // but it has plausible uses
        if (this.pageNo == -1) this.pageNo = 0;
        int pageNo = this.pageNo++;
        for (int i = 18; i < 22; i++) {
            this.headerData[i] = (byte) (pageNo & 0xff);
            pageNo >>= 8;
        }

        // zero for computation, filled in later
        this.headerData[22] = 0;
        this.headerData[23] = 0;
        this.headerData[24] = 0;
        this.headerData[25] = 0;

        // segment table
        this.headerData[26] = (byte) (values & 0xff);
        for (int i = 0; i < values; i++) {
            int size = this.lacingValues[i] & 0xff;
            this.headerData[27 + i] = (byte) size;
            bytes += size;
        }

        // set pointers in the Page
        page.headerBase = this.headerData;
        page.headerPointer = 0;
        page.headerBytes = this.headerFill = values + 27;
        page.bodyBase = this.bodyData;
        page.bodyPointer = this.bodyReturned;
        page.bodyBytes = bytes;

        // Advance the lacing data and set the bodyReturned pointer
        this.lacingFill -= values;
        System.arraycopy(this.lacingValues, values, this.lacingValues, 0, this.lacingFill);
        System.arraycopy(this.granuleValues, values, this.granuleValues, 0, this.lacingFill);
        this.bodyReturned += bytes;

        // calculate the checksum
        page.checksum();

        // done
        return 1;
    }

    /**
     * For packetOut/Peek ues
     */
    private int packetOutInternal(Packet packet, int advance) {
        // The last part of decode. We have the stream broken into packet segments.
        // Now we need to group them into packets (or return the out of sync markers)

        int pointer = this.lacingReturned;
        if (this.lacingPacket <= pointer) return 0;

        if ((this.lacingValues[pointer] & 0x0400) != 0) {
            // We lost sync here, let the app know
            this.lacingReturned++;

            // We need to tell the codec there's a gap.
            // It might need to handle previous packet dependencies
            this.packetNo++;

            return -1;
        }

        // Just using peek as an inexpensive way to ask
        // if there's a whole packet waiting.
        if (packet == null && advance == 0) return 1;

        // Gather the whole packet.
        // We'll have no holes or a partial packet.
        int size = this.lacingValues[pointer] & 0x00ff;
        int bos  = this.lacingValues[pointer] & 0x0100;
        int eos  = this.lacingValues[pointer] & 0x0200;

        int bytes = size;
        while (size == 255) {
            int val = this.lacingValues[++pointer];
            size = val & 0xff;
            if ((val & 0x0200) != 0) eos = 0x0200;
            bytes += size;
        }

        if (packet != null) {
            packet.bos = bos;
            packet.eos = eos;
            packet.data = this.bodyData;
            packet.pointer = this.bodyReturned;
            packet.bytes = bytes;
            packet.packetNo = this.packetNo;
            packet.granulePos = this.granuleValues[pointer];
        }

        if (advance != 0) {
            this.bodyReturned += bytes;
            this.lacingReturned = pointer + 1;
            this.packetNo++;
        }

        return 1;
    }

    // ----- Encode & Decode ----- //

    /**
     * Submit a packet to the bitstream for page encapsulation.
     * After this is called, more packets can be submitted, or pages can be written out.
     *
     * In a typical encoding situation, this method should be used after filling a packet with data.
     * The data in the packet is copied into the internal storage managed by Stream,
     * so the caller is free to alter the contents of packet after this call has returned.
     *
     * @param packet Packet will be copied to Stream
     * @return 0 success; -1 error
     */
    public int packetIn(Packet packet) {
        return bytesIn(new byte[][] { packet.data }, new int[] { packet.pointer }, new int[] { packet.bytes }, packet.eos, packet.granulePos);
    }

    /**
     * Assemble a data packet for output to the codec decoding engine.
     * The data has already been submitted to the `Stream` and broken into segments.
     * Each successive call returns the next complete packet built from those segments.
     *
     * In a typical decoding situation, this method should be used after calling
     * `pageIn()` to submit a page of data to the bitstream.
     * If the function returns 0, more data is needed and another page should be submitted.
     * A non-zero return value indicates successful return of a packet.
     *
     * The op is filled in with pointers to memory managed by the stream state
     * and is only valid until the next call.
     * The client must copy the packet data if a longer lifetime is required.
     *
     * @param packet Destination packet
     * @return 0 Insufficient data available to complete a packet,
     *           or on unrecoverable internal error occurred.
     *           `packet` has not been updated.
     *        -1 if we are out of sync and there is a gap in the data.
     *           This is usually a recoverable error and subsequent calls
     *           to `packetOut()` are likely to succeed.
     *           `packet` has not been updated.
     *         1 A packet was assembled normally.
     *           `packet` contains the next packet from the stream.
     */
    public int packetOut(Packet packet) {
        // if (check() != 0) return 0;
        return packetOutInternal(packet, 1);
    }

    /**
     * Attempt to assemble a raw data packet and returns it without advancing decoding.
     *
     * In a typical situation, this method would be called speculatively after `pageIn()`
     * to check the packet contents before handing it off to a codec for decompression.
     * To advance page decoding and remove the packet from the sync structure, call `packetOut()`.
     *
     * @param packet Pointer to the next packet available in the bitstream, if any.
     *               A NULL value may be passed in the case of a simple "is there a packet?" check.
     * @return -1 No packet available due to lost sync or a hole in the data.
     *          0 Insufficient data available to complete a packet, or unrecoverable internal error occurred.
     *          1 A packet is available.
     */
    public int packetPeek(Packet packet) {
        // if (check() != 0) return 0;
        return packetOutInternal(packet, 0);
    }

    /**
     * Add a complete page to the bitstream
     *
     * In a typical decoding situation, this method would be called after using pageOut() to create a valid Page
     *
     * Internally, this method breaks the page into packet segments in preparation for
     * outputting a valid packet to the codec decoding layer
     * @param page Page to be submitted to bitstream
     * @return 0 success; -1 error, means that the serial number of the page did not
     *         match the serial number of the bitstream, the page version was incorrect,
     *         or an internal error occurred.
     */
    public int pageIn(Page page) {
        final byte[] headerBase = page.headerBase;
        int headerPointer = page.headerPointer;

        final byte[] bodyBase = page.bodyBase;
        int bodyPointer = page.bodyPointer;
        int bodyBytes = page.bodyBytes;

        final int version = page.version();
        final int continued = page.continued();
        final int bos = page.bos();
        final int eos = page.eos();
        final int serialNo = page.serialNo();
        final int pageNo = page.pageNo();
        final long granulePos = page.granulePos();

        final int segments = headerBase[headerPointer + 26] & 0xff;
        int segmentPointer = 0;

        int newBos = bos;

        // if (check() != 0) return -1;

        // Cleanup 'returned data'
        if (this.bodyReturned != 0) { // body data
            this.bodyFill -= this.bodyReturned;
            if (this.bodyFill != 0) {
                System.arraycopy(
                        this.bodyData, this.bodyReturned,
                        this.bodyData, 0,
                        this.bodyFill
                );
            }
            this.bodyReturned = 0;
        }
        if (this.lacingReturned != 0) { // segment table
            if (this.lacingFill - this.lacingReturned != 0) {
                System.arraycopy(
                        this.granuleValues, this.lacingReturned,
                        this.granuleValues, 0,
                        this.lacingFill - this.lacingReturned
                );
                System.arraycopy(
                        this.lacingValues, this.lacingReturned,
                        this.lacingValues , 0,
                        this.lacingFill - this.lacingReturned
                );
            }
            this.lacingFill -= this.lacingReturned;
            this.lacingPacket -= this.lacingReturned;
            this.lacingReturned = 0;
        }

        // check the serial number
        if (serialNo != this.serialNo) return -1;
        if (version > 0) return -1;

        if (lacingExpand(segments + 1) != 0) return -1;

        // Are we in sequence?
        if (pageNo != this.pageNo) {
            // Unroll previous partial packet (if any)
            for (int i = this.lacingPacket; i < this.lacingFill; i++) {
                this.bodyFill -= this.lacingValues[i] & 0xff;
            }
            this.lacingFill = this.lacingPacket;

            // Make a note of dropped data in segment table
            if (this.pageNo != -1) {
                this.lacingValues[this.lacingFill++] = 0x0400;
                this.lacingPacket++;
            }

            // Are we a 'continued packet' page?
            // If so, we'll need to skip some segments
            if (continued != 0) {
                if (this.lacingFill < 1
                || (this.lacingValues[this.lacingFill - 1] & 0xff) < 255
                || (this.lacingValues[this.lacingFill - 1]) == 0x0400
                ) {
                    newBos = 0;
                }

                for (; segmentPointer < segments; segmentPointer++) {
                    int val = headerBase[headerPointer + 27 + segmentPointer] & 0xff;
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
            if (bodyExpand(bodyBytes) != 0) return -1;
            System.arraycopy(bodyBase, bodyPointer, this.bodyData, this.bodyFill, bodyBytes);
            this.bodyFill += bodyBytes;
        }

        int saved = -1;
        while (segmentPointer < segments) {
            int val = headerBase[headerPointer + 27 + segmentPointer] & 0xff;
            this.granuleValues[this.lacingFill] = -1;
            this.lacingValues[this.lacingFill] = val;

            if (newBos != 0) {
                this.lacingValues[this.lacingFill] |= 0x0100;
                newBos = 0;
            }

            if (val < 255) saved = this.lacingFill;

            this.lacingFill++;
            segmentPointer++;

            if (val < 255) this.lacingPacket = this.lacingFill;
        }
        // Set the granulePos on the last pcm val of the last full packet
        if (saved != -1) this.granuleValues[saved] = granulePos;

        if (eos != 0) {
            this.eos = 1;
            if (this.lacingFill > 0) this.lacingValues[this.lacingFill - 1] |= 0x0200;
        }

        this.pageNo = pageNo + 1;

        return 0;
    }

    /**
     * Form packets into pages.
     *
     * In a typical encoding situation, this would be called after using
     * `packetIn()` to submit data packets to the bitstream.
     * Internally, this method assembles the accumulated packet bodies
     * into an Ogg page suitable for writing to a stream.
     * The method is typically called in a loop until there are no more
     * pages ready for output.
     *
     * This method will only return a page when a "reasonable" amount of
     * packet data is available. Normally this is appropriate since it
     * limits the overhead of the Ogg page headers in the bitstream,
     * and so calling `pageOut()` after `packetIn()` should be the common case.
     * Call `flush()` if immediate page generation is desired.
     * This may be occasionally necessary, for example, to limit the
     * temporal latency of a variable bitrate stream.
     *
     * @param page Destination page
     * @return   0 Zero means that insufficient data has accumulated to fill a page,
     *             or an internal error occurred. In this case og is not modified.
     *        else a page has been completed and returned.
     */
    public int pageOut(Page page) {
        // if (check() != 0) return 0;
        int force = 0;
        //  'were done, now flush' case                 'initial header page' case
        if ((this.eos != 0 && this.lacingFill != 0) || (this.bos == 0 && this.lacingFill != 0)) force = 1;
        return flushInternal(page, force, 4096);
    }

    /**
     * Form packets into pages, similar to `pageOut()`,
     * but allows applications to explicitly request a specific page spill size.
     *
     * In a typical encoding situation, this would be called after using `packetIn()`
     * to submit data packets to the bitstream. Internally, this function assembles
     * the accumulated packet bodies into an Ogg page suitable for writing to a stream.
     * The method is typically called in a loop until there are no more pages ready for output.
     *
     * This function will return a page when at least four packets have been accumulated
     * and accumulated packet data meets or exceeds the specified number of bytes,
     * and/or when the accumulated packet data meets/exceeds the maximum page size
     * regardless of accumulated packet count.
     * Call `flush()` or `flushFill()` if immediate page generation is desired regardless
     * of accumulated data.
     *
     * @param page Destination page
     * @param nFill Packet data watermark in bytes
     * @return   0 Zero means that insufficient data has accumulated to fill a page,
     *             or an internal error occurred. In this case og is not modified.
     *        else a page has been completed and returned.
     */
    public int pageOutFill(Page page, int nFill) {
        // if (check() != 0) return 0;
        int force = 0;
        //  'were done, now flush' case                 'initial header page' case
        if ((this.eos != 0 && this.lacingFill != 0) || (this.bos == 0 && this.lacingFill != 0)) force = 1;
        return flushInternal(page, force, nFill);
    }

    /**
     * Check for remaining packets inside the stream and force remaining packets into a page,
     * regardless of the size of the page.
     *
     * This method should only be used when you want to flush an undersized page from
     * the middle of the stream. Otherwise, `pageOut()` or `pageOutFill()` should always be used.
     *
     * This method can also be used to verify that all packets have been flushed.
     * If the return value is 0, all packets have been placed into a page.
     * Like `pageOut()`, it should generally be called in a loop until available
     * packet data has been flushes, since even a single packet may span multiple pages.
     *
     * @param page The remaining packets in the stream will be placed into this page, if any remain.
     * @return   0 all packet data has already been flushed into pages, and there are no packets to put into the page
     *             A Stream has been cleared explicitly or implicitly due to an internal error;
     *        else remaining packets have successfully been flushed into the page
     */
    public int flush(Page page) {
        return flushInternal(page, 1, 4096);
    }

    /**
     * Flush available packets into pages, similar to `flush()`,
     * but allow applications to explicitly request a specific page spill size.
     *
     * This method checks for remaining packets inside the stream and forces
     * remaining packets into pages of approximately the requested size.
     * This should be used when you want to flush all remaining data from a stream
     * `flush()` may be used instead if a particular page size isn't important.
     *
     * This method can be used to verify that all packets have been flushed.
     * If the return value is 0, all packets have been placed into a page.
     * Generally speaking, it should be called in a loop until all packets are flushed,
     * since even a single packet may span multiple pages.
     *
     * @param page The remaining packets in the stream will be placed into this page, if any remain.
     * @param nFill Packet data watermark in bytes
     * @return   0 all packet data has already been flushed into pages, and there are no packets to put into the page
     *             A Stream has been cleared explicitly or implicitly due to an internal error;
     *        else remaining packets have successfully been flushed into the page
     */
    public int flushFill(Page page, int nFill) {
        return flushInternal(page, 1, nFill);
    }

}
