package org.scau.mimi.SingleInstance;

import org.scau.mimi.gson.Comment;
import org.scau.mimi.gson.CommentsInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by 10313 on 2017/9/8.
 */

public class CommentLab {

    private static AtomicReference<CommentLab> INSTANCE = new AtomicReference<>();
    private List<Comment> mCommentList;

    private CommentLab() {
        mCommentList = new ArrayList<>();
    }

    public static CommentLab getInstance() {
        for (;;) {
            CommentLab current = INSTANCE.get();
            if (current != null) {
                return current;
            }
            current = new CommentLab();
            if (INSTANCE.compareAndSet(null, current)) {
                return current;
            }
        }
    }

    public void addComments(int position, List<Comment> comments) {
        mCommentList.addAll(position, comments);
    }

    public List<Comment> getCommentList() {
        return mCommentList;
    }

    public int getSize() {
        return mCommentList.size();
    }

    public long getTimeBefore() {
        return mCommentList.get(mCommentList.size() - 1).tmCreated - 1;
    }

    public long getTimeAfter() {
        return mCommentList.get(0).tmCreated + 1;
    }

    public void addSingleComment(int position, Comment comment) {
        mCommentList.add(position, comment);
    }

    public void removeSingleComment(int position) {
        mCommentList.remove(position);
    }

    public Comment getSingleComment(int position) {
        return mCommentList.get(position);
    }

    public void addSingleComment(Comment comment) {
        mCommentList.add(comment);
    }

    public void addComments(List<Comment> comments) {
        mCommentList.addAll(comments);
    }
}
