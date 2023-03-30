package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.function.Predicate;

public class Messageable implements Runnable {
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;
    private final Predicate<String> predicate;
    private final MessageConsumer consumer;

    public Messageable(BufferedReader bufferedReader, BufferedWriter bufferedWriter,
                       Predicate<String> predicate, MessageConsumer consumer) {
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;
        this.predicate = predicate;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String message = bufferedReader.readLine();

                if (predicate.test(message)) {
                    consumer.accept(message);
                    continue;
                }

                bufferedWriter.write(message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException | NullPointerException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

@FunctionalInterface
interface MessageConsumer {
    void accept(String message) throws IOException;
}
