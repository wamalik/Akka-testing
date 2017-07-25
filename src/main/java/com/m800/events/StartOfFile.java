package com.m800.events;

/**
 * STARTOFFILE
 * An Event
 * Dispatched
 * - from FileParser
 * - to Aggregator
 * - when FileParser reaches the start of a file.
 *
 * Specifies
 * - filePath
 *
 **/

import java.nio.file.Path;

public class StartOfFile {

  public final Path targetFile;

  public StartOfFile( Path targetFile ) {

    this.targetFile = targetFile;

  }

  @Override
  public String toString( ) {

    return "start-of-file";

  }

}

