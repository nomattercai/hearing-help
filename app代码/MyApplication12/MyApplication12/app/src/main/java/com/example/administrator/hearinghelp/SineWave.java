package com.example.administrator.hearinghelp;

/**
 * Created by John.Doe on 2020/3/3.
 */

public class SineWave {

    protected static final int SAMPLERATE = 44100;

    private final double TWOPI = 3.1416 * 2;

    private short HEIGHT = 32767;         // 这次采用16bit的数据

    protected short[] wave;
    protected int wavelen;
    protected int length;

    protected void setSineWave(int frequency, int decible) {
        HEIGHT = (short) Math.pow(10.0, (double) decible / 20.0);  // 声音强度范围为0 - 90dB

        wavelen = SAMPLERATE / frequency;
        length = wavelen * frequency * 2;
        wave = new short[length];

        for (int i = 0; i < length; i++) {
            wave[i] = (short) (HEIGHT * (1 - Math.sin(TWOPI * (double) (i % wavelen) / (double) wavelen)) / 2);
        }
    }
}