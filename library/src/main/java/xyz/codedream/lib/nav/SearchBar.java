package xyz.codedream.lib.nav;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by sky.
 */
public class SearchBar extends LinearLayout {
    public SearchBar(Context context) {
        super(context);
        init();
    }

    private Drawable mEditTextBg;

    public SearchBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchBar);
        mEditTextBg = a.getDrawable(R.styleable.SearchBar_search_bar_edit_bankgroud);
        a.recycle();
        init();
    }

    private EditText mEditText;
    private View mClearKeywordBtn;
    private TextView mActionBtn;

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.temp_common_serach_bar, this, true);
        mEditText = (EditText) findViewById(R.id.et_search_keyword);
        mEditText.addTextChangedListener(textWatcher);
        mEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && (event == null || event.getAction() == KeyEvent.ACTION_UP)) {
                    hideInputMethod();
                    if (mSearchBarListener != null) {
                        mSearchBarListener.onRequestDoSearch(SearchBar.this, getKeyword());
                    }
                }
                return false;
            }
        });
        if (mEditTextBg != null) {
            mEditText.setBackgroundDrawable(mEditTextBg);
        }
        //
        mClearKeywordBtn = findViewById(R.id.btn_search_keyword_clear);
        mActionBtn = (TextView) findViewById(R.id.btn_search_cancel);
        mActionBtn.setOnClickListener(onClickListener);
        mClearKeywordBtn.setOnClickListener(onClickListener);
        //
        checkClearButtonShow();
    }

    public void clearKeyword() {
        mEditText.setText("");
        mEditText.clearFocus();
        this.requestFocus();
    }

    public void setKeyword(String keyword) {
        mEditText.setText(keyword);
    }

    private void hideInputMethod() {
        if (mEditText != null) {
            InputMethodManager ipm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            ipm.hideSoftInputFromWindow(mEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkClearButtonShow();
        }
    };

    private void checkClearButtonShow() {
        if (haveValideKeyword()) {
            mClearKeywordBtn.setVisibility(View.VISIBLE);
            mActionBtn.setText("搜索");
        } else {
            mClearKeywordBtn.setVisibility(View.INVISIBLE);
            mActionBtn.setText("取消");
        }
    }

    private final OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_search_keyword_clear) {
                mEditText.setText("");
            } else if (v.getId() == R.id.btn_search_cancel) {
                hideInputMethod();
                if (mSearchBarListener != null) {
                    if (haveValideKeyword()) {
                        mSearchBarListener.onRequestDoSearch(SearchBar.this, getKeyword());
                    } else {
                        mSearchBarListener.onRequestHideSearchBar(SearchBar.this);
                    }
                }
            }
        }
    };

    public void setSearchHint(String hint) {
        mEditText.setHint(hint);
    }

    public boolean haveValideKeyword() {
        String k = getKeyword();
        return k != null && k.trim().length() > 0;
    }

    public String getKeyword() {
        Editable e = mEditText.getText();
        if (e == null) {
            return "";
        } else {
            return e.toString();
        }
    }

    public void setActionButtonVisiblity(boolean show) {
        if (show) {
            mActionBtn.setVisibility(View.VISIBLE);
        } else {
            mActionBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        super.setOnClickListener(onClickListener);
        if (onClickListener == null) {
            mEditText.setFocusable(true);
            mEditText.setFocusableInTouchMode(true);
            mEditText.setClickable(true);
        } else {
            mEditText.setFocusable(false);
            mEditText.setFocusableInTouchMode(false);
            mEditText.setClickable(false);
        }
    }

    private SearchBarListener mSearchBarListener;

    public void setSearchBarListener(SearchBarListener l) {
        mSearchBarListener = l;
    }

    public interface SearchBarListener {
        public void onRequestHideSearchBar(SearchBar bar);

        public void onRequestDoSearch(SearchBar bar, String keyword);
    }
}
