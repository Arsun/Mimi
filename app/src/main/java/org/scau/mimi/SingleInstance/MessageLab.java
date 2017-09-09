package org.scau.mimi.SingleInstance;

import org.scau.mimi.gson.MessagesInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by 10313 on 2017/9/8.
 */

public class MessageLab {

    private static AtomicReference<MessageLab> INSTANCE = new AtomicReference<>();
    private List<MessagesInfo.Content.Message> mMessageList;


    private MessageLab() {
        mMessageList = new ArrayList<>();
    }

    public static MessageLab getInstance() {
        for (;;) {
            MessageLab current = INSTANCE.get();
            if (current != null) {
                return current;
            }
            current = new MessageLab();
            if (INSTANCE.compareAndSet(null, current)) {
                return current;
            }
        }
    }

    public void addMessages(int position, List<MessagesInfo.Content.Message> messages) {
        mMessageList.addAll(position, messages);
    }

    public List<MessagesInfo.Content.Message> getMessageList() {
        return mMessageList;
    }

    public long getTimeBefore() {
        return mMessageList.get(mMessageList.size() - 1).tmCreated - 1;
    }

    public long getTimeAfter() {
        return mMessageList.get(0).tmCreated + 1;
    }

    public int getSize() {
        return mMessageList.size();
    }
}
