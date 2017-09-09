package org.scau.mimi.SingleInstance;

import org.java_websocket.WebSocket;
import org.scau.mimi.util.LogUtil;

import java.util.concurrent.atomic.AtomicReference;

import rx.functions.Action1;
import ua.naiksoftware.stomp.LifecycleEvent;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

/**
 * Created by 10313 on 2017/9/8.
 */

public class Client {

    private static final String TAG = "Client";

    //Subscribe
    public static final String ADDRESS = "http://104.224.174.146:8080/Mimi-2.0";
    public static final String SUBSCRIBE_CONNECT_SERVER = "/client?srect=";
    public static final String SUBSCRIBE_MSG_INIT = "/socket/chat/init";
    public static final String SUBSCRIBE_MSG_SEND = "/socket/chat/send";
    public static final String SUBSCRIBE_RECEIVE_MSG_PART_1 = "/user/";
    public static final String SUBSCRIBE_RECEIVE_MSG_PART_2 = "/recive";
    public static final String SUBSCRIBE_MSG_SEND_STATUS = "/user/queue/send/status";
    public static final String SUBSCRIBE_MSG_SEND_ERROR = "/user/queue/status";
    public static final String SUBSCRIBE_MSG_RECEIVED_STATUS = "/user/queue/recive/status";
    public static final String SUBSCRIBE_MSG_RECEIVE_CONFIRM = "/socket/chat/recive";

    private static final AtomicReference<Client> INSTANCE = new AtomicReference<Client>();
    private StompClient mStompClient;

    private Client() {
    }

    private Client(String secret) {
        mStompClient = Stomp.over(WebSocket.class, Client.ADDRESS + Client.SUBSCRIBE_CONNECT_SERVER + secret);
    }

    public static Client getInstance(String secret) {
        for ( ; ; ) {
            Client current = INSTANCE.get();
            if (current != null) {
                return current;
            }
            current = new Client(secret);
            if (INSTANCE.compareAndSet(null, current)) {
                return current;
            }
        }

    }

    public StompClient getStompClient() {
        return mStompClient;
    }

    public void conncectServer() {
        mStompClient.connect();
        mStompClient.lifecycle()
                .subscribe(new Action1<LifecycleEvent>() {
                    @Override
                    public void call(LifecycleEvent lifecycleEvent) {
                        switch (lifecycleEvent.getType()) {
                            case OPENED:
                                LogUtil.d(TAG, "lifecycleEvent: OPENED");
                                initMsg();
                                break;
                            case CLOSED:
                                LogUtil.d(TAG, "lifecycleEvent: CLOSED");
                                break;
                            case ERROR:
                                LogUtil.d(TAG, "lifecycleEvent: ERROR");
                                break;
                        }
                    }
                });
    }


    private void initMsg() {
        mStompClient.send(Client.SUBSCRIBE_MSG_INIT);
    }


}
