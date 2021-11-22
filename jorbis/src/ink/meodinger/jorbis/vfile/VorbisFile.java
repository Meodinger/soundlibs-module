package ink.meodinger.jorbis.vfile;

import ink.meodinger.jogg.*;
import ink.meodinger.jorbis.codec.*;

import java.io.File;

/**
 * Author: Meodinger
 * Date: 2021/11/22
 * Have fun with my code!
 */

public class VorbisFile {

    private Object dataSource;
    private int seekable;
    private long offset;
    private long end;
    private Sync oSync;

    // If the FILE handle isn't seekable (eg, a pipe), only the current stream appears
    private int links;
    private long[] offsets;
    private long[] dataOffsets;
    private long[] serialNos;
    private long[] pcmLengths;
    // ^ overloaded to maintain binary compatibility; x2 size, stores both beginning and end values

    private Info vInfo;
    private Comment vComment;

    // Decoding working state local storage
    private long pcmOffset;
    private int readyState;
    private long currentSerialNo;
    private long currentLink;

    private double bitTrack;
    private double sampleTrack;

    /**
     * Take physical pages, weld into a logical stream of packets
     */
    private Stream oStream;
    /**
     * Central working state for the packet -> PCM decoder
     */
    private Dsp vDsp;
    /**
     * Local working space for packet->PCM decode
     */
    private Block vBlock;

    // callbacks

    // ----- Methods ----- //

    public int clear() {
        return 0;
    }

    public int open(String path) {
        return 0;
    }
    public int open(File file) {
        return 0;
    }
    public int open(Object dataSource, String initial, long bytes /*, callbacks */) {
        return 0;
    }

    public int testOpen(File file, String initial, long bytes) {
        return 0;
    }
    public int testOpen() {
        return 0;
    }
    public int testOpen(Object dataSource, String initial, long bytes /*, callbacks*/) {
        return 0;
    }

    public long bitrate() {
        return 0;
    }
    public long bitrate(int i) {
        return 0;
    }
    public long streams() {
        return 0;
    }
    public long seekable() {
        return 0;
    }
    public long serialNo(int i) {
        return 0;
    }

    public long rawTotal(int i) {
        return 0;
    }
    public long pcmTotal(int i) {
        return 0;
    }
    public double timeTotal(int i) {
        return 0;
    }

    public int rawSeek(long pos) {
        return 0;
    }
    public int pcmSeek(long pos) {
        return 0;
    }
    public int pcmSeekPage(long pos) {
        return 0;
    }
    public int timeSeek(long pos) {
        return 0;
    }
    public int timeSeekPage(long pos) {
        return 0;
    }

    public int rawSeekLap(long pos) {
        return 0;
    }
    public int pcmSeekLap(long pos) {
        return 0;
    }
    public int pcmSeekPageLap(long pos) {
        return 0;
    }
    public int timeSeekLap(long pos) {
        return 0;
    }
    public int timeSeekPgeLap(long pos) {
        return 0;
    }

    public long rawTell() {
        return 0;
    }
    public long pcmTell() {
        return 0;
    }
    public double timeTell() {
        return 0;
    }

    public Info info(int link) {
        return null;
    }
    public Comment comment(int link) {
        return null;
    }

    public long read(byte[] buffer, int length, int bigEndian, int word, int signed, int[] bitStream) {
        return 0;
    }
    public long readFilter(byte[] buffer, int length, int bigEndian, int word, int signed, int[] bitStream) {
        return 0;
    }
    public long readFloat(float[][][] pcmChannels, int samples, int[] bitStream) {
        return 0;
    }

    public int crossLap(VorbisFile another) {
        return 0;
    }

    public int halfRate(int flag) {
        return 0;
    }
    public int halfRate() {
        return 0;
    }

}
