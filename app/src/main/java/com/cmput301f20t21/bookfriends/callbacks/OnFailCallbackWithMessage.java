package com.cmput301f20t21.bookfriends.callbacks;

public interface OnFailCallbackWithMessage<MsgType> {
    void run(MsgType msg);
}
