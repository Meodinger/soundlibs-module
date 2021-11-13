package ink.meodinger.rhythm.midi.file;

import javax.sound.midi.Sequence;
import javax.sound.midi.spi.MidiFileWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Author: Meodinger
 * Date: 2021/11/13
 * Have fun with my code!
 */

public class RMidiFileWriter extends MidiFileWriter {
    @Override
    public int[] getMidiFileTypes() {
        return new int[0];
    }

    @Override
    public int[] getMidiFileTypes(Sequence sequence) {
        return new int[0];
    }

    @Override
    public int write(Sequence in, int fileType, OutputStream out) throws IOException {
        return 0;
    }

    @Override
    public int write(Sequence in, int fileType, File out) throws IOException {
        return 0;
    }
}
