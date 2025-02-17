package org.pinsoft.friendapp.web.websocket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebSocketIntegrationTest {
//    WebSocketClient client;
//    WebSocketStompClient stompClient;
//    @LocalServerPort
//    private int port;
//    private static final Logger logger= LoggerFactory.getLogger(WebSocketIntegrationTest.class);
//
//    @BeforeEach
//    public void setup() {
//        logger.info("Setting up the tests ...");
//        client = new StandardWebSocketClient();
//        stompClient = new WebSocketStompClient(client);
//        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
//    }

//    @Test
//    void givenWebSocket_whenMessage_thenVerifyMessage() throws Exception {
//        final CountDownLatch latch = new CountDownLatch(1);
//        final AtomicReference<Throwable> failure = new AtomicReference<>();
//        StompSessionHandler sessionHandler = new StompSessionHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return null;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//            }
//
//            @Override
//            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
//                logger.info("Connected to the WebSocket ...");
//                session.subscribe("/chat/login", new StompFrameHandler() {
//                    @Override
//                    public Type getPayloadType(StompHeaders headers) {
//                        return Map.class;
//                    }
//
//                    @Override
//                    public void handleFrame(StompHeaders headers, Object payload) {
//                        try {
//
//                            assertThat(payload).isNotNull();
//                            assertThat(payload).isInstanceOf(Map.class);
//
//                            @SuppressWarnings("unchecked")
//                            Map<String, Integer> map = (Map<String, Integer>) payload;
//
//                            assertThat(map).containsKey("HPE");
//                            assertThat(map.get("HPE")).isInstanceOf(Integer.class);
//                        } catch (Throwable t) {
//                            failure.set(t);
//                            logger.error("There is an exception ", t);
//                        } finally {
//                            session.disconnect();
//                            latch.countDown();
//                        }
//
//                    }
//                });
//            }
//
//            @Override
//            public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
//            }
//
//            @Override
//            public void handleTransportError(StompSession session, Throwable exception) {
//            }
//        };
//        //wait someone to login and see result
//        stompClient.connect("ws://localhost:{port}/socket", sessionHandler, this.port);
//        if (latch.await(200, TimeUnit.SECONDS)) {
//            if (failure.get() != null) {
//                fail("Assertion Failed", failure.get());
//            }
//        } else {
//            fail("Could not receive the message on time");
//        }
//    }
}
