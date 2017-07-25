package com.m800.events;

/**
 * ENDOFFILE
 * An Event
 * Dispatched
 * - from FileParser
 * - to Aggregator
 * - when FileParser reaches the end of a file.
 *
 * Specifies
 * - filePath
 * - numOfWords
 **/

import java.nio.file.Path;

public class EndOfFile {

  public final Path filePath;


  public EndOfFile(Path filePath) {

    this.filePath = filePath;
  }

  @Override
  public String toString( ) {
    return "end-of-file";
  }

}

