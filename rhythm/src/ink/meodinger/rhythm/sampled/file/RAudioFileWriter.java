package ink.meodinger.rhythm.sampled.file;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.spi.AudioFileWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Author: Meodinger
 * Date: 2021/11/13
 * Have fun with my code!
 */

public class RAudioFileWriter extends AudioFileWriter {
    @Override
    public AudioFileFormat.Type[] getAudioFileTypes() {
        return new AudioFileFormat.Type[0];
    }

    @Override
    public AudioFileFormat.Type[] getAudioFileTypes(AudioInputStream stream) {
        return new AudioFileFormat.Type[0];
    }

    @Override
    public int write(AudioInputStream stream, AudioFileFormat.Type fileType, OutputStream out) throws IOException {
        return 0;
    }

    @Override
    public int write(AudioInputStream stream, AudioFileFormat.Type fileType, File out) throws IOException {
        return 0;
    }
}
