/*
 * Copyright (c) 2008 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */


import java.io.File;
import java.util.concurrent.CountDownLatch;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;


/**
 * Test.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 080701 nsano initial version <br>
 */
public class Test {

    /**
     * @param args 0: midi
     */
    public static void main(String[] args) throws Exception {
        File file = new File(args[0]);

        Sequence sequence = MidiSystem.getSequence(file);
System.err.println("sequence: " + sequence);

        Sequencer sequencer = MidiSystem.getSequencer(true);
System.err.println("sequencer: " + sequencer);

        Synthesizer synthesizer = MidiSystem.getSynthesizer();
System.err.println("synthesizer: " + synthesizer);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        MetaEventListener mel = new MetaEventListener() {
            public void meta(MetaMessage meta) {
System.err.println("META: " + meta.getType());
                if (meta.getType() == 47) {
                    countDownLatch.countDown();
                }
            }
        };

        sequencer.open();
        sequencer.setSequence(sequence);
        sequencer.addMetaEventListener(mel);
        sequencer.start();
System.err.println("START");
        countDownLatch.await();
System.err.println("END");
        sequencer.stop();
        sequencer.removeMetaEventListener(mel);
        sequencer.close();
    }
}

/* */
