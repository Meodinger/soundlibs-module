package ink.meodinger.rhythm.sampled.file;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileReader;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;


/**
 * Author: Meodinger
 * Date: 2021/11/13
 * Have fun with my code!
 */

public abstract class RAudioFileReader extends AudioFileReader {

    private int rMarkLimit;
    private boolean rRereading;

    protected RAudioFileReader(int rMarkLimit, boolean rRereading) {
        this.rMarkLimit = rMarkLimit;
        this.rRereading = rRereading;
    }

    protected RAudioFileReader(int rMarkLimit) {
        this(rMarkLimit, false);
    }

    // ----- Methods ----- //

    /**
     * Get an AudioFileFormat (internal implementation).
     * Subclasses must implement this method in a way specific to the file format they handle.
     * Note that depending on the implementation of this method, you should or should not
     * override getAudioInputStream(InputStream, long), too (see comment there).
     *
     * @param inputStream The InputStream to read from.
     *                    It should be tested if it is mark-able.
     *                    If not, and it is re-reading, wrap it into a BufferedInputStream
     *                    with getMarkLimit() size.
     * @param fileLengthInBytes The size of the originating file, if known.
     *                          If it isn't known, AudioSystem.NOT_SPECIFIED should be passed.
     *                          This value may be used for byteLength in AudioFileFormat,
     *                          if this value can't be derived from the information
     *                          in the file header.
     * @return an AudioFileFormat instance containing information from the
     *         header of the stream passed in as inputStream.
     * @throws UnsupportedAudioFileException when unsupported audio format encountered.
     * @throws IOException when I/O failed.
     */
    protected abstract AudioFileFormat getAudioFileFormat(InputStream inputStream, long fileLengthInBytes) throws UnsupportedAudioFileException, IOException;

    /**
     * Get an AudioInputStream (internal implementation).
     * This implementation calls getAudioFileFormat() with the same arguments as passed in here.
     * Then, it constructs an AudioInputStream instance.
     * This instance takes the passed inputStream in the state it is left after
     * getAudioFileFormat() did its work.
     * In other words, the implementation here assumes that getAudioFileFormat() reads the entire
     * header up to a position exactly where the audio data starts.
     * If this can't be realized for a certain format, this method should be overridden.
     *
     * @param inputStream The InputStream to read from.
     *                    It should be tested if it is mark-able.
     *                    If not, and it is re-reading, wrap it into a BufferedInputStream
     *                    with getMarkLimit() size.
     * @param fileLengthInBytes The size of the originating file, if known.
     *                           If it isn't known, AudioSystem.NOT_SPECIFIED should be passed.
     *                           This value may be used for byteLength in AudioFileFormat,
     *                           if this value can't be derived from the information
     *                           in the file header.
     * @return an AudioInputStream instance containing audio data.
     * @throws UnsupportedAudioFileException when unsupported audio format encountered.
     * @throws IOException when I/O failed.
     */
    protected AudioInputStream getAudioInputStream(InputStream inputStream, long fileLengthInBytes) throws UnsupportedAudioFileException, IOException {
        if (rRereading) {
            if (!inputStream.markSupported()) inputStream = new BufferedInputStream(inputStream, rMarkLimit);
            inputStream.mark(rMarkLimit);
        }

        AudioFileFormat audioFileFormat = getAudioFileFormat(inputStream, fileLengthInBytes);
        if (rRereading) {
            inputStream.reset();
        }

        return new AudioInputStream(inputStream, audioFileFormat.getFormat(), audioFileFormat.getFrameLength());
    }

    // ----- SPI ----- //

    /**
     * Get an AudioFileFormat object for an InputStream.
     * This method calls getAudioFileFormat(InputStream, long).
     * Subclasses should not override this method unless there are really severe reasons.
     * Normally, it is sufficient to implement getAudioFileFormat(InputStream, long).

     * @param inputStream the InputStream to read from.
     * @return an AudioFileFormat instance containing information from the header of the stream passed in.
     * @throws UnsupportedAudioFileException when unsupported audio format encountered.
     * @throws IOException when I/O failed.
     */
    @Override
    public AudioFileFormat getAudioFileFormat(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        final long fileLengthInBytes = AudioSystem.NOT_SPECIFIED;

        if (!inputStream.markSupported()) inputStream = new BufferedInputStream(inputStream, rMarkLimit);
        inputStream.mark(rMarkLimit);

        AudioFileFormat audioFileFormat;
        try {
            audioFileFormat = getAudioFileFormat(inputStream, fileLengthInBytes);
        } finally {
            // Required semantics is unclear:
            // should reset() be executed only when there is an exception,
            // or should it be done always?
            inputStream.reset();
        }

        return audioFileFormat;
    }

    /**
     * Get an AudioFileFormat object for an InputStream.
     * This method calls getAudioFileFormat(InputStream, long).
     * Subclasses should not override this method unless there are really severe reasons.
     * Normally, it is sufficient to implement getAudioFileFormat(InputStream, long).

     * @param url the URL to read from.
     * @return an AudioFileFormat instance containing information from the header of the stream passed in.
     * @throws UnsupportedAudioFileException when unsupported audio format encountered.
     * @throws IOException when I/O failed.
     */
    @Override
    public AudioFileFormat getAudioFileFormat(URL url) throws UnsupportedAudioFileException, IOException {
        final long fileLengthInBytes = getDataLength(url);
        final InputStream inputStream = url.openStream();

        AudioFileFormat audioFileFormat;
        try {
            audioFileFormat = getAudioFileFormat(inputStream, fileLengthInBytes);
        } finally {
            inputStream.close();
        }

        return audioFileFormat;
    }

    /**
     * Get an AudioFileFormat object for an InputStream.
     * This method calls getAudioFileFormat(InputStream, long).
     * Subclasses should not override this method unless there are really severe reasons.
     * Normally, it is sufficient to implement getAudioFileFormat(InputStream, long).

     * @param file the File to read from.
     * @return an AudioFileFormat instance containing information from the header of the stream passed in.
     * @throws UnsupportedAudioFileException when unsupported audio format encountered.
     * @throws IOException when I/O failed.
     */
    @Override
    public AudioFileFormat getAudioFileFormat(File file) throws UnsupportedAudioFileException, IOException {
        final long fileLengthInBytes = file.length();
        final InputStream inputStream = new FileInputStream(file);

        AudioFileFormat audioFileFormat;
        try {
            audioFileFormat = getAudioFileFormat(inputStream, fileLengthInBytes);
        } finally {
            inputStream.close();
        }

        return audioFileFormat;
    }

    /**
     * Get an AudioInputStream object for a file.
     * This method calls getAudioInputStream(InputStream, long).
     * Subclasses should not override this method unless there are really severe reasons.
     * Normally, it is sufficient to implement getAudioFileFormat(InputStream, long)
     * and perhaps override getAudioInputStream(InputStream, long).
     *
     *  @param inputStream the InputStream to read from.
     * @return an AudioInputStream instance containing the audio data from this file.
     * @throws UnsupportedAudioFileException when unsupported audio format encountered.
     * @throws IOException when I/O failed.
     */
    @Override
    public AudioInputStream getAudioInputStream(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        final long fileLengthInBytes = AudioSystem.NOT_SPECIFIED;

        if (!inputStream.markSupported()) inputStream = new BufferedInputStream(inputStream, rMarkLimit);
        inputStream.mark(rMarkLimit);

        AudioInputStream audioInputStream;
        try {
            audioInputStream = getAudioInputStream(inputStream, fileLengthInBytes);
        } catch (UnsupportedAudioFileException | IOException e) {
            try {
                inputStream.reset();
            } catch (IOException _e) {
                if (_e.getCause() == null) {
                    _e.initCause(e);
                    throw _e;
                }
            }
            throw e;
        }
        return audioInputStream;
    }

    /**
     * Get an AudioInputStream object for a file.
     * This method calls getAudioInputStream(InputStream, long).
     * Subclasses should not override this method unless there are really severe reasons.
     * Normally, it is sufficient to implement getAudioFileFormat(InputStream, long)
     * and perhaps override getAudioInputStream(InputStream, long).
     *
     * @param url the URL to read from.
     * @return an AudioInputStream instance containing the audio data from this file.
     * @throws UnsupportedAudioFileException when unsupported audio format encountered.
     * @throws IOException when I/O failed.
     */
    @Override
    public AudioInputStream getAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        final long fileLengthInBytes = getDataLength(url);
        final InputStream inputStream = url.openStream();

        AudioInputStream audioInputStream;
        try {
            audioInputStream = getAudioInputStream(inputStream, fileLengthInBytes);
        } catch (UnsupportedAudioFileException | IOException e) {
            inputStream.close();
            throw e;
        }
        return audioInputStream;
    }

    /**
     * Get an AudioInputStream object for a file.
     * This method calls getAudioInputStream(InputStream, long).
     * Subclasses should not override this method unless there are really severe reasons.
     * Normally, it is sufficient to implement getAudioFileFormat(InputStream, long)
     * and perhaps override getAudioInputStream(InputStream, long).
     *
     * @param file the File to read from.
     * @return an AudioInputStream instance containing the audio data from this file.
     * @throws UnsupportedAudioFileException when unsupported audio format encountered.
     * @throws IOException when I/O failed.
     */
    @Override
    public AudioInputStream getAudioInputStream(File file) throws UnsupportedAudioFileException, IOException {
        final long fileLengthInBytes = file.length();
        final InputStream inputStream = new FileInputStream(file);

        AudioInputStream audioInputStream;
        try {
            audioInputStream = getAudioInputStream(inputStream, fileLengthInBytes);
        } catch (UnsupportedAudioFileException | IOException e) {
            inputStream.close();
            throw e;
        }
        return audioInputStream;
    }

    // ----- Static ----- //

    private static long getDataLength(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.connect();

        int length = connection.getContentLength();
        if (length > 0) return length;
        else return AudioSystem.NOT_SPECIFIED;
    }

    protected static int calculateFrameSize(int sampleSize, int channelsNum) {
        return ((sampleSize + 7) / 8) * channelsNum;
    }

    public static int readLittleEndianInt(InputStream is) throws IOException {
        final int b0 = is.read();
        final int b1 = is.read();
        final int b2 = is.read();
        final int b3 = is.read();

        if ((b0 | b1 | b2 | b3) < 0) throw new EOFException();
        return (b3 << 24) + (b2 << 16) + (b1 << 8) + (b0);
    }
    public static short readLittleEndianShort(InputStream is) throws IOException {
        final int b0 = is.read();
        final int b1 = is.read();

        if ((b0 | b1) < 0) throw new EOFException();
        return (short) ((b1 << 8) + b0);
    }
    public static double readIeeeExtended(DataInputStream dis) throws IOException {
        /*
         * Copyright (C) 1988-1991 Apple Computer, Inc.
         * All rights reserved.
         *
         * Machine-independent I/O routines for IEEE floating-point numbers.
         *
         * NaN's and infinities are converted to HUGE_VAL or HUGE, which
         * happens to be infinity on IEEE machines.  Unfortunately, it is
         * impossible to preserve NaN's in a machine-independent way.
         * Infinities are, however, preserved on IEEE machines.
         *
         * These routines have been tested on the following machines:
         *    Apple Macintosh, MPW 3.1 C compiler
         *    Apple Macintosh, THINK C compiler
         *    Silicon Graphics IRIS, MIPS compiler
         *    Cray X/MP and Y/MP
         *    Digital Equipment VAX
         *
         *
         * Implemented by Malcolm Slaney and Ken Turkowski.
         *
         * Malcolm Slaney contributions during 1988-1990 include big- and little-
         * endian file I/O, conversion to and from Motorola's extended 80-bit
         * floating-point format, and conversions to and from IEEE single-
         * precision floating-point format.
         *
         * In 1991, Ken Turkowski implemented the conversions to and from
         * IEEE double-precision format, added more precision to the extended
         * conversions, and accommodated conversions involving +/- infinity,
         * NaN's, and denormalized numbers.
         */
        double HUGE = 3.4028234663852886E+038D;
        double f;
        long t1, t2;

        int exp = dis.readUnsignedShort();

        t1 = dis.readUnsignedShort();
        t2 = dis.readUnsignedShort();
        long high = (t1 << 16) | t2;

        t1 = dis.readUnsignedShort();
        t2 = dis.readUnsignedShort();
        long low = (t1 << 16) | t2;

        if(exp == 0 && high == 0L && low == 0L) {
            f = 0.0D;
        } else {
            if(exp == 32767) {
                f = HUGE;
            } else {
                exp -= 16383;
                exp -= 31;
                f = high * Math.pow(2D, exp);
                exp -= 32;
                f += low * Math.pow(2D, exp);
            }
        }

        return f;
    }
}
