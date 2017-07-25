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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileParser extends AbstractActor {

    final LoggingAdapter log = Logging.getLogger(context().system(), this);
    Path filePath;
    ActorRef aggregator;
    long position;
    long lineSequence;
    ByteArrayOutputStream line;
    AsynchronousFileChannel asynchronousFileChannel;
    final CompletionHandler<Integer, ByteBuffer> readCompletionHandler;
    ByteBuffer dst;

    private FileParser() {
        readCompletionHandler = buildCompletionHandler();
        dst = ByteBuffer.allocate(1);
    }

    static public Props props( ) {
        return Props.create(FileParser.class, () -> new FileParser());
    }




    @Override
    public Receive createReceive() {
        return receiveBuilder().
                match(Parse.class, message -> {
                    processParseMessage(message);
                }).
                matchAny(o -> {
                    log.info(LoggingMessages.MESSAGE_NOT_RECOGNIZED, o);
                })
                .build();
    }


    public void publishEndOfFile() {
     try {
         context().system().eventStream().publish(new EndOfFile(this.filePath));
     }catch (Exception e){
         log.info(e.getMessage());
     }
    }

    private void processLine() {
        try {
            String line = String.valueOf(this.line);
            this.line.reset();
            context().system().eventStream().publish(new Line(this.filePath, this.lineSequence, line));
            this.lineSequence += 1;
        }catch (Exception e){
            log.info(e.getMessage());
        }

    }

    private void readNextByte() throws IOException {
        this.dst = ByteBuffer.allocate(1);
        this.asynchronousFileChannel.read(this.dst, this.position, null, this.readCompletionHandler);
    }

    private void processParseMessage(Parse message) {
        log.info(LoggingMessages.PARSE_MESSAGE_RECEIVED, message);
        configureAggregatorDetails(message);
        try {
            //open asynchronous channel
            this.asynchronousFileChannel = AsynchronousFileChannel.open(filePath, StandardOpenOption.READ);
            // process Start of File
            context().system().eventStream().publish(new StartOfFile(this.filePath));
            this.readNextByte();
        } catch (IOException e) {
            log.info(LoggingMessages.PARSE_MESSAGE_ERROR, message, this.filePath, e);
        }

    }

    private void configureAggregatorDetails(Parse message) {
        this.filePath = message.filePath;
        this.aggregator = getContext().actorOf(Props.create(Aggregator.class, this.filePath), "aggregator");
        this.position = 0;
        this.lineSequence = 0;
        this.line = new ByteArrayOutputStream();

        // events
        context().system().eventStream().subscribe(this.aggregator, StartOfFile.class);
        context().system().eventStream().subscribe(this.aggregator, Line.class);
        context().system().eventStream().subscribe(this.aggregator, EndOfFile.class);
    }


    private CompletionHandler<Integer, ByteBuffer> buildCompletionHandler() {
        return new CompletionHandler<Integer, ByteBuffer>() {

            public void completed(Integer result, ByteBuffer target) {
                try {
                    processNext(result);
                } catch (IOException e) {
                    log.info(LoggingMessages.PARSE_MESSAGE_COMPLETION_ERROR, e);
                }
            }

            public void failed(Throwable exception, ByteBuffer target) {
                try {
                    log.info(LoggingMessages.READ_FAILED, exception);
                    publishEndOfFile();
                } catch (Exception e) {
                    log.info(LoggingMessages.PARSE_MESSAGE_COMPLETION_ERROR, e);
                }
            }
        };

    }


    private void processNext(Integer numRead) throws IOException {

        if (numRead <= 0) {
            this.publishEndOfFile();
            return;
        }
        this.line.write(this.dst.get(0));

        if (this.dst.get(0) == Constants.NEW_LINE_DELIMETER) {
            this.processLine();
        }
        this.position += 1;
        this.readNextByte();

    }


    static public class Parse {

        public final String name;
        public final Path filePath;

        public Parse(Path filePath) {

            this.name = "parse";
            this.filePath = filePath;

        }

        @Override
        public String toString() {
            return "parse";
        }


    }
}

