package com.m800.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.m800.events.EndOfFile;
import com.m800.events.Line;
import com.m800.events.StartOfFile;
import com.m800.utilities.Constants;
import com.m800.utilities.LoggingMessages;

import java.io.IOException;
import java.nio.file.Path;

public class Aggregator extends AbstractActor {

    protected final LoggingAdapter logger = Logging.getLogger(context().system(), this);
    protected final Path filePath;
    protected long totalWords;

    private Aggregator(Path filePath) {
        this.filePath = filePath;
    }




    @Override
    public Receive createReceive() {
        return receiveBuilder().
                match(StartOfFile.class, event -> {
                    if (this.filePath.equals(event.targetFile))
                        logger.info(LoggingMessages.EVENT_RECEIVED, event);

                }).
                match(Line.class, event -> {
                    if (this.filePath.equals(event.targetFile))
                        logger.info(LoggingMessages.EVENT_RECEIVED, event);
                    this.aggregateTotalWords(event);

                }).
                match(EndOfFile.class, event -> this.processEndOfFile(event)).
                matchAny(i -> logger.info(LoggingMessages.EVENT_RECEIVED, i))
                .build();
    }

    private void aggregateTotalWords(Line event) {
        // get the words in each and accumulate them
        String line = event.read;
        long lineWords = line.isEmpty() ? 0 : line.split(Constants.SPACE_DELIMETER).length;
        this.totalWords += lineWords;
    }

    private void processEndOfFile(EndOfFile event) {

        logger.info(LoggingMessages.EVENT_RECEIVED, event);
        // display word counts
        logger.info("File " + event.filePath + " has " + totalWords + " words");
        System.out.println("\n\n\n\nFile " + event.filePath + " has " + totalWords + " words");
        System.out.flush();
        shutdownSystem();

    }

    private void shutdownSystem() {
        try {
            getContext().system().terminate();
        } catch (Exception e) {
            logger.info("shutdown !");
        }
    }

}


