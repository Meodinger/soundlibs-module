package ink.meodinger.rhythm.sampled.mixer;

import javax.sound.sampled.Mixer;
import javax.sound.sampled.spi.MixerProvider;


/**
 * Author: Meodinger
 * Date: 2021/11/13
 * Have fun with my code!
 */

public class RMixerProvider extends MixerProvider {
    @Override
    public Mixer.Info[] getMixerInfo() {
        return new Mixer.Info[0];
    }

    @Override
    public Mixer getMixer(Mixer.Info info) {
        return null;
    }
}
