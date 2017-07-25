package com.m800.actors;


import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.m800.utilities.LoggingMessages;
import com.m800.actors.FileParser.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileScanner extends AbstractActor {

    protected final LoggingAdapter logger = Logging.getLogger(context().system(), this);
    protected final Path fileDirectory =  Paths.get(System.getProperty("user.dir")+"/m800").toAbsolutePath();
    protected ActorRef fileParser;
    private String message;
    private String receivedMessage = "";


    FileScanner(String message, ActorRef fileParser){
        this.message = message;
        this.fileParser = fileParser;

    }
    FileScanner() {
        this.fileParser = getContext().actorOf(Props.create(FileParser.class), "fileParser-" + UUID.randomUUID());
    }

    static public Props props(String message, ActorRef fileParser) {
        return Props.create(FileScanner.class, () -> new FileScanner(message, fileParser));
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Scan.class, message -> {
                    this.receivedMessage = message.name;
                    processScanMessage(message);
                })
                .build();
    }


    private void processScanMessage(Scan message) throws IOException {

        logger.info(message.name);
         // process for all files in the pre-defined directory
        logger.info(LoggingMessages.SCAN_MESSAGE_RECEIVED, message);
        Files.list(fileDirectory)
                .filter(Files::isRegularFile)
                .forEach(file -> processFileWithAggregator(file));

    }

    private void processFileWithAggregator(Path filePath) {

        // send Message  to file parser

        logger.info(LoggingMessages.FILE_SCANNER_FILE_DETECTED, filePath);
        Parse parseMessage = new Parse(filePath);
        fileParser.tell(parseMessage, getSelf());
    }




    static public class Scan {
        public final String name;
        public Scan( ) {
            this.name = "scan";
        }

        @Override
        public String toString( ) {
            return "scan";

        }

    }

}


