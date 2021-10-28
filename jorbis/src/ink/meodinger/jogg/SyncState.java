package ink.meodinger.jogg;

/**
 * Author: Meodinger
 * Date: 2021/10/27
 * Have fun with my code!
 */

public class SyncState {

    /**
     * Buffered stream data.
     */
    public byte[] data = null;

    /**
     * Current allocated size of the stream buffer held in data
     */
    private int storage = 0;
    /**
     * The number of valid bytes currently held in data
     * It functions as the buffer head pointer
     */
    private int fill = 0;
    /**
     * The number of bytes at the head of data that have already been returned as pages
     * It functions as the buffer tail pointer
     */
    private int returned = 0;

    /**
     * Synchronization state flag
     * Nonzero if sync has not yet been attained or has been lost
     */
    private int unSynced = 0;
    /**
     * If synced, the number of bytes used by the synced page's header
     */
    private int headerBytes = 0;
    /**
     * If synced, the number of bytes used by the synced page's body
     */
    private int bodyBytes = 0;

    // ----- ----- //

    /**
     * Free the internal storage of SyncState and reset the struct to the initial state
     *
     * To free the entire struct, destroy() should be used instead;
     * (In java simply set syncState = null is enough, so we don't have method destroy())
     *
     * In situations where the struct needs to be reset but the
     * internal storage does not need to be freed, reset() should be used.
     *
     * Will not return 0 as libogg said
     */
    public void clear() {
        data = null;
    }

    /**
     * Reset the internal counters of the SyncState to initial values
     *
     * It is a good idea to call this before seeking within a bitstream
     *
     * Will not return 0 as libogg said
     */
    public void reset() {
        fill = 0;
        returned = 0;

        unSynced = 0;
        headerBytes = 0;
        bodyBytes = 0;
    }

    public int getDataOffset() {
        return returned;
    }

    public int getBufferOffset() {
        return fill;
    }

    // ----- ----- //

    /**
     * Provide a properly-sized buffer for writing
     *
     * Buffer space which has already been returned is cleared,
     * and the buffer is extended as necessary by the size plus some additional bytes
     * Within the current implementation, an extra 4096 bytes are allocated,
     * but applications should not rely on this additional buffer space
     *
     * The buffer allocated by this function is the internal storage of SyncState,
     * beginning at the fill mark within the struct.
     *
     * @param size Wanted buffer size
     * @return New storage the internal buffer could hold
     */
    public int buffer(int size) {
        // First, clear out any space that has been previously returned
        if (returned != 0) {
            fill -= returned;
            if( fill > 0) {
                System.arraycopy(data, returned, data, 0, fill);
            }
            returned = 0;
        }

        // Then, check if internal buffer should be extended
        if(size > storage - fill){
            // An extra page would be nice
            final int newsize = size + fill + 4096;
            if(data != null) {
                byte[] temp = new byte[newsize];
                System.arraycopy(data, 0, temp, 0, data.length);
                data = temp;
            } else {
                data = new byte[newsize];
            }
            storage = newsize;
        }

        return fill;
    }

    /**
     * Tell the SyncState how many bytes we wrote into the buffer
     *
     * The general procedure is to request a properly-sized buffer by calling buffer()
     * The buffer is then filled up to the requested size with new input,
     * and wrote() is called to advance the fill pointer by however much data was actually available
     *
     * @param bytes Number of bytes of new data written
     * @return -1 when overflows; 0 otherwise
     */
    public int wrote(int bytes) {
        if (fill + bytes > storage) return -1;

        fill += bytes;
        return 0;
    }

    // ----- ----- //

    private final Page pageSeeked = new Page();
    private final byte[] checksum = new byte[4];

    /**
     * Sync the stream
     * This is meant to be useful for finding page boundaries
     *
     * @param p Destination page
     * @return -n: skipped n bytes
     *          0: page not ready; more data (no bytes skipped)
     *          n: page synced at current location; page length n bytes
     */
    public int pageSeek(Page p) {
        final int page = returned;
        final int bytes = fill - returned;

        if (headerBytes == 0) {
            // not enough for a header
            if (bytes < 27) return 0;

            // verify capture pattern
            if (data[page] != 'O' || data[page + 1] != 'g' || data[page + 2] != 'g' || data[page + 3] != 'S') {
                int next = 0;

                // Same code occurred around line No.233
                // For simpleness, we don't make these lines another method
                // For constancy, we need assign headerBytes with 0
                headerBytes = 0;
                bodyBytes = 0;

                // search for possible capture
                for (int i = 0; i < bytes - 1; i++) {
                    if (data[page + 1 + i] == 'O') {
                        next = page + 1 + i;
                        break;
                    }
                }
                if (next == 0) next = fill;

                returned = next;
                return (-(next - page));
            }

            int _headerBytes = (data[page + 26] & 0xff) + 27;

            // not enough for header + seg table
            if (bytes < _headerBytes) return 0;

            // count up body length in the segment table
            for(int i = 0; i < (data[page + 26] & 0xff); i++){
                bodyBytes += (data[page + 27 + i] & 0xff);
            }
            headerBytes = _headerBytes;
        }

        if (bodyBytes + headerBytes > bytes) return 0;

        // The whole test page is buffered, verify the checksum
        synchronized (checksum) {
            // Grab the checksum bytes, set the header field to zero
            System.arraycopy(data, page + 22, checksum, 0, 4);
            data[page + 22] = 0;
            data[page + 23] = 0;
            data[page + 24] = 0;
            data[page + 25] = 0;

            // set up a temp page struct and recompute the checksum
            Page log = pageSeeked;
            log.headerBase = data;
            log.headerPointer = page;
            log.headerBytes = headerBytes;
            log.bodyBase = data;
            log.bodyPointer = page + headerBytes;
            log.bodyBytes = bodyBytes;
            log.checksum();

            // compare
            if (checksum[0] != data[page + 22] || checksum[1] != data[page + 23] ||
                checksum[2] != data[page + 24] || checksum[3] != data[page + 25]) {
                // D'oh, mismatch! Corrupt page (or mis-capture and not a page at all)
                // Replace the computed checksum with the one actually read in
                System.arraycopy(checksum, 0, data, page + 22, 4);

                // Bad checksum. Lose sync
                int next = 0;

                headerBytes = 0;
                bodyBytes = 0;

                // search for possible capture
                for (int i = 0; i < bytes - 1; i++) {
                    if (data[page + 1 + i] == 'O') {
                        next = page + 1 + i;
                        break;
                    }
                }
                if (next == 0) next = fill;

                returned = next;
                return (-(next - page));
            }
        }

        // Yeah, have a whole page all ready to go
        // int newPage = returned -> pointer = page
        if(p != null){
            p.headerBase = data;
            p.headerPointer = returned;
            p.headerBytes = headerBytes;
            p.bodyBase = data;
            p.bodyPointer = returned + headerBytes;
            p.bodyBytes = bodyBytes;
        }

        int newBytes = headerBytes + bodyBytes;
        returned += (headerBytes + bodyBytes);
        unSynced = 0;
        headerBytes = 0;
        bodyBytes = 0;

        return newBytes;
    }

    /**
     * Sync the stream and get a page
     * Keep trying until we find a page
     * Suppress 'sync errors' after reporting the first
     *
     * @param p Destination page
     * @return -1: recaptured (hole in data)
     *          0: need more data
     *          1: page returned
     */
    public int pageOut(Page p) {
        // All we need to do is verify a page at the head of the stream buffer
        // If it doesn't verify, we look for the next potential frame

        int ret;
        while (true) {
            ret = pageSeek(p);

            // have a page
            if (ret > 0) return 1;

            // need more data
            if (ret == 0) return 0;

            // head did not start a synced page... skipped some bytes
            if (unSynced == 0) {
                unSynced = 1;
                return -1;
            }
        } // keep looking
    }
}
