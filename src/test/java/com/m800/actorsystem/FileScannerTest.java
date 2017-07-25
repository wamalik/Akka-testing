package com.m800.actorsystem;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.m800.actors.FileParser;
import com.m800.actors.FileScanner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Waqar Malik on 7/25/2017.
 */
public class FileScannerTest {
    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testFileScannerOnMessageReceived() {
        // given
        String message = "scan";
        final TestKit testProbe = new TestKit(system);
        final ActorRef fileScanner = system.actorOf(FileScanner.props(message, testProbe.getRef()));

        //when
        fileScanner.tell(new FileScanner.Scan(), ActorRef.noSender());

        //then
        FileParser.Parse parseMessage = testProbe.expectMsgClass(FileParser.Parse.class);
        assertEquals("parse", parseMessage.name);
    }
}
