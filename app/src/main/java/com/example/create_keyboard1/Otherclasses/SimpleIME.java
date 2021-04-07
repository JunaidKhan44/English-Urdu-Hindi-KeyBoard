package com.example.create_keyboard1.Otherclasses;

import android.Manifest;
import android.annotation.SuppressLint;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.MetaKeyKeyListener;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.Toast;
import com.example.create_keyboard1.Fragments.FlagsFrag;
import com.example.create_keyboard1.Fragments.GradientFrag;
import com.example.create_keyboard1.Fragments.ImageFrag;
import com.example.create_keyboard1.Fragments.SportsFrag;
import com.example.create_keyboard1.R;
import com.example.create_keyboard1.database.DatabaseManager;
import com.example.create_keyboard1.util.Keyboard;
import com.example.create_keyboard1.util.KeyboardView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.create_keyboard1.Adapter.KeyThemeAdapter.GROUPSNAME_SHARED_PREF1;
import static com.example.create_keyboard1.Adapter.KeyThemeAdapter.POSITION_AD1;
import static com.example.create_keyboard1.Adapter.KeyThemeAdapter.SHARED_PREF_NAME1;
import static com.example.create_keyboard1.Adapter.MyAdapter.GROUPSNAME_SHARED_PREF;
import static com.example.create_keyboard1.Adapter.MyAdapter.POSITION_AD;
import static com.example.create_keyboard1.Adapter.MyAdapter.SHARED_PREF_NAME;
import static com.example.create_keyboard1.Otherclasses.CamGalleryBackground.SHARED_PREF_BACKID;
import static com.example.create_keyboard1.Otherclasses.CamGalleryBackground.SHARED_PREF_CUSTOMBACK;
import static com.example.create_keyboard1.Otherclasses.SettingActivity.POSITION_AD_PREVIEW;
import static com.example.create_keyboard1.Otherclasses.SettingActivity.PREVIEW_PREF_NAME;


public class SimpleIME extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {


    boolean  sphenglish,sphurdu,sphhindu;
    public static InputConnection ic;
    public boolean flagforemoji = false;
    public static KeyboardView kv;
    public Keyboard keyboard;
    SharedPreferences sharedPreferences, sharedPreferencesforcustback;
    SharedPreferences forpreview;
    private boolean caps = false;
    public boolean isflagforurdu=false;
    private boolean flagforenglish=false;
    //new urdu key code---------
    private SharedPreferences sharedPreferences2;
    private int mLastDisplayWidth;
    private InputMethodManager mInputMethodManager;
    private String mWordSeparators;
    private CandidateView mCandidateView;
    private long mMetaState;
    private DatabaseManager db;
    private StringBuilder mComposing = new StringBuilder();
    public static boolean mPredictionOn;
    private boolean mCompletionOn;
    private CompletionInfo[] mCompletions;
    private boolean mSound;
    private ArrayList<String> list;
    static final boolean PROCESS_HARD_KEYS = true;

    public static String mActiveKeyboard;
    private BitmapDrawable mBitmapDrawable;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        flagforemoji = false;





        //for preview purpose
        forpreview = getSharedPreferences(PREVIEW_PREF_NAME, MODE_PRIVATE);
        int isval=forpreview.getInt(POSITION_AD_PREVIEW,1);
        if(isval==0){
            kv.setPreviewEnabled(false);
            Log.d("simpleime",""+0);
        }
        if(isval==1){
            kv.setPreviewEnabled(true);
            Log.d("simpleime",""+1);
        }

        //for theme purpose
        sharedPreferences2 = getSharedPreferences(SHARED_PREF_NAME1, MODE_PRIVATE);
        int pos1 = sharedPreferences2.getInt(POSITION_AD1, 0);
        if (pos1 == 0) {
            kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
            Log.d("simpleime","Theme 0");

        } else if (pos1 == 1) {
            kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard2, null);
            Log.d("simpleime","Theme 1");
        } else if (pos1 == 2) {
            kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard3, null);
            Log.d("simpleime","Theme 2");
        }

        keyboard = new Keyboard(this, R.xml.custom_qwerty);
        kv.setKeyboard(keyboard);
        flagforenglish=true;
        isflagforurdu=false;
        sphenglish=true;

        //shared preference code start
        //for background  change
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String value = sharedPreferences.getString(GROUPSNAME_SHARED_PREF, "");
        int pos = sharedPreferences.getInt(POSITION_AD, 0);
        if (TextUtils.equals("gradient", value)) {

            if (GradientFrag.mygradient.size() > 0)
                kv.setBackgroundResource(GradientFrag.mygradient.get(pos).getTheme_image());
            Log.d("simpleime","background g");
        } else if (TextUtils.equals("flag", value)) {

            if (FlagsFrag.myflag.size() > 0)
                kv.setBackgroundResource(FlagsFrag.myflag.get(pos).getTheme_image());
            Log.d("simpleime","background f");
        } else if (TextUtils.equals("image", value)) {

            if (ImageFrag.myimg.size() > 0)
                kv.setBackgroundResource(ImageFrag.myimg.get(pos).getTheme_image());
            Log.d("simpleime","background i");
        } else if (TextUtils.equals("sport", value)) {

            if (SportsFrag.mysport.size() > 0)
                kv.setBackgroundResource(SportsFrag.mysport.get(pos).getTheme_image());
            Log.d("simpleime","background s");
        } else {

                kv.setBackgroundResource(R.drawable.gradient_0);
                Log.d("simpleime", "default");

        }



        sharedPreferencesforcustback = getSharedPreferences(SHARED_PREF_CUSTOMBACK, MODE_PRIVATE);
        String  urival=sharedPreferencesforcustback.getString(SHARED_PREF_BACKID,"");

        if(!urival.isEmpty()){

//
//            String base = urival;
//            byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
//            Bitmap b = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
//            mBitmapDrawable = new BitmapDrawable(getResources(), b);
//            this.kv.setBackground(mBitmapDrawable);
//
//            String data=getFileToByte(urival);
//            kv.setBackgroundResource(Integer.parseInt(data));
//
//            Bitmap  b= getBitmapFromURL(urival);
//            Drawable d= new BitmapDrawable(this.getResources(), b);
//            kv.setBackground(d);

            Log.d("simpleime","custom background "+urival);
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
    @Override
    public void onInitializeInterface() {
        if (keyboard != null) {
            // Configuration changes can happen after the keyboard gets recreated,
            // so we need to be able to re-build the keyboards if the available
            // space has changed.
            int displayWidth = getMaxWidth();
            if (displayWidth == mLastDisplayWidth) return;
            mLastDisplayWidth = displayWidth;
        }
        keyboard = new Keyboard(this, R.xml.custom_qwerty);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mWordSeparators = getResources().getString(R.string.word_separators);

        //optional line may commit
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }
    @Override
    public View onCreateCandidatesView() {
        mCandidateView = new CandidateView(this);
        mCandidateView.setService(this);

        return mCandidateView;
    }
    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);

        // Restart the InputView to apply right theme selected.
        setInputView(onCreateInputView());
        // Reset our state.  We want to do this even if restarting, because
        // the underlying state of the text editor could have changed in any way.
        mComposing.setLength(0);
        updateCandidates();
        if (!restarting) {
            // Clear shift states.
            mMetaState = 0;
        }

        mPredictionOn = false;
        mCompletionOn = false;
        mCompletions = null;

        // We are now going to initialize our state based on the type of
        // text being edited.
        switch (attribute.inputType & InputType.TYPE_MASK_CLASS) {
            case InputType.TYPE_CLASS_NUMBER:
                // mCurKeyboard = mNumbersKeyboard;
                break;
            case InputType.TYPE_CLASS_DATETIME:
                // Numbers and dates default to the symbols keyboard, with
                // no extra features.
                //mCurKeyboard = mSymbolsKeyboard;
                break;

            case InputType.TYPE_CLASS_PHONE:
                // Phones will also default to the symbols keyboard, though
                // often you will want to have a dedicated phone keyboard.
                //mCurKeyboard = mPhoneKeyboard;
                break;

            case InputType.TYPE_CLASS_TEXT:
                // This is general text editing.  We will default to the
                // normal alphabetic keyboard, and assume that we should
                // be doing predictive text (showing candidates as the
                // user types).
                //mCurKeyboard = getSelectedSubtype();
                mPredictionOn = sharedPreferences.getBoolean("suggestion", true);

                // We now look for a few special variations of text that will
                // modify our behavior.
                int variation = attribute.inputType & InputType.TYPE_MASK_VARIATION;
                if (variation == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
                        variation == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Do not display predictions / what the user is typing
                    // when they are entering a password.
                    mPredictionOn = false;
                }

                if (variation == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                        || variation == InputType.TYPE_TEXT_VARIATION_URI
                        || variation == InputType.TYPE_TEXT_VARIATION_FILTER) {
                    // Our predictions are not useful for e-mail addresses
                    // or URIs.
                    mPredictionOn = false;
                    //  mActiveKeyboard = "en_US";
                    // mCurKeyboard = mQwertyKeyboard;
                }

                if ((attribute.inputType & InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE) != 0) {
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
             //   updateShiftKeyState(attribute);
                break;

            default:
                // For all unknown input types, default to the alphabetic
                // keyboard with no special features.
               // mCurKeyboard = getSelectedSubtype();
               // updateShiftKeyState(attribute);
        }
//        if (mCurKeyboard == mPashtoLatinKeyboard || mCurKeyboard == mPashtoLatinShiftedKeyboard)
//            mPredictionOn = false;
        if (mPredictionOn) db = new DatabaseManager(this);

        // Update the label on the enter key, depending on what the application
        // says it will do.
        keyboard.setImeOptions(getResources(), attribute.imeOptions);

        mSound = sharedPreferences.getBoolean("sound", true);

        // Apply the selected keyboard to the input view.
       // setLatinKeyboard(mCurKeyboard);
    }
    @Override
    public void onFinishInput() {
        super.onFinishInput();

        // clear suggestions list
        clearCandidateView();

        // Clear current composing text and candidates.
        mComposing.setLength(0);
        updateCandidates();

        // We only hide the candidates window when finishing input on
        // a particular editor, to avoid popping the underlying application
        // up and down if the user is entering text into the bottom of
        // its window.
        setCandidatesViewShown(false);

//        mCurKeyboard = mQwertyKeyboard;
//        if (mInputView != null) {
//            mInputView.closing();
//        }

        if (db != null) db.close();
    }
    public void clearCandidateView() {
        if (list != null) list.clear();
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


        forpreview = getSharedPreferences(PREVIEW_PREF_NAME, MODE_PRIVATE);
        int isval=forpreview.getInt(POSITION_AD_PREVIEW,1);
        if(isval==0){
            kv.setPreviewEnabled(false);
          //  Log.d("simpleime","onstat "+0);
        }
        if(isval==1){
            kv.setPreviewEnabled(true);
        //    Log.d("simpleime","onstat "+1);
        }



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
                Log.d("simpleime", "on stat default");

        }


        sharedPreferencesforcustback = getSharedPreferences(SHARED_PREF_CUSTOMBACK, MODE_PRIVATE);
        String  urival=sharedPreferencesforcustback.getString(SHARED_PREF_BACKID,"");

        if(!urival.isEmpty()){
//
//            Bitmap  b= getBitmapFromURL(urival);
//            Drawable d= new BitmapDrawable(this.getResources(), b);
//            //base 64 then use
//            kv.setBackground(d);
//
//            String base = urival;
//            byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
//            Bitmap b = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
//            mBitmapDrawable = new BitmapDrawable(getResources(), b);
//            this.kv.setBackground(mBitmapDrawable);
//            kv.setBackground(getResources().getDrawable(R.drawable.picart));
            Log.d("simpleime","on stat custom background"+urival);

        }


        super.onStartInputView(info, restarting);
//        kv.closing();
//        final InputMethodSubtype subtype = mInputMethodManager.getCurrentInputMethodSubtype();
//        kv.setSubtypeOnSpaceKey(subtype);

    }

    /**
     * Switch to language when it is changed from Choose Input Method.
     */
    @Override
    public void onCurrentInputMethodSubtypeChanged(InputMethodSubtype subtype) {
    //    kv.setSubtypeOnSpaceKey(subtype);
        String s = subtype.getLocale();
        switch (s) {
            case "ps_AF":
//                mActiveKeyboard = "ps_AF";
//                mCurKeyboard = mPashtoKeyboard;
//
                break;
            case "ps_latin_AF":
//                mActiveKeyboard = "ps_latin_AF";
//                mCurKeyboard = mPashtoLatinKeyboard;
//
                break;
            case "fa_AF":
//                mActiveKeyboard = "fa_AF";
//                mCurKeyboard = mFarsiKeyboard;
//
                break;
            default:
//                mActiveKeyboard = "en_US";
//                mCurKeyboard = mQwertyKeyboard;
//
        }

        // Apply the selected keyboard to the input view.
        //setLatinKeyboard(mCurKeyboard);
    }
    /**
     * Deal with the editor reporting movement of its cursor.
     */
    @Override
    public void onUpdateSelection(int oldSelStart, int oldSelEnd,
                                  int newSelStart, int newSelEnd,
                                  int candidatesStart, int candidatesEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd,
                candidatesStart, candidatesEnd);

        // If the current selection in the text view changes, we should
        // clear whatever candidate text we have.
        if (mComposing.length() > 0 && (newSelStart != candidatesEnd
                || newSelEnd != candidatesEnd)) {
            mComposing.setLength(0);
            updateCandidates();
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.finishComposingText();
            }
        }
    }
    /**
     * This tells us about completions that the editor has determined based
     * on the current text in it.  We want to use this in fullscreen mode
     * to show the completions ourselves, since the editor can not be seen
     * in that situation.
     */
    @Override
    public void onDisplayCompletions(CompletionInfo[] completions) {
        if (mCompletionOn) {
            mCompletions = completions;
            if (completions == null) {
                setSuggestions(null, false, false);
                return;
            }

            List<String> stringList = new ArrayList<>();
            for (CompletionInfo ci : completions) {
                if (ci != null) stringList.add(ci.getText().toString());
            }
            setSuggestions(stringList, true, true);
        }
    }
    /**
     * This translates incoming hard key events in to edit operations on an
     * InputConnection.  It is only needed when using the
     * PROCESS_HARD_KEYS option.
     */
    private boolean translateKeyDown(int keyCode, KeyEvent event) {
        mMetaState = MetaKeyKeyListener.handleKeyDown(mMetaState,
                keyCode, event);
        int c = event.getUnicodeChar(MetaKeyKeyListener.getMetaState(mMetaState));
        mMetaState = MetaKeyKeyListener.adjustMetaAfterKeypress(mMetaState);
        InputConnection ic = getCurrentInputConnection();
        if (c == 0 || ic == null) {
            return false;
        }

        if ((c & KeyCharacterMap.COMBINING_ACCENT) != 0) {
            c = c & KeyCharacterMap.COMBINING_ACCENT_MASK;
        }

        if (mComposing.length() > 0) {
            char accent = mComposing.charAt(mComposing.length() - 1);
            int composed = KeyEvent.getDeadChar(accent, c);
            if (composed != 0) {
                c = composed;
                mComposing.setLength(mComposing.length() - 1);
            }
        }

        onKey(c, null);

        return true;
    }
    /**
     * Use this to monitor key events being delivered to the application.
     * We get first crack at them, and can either resume them or let them
     * continue to the app.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
                    return true;
                }
                break;

            case KeyEvent.KEYCODE_ENTER:
                // Let the underlying text editor always handle these.
                return false;

            default:
                // For all other keys, if we want to do transformations on
                // text being entered with a hard keyboard, we need to process
                // it and do the appropriate action.
                if (PROCESS_HARD_KEYS) {
                    if (keyCode == KeyEvent.KEYCODE_SPACE
                            && (event.getMetaState() & KeyEvent.META_ALT_ON) != 0) {
                        // A silly example: in our input method, Alt+Space
                        // is a shortcut for 'android' in lower case.
                        InputConnection ic = getCurrentInputConnection();
                        if (ic != null) {
                            // First, tell the editor that it is no longer in the
                            // shift state, since we are consuming this.
                            ic.clearMetaKeyStates(KeyEvent.META_ALT_ON);
                            keyDownUp(KeyEvent.KEYCODE_A);
                            keyDownUp(KeyEvent.KEYCODE_N);
                            keyDownUp(KeyEvent.KEYCODE_D);
                            keyDownUp(KeyEvent.KEYCODE_R);
                            keyDownUp(KeyEvent.KEYCODE_O);
                            keyDownUp(KeyEvent.KEYCODE_I);
                            keyDownUp(KeyEvent.KEYCODE_D);
                            // And we consume this event.
                            return true;
                        }
                    }
                    if (mPredictionOn && translateKeyDown(keyCode, event)) {
                        return true;
                    }
                }
        }

        return super.onKeyDown(keyCode, event);
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
            case -16:
                keyboard = new Keyboard(this, R.xml.urdu);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                isflagforurdu=true;
                flagforenglish=false;
                sphenglish=false;
                sphhindu=false;
                sphurdu=true;
                break;

            case -14:
                Intent i = new Intent(this, Background.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;

            case -11:
                requestHideSelf(0);
                break;
            case -13:

                Dexter.withContext(this)
                        .withPermissions(
                                Manifest.permission.RECORD_AUDIO)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {

                                if (report.areAllPermissionsGranted()) {
                                    Log.i("tag", "permission granted");

                                    if(sphurdu){
                                        speechToTextMultipleUrdu();
                                        sphhindu=false;
                                        sphenglish=false;
                                    }
                                    else if(sphhindu){
                                        speechToTextMultipleHindi();
                                        sphenglish=false;
                                        sphurdu=false;
                                    }
                                    else{
                                        speechToText();
                                        sphurdu=false;
                                        sphhindu=false;
                                    }
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
            case -15:
                Intent intent = new Intent(this, SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;

            default:

        }
        /////////////////////////////////////////////

        if (isWordSeparator(primaryCode)) {
            // Handle separator
            if (mComposing.length() > 0) {
                commitTyped(getCurrentInputConnection());
                Log.d("mytagfor","world separator");
            }
            if (primaryCode == 32) {
                if (list != null) {
                    clearCandidateView();
                }

                // Add update word in the dictionary
                try {
                    addUpdateWord();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            sendKey(primaryCode);
    //        updateShiftKeyState(getCurrentInputEditorInfo());
        } else if (primaryCode == android.inputmethodservice.Keyboard.KEYCODE_DELETE) {
            handleBackspace();
        } else if (primaryCode == android.inputmethodservice.Keyboard.KEYCODE_SHIFT) {
            //handleShift();
        } else if (primaryCode == android.inputmethodservice.Keyboard.KEYCODE_CANCEL) {
            handleClose();
            return;
        }
         else if (primaryCode == -10000) {
            // Show Emoticons

        } else if (primaryCode == -10001) {
            // Zero Space
            mComposing.append("\u200C");
            getCurrentInputConnection().setComposingText(mComposing, 1);
        } else if (primaryCode == -10002) {
            // ẋ
            mComposing.append("ẋ");
            getCurrentInputConnection().setComposingText(mComposing, 1);
        } else if (primaryCode == -10003) {
            // Ẋ
            mComposing.append("\u1E8A");
            getCurrentInputConnection().setComposingText(mComposing, 1);
        } else if (primaryCode == 1567) {
            // Question mark.
            mComposing.append("\u061F");
            getCurrentInputConnection().setComposingText(mComposing, 1);
        }
         else {
             Log.d("mytagfor","else handlecharater");
             if(primaryCode==-100002){
                 Log.d("mytagfor","code execute");
                     keyboard = new Keyboard(this, R.xml.hindi1);
                     kv.setKeyboard(keyboard);
                     kv.setOnKeyboardActionListener(this);
                     sphurdu=false;
                     sphenglish=false;
                     sphhindu=true;

             }
             else if(primaryCode==0x2752){
                     keyboard = new Keyboard(this, R.xml.custom_qwerty);
                     kv.setKeyboard(keyboard);
                     kv.setOnKeyboardActionListener(this);
                     flagforenglish=true;
                     isflagforurdu=false;
                 sphurdu=false;
                 sphenglish=true;
                 sphhindu=false;

             }
             else if(primaryCode==-6) {
                     keyboard = new Keyboard(this, R.xml.roman);
                     kv.setKeyboard(keyboard);
                     kv.setOnKeyboardActionListener(this);
                 sphurdu=false;
                 sphenglish=true;
                 sphhindu=false;

             }
             else if(primaryCode==-61){

                     keyboard = new Keyboard(this, R.xml.custom_qwerty);
                     kv.setKeyboard(keyboard);
                     kv.setOnKeyboardActionListener(this);
                     flagforenglish=true;
                     isflagforurdu=false;

                 sphurdu=false;
                 sphenglish=true;
                 sphhindu=false;

             }
             else if(primaryCode==0x2121){

                     keyboard = new Keyboard(this, R.xml.roman2);
                     kv.setKeyboard(keyboard);
                     kv.setOnKeyboardActionListener(this);

                 sphurdu=false;
                 sphenglish=true;
                 sphhindu=false;

             }
             else if(primaryCode==0x221E){
                     keyboard = new Keyboard(this, R.xml.roman);
                     kv.setKeyboard(keyboard);
                     kv.setOnKeyboardActionListener(this);

                 sphurdu=false;
                 sphenglish=true;
                 sphhindu=false;

             }
             else if(primaryCode==-62){
                     keyboard = new Keyboard(this, R.xml.urdu_numeric);
                     kv.setKeyboard(keyboard);
                     kv.setOnKeyboardActionListener(this);
                     isflagforurdu=true;
                     flagforenglish=false;


                 sphurdu=true;
                 sphenglish=false;
                 sphhindu=false;

             }
             else if(primaryCode==-63){

                     keyboard = new Keyboard(this, R.xml.hindi2);
                     kv.setKeyboard(keyboard);
                     kv.setOnKeyboardActionListener(this);

                 sphurdu=false;
                 sphenglish=false;
                 sphhindu=true;

             }
             else if(primaryCode==-64){

                     keyboard = new Keyboard(this, R.xml.hindi3);
                     kv.setKeyboard(keyboard);
                     kv.setOnKeyboardActionListener(this);


                 sphurdu=false;
                 sphenglish=false;
                 sphhindu=true;

             }
             else if(primaryCode==-65){

                     keyboard = new Keyboard(this, R.xml.hindi1);
                     kv.setKeyboard(keyboard);
                     kv.setOnKeyboardActionListener(this);

                 sphurdu=false;
                 sphenglish=false;
                 sphhindu=true;

             }
             else if(primaryCode==-4){

                     ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));

             }
             else if(primaryCode==207){
                     keyboard = new Keyboard(this, R.xml.custom_qwerty);
                     kv.setKeyboard(keyboard);
                     kv.setOnKeyboardActionListener(this);
                     flagforenglish=true;
                     isflagforurdu=false;

                 sphurdu=false;
                 sphenglish=true;
                 sphhindu=false;

             }
             else if(primaryCode==-12){
                     flagforemoji = true;
                     keyboard = new Keyboard(this, R.xml.emojis);
                     kv.setKeyboard(keyboard);
                     kv.setOnKeyboardActionListener(this);

                 sphurdu=false;
                 sphenglish=true;
                 sphhindu=false;
             }
             else if(primaryCode==0x0730){

                     keyboard = new Keyboard(this, R.xml.time_emoji);
                     kv.setKeyboard(keyboard);
                     kv.setOnKeyboardActionListener(this);

                 sphurdu=false;
                 sphenglish=true;
                 sphhindu=false;

             }
             else if(primaryCode==0x0731){

                     keyboard = new Keyboard(this, R.xml.emojis);
                     kv.setKeyboard(keyboard);
                     kv.setOnKeyboardActionListener(this);

                 sphurdu=false;
                 sphenglish=true;
                 sphhindu=false;

             }
             else if(primaryCode==0x0732){

                     keyboard = new Keyboard(this, R.xml.weather_emojis);
                     kv.setKeyboard(keyboard);
                     kv.setOnKeyboardActionListener(this);

                 sphurdu=false;
                 sphenglish=true;
                 sphhindu=false;

             }
             else if(primaryCode==0x0733){
                     keyboard = new Keyboard(this, R.xml.heart_emojis);
                     kv.setKeyboard(keyboard);
                     kv.setOnKeyboardActionListener(this);

                 sphurdu=false;
                 sphenglish=true;
                 sphhindu=false;

             }
             else  if (flagforemoji) {
                 Log.d("mytagfor","default emojis");
               // mComposing.append(String.valueOf(Character.toChars(primaryCode)));
                // getCurrentInputConnection().commitText(String.valueOf(Character.toChars(primaryCode)), 1);
                 try {
                     ic.commitText(String.valueOf(Character.toChars(primaryCode)),1);

                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }
             else {

                     handleCharacter(primaryCode, keyCodes);
             }
        }

        if (mSound) playClick(primaryCode); // Play sound with button click.
    }

    /**
     * Use this to monitor key events being delivered to the application.
     * We get first crack at them, and can either resume them or let them
     * continue to the app.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // If we want to do transformations on text being entered with a hard
        // keyboard, we need to process the up events to update the meta key
        // state we are tracking.
        if (PROCESS_HARD_KEYS) {
            if (mPredictionOn) {
                mMetaState = MetaKeyKeyListener.handleKeyUp(mMetaState,
                        keyCode, event);
            }
        }

        return super.onKeyUp(keyCode, event);
    }
    /**
     * Helper function to commit any text being composed in to the editor.
     */
    private void commitTyped(InputConnection inputConnection) {
        if (mComposing.length() > 0) {
            inputConnection.commitText(mComposing, mComposing.length());
            mComposing.setLength(0);
            updateCandidates();
        }
    }
    /**
     * Helper to determine if a given character code is alphabetic.
     */
    private boolean isAlphabet(int code) {
        return Character.isLetter(code);
    }
    /**
     * Helper to send a key down / key up pair to the current editor.
     */
    private void keyDownUp(int keyEventCode) {
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
    }
    /**
     * Helper to send a character to the editor as raw key events.
     */
    private void sendKey(int keyCode) {
        switch (keyCode) {
            case '\n':
                keyDownUp(KeyEvent.KEYCODE_ENTER);
                break;
            default:
                if (keyCode >= '0' && keyCode <= '9') {
                    keyDownUp(keyCode - '0' + KeyEvent.KEYCODE_0);
                } else {
                    getCurrentInputConnection().commitText(String.valueOf((char) keyCode), 1);
                    Log.d("mytagfor","sendKey");
                }
                break;
        }
    }

    /**
     * Implementation of KeyboardViewListener
     */


    /**
     * Play sound when key is pressed.
     */
    private void playClick(int keyCode) {
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (am != null) {
            switch (keyCode) {
                case 32:
                    am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                    break;
                case android.inputmethodservice.Keyboard.KEYCODE_DONE:
                case 10:
                    am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                    break;
                case android.inputmethodservice.Keyboard.KEYCODE_DELETE:
                    am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                    break;
                default:
                    am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
            }
        }
    }

    public void onText(CharSequence text) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        ic.beginBatchEdit();
        if (mComposing.length() > 0) {
            commitTyped(ic);
        }
        ic.commitText(text, 0);
        ic.endBatchEdit();
      //  updateShiftKeyState(getCurrentInputEditorInfo());
    }

    /**
     * Update the list of available candidates from the current composing
     * text.  This will need to be filled in by however you are determining
     * candidates.
     */
    private void updateCandidates() {
        if (!mCompletionOn && mPredictionOn) {
            if (mComposing.length() > 0) {
                SelectDataTask selectDataTask = new SelectDataTask();

                list = new ArrayList<>();
                list.add(mComposing.toString());

                selectDataTask.getSubtype(keyboard);
                selectDataTask.execute(mComposing.toString());
            } else {
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
            mCandidateView.setSuggestions(suggestions, completions, typedWordValid);
        }
    }

    private void handleBackspace() {
        final int length = mComposing.length();
        if (length > 1) {
            mComposing.delete(length - 1, length);
            getCurrentInputConnection().setComposingText(mComposing, 1);
            updateCandidates();
        } else if (length > 0) {
            mComposing.setLength(0);
            getCurrentInputConnection().commitText("", 0);
            updateCandidates();
        } else {
            keyDownUp(KeyEvent.KEYCODE_DEL);
        }
    //    updateShiftKeyState(getCurrentInputEditorInfo());
    }

    private void handleShift() {
        if (kv == null) {
            return;
        }

    }

    private void handleCharacter(int primaryCode, int[] keyCodes) {
        if (isInputViewShown()) {
            if (kv.isShifted()) {
                primaryCode = Character.toUpperCase(primaryCode);
                Log.d("mytagfor","isInputviewshow");
            }
        }
        if (isAlphabet(primaryCode) && mPredictionOn) {
            mComposing.append((char) primaryCode);
            getCurrentInputConnection().setComposingText(mComposing, 1);
           // updateShiftKeyState(getCurrentInputEditorInfo());
            updateCandidates();
            Log.d("mytagfor","isAlphabet");
        } else {
            mComposing.append((char) primaryCode);
            getCurrentInputConnection().setComposingText(mComposing, 1);
            Log.d("mytagfor","else case");

        }

    }

    private void handleClose() {
        commitTyped(getCurrentInputConnection());
        requestHideSelf(0);
        kv.closing();
    }

    private IBinder getToken() {
        final Dialog dialog = getWindow();
        if (dialog == null) {
            return null;
        }
        final Window window = dialog.getWindow();
        if (window == null) {
            return null;
        }
        return window.getAttributes().token;
    }

    private void handleLanguageSwitch() {
        mInputMethodManager.switchToNextInputMethod(getToken(), true /* onlyCurrentIme */);
    }



    private String getWordSeparators() {
        return mWordSeparators;
    }

    public boolean isWordSeparator(int code) {
        String separators = getWordSeparators();
        return separators.contains(String.valueOf((char) code));
    }

    public void pickDefaultCandidate() {
        pickSuggestionManually(0);
    }

    // Tap on suggestion to commit
    public void pickSuggestionManually(int index) {
        if (mCompletionOn && mCompletions != null && index >= 0 && index < mCompletions.length) {
            CompletionInfo ci = mCompletions[index];
            getCurrentInputConnection().commitCompletion(ci);
            if (mCandidateView != null) {
                mCandidateView.clear();
            }
         //   updateShiftKeyState(getCurrentInputEditorInfo());
        } else if (mComposing.length() > 0) {
            // If we were generating candidate suggestions for the current
            // text, we would commit one of them here. But for this sample,
            // we will just commit the current text.
            mComposing.setLength(index);
            mComposing = new StringBuilder(list.get(index) + " ");
            commitTyped(getCurrentInputConnection());
        }
    }

    public void swipeRight() {
        if (mCompletionOn) {
            pickDefaultCandidate();
        }
    }

    public void swipeLeft() {
        handleBackspace();
    }

    public void swipeDown() {
        handleClose();
    }

    public void swipeUp() {
    }

    public void onPress(int primaryCode) {
        kv.setPreviewEnabled(true);

        // Disable preview key on Shift, Delete, Space, Language, Symbol and Emoticon.
//        if (primaryCode == -1 || primaryCode == -5 || primaryCode == -2 || primaryCode == -10000
//                || primaryCode == -101 || primaryCode == 32) {
//            kv.setPreviewEnabled(false);
//        }
    }

    public void onRelease(int primaryCode) {
    }

    /**
     * This class improves performance of the app when prediction is on.
     * The database query is executed in the background.
     */
    private class SelectDataTask extends AsyncTask<String, Void, ArrayList<String>> {

        private String subType;

        void getSubtype(Keyboard mCurKeyboard) {
            if (flagforenglish) {
                subType = "english";
            } else if(isflagforurdu) {
                subType = "pashto";
            }
        }

        @Override
        protected ArrayList<String> doInBackground(String... str) {
            list = db.getAllRow(str[0], subType);
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            list = result;
            setSuggestions(result, true, true);
        }
    }
    /**
     * Add or update word in the dictionary
     */
    public void addUpdateWord() {

        if (!getLastWord().isEmpty()) {
            Integer freq = db.getWordFrequency(getLastWord(), mActiveKeyboard);
            if (freq > 0) {
                db.updateRecord(getLastWord(), freq, mActiveKeyboard);
            } else {
                db.insertNewRecord(getLastWord(), mActiveKeyboard);
            }
        }
    }
    /**
     * Return a last word from input connection with space
     *
     * @return
     */
    public String getLastWord() {
        CharSequence inputChars = getCurrentInputConnection().getTextBeforeCursor(50, 0);
        String inputString = String.valueOf(inputChars);
        return inputString.substring(inputString.lastIndexOf(" ") + 1);
    }

    //--------------------------
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
                    ic.commitText(voiceResults.get(0), 1);
                    Log.i("tag", voiceResults.get(0));

                }
            }

            @Override
            public void onReadyForSpeech(Bundle params) {
                System.out.println("Ready for speech");
                Log.i("tag", "Ready for speech");

            }
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
    void speechToTextMultipleHindi() {
        Toast.makeText(this, "Listening...", Toast.LENGTH_SHORT).show();
        CharSequence language="hi-IN";
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language);
        intent.putExtra(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES, language);
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, language);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, language);
        intent.putExtra(RecognizerIntent.EXTRA_RESULTS, language);


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
                    ic.commitText(voiceResults.get(0), 1);
                    Log.i("tag", voiceResults.get(0));

                }
            }

            @Override
            public void onReadyForSpeech(Bundle params) {
                System.out.println("Ready for speech");
                Log.i("tag", "Ready for speech");

            }
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
    void speechToTextMultipleUrdu() {
        Toast.makeText(this, "Listening...", Toast.LENGTH_SHORT).show();
        CharSequence language="ur";
    //    CharSequence language="hi-IN";
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language);
        intent.putExtra(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES, language);
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, language);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, language);
        intent.putExtra(RecognizerIntent.EXTRA_RESULTS, language);


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
                    ic.commitText(voiceResults.get(0), 1);
                    Log.i("tag", voiceResults.get(0));

                }
            }

            @Override
            public void onReadyForSpeech(Bundle params) {
                System.out.println("Ready for speech");
                Log.i("tag", "Ready for speech");

            }
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


    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}




