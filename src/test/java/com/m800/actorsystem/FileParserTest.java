package com.m800.actorsystem;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.m800.actors.FileParser;
import com.m800.actors.FileScanner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * Created by Waqar Malik on 7/25/2017.
 */
public class FileParserTest {
    static ActorSystem system;

    protected final Path fileDirectory =  Paths.get(System.getProperty("user.dir")+"/m800").toAbsolutePath();


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
    public void testFileParserOnMessageReceived() {
        // given
        final TestKit testProbe = new TestKit(system);
        final ActorRef fileParser = system.actorOf(FileParser.props());

        //when
        fileParser.tell(new FileParser.Parse(fileDirectory), testProbe.getRef());

        //then
        testProbe.expectNoMsg();

    }

}
