package com.m800.actorsystem;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.m800.actors.FileScanner.*;
import com.m800.utilities.Constants;
import com.m800.actors.FileScanner;


public class App {


    public static void init(){
        ActorSystem system = ActorSystem.create(Constants.ACTOR_SYSTEM_NAME);
        final LoggingAdapter logger = Logging.getLogger(system, App.class);
        try {
            logger.info("Starting AKKA Test App");
            ActorRef fileScannerActor = system.actorOf(Props.create(FileScanner.class), "fileScanner");
            fileScannerActor.tell(new Scan(), ActorRef.noSender());

        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    public static void main(String[] args) {
        App.init();
    }

}
