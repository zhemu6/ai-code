package com.lushihao.aicode.langgraph4j.demo;

import org.bsc.langgraph4j.GraphRepresentation;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.GraphStateException;

import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.StateGraph.END;

import java.util.List;
import java.util.Map;

public class SimpleGraphApp {

    public static void main(String[] args) throws GraphStateException {
        // Initialize nodes
        GreeterNode greeterNode = new GreeterNode();
        ResponderNode responderNode = new ResponderNode();

        // Define the graph structure
        var stateGraph = new StateGraph<>(SimpleState.SCHEMA, initData -> new SimpleState(initData))
                .addNode("greeter", node_async(greeterNode))
                .addNode("responder", node_async(responderNode))
                // Define edges
                // Start with the greeter node
                .addEdge(START, "greeter")
                .addEdge("greeter", "responder")
                // End after the responder node
                .addEdge("responder", END);
        // Compile the graph
        var compiledGraph = stateGraph.compile();
        GraphRepresentation demo = stateGraph.getGraph(GraphRepresentation.Type.MERMAID, "demo");
        System.out.println(demo.toString());
        // Run the graph
        // The `stream` method returns an AsyncGenerator.
        // For simplicity, we'll collect results. In a real app, you might process them as they arrive.
        // Here, the final state after execution is the item of interest.

        for (var item : compiledGraph.stream(Map.of(SimpleState.MESSAGES_KEY, "Let's, begin!"))) {

            System.out.println(item);
        }

    }
}