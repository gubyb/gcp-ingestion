package com.mozilla.telemetry.ingestion.core.util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class JsonLogFormatter extends Formatter {

  @Override
  public String format(LogRecord record) {
    ObjectNode result = Json.createObjectNode().put("level", record.getLevel().toString())
        .put("logger", record.getLoggerName()).put("message", record.getMessage());
    Throwable thrown = record.getThrown();
    if (thrown != null) {
      ByteArrayOutputStream trace = new ByteArrayOutputStream();
      thrown.printStackTrace(new PrintStream(trace));
      result.put("trace", trace.toString());
    }
    return Json.asString(result) + "\n";
  }
}
