package com.gibbsdevops.alfred.model.alfred.utils;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class AlfredObjectMapperFactoryTest {

    private AlfredObjectMapperFactory factory;

    private static String endl = System.getProperty("line.separator");

    String format(String pattern) {
        return pattern.replaceAll("<n>", endl);
    }

    @Before
    public void before() {
        factory = new AlfredObjectMapperFactory();
    }

    @Test
    public void testJsonAllPropertySerialization() throws Exception {
        JsonTestObject user = new JsonTestObject(2L, "albert", "Albert Einstein");
        String s = factory.getObject().writeValueAsString(user);
        assertThat(s, equalTo(format("{<n>  \"id\" : 2,<n>  \"name\" : \"albert\",<n>  \"full_name\" : \"Albert Einstein\"<n>}")));
    }

    @Test
    public void testJsonMissingPropertySerialization() throws Exception {
        JsonTestObject user = new JsonTestObject(2L, "albert", null);
        String s = factory.getObject().writeValueAsString(user);
        assertThat(s, equalTo(format("{<n>  \"id\" : 2,<n>  \"name\" : \"albert\"<n>}")));
    }

    @Test
    public void testJsonFullPropertyDeserialization() throws Exception {
        JsonTestObject o = factory.getObject().readValue("{\"id\":2,\"name\":\"albert\",\"full_name\":\"Albert Einstein\"}", JsonTestObject.class);
        assertThat(o.getId(), equalTo(2L));
        assertThat(o.getName(), equalTo("albert"));
        assertThat(o.getFullName(), equalTo("Albert Einstein"));
    }

    @Test
    public void testJsonMissingPropertyDeserialization() throws Exception {
        JsonTestObject o = factory.getObject().readValue("{\"id\":2,\"name\":\"albert\"}", JsonTestObject.class);
        assertThat(o.getId(), equalTo(2L));
        assertThat(o.getName(), equalTo("albert"));
        assertThat(o.getFullName(), equalTo(null));
    }

    @Test
    public void testJsonTolerantToNewFields() throws Exception {
        JsonTestObject o = factory.getObject().readValue("{\"id\":2,\"new_field\":\"abc\"}", JsonTestObject.class);
        assertThat(o.getId(), equalTo(2L));
    }

    public static class JsonTestObject {

        private Long id;
        private String name;
        private String fullName;

        public JsonTestObject() {
        }

        public JsonTestObject(Long id, String name, String fullName) {
            this.id = id;
            this.name = name;
            this.fullName = fullName;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

    }

}
