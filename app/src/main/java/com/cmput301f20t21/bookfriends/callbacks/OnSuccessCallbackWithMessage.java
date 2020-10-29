package com.cmput301f20t21.bookfriends.callbacks;

public interface OnSuccessCallbackWithMessage<MsgType> {
    void run(MsgType msg);
}
