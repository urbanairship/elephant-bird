package com.twitter.elephantbird.util;

import com.google.protobuf.Message;
import com.twitter.data.proto.tutorial.AddressBookProtos.AddressBook;
import com.twitter.data.proto.tutorial.AddressBookProtos.Person;
import com.twitter.elephantbird.mapreduce.io.ProtobufConverter;
import com.twitter.elephantbird.pig.piggybank.Fixtures;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.matchers.StringContains;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestProtobufs {

  private static final AddressBook ab_ = Fixtures.buildAddressBookProto();
  private static final byte[] abBytes_ = ab_.toByteArray();

  @Test
  public void testGetInnerProtobufClass() {
    String canonicalClassName = "com.twitter.data.proto.tutorial.AddressBookProtos.Person";
    Class<? extends Message> klass = Protobufs.getByCanonicalClassName(canonicalClassName);
    assertEquals(Person.class, klass);
  }

  @Test
  public void testDynamicParsing() {
    assertEquals(ab_, Protobufs.parseDynamicFrom(AddressBook.class, abBytes_));
  }

  @Test
  public void testStaticParsing() {
    assertEquals(ab_, Protobufs.parseFrom(AddressBook.class, abBytes_));
  }

  @Test
  public void testConverterParsing() {
    ProtobufConverter<AddressBook> protoConverter = ProtobufConverter.newInstance(AddressBook.class);
    assertEquals(ab_, protoConverter.fromBytes(abBytes_));
  }

  @Test
  public void testGetInnerInnerProtobufClass() {
    String canonicalClassName = "com.twitter.data.proto.tutorial.AddressBookProtos.Person.PhoneNumber";
    Class<? extends Message> klass = Protobufs.getByCanonicalClassName(canonicalClassName);
    assertEquals(Person.PhoneNumber.class, klass);
  }

  @Test
  public void testNotFoundInnerInnerProtobufClass() {
    String canonicalClassName = "com.twitter.data.proto.tutorial.AddressBookProtos.Person.Nope";
    Class<? extends Message> klass = Protobufs.getByCanonicalClassName(canonicalClassName);
    assertNull(klass);
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testNotFoundOuterInnerProtobufClass() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage(StringContains.containsString("Could not find"));
    String canonicalClassName = "com.twitter.data.proto.tutorial.LolNope";
    Class<? extends Message> klass = Protobufs.getByCanonicalClassName(canonicalClassName);
    assertNull(klass);
  }

}
