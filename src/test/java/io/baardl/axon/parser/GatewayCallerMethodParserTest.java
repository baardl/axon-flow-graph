package io.baardl.axon.parser;

import io.baardl.axon.action.ActionService;
import io.baardl.axon.graph.MethodDescriptor;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GatewayCallerMethodParserTest {

    private GatewayCallerMethodParser gatewayCallerMethodParser;
    @Before
    public void setUp() throws Exception {
        gatewayCallerMethodParser = new GatewayCallerMethodParser();
    }

    @Test
    public void parseFile() {
        List<MethodDescriptor> parsedDescriptors = gatewayCallerMethodParser.parseFile("src/main/java/", ActionService.class.getName());
        assertNotNull(parsedDescriptors);
        assertEquals(1, parsedDescriptors.size());
        //Look to axon-flow-graph-template.json for expected output
        /*
        {
            "name": "io.baardl.axon.action.ActionService",
            "type": "CommandGateway",
            "method_name": "create",
             "next": "io.baardl.axon.action.CreateActionCommand"
        }
        */
        MethodDescriptor descriptor = parsedDescriptors.get(0);
        assertEquals("io.baardl.axon.action.ActionService", descriptor.getName());
        assertEquals("CommandGateway", descriptor.getType());
        assertEquals("io.baardl.axon.action.CreateActionCommand", descriptor.getNext());


    }
}