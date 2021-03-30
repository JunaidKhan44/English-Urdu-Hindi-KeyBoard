package com.example.create_keyboard1.Otherclasses;

import android.Manifest;
import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;


import com.example.create_keyboard1.Fragments.FlagsFrag;
import com.example.create_keyboard1.Fragments.GradientFrag;
import com.example.create_keyboard1.Fragments.ImageFrag;
import com.example.create_keyboard1.Fragments.SportsFrag;
import com.example.create_keyboard1.R;
import com.example.create_keyboard1.dictionary.TrieCharacters;
import com.example.create_keyboard1.dictionary.TrieWords;
import com.example.create_keyboard1.dictionary.WordFreq;
import com.example.create_keyboard1.util.Keyboard;
import com.example.create_keyboard1.util.KeyboardView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.create_keyboard1.Adapter.KeyThemeAdapter.GROUPSNAME_SHARED_PREF1;
import static com.example.create_keyboard1.Adapter.KeyThemeAdapter.POSITION_AD1;
import static com.example.create_keyboard1.Adapter.KeyThemeAdapter.SHARED_PREF_NAME1;
import static com.example.create_keyboard1.Adapter.MyAdapter.GROUPSNAME_SHARED_PREF;
import static com.example.create_keyboard1.Adapter.MyAdapter.POSITION_AD;
import static com.example.create_keyboard1.Adapter.MyAdapter.SHARED_PREF_NAME;


public class SimpleIME extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    public static InputConnection ic;
    public boolean flagforemoji = false;
    public static KeyboardView kv;
    public Keyboard keyboard;
    SharedPreferences sharedPreferences,sharedPreferences1;
    private boolean caps = false;

    //new urdu key code---------
    TrieCharacters trieCharacters;
    TrieWords trieWords;
    String previousWord;
    Context context  = this;
    boolean characterReady = false;
    boolean wordsReady = false;
    private boolean mPredictionOn =true;
    private boolean mCompletionOn = false;
    private boolean mCapsLock;
    private StringBuilder mComposing = new StringBuilder();
    CandidateView mCandidateView;
    private CompletionInfo[] mCompletions;
    private String mWordSeparators;
    private String uWordSeparators;
    private int mLastDisplayWidth;
    private long mLastShiftTime;
    private SharedPreferences sharedPreferences2;


    public String getuWordSeparators() {
        return uWordSeparators;
    }
    private List<String> fromDictionary;
    private List<String> nextWord;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        flagforemoji = false;

        sharedPreferences2 = getSharedPreferences(SHARED_PREF_NAME1, MODE_PRIVATE);
        String value1 = sharedPreferences2.getString(GROUPSNAME_SHARED_PREF1, "");
        int pos1 = sharedPreferences2.getInt(POSITION_AD1 , 0);
        if(pos1==0){   kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);

        }
        else if(pos1==1){   kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard2, null);

        }
        else if(pos1==2){   kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard3, null);

        }

        keyboard = new Keyboard(this, R.xml.custom_qwerty);
        kv.setKeyboard(keyboard);

        //shared preference code start
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String value = sharedPreferences.getString(GROUPSNAME_SHARED_PREF, "");
        int pos = sharedPreferences.getInt(POSITION_AD, 0);
        if (TextUtils.equals("gradient", value)) {

            if (GradientFrag.mygradient.size() > 0)
                kv.setBackgroundResource(GradientFrag.mygradient.get(pos).getTheme_image());
        } else if (TextUtils.equals("flag", value)) {

            if (FlagsFrag.myflag.size() > 0)
                kv.setBackgroundResource(FlagsFrag.myflag.get(pos).getTheme_image());
        } else if (TextUtils.equals("image", value)) {

            if (ImageFrag.myimg.size() > 0)
                kv.setBackgroundResource(ImageFrag.myimg.get(pos).getTheme_image());
        } else if (TextUtils.equals("sport", value)) {

            if (SportsFrag.mysport.size() > 0)
                kv.setBackgroundResource(SportsFrag.mysport.get(pos).getTheme_image());
        }
        else {
            kv.setBackgroundResource(R.drawable.gradient_0);
        }

        // Close popup keyboard when screen is touched, if it's showing
        kv.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                kv.closing();
            }
            return false;
        });

        kv.setOnKeyboardActionListener(this);
        return kv;
    }


    @Override public void onInitializeInterface() {
        if (keyboard != null) {
            // Configuration changes can happen after the keyboard gets recreated,
            // so we need to be able to re-build the keyboards if the available
            // space has changed.
            int displayWidth = getMaxWidth();
            if (displayWidth == mLastDisplayWidth) return;
            mLastDisplayWidth = displayWidth;
        }
        keyboard = new Keyboard(this, R.xml.custom_qwerty );
    }
    @Override public void onCreate() {
        super.onCreate();
        mWordSeparators = getResources().getString(R.string.word_separators);
        uWordSeparators = getResources().getString(R.string.urdu_separators);


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //Background work here
                trieCharacters = new TrieCharacters();
                try {
                    Log.e("Unigram Reading", "start");
                    InputStream inputStream = context.getResources().openRawResource(R.raw.wordcount);

                    InputStreamReader inputreader = new InputStreamReader(inputStream);
                    BufferedReader buff = new BufferedReader(inputreader);
                    String line;

                    while ((line = buff.readLine()) != null) {
                        trieCharacters.insert(line);
                    }

                }

                catch (IOException iex) {
                    Logger.getLogger(SimpleIME.class.getName()).log(Level.SEVERE, null, iex);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                        Log.e("Unigram", "ready");
                        characterReady = true;
                    }
                });
            }
        });


        ExecutorService executor2 = Executors.newSingleThreadExecutor();
        Handler handler2 = new Handler(Looper.getMainLooper());
        executor2.execute(new Runnable() {
            @Override
            public void run() {

                //Background work here
                trieWords = new TrieWords();
                try {
                    Log.e("Biagram Reading", "start");
                    InputStream inputStream = context.getResources().openRawResource(R.raw.wordcount_n);

                    @SuppressLint("StaticFieldLeak") InputStreamReader inputreader = new InputStreamReader(inputStream);
                    BufferedReader buff = new BufferedReader(inputreader);
                    String line;

                    while ((line = buff.readLine()) != null) {
                        try {
                            trieWords.insert(line);
                        }
                        catch (Exception e){

                        }
                    }

                }

                catch (IOException iex) {
                    Logger.getLogger(SimpleIME.class.getName()).log(Level.SEVERE, null, iex);
                }



                handler2.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                        Log.e("Biagram", "ready");
                        characterReady = true;

                    }
                });
            }
        });

    }
    @Override public View onCreateCandidatesView() {
        mCandidateView = new CandidateView(this);
        mCandidateView.setService(SimpleIME.this);
        mCandidateView.setBackgroundColor(getResources().getColor(R.color.white));
        return mCandidateView;
    }




    @Override
    public void onKey(int primaryCode, int[] keyCodes) {


        ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
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
            case 0x1F310:
                keyboard = new Keyboard(this, R.xml.urdu);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case 0x2666:
                keyboard = new Keyboard(this, R.xml.hindi1);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case 0x2752:
                keyboard = new Keyboard(this, R.xml.custom_qwerty);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case -63:
                keyboard = new Keyboard(this, R.xml.hindi2);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case -64:
                keyboard = new Keyboard(this, R.xml.hindi3);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case -65:
                keyboard = new Keyboard(this, R.xml.hindi1);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case -6:
                keyboard = new Keyboard(this, R.xml.roman);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case -62:
                keyboard = new Keyboard(this, R.xml.urdu_numeric);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case -61:
                keyboard = new Keyboard(this, R.xml.custom_qwerty);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case 0x221E:
                keyboard = new Keyboard(this, R.xml.roman);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case 0x2121:
                keyboard = new Keyboard(this, R.xml.roman2);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case 207:
                keyboard = new Keyboard(this, R.xml.custom_qwerty);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case 0x0709:
                Intent i = new Intent(this, Background.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            case 0x0707:
                flagforemoji = true;
                keyboard = new Keyboard(this, R.xml.emojis);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case 0x0706:
                requestHideSelf(0);
                break;
            case 0x0708:

                Dexter.withContext(this)
                        .withPermissions(
                                Manifest.permission.RECORD_AUDIO)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {

                                if (report.areAllPermissionsGranted()) {
                                    Log.i("tag", "permission granted");

                                    speechToText();
                                }

                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    Log.i("tag", "not permission granted");
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        })
                        .onSameThread()
                        .check();

                break;
            case 0x070A:

                Intent intent = new Intent(this, SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;
            case 0x0730:
                keyboard = new Keyboard(this, R.xml.time_emoji);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case 0x0731:
                keyboard = new Keyboard(this, R.xml.emojis);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case 0x0732:
                keyboard = new Keyboard(this, R.xml.weather_emojis);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case 0x0733:
                keyboard = new Keyboard(this, R.xml.heart_emojis);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;

            default:
                if (flagforemoji) {
                    getCurrentInputConnection().commitText(String.valueOf(Character.toChars(primaryCode)), 1);

                } else {

//                    char code = (char) primaryCode;
//                    if (Character.isLetter(code) && caps) {
//                        code = Character.toUpperCase(code);
//                    }
//                 ic.commitText(String.valueOf(code), 1);

                    Log.d("mytag","key pop up"+primaryCode);

                }

        }
        /////////////////////////////////////////////
        previousWord="";
        if (isWordSeparator(primaryCode)) {
            // Handle separator
            if (mComposing.length() > 0) {
                previousWord=mComposing.toString();
                Log.d("mytag","isWordSeparator :"+mComposing);
                commitTyped(getCurrentInputConnection());
            }
            sendKey(primaryCode);
        }
        else {
            handleCharacter(primaryCode, keyCodes);
            Log.d("mytag","isWordSeparator + handleCharacter :"+mComposing);
         //   Log.d("mytag","key pop up"+primaryCode);
        }




    }

    //general Overridden Fuctions
    @Override public void onDisplayCompletions(CompletionInfo[] completions) {
        if (mCompletionOn) {
            mCompletions = completions;
            if (completions == null) {
                setSuggestions(null, false, false);
                return;
            }

            List<String> stringList = new ArrayList<String>();
            for (int i=0; i<(completions != null ? completions.length : 0); i++) {
                CompletionInfo ci = completions[i];
                if (ci != null) stringList.add(ci.getText().toString());
            }
            setSuggestions(stringList, true, true);
        }
    }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                // The InputMethodService already takes care of the back
                // key for us, to dismiss the input method if it is shown.
                // However, our keyboard could be showing a pop-up window
                // that back should dismiss, so we first allow it to do that.
                if (event.getRepeatCount() == 0 && kv != null) {
                    if (kv.handleBack()) {
                        return true;
                    }
                }
                break;

            case KeyEvent.KEYCODE_DEL:
                // Special handling of the delete key: if we currently are
                // composing text for the user, we want to modify that instead
                // of let the application to the delete itself.
                if (mComposing.length() > 0) {
                    onKey(Keyboard.KEYCODE_DELETE, null);
                    Log.d("mytag","KeyEvent.KEYCODE_DEL :"+mComposing);
                    return true;
                }
                break;

            case KeyEvent.KEYCODE_ENTER:
                // Let the underlying text editor always handle these.
                return false;

            default:
                return false;
        }

        return super.onKeyDown(keyCode, event);
    }
    @Override public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        Keyboard current;
        // Reset our state.  We want to do this even if restarting, because
        // the underlying state of the text editor could have changed in any way.
        mComposing.setLength(0);
        updateCandidates();


        mPredictionOn = false;
        mCompletionOn = false;
        mCompletions = null;

        // We are now going to initialize our state based on the type of
        // text being edited.
        switch (attribute.inputType&EditorInfo.TYPE_MASK_CLASS) {
            case EditorInfo.TYPE_CLASS_NUMBER:
            case EditorInfo.TYPE_CLASS_DATETIME:
                // Numbers and dates default to the symbols keyboard, with
                // no extra features.
              //  current = symbols;
                break;

            case EditorInfo.TYPE_CLASS_PHONE:
                // Phones will also default to the symbols keyboard, though
                // often you will want to have a dedicated phone keyboard.
                //current = symbols;
                break;

            case EditorInfo.TYPE_CLASS_TEXT:
                // This is general text editing.  We will default to the
                // normal alphabetic keyboard, and assume that we should
                // be doing predictive text (showing candidates as the
                // user types).
                //current = symbols;
                mPredictionOn = true;

                // We now look for a few special variations of text that will
                // modify our behavior.
                int variation = attribute.inputType &  EditorInfo.TYPE_MASK_VARIATION;
                if (variation == EditorInfo.TYPE_TEXT_VARIATION_PASSWORD ||
                        variation == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Do not display predictions / what the user is typing
                    // when they are entering a password.
                    mPredictionOn = false;
                }

                if (variation == EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                        || variation == EditorInfo.TYPE_TEXT_VARIATION_URI
                        || variation == EditorInfo.TYPE_TEXT_VARIATION_FILTER) {
                    // Our predictions are not useful for e-mail addresses
                    // or URIs.
                    mPredictionOn = false;
                }

                if ((attribute.inputType&EditorInfo.TYPE_TEXT_FLAG_AUTO_COMPLETE) != 0) {
                    // If this is an auto-complete text view, then our predictions
                    // will not be shown and instead we will allow the editor
                    // to supply their own.  We only show the editor's
                    // candidates when in fullscreen mode, otherwise relying
                    // own it displaying its own UI.
                    mPredictionOn = false;
                    mCompletionOn = isFullscreenMode();
                }

                // We also want to look at the current state of the editor
                // to decide whether our alphabetic keyboard should start out
                // shifted.
                break;

            default:
                // For all unknown input types, default to the alphabetic
                // keyboard with no special features.
                //current = keyboard;
        }

        // Update the label on the enter key, depending on what the application
        // says it will do.
    }
    @Override public void onFinishInputView(boolean finishingInput) {
        if (!finishingInput) {
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                previousWord = "";
                nextWord = new ArrayList<>();
                fromDictionary = new ArrayList<>();
                mComposing.setLength(0);
                updateCandidates();
                ic.finishComposingText();
                handleClose();
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

        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        ic.beginBatchEdit();
        if (mComposing.length() > 0) {
            commitTyped(ic);
        }
        Log.v("text", text.toString());
        ic.commitText(text, 0);
        ic.endBatchEdit();
    }
    @Override
    public void swipeDown() {
        handleClose();
    }
    @Override
    public void swipeLeft() {
        pickSuggestionManually(1);
    }
    @Override
    public void swipeRight() {
                 handleBackspace();
    }
    @Override
    public void swipeUp() {
    }


    //Handling functions
    private void handleClose() {
        requestHideSelf(0);
        mComposing = new StringBuilder();
        setSuggestions(null, false, false);
        Log.d("mytag","handleClose :"+mComposing);
        updateCandidates();
        kv.closing();
    }

    private void handleCharacter(int primaryCode, int[] keyCodes) {
        if (isInputViewShown()) {
            if (isInputViewShown()) {
                if (kv.isShifted()) {
                    primaryCode = Character.toUpperCase(primaryCode);
                }
            }
        }

        if (isAlphabet(primaryCode) && mPredictionOn) {

            Log.d("mytag","isAlphabet : "+mComposing);
            mComposing.append((char) primaryCode);
            getCurrentInputConnection().setComposingText(mComposing, 1);
            Log.d("mytag","isAlphabet : "+mComposing);
            updateCandidates();
//            mComposing.setLength(0);
        }
    }

    private void handleShift() {
        if (kv == null) {
            return;
        }

    }




    private void handleBackspace() {
        final int length = mComposing.length();
        if (length > 1) {
            mComposing.delete(length - 1, length);
            mComposing.setLength(0);
            setSuggestions(null, false, false);
            getCurrentInputConnection().setComposingText(mComposing, 1);
            Log.d("mytag","handleBackspace 1: "+mComposing);
            updateCandidates();
        } else if (length > 0) {
            mComposing.setLength(0);
            setSuggestions(null, false, false);
            getCurrentInputConnection().commitText("", 0);
            Log.d("mytag","handleBackspace 2: "+mComposing);
            updateCandidates();


        }else if (length == 0) {

            mComposing.setLength(0);
            setSuggestions(null, false, false);
            Log.d("mytag","handleBackspace 3: "+mComposing);
            try{
                checkForCompose();
            }
            catch(Exception e){
                Log.v("Empty", "nothing to get");
                keyDownUp(KeyEvent.KEYCODE_DEL);
            }

            updateCandidates();
        }else{
            keyDownUp(KeyEvent.KEYCODE_DEL);
        }
    }

    private void checkForCompose() {
        String backWord = getTextBeforeCursor();
        Log.v("String", backWord);
        mComposing.setLength(0);
        mComposing.append(backWord);
        Log.d("mytag","checkForCompose :"+mComposing);
        getCurrentInputConnection().setComposingText(backWord, 1);

    }

    public String getTextBeforeCursor() {
        // TODO: use mCommittedTextBeforeComposingText if possible to improve performance
        if (null != getCurrentInputConnection())
        {
            CharSequence sentence = getCurrentInputConnection().getTextBeforeCursor(100, 0);

            if (sentence.charAt(sentence.length() - 1) == ' ') {
                sentence = sentence.subSequence(0, sentence.length() - 1);
                getCurrentInputConnection().deleteSurroundingText(1, 0);
            }
            String[] returner = sentence.toString().split(" ");
            String word = returner[returner.length - 1];
            Log.e("Word", word);
            getCurrentInputConnection().deleteSurroundingText(word.length(), 0);

            return word;

        }
        return null;
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
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    private String getWordSeparators() {
        return mWordSeparators;
    }

    public boolean isWordSeparator(int code) {
        String separators = getWordSeparators()+getuWordSeparators();
        return separators.contains(String.valueOf((char)code));
    }

    private void sendKey(int keyCode) {
        if (keyCode == '\n') {
            keyDownUp(KeyEvent.KEYCODE_ENTER);

        } else {
            if (keyCode >= '0' && keyCode <= '9') {
                keyDownUp(keyCode - '0' + KeyEvent.KEYCODE_0);
            } else {
                getCurrentInputConnection().commitText(String.valueOf((char) keyCode), 1);
            }

        }
    }

    private void keyDownUp(int keyEventCode) {
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
    }

    private void updateCandidates() {

        if (!mCompletionOn) {
            if (mComposing.length() > 0) {
                ArrayList<String> list = new ArrayList<String>();
                list.add(mComposing.toString());
                Log.d("mytag","updateCandidates :"+mComposing);
                setSuggestions(list, true, true);
            } else if(previousWord!=null){
                if(!previousWord.equals("")) {
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(previousWord.trim());
                    setSuggestions(list, true, true);

                }
            }else{
                setSuggestions(null, false, false);
            }
        }
    }

    public void setSuggestions(List<String> suggestions, boolean completions,
                               boolean typedWordValid) {

        if (suggestions != null && suggestions.size() > 0) {
            setCandidatesViewShown(true);
        } else if (isExtractViewShown()) {
            setCandidatesViewShown(true);
        }
        if (mCandidateView != null) {
            try {
                suggestions = getFromDictionary(suggestions.get(0));
                if(!previousWord.equals(""))
                    suggestions = getNextWord(previousWord);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            mCandidateView.setSuggestions(suggestions, completions, typedWordValid);
        }
    }

    public void pickSuggestionManually(int index) {
        if (mCompletionOn && mCompletions != null && index >= 0
                && index < mCompletions.length) {
            CompletionInfo ci = mCompletions[index];
            getCurrentInputConnection().commitCompletion(ci);
            if (mCandidateView != null) {
                mCandidateView.clear();
            }
            //updateShiftKeyState(getCurrentInputEditorInfo());
        } else if (mComposing.length() > 0) {
            mComposing = new StringBuilder();
            mComposing.append(fromDictionary.get(index)+" ");
            Log.d("mytag","pickSuggestionManually1 :"+mComposing);

            commitTyped(getCurrentInputConnection());
        }else if(!previousWord.equals("")){
            try {
                mComposing = new StringBuilder();
                mComposing.append(nextWord.get(index) + " ");
                previousWord = mComposing.toString().trim();
                Log.d("mytag","pickSuggestionManually2 :"+mComposing);
                commitTyped(getCurrentInputConnection());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void commitTyped(InputConnection inputConnection) {
        if (mComposing.length() > 0) {
            previousWord = mComposing.toString().trim();
            inputConnection.commitText(mComposing, mComposing.length());
            Log.d("mytag","commitTyped :"+mComposing);
            mComposing.setLength(0);
            updateCandidates();
        }
    }

    private boolean isAlphabet(int code) {
        if (Character.isLetter(code)) {
            return true;
        } else {
            return false;
        }
    }


    //Populating predictions

    public List<String> getFromDictionary(String string) {

        fromDictionary = new ArrayList<>();
        Collection<String> list = trieCharacters.autoComplete(string);
        if(!list.isEmpty()){
            List<String> wordList = new ArrayList(list);
            List<WordFreq> sortedList = new ArrayList();
            for (String wordList1 : wordList) {

                String[] tokens = wordList1.split(" ");
                WordFreq wordFreq = new WordFreq(tokens[0], Integer.parseInt(tokens[1]));
                sortedList.add(wordFreq);
            }
            Collections.sort(sortedList);
            int noOfSuggestions = 6;
            if(sortedList.size()<noOfSuggestions)
            {
                noOfSuggestions = sortedList.size();
            }
            fromDictionary.add(string);
            for(int i=0; i<noOfSuggestions; i++)
            {
                WordFreq w = sortedList.get(i);
                fromDictionary.add(w.getLetter());
            }
        }
        else
        {
            Log.v("No Suggestion","Empty String");
        }
        return fromDictionary;
    }

    public List<String> getNextWord(String string) {

        nextWord = new ArrayList<>();
        Collection<String> list = trieWords.autoComplete(string);
        if(!list.isEmpty()){
            List<String> wordList = new ArrayList(list);
            List<WordFreq> sortedList = new ArrayList();
            for (String wordList1 : wordList) {

                String[] tokens = wordList1.split(" ");
                WordFreq wordFreq = new WordFreq(tokens[0], Integer.parseInt(tokens[1]));
                sortedList.add(wordFreq);
            }
            Collections.sort(sortedList);
            int noOfSuggestions = 6;
            if(sortedList.size()<noOfSuggestions)
            {
                noOfSuggestions = sortedList.size();
            }
            for(int i=0; i<noOfSuggestions; i++)
            {
                WordFreq w = sortedList.get(i);
                String[] next = w.getLetter().split("_");
                try {
                    nextWord.add(next[1]);
                }
                catch(ArrayIndexOutOfBoundsException e)
                {

                }

            }

        }
        return nextWord;
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {

//
//        sharedPreferences2 = getSharedPreferences(SHARED_PREF_NAME1, MODE_PRIVATE);
//        String value1 = sharedPreferences2.getString(GROUPSNAME_SHARED_PREF1, "");
//        int pos1 = sharedPreferences2.getInt(POSITION_AD1 , 0);
//
//        if(pos1==0){   kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
//            Toast.makeText(context, "pos "+pos1, Toast.LENGTH_SHORT).show();
//        }
//        else if(pos1==1){   kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard2, null);
//            Toast.makeText(context, "pos "+pos1, Toast.LENGTH_SHORT).show();
//        }
//        else if(pos1==2){   kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard3, null);
//            Toast.makeText(context, "pos "+pos1, Toast.LENGTH_SHORT).show();
//        }
//


        // Toast.makeText(context, "onStart "+value1+" "+pos1, Toast.LENGTH_SHORT).show();
       //------------------------------------------------------------------
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String value = sharedPreferences.getString(GROUPSNAME_SHARED_PREF, "");
        int pos = sharedPreferences.getInt(POSITION_AD, 0);
        if (TextUtils.equals("gradient", value)) {

            if (GradientFrag.mygradient.size() > 0)
                kv.setBackgroundResource(GradientFrag.mygradient.get(pos).getTheme_image());
        } else if (TextUtils.equals("flag", value)) {

            if (FlagsFrag.myflag.size() > 0)
                kv.setBackgroundResource(FlagsFrag.myflag.get(pos).getTheme_image());
        } else if (TextUtils.equals("image", value)) {

            if (ImageFrag.myimg.size() > 0)
                kv.setBackgroundResource(ImageFrag.myimg.get(pos).getTheme_image());
        } else if (TextUtils.equals("sport", value)) {


            if (SportsFrag.mysport.size() > 0)
                kv.setBackgroundResource(SportsFrag.mysport.get(pos).getTheme_image());
        } else {

            kv.setBackgroundResource(R.drawable.gradient_0);
        }

        super.onStartInputView(info, restarting);
    }
    void speechToText() {
        Toast.makeText(this, "Listening...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                "com.example.create_keyboard1");

        SpeechRecognizer recognizer = SpeechRecognizer
                .createSpeechRecognizer(this.getApplicationContext());
        RecognitionListener listener = new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> voiceResults = results
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (voiceResults == null) {
                    System.out.println("No voice results");
                } else {
                    System.out.println("Printing matches: ");
//                    for (String match : voiceResults) {
//                        System.out.println(match);
//                        Log.i("tag",match);
//
//                    }
                    ic.commitText(voiceResults.get(0), 1);
                    Log.i("tag", voiceResults.get(0));
//                    kv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onReadyForSpeech(Bundle params) {
                System.out.println("Ready for speech");
                Log.i("tag", "Ready for speech");

            }

            /**
             *  ERROR_NETWORK_TIMEOUT = 1;
             *  ERROR_NETWORK = 2;
             *  ERROR_AUDIO = 3;
             *  ERROR_SERVER = 4;
             *  ERROR_CLIENT = 5;
             *  ERROR_SPEECH_TIMEOUT = 6;
             *  ERROR_NO_MATCH = 7;
             *  ERROR_RECOGNIZER_BUSY = 8;
             *  ERROR_INSUFFICIENT_PERMISSIONS = 9;
             *
             * @param error code is defined in SpeechRecognizer
             */
            @Override
            public void onError(int error) {
                System.err.println("Error listening for speech: " + error);
                Log.i("tag", "Error listening for speech: " + error);
            }

            @Override
            public void onBeginningOfSpeech() {
                System.out.println("Speech starting");
                Log.i("tag", "Speech starting");
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                // TODO Auto-generated method stub
                Log.i("tag", "on buffered received");

            }

            @Override
            public void onEndOfSpeech() {
                // TODO Auto-generated method stub
                Log.i("tag", "onEndOfSpeech");


            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                // TODO Auto-generated method stub
                Log.i("tag", "onEvent");

            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                // TODO Auto-generated method stub
                Log.i("tag", "onPartialResults");

            }

            @Override
            public void onRmsChanged(float rmsdB) {
                // TODO Auto-generated method stub
                Log.i("tag", "onRmsChanged");

            }
        };
        recognizer.setRecognitionListener(listener);
        recognizer.startListening(intent);
    }


}





