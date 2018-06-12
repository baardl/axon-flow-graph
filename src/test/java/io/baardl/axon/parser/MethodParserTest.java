package io.baardl.axon.parser;

import io.baardl.axon.action.ActionCommandHandler;
import io.baardl.axon.graph.HandlerDescriptor;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MethodParserTest {

    private MethodParser methodParser;

    @Before
    public void setUp() throws Exception {
        methodParser = new MethodParser();
    }

    @Test
    public void parseFile() {
        List<HandlerDescriptor> parsedDescriptors = methodParser.parseFile("src/main/java/", ActionCommandHandler.class.getName());
        assertNotNull(parsedDescriptors);
        assertEquals(1, parsedDescriptors.size());
        //Look to axon-flow-graph-template.json for expected output
        /*
        {
            "name": "io.baardl.axon.action.ActionCommandHandler",
            "type": "CommandHandler",
            "method_name": "create",
            "handle": "io.baardl.axon.action.CreateActionCommand",
            "next": "io.baardl.axon.action.ActionCreatedEvent"
        }
        */
        HandlerDescriptor descriptor = parsedDescriptors.get(0);
        assertEquals("io.baardl.axon.action.ActionCommandHandler", descriptor.getName());
        assertEquals("CommandHandler", descriptor.getType());
        assertEquals("create", descriptor.getMethodName());
        assertEquals("io.baardl.axon.action.CreateActionCommand", descriptor.getHandle());
        assertEquals("io.baardl.axon.action.ActionCreatedEvent", descriptor.getNext());
    }
}