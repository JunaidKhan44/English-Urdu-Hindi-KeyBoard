package com.example.create_keyboard1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.create_keyboard1.util.Keyboard;
import com.example.create_keyboard1.util.KeyboardView;


public class SimpleIME extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    public boolean flagforemoji=false;
    public static KeyboardView kv;
    private Keyboard keyboard;

    private boolean caps = false;
    InputConnection ic;
    private View mInputView;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        flagforemoji=false;
        keyboard = new Keyboard(this, R.xml.custom_qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        // Close popup keyboard when screen is touched, if it's showing
        kv.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                kv.closing();
            }
            return false;
        });

        return kv;
    }



    @Override
    public void onKey(int primaryCode, int[] keyCodes) {


        ic = getCurrentInputConnection();
        playClick(primaryCode);
            switch(primaryCode){
                case Keyboard.KEYCODE_DELETE :
                    ic.deleteSurroundingText(1, 0);
                    break;
                case Keyboard.KEYCODE_SHIFT:
                    caps = !caps;
                    keyboard.setShifted(caps);
                    kv.invalidateAllKeys();
                    break;
                case Keyboard.KEYCODE_DONE:
                    ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    break;

                default:
                    if(flagforemoji) {
                        getCurrentInputConnection().commitText(String.valueOf(Character.toChars(primaryCode)), 1);
                    }
                    else if(primaryCode==0x1F310){
                        flagforemoji=false;
                        keyboard = new Keyboard(this, R.xml.urdu);
                        kv.setKeyboard(keyboard);
                        kv.setOnKeyboardActionListener(this);
//                        kv.setBackground(getResources().getDrawable(R.drawable.picart));
                    }
                    else if(primaryCode==0x2666){
                        flagforemoji=false;
                        keyboard = new Keyboard(this, R.xml.hindi1);
                        kv.setKeyboard(keyboard);
                        kv.setOnKeyboardActionListener(this);
//                        kv.setBackground(getResources().getDrawable(R.drawable.picart));
                    }
                    else if(primaryCode==0x2752){
                        flagforemoji=false;
                        keyboard = new Keyboard(this, R.xml.custom_qwerty);
                        kv.setKeyboard(keyboard);
                        kv.setOnKeyboardActionListener(this);

                    }
                    else if(primaryCode==0xFF9B){
                        flagforemoji=false;
                        keyboard = new Keyboard(this, R.xml.hindi2);
                        kv.setKeyboard(keyboard);
                        kv.setOnKeyboardActionListener(this);
                    }
                    else if(primaryCode==0x3145){
                        flagforemoji=false;
                        keyboard = new Keyboard(this, R.xml.hindi3);
                        kv.setKeyboard(keyboard);
                        kv.setOnKeyboardActionListener(this);
                    }
                    else if(primaryCode==0x0D26){
                        flagforemoji=false;
                        keyboard = new Keyboard(this, R.xml.hindi1);
                        kv.setKeyboard(keyboard);
                        kv.setOnKeyboardActionListener(this);
                    }
                    else if(primaryCode==0x1557){
                        flagforemoji=false;
                        keyboard = new Keyboard(this, R.xml.roman);
                        kv.setKeyboard(keyboard);
                        kv.setOnKeyboardActionListener(this);
                    }
                    else if(primaryCode==0xFE70){
                        flagforemoji=false;
                        keyboard = new Keyboard(this, R.xml.urdu_numeric);
                        kv.setKeyboard(keyboard);
                        kv.setOnKeyboardActionListener(this);
                    }
                    else if(primaryCode==0x1111){
                        flagforemoji=false;
                        keyboard = new Keyboard(this, R.xml.custom_qwerty);
                        kv.setKeyboard(keyboard);
                        kv.setOnKeyboardActionListener(this);
                    }
                    else if(primaryCode== 0x221E){
                        flagforemoji=false;
                        keyboard = new Keyboard(this, R.xml.roman);
                        kv.setKeyboard(keyboard);
                        kv.setOnKeyboardActionListener(this);
                    }
                    else if(primaryCode==0x2121){
                        flagforemoji=false;
                        keyboard = new Keyboard(this, R.xml.roman2);
                        kv.setKeyboard(keyboard);
                        kv.setOnKeyboardActionListener(this);
                    }
                    else if(primaryCode==207){
                        flagforemoji=false;
                        keyboard = new Keyboard(this, R.xml.custom_qwerty);
                        kv.setKeyboard(keyboard);
                        kv.setOnKeyboardActionListener(this);
                    }
                    else if(primaryCode==209){

                        Intent i = new Intent(this, ThemesActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                    else if(primaryCode==206){
                        keyboard = new Keyboard(this, R.xml.emojis);
                        kv.setKeyboard(keyboard);
                        kv.setOnKeyboardActionListener(this);

                        char code = (char) primaryCode;
                        if (Character.isLetter(code) && caps) {
                            code = Character.toUpperCase(code);
                        }
                        ic.commitText(String.valueOf(code), 1);
                    }
                    else {

                        char code = (char) primaryCode;
                        if (Character.isLetter(code) && caps) {
                            code = Character.toUpperCase(code);
                        }
                        ic.commitText(String.valueOf(code), 1);
                    }

            }


    }

    @Override
    public void onPress(int primaryCode) {


    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }


    private void playClick(int keyCode){
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            case 0x1F30E:
//                flagforemoji=false;
//                keyboard = new Keyboard(this, R.xml.qwerty);
//                kv.setKeyboard(keyboard);
//                kv.setOnKeyboardActionListener(this);
                break;
            case  0x1F310:
//                flagforemoji=false;
//                keyboard = new Keyboard(this, R.xml.urdu);
//                kv.setKeyboard(keyboard);
//                kv.setOnKeyboardActionListener(this);
                break;
            case  43:
//
//                keyboard = new Keyboard(this, R.xml.emojis);
//                kv.setKeyboard(keyboard);
//                kv.setOnKeyboardActionListener(this);
//                flagforemoji=true;
//
                break;
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }




}




   