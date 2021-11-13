package ink.meodinger.rhythm.midi.soundbank;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.spi.SoundbankReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * Author: Meodinger
 * Date: 2021/11/13
 * Have fun with my code!
 */

public class RSoundbankReader extends SoundbankReader {
    @Override
    public Soundbank getSoundbank(URL url) throws InvalidMidiDataException, IOException {
        return null;
    }

    @Override
    public Soundbank getSoundbank(InputStream stream) throws InvalidMidiDataException, IOException {
        return null;
    }

    @Override
    public Soundbank getSoundbank(File file) throws InvalidMidiDataException, IOException {
        return null;
    }
}
