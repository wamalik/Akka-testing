
package com.m800.actorsystem;


import akka.actor.*;
import akka.testkit.javadsl.EventFilter;
import akka.testkit.javadsl.TestKit;
import com.m800.actors.Aggregator;
import com.m800.actors.FileParser;
import com.m800.actors.FileScanner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


import java.nio.file.Path;


/**
 * Created by Waqar Malik on 7/5/2017.
 */



public class AppTest {

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
    public void testFileScannerActorCreation() {
        Props props = Props.create(FileScanner.class);
        assertThat(props.actorClass(), is(equalTo(FileScanner.class)));
    }

    @Test
    public void testFileParserActorCreation() {
        Props props = Props.create(FileParser.class);
        assertThat(props.actorClass(), is(equalTo(FileParser.class)));
    }

    @Test
    public void testAggregatorActorCreation() {
        Path path = null;
        Props props = Props.create(Aggregator.class, path);
        assertEquals(props.actorClass(), (Aggregator.class));
    }


}



