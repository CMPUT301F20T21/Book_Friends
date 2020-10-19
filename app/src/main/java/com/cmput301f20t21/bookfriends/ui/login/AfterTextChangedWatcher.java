/*
 * AfterTextChangedWatcher.java
 * Version: 1.0
 * Date: October 18, 2020
 * Copyright (c) 2020. Book Friends Team
 * All rights reserved.
 * github URL: https://github.com/CMPUT301F20T21/Book_Friends
 */

package com.cmput301f20t21.bookfriends.ui.login;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * A simple abstract class that declares all abstract methods in the TextWatcher interface
 * the beforeTextChanged() and onTextChanged() need to be declared but not used
 * the abstract method afterTextChanged() will need to be implemented in (anonymous) subclass
 */
abstract class AfterTextChangedWatcher implements TextWatcher {
    // function must be declared from TextWatcher, but not implemented
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    // function must be declared from TextWatcher, but not implemented
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    // function must be implemented in the subclass or anonymous subclass
    @Override
    public abstract void afterTextChanged(Editable s);
}
