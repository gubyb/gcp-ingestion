package com.mozilla.telemetry.ingestion.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.junit.Test;

public class DerivedAttributesMapTest {

  @Test
  public void testGet() {
    Map<String, String> attributes = ImmutableMap.of("submission_timestamp",
        "2018-03-12T21:02:18.123456Z");
    Map<String, String> derived = DerivedAttributesMap.of(attributes);
    assertEquals("2018-03-12", derived.get("submission_date"));
    assertEquals("21", derived.get("submission_hour"));
    assertNull(derived.get("nonexistent_attribute"));
  }

  @Test
  public void testGetOrDefault() {
    Map<String, String> attributes = ImmutableMap.of("submission_timestamp",
        "2018-03-12T21:02:18.123456Z");
    Map<String, String> derived = DerivedAttributesMap.of(attributes);
    assertEquals("2018-03-12", derived.getOrDefault("submission_date", "default"));
    assertEquals("21", derived.getOrDefault("submission_hour", "default"));
    assertEquals("default", derived.getOrDefault("nonexistent_attribute", "default"));
  }

  @Test
  public void testNullMap() {
    Map<String, String> attributes = null;
    Map<String, String> derived = DerivedAttributesMap.of(attributes);
    assertNull(derived);
  }

  @Test
  public void testMissingAttribute() {
    Map<String, String> attributes = ImmutableMap.of();
    Map<String, String> derived = DerivedAttributesMap.of(attributes);
    assertNull(derived.get("submission_date"));
    assertNull(derived.get("submission_hour"));
  }

  @Test
  public void testEmptyAttribute() {
    Map<String, String> attributes = ImmutableMap.of("submission_timestamp", "");
    Map<String, String> derived = DerivedAttributesMap.of(attributes);
    assertNull(derived.get("submission_date"));
    assertNull(derived.get("submission_hour"));
  }

  @Test
  public void testDateOnly() {
    Map<String, String> attributes = ImmutableMap.of("submission_timestamp", "2018-03-12T");
    Map<String, String> derived = DerivedAttributesMap.of(attributes);
    assertEquals("2018-03-12", derived.get("submission_date"));
    assertNull(derived.get("submission_hour"));
  }

}
