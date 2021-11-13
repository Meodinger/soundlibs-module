package ink.meodinger.rhythm.midi.device;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.spi.MidiDeviceProvider;


/**
 * Author: Meodinger
 * Date: 2021/11/13
 * Have fun with my code!
 */

public class RMidiDeviceProvider extends MidiDeviceProvider {
    @Override
    public MidiDevice.Info[] getDeviceInfo() {
        return new MidiDevice.Info[0];
    }

    @Override
    public MidiDevice getDevice(MidiDevice.Info info) {
        return null;
    }
}
