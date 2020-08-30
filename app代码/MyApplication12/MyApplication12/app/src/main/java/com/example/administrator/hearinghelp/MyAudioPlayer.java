package com.example.administrator.hearinghelp;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by John.Doe on 2020/3/26.
 */

public class MyAudioPlayer {
    private AudioTrack audioTrack;

    private final int SAMPLERATE = 44100;
    private final int BUFFERSIZE = SAMPLERATE * 2;
    private final int LEFTCHANNEL = 0;
    private final int RIGHTCHANNNEL = 1;

    private short wave[];

    public MyAudioPlayer() {
        audioTrack = new AudioTrack(AudioManager.USE_DEFAULT_STREAM_TYPE,
                SAMPLERATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                BUFFERSIZE,
                AudioTrack.MODE_STREAM);
    }

    public void playPureTone(int channel, int hertz, int decibel) {
        audioTrack.play();
        setChannel(channel);
        setAduioWave(hertz, decibel);
        audioTrack.write(wave, 0, BUFFERSIZE);
    }

    public void stopPureTone() {
        if (audioTrack != null)
            audioTrack.stop();
    }

    private void setChannel(int channel) {
        if (channel == LEFTCHANNEL)
            audioTrack.setStereoVolume(1.0f, 0.0f);
        else if (channel == RIGHTCHANNNEL)
            audioTrack.setStereoVolume(0.0f, 1.0f);
    }

    private void setAduioWave(int hertz, int decibel) {
        final double TWOPI = 3.1416 * 2;
        final int waveLength = BUFFERSIZE;
        short height = (short) Math.pow(10.0, (double) decibel / 20.0);
        int wavePeriod = SAMPLERATE / hertz;
        wave = new short[waveLength];

        for (int i = 0; i < waveLength; i++) {
            wave[i] = (short) (height * (1 - Math.sin(TWOPI * (double) (i % wavePeriod) / (double) wavePeriod)) / 2);
        }
    }
}
