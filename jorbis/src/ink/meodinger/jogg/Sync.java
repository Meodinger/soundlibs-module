package ink.meodinger.jogg;

/**
 * Author: Meodinger
 * Date: 2021/10/27
 * Have fun with my code!
 */

public class Sync {

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

    // ----- Sync related ----- //

    /**
     * Initialize a `Sync` struct to a known initial value
     * in preparation for manipulation of an Ogg bitstream.
     * The `Sync` is important when decoding,
     * as it synchronizes retrieval and return of data.
     */
    public void init() {
        this.storage = -1;
        clear();
    }

    /**
     * Free the internal storage of SyncState and reset the struct to the initial state.
     * To free the entire struct, destroy() should be used instead;
     * (In java simply set syncState = null is enough, so we don't have method destroy())
     * In situations where the struct needs to be reset but the
     * internal storage does not need to be freed, reset() should be used.
     * Will not return 0 as libogg said.
     */
    public void clear() {
        this.data = null;
        this.storage = 0;
        this.fill = 0;
        this.returned = 0;

        this.unSynced = 0;
        this.headerBytes = 0;
        this.bodyBytes = 0;
    }

    /**
     * Reset the internal counters of the SyncState to initial values.
     * It is a good idea to call this before seeking within a bitstream.
     * Will not return 0 as libogg said.
     */
    public void reset() {
        // if (check() != 0) return;

        this.fill = 0;
        this.returned = 0;

        this.unSynced = 0;
        this.headerBytes = 0;
        this.bodyBytes = 0;
    }

    /**
     * Check the error or readiness condition of a `Sync`.
     *
     * It is a safe practice to ignore unrecoverable errors
     * (such as an internal error) returned by ogg stream
     * synchronization calls. Should an internal error occur,
     * the `Sync` will be cleared (equivalent to a call to `clear()`)
     * and subsequent calls using this `Sync` will be noops.
     *
     * Error detection is then handled via a single call to `check()`
     * at the end of the operational block.
     *
     * @return 0 is returned if the ogg_sync_state structure is initialized and ready.
     *         Else is returned if the structure was never initialized, or if an unrecoverable
     *         internal error occurred in a previous call using the passed in `Sync`.
     */
    @Deprecated(forRemoval = true)
    public int check() {
        return 0;
    }

    /**
     * Destroy an `Sync` and free all memory used.
     *
     * Note this calls free() on its argument,
     * so you should only use this function if you've allocated the `Sync` on the heap.
     * If it is allocated on the stack, or it will otherwise be freed
     * by your own code, use `Sync` instead to release just the internal memory.
     *
     * @return 0
     */
    @Deprecated(forRemoval = true)
    public int destroy() {
        clear();
        return 0;
    }

    public int dataOffset() {
        return this.returned;
    }

    public int bufferOffset() {
        return this.fill;
    }

    // ----- Buffer related ----- //

    /**
     * Provide a properly-sized buffer for writing
     *
     * Buffer space which has already been returned is cleared,
     * and the buffer is extended as necessary by the size plus some additional bytes
     * Within the current implementation, an extra 4096 bytes are allocated,
     * but applications should not rely on this additional buffer space
     *
     * The buffer allocated by this function is the internal storage of `Sync`,
     * beginning at the fill mark within the struct.
     *
     * @param size Wanted buffer size.
     *             The actual size of the buffer returned will be
     *             this size plus some extra bytes (currently 4096).
     * @return Next segment pointer; a segment at least as large as requested at the fill mark
     */
    public int buffer(int size) {
        // if (check() != 0) return -1;

        // First, clear out any space that has been previously returned
        if (this.returned != 0) {
            this.fill -= this.returned;
            if(this.fill > 0) {
                System.arraycopy(this.data, this.returned, this.data, 0, this.fill);
            }
            this.returned = 0;
        }

        // Then, check if internal buffer should be extended
        if(size > this.storage - this.fill){
            // An extra page would be nice

            // if (size > Integer.MAX_VALUE - 4096 - this.fill) { /* err */  }
            final int newsize = size + this.fill + 4096;
            if(this.data != null) {
                byte[] temp = new byte[newsize];
                System.arraycopy(this.data, 0, temp, 0, this.data.length);
                this.data = temp;
            } else {
                this.data = new byte[newsize];
            }
            this.storage = newsize;
        }

        return this.fill;
    }

    /**
     * Tell the SyncState how many bytes we wrote into the buffer
     *
     * The general procedure is to request a properly-sized buffer by calling buffer()
     * The buffer is then filled up to the requested size with new input,
     * and wrote() is called to advance the fill pointer by however much data was actually available
     *
     * @param bytes Number of bytes of new data written
     * @return -1 when overflows, or internal error occurred; 0 otherwise
     */
    public int wrote(int bytes) {
        // if (check() != 0) return -1;

        if (this.fill + bytes > this.storage) return -1;

        this.fill += bytes;
        return 0;
    }

    // ----- Page related ----- //

    private final Page pageSeeked = new Page();
    private final byte[] checksum = new byte[4];

    /**
     * Sync the stream
     * This is meant to be useful for finding page boundaries
     *
     * @param p Destination page
     * @return -n Skipped n bytes
     *          0 Page not ready; more data (no bytes skipped)
     *          n Page synced at current location; page length n bytes
     */
    public int pageSeek(Page p) {
        // if (check() != 0) return -1;

        int nextPagePointer = 0;
        final int pagePointer = this.returned;
        final int pageBytes = this.fill - this.returned;

        if (this.headerBytes == 0) {
            // not enough for a header
            if (pageBytes < 27) return 0;

            // verify capture pattern
            if (this.data[pagePointer] != 'O'
             || this.data[pagePointer + 1] != 'g'
             || this.data[pagePointer + 2] != 'g'
             || this.data[pagePointer + 3] != 'S'
            ) {
                // Same code occurred around line No.233
                // For simpleness, we don't make these lines another method
                // For constancy, we need assign headerBytes with 0
                // noinspection ConstantConditions
                this.headerBytes = 0;
                this.bodyBytes = 0;

                // search for possible capture
                for (int i = 0; i < pageBytes - 1; i++) {
                    if (this.data[pagePointer + 1 + i] == 'O') {
                        nextPagePointer = pagePointer + 1 + i;
                        break;
                    }
                }
                if (nextPagePointer == 0) nextPagePointer = this.fill;

                this.returned = nextPagePointer;
                return (-(nextPagePointer - pagePointer));
            }

            int headerBytes = (this.data[pagePointer + 26] & 0xff) + 27;

            // not enough for header + seg table
            if (pageBytes < headerBytes) return 0;

            // count up body length in the segment table
            for(int i = 0; i < (this.data[pagePointer + 26] & 0xff); i++){
                this.bodyBytes += (this.data[pagePointer + 27 + i] & 0xff);
            }
            this.headerBytes = headerBytes;
        }

        if (this.bodyBytes + this.headerBytes > pageBytes) return 0;

        // The whole test page is buffered, verify the checksum
        synchronized (this.checksum) {
            // Grab the checksum bytes, set the header field to zero
            System.arraycopy(this.data, pagePointer + 22, this.checksum, 0, 4);
            this.data[pagePointer + 22] = 0;
            this.data[pagePointer + 23] = 0;
            this.data[pagePointer + 24] = 0;
            this.data[pagePointer + 25] = 0;

            // set up a temp page struct and recompute the checksum
            Page temp = this.pageSeeked;
            temp.headerBase = this.data;
            temp.headerPointer = pagePointer;
            temp.headerBytes = this.headerBytes;
            temp.bodyBase = this.data;
            temp.bodyPointer = pagePointer + this.headerBytes;
            temp.bodyBytes = this.bodyBytes;
            temp.checksum();

            // compare
            if (this.checksum[0] != this.data[pagePointer + 22] || this.checksum[1] != this.data[pagePointer + 23] ||
                this.checksum[2] != this.data[pagePointer + 24] || this.checksum[3] != this.data[pagePointer + 25]) {
                // D'oh, mismatch! Corrupt page (or mis-capture and not a page at all)
                // Replace the computed checksum with the one actually read in
                System.arraycopy(this.checksum, 0, this.data, pagePointer + 22, 4);

                // Bad checksum. Lose sync
                this.headerBytes = 0;
                this.bodyBytes = 0;

                // search for possible capture
                for (int i = 0; i < pageBytes - 1; i++) {
                    if (this.data[pagePointer + 1 + i] == 'O') {
                        nextPagePointer = pagePointer + 1 + i;
                        break;
                    }
                }
                if (nextPagePointer == 0) nextPagePointer = this.fill;

                this.returned = nextPagePointer;
                return (-(nextPagePointer - pagePointer));
            }
        }

        // Yeah, have a whole page all ready to go

        // int newPage = returned -> pointer = page
        if(p != null){
            p.headerBase = this.data;
            p.headerPointer = this.returned;
            p.headerBytes = this.headerBytes;
            p.bodyBase = this.data;
            p.bodyPointer = this.returned + this.headerBytes;
            p.bodyBytes = this.bodyBytes;
        }

        int newBytes = this.headerBytes + this.bodyBytes;

        this.unSynced = 0;
        this.returned += newBytes;
        this.headerBytes = 0;
        this.bodyBytes = 0;

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
        // if (check() != 0) return 0;

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
