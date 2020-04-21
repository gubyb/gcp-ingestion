package com.mozilla.telemetry.ingestion.core.util;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.io.Resources;
import java.io.IOException;
import java.io.UncheckedIOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ProvideSystemProperty;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.slf4j.LoggerFactory;

public class JsonLogFormatterTest {

  @Rule
  public final SystemErrRule stderr = new SystemErrRule();

  @Rule
  public final ProvideSystemProperty loggingConfig = new ProvideSystemProperty(
      "java.util.logging.config.file", Resources.getResource("logging.properties").getPath());

  @Test
  public void canWriteLogs() {
    stderr.mute();
    stderr.enableLog();
    LoggerFactory.getLogger(JsonLogFormatterTest.class).warn("msg",
        new UncheckedIOException(new IOException("test")));
    assertThat(stderr.getLog(),
        allOf(containsString(JsonLogFormatterTest.class.getName()),
            containsString("\"level\":\"WARNING\""), containsString("\"message\":\"msg\""),
            containsString("java.io.UncheckedIOException"), containsString("java.io.IOException"),
            containsString("test")));

    stderr.clearLog();
    LoggerFactory.getLogger(JsonLogFormatterTest.class).error("test message");
    assertThat(stderr.getLog(), allOf(containsString(JsonLogFormatterTest.class.getName()),
        // slf4j-jdk14 converts slf4j's ERROR level to java.util.logging.Level.SEVERE
        containsString("\"level\":\"SEVERE\""), containsString("\"message\":\"test message\"")));
  }
}
