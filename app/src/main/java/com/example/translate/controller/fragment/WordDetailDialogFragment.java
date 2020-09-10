package com.example.translate.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.translate.R;
import com.example.translate.model.WordTranslate;
import com.example.translate.repository.WordDBRepository;

import java.util.ArrayList;
import java.util.List;

public class WordDetailDialogFragment extends DialogFragment {

    public static final String EXTRA_RESPONSE_WORD_OBJECT= "WordObjectResponse";
    private WordDBRepository mRepository;
    public static final String ARGS_WORD_OBJECT = "wordObject";

    private EditText mEditTextPersian,mEditTextEnglish,mEditTextFrance,mEditTextArab;
    private Button mButtonSave, mButtonEdit , mButtonDelete;
    private WordTranslate mWord;

    public static WordDetailDialogFragment newInstance(WordTranslate wordTranslate) {
        WordDetailDialogFragment fragment = new WordDetailDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_WORD_OBJECT, wordTranslate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = WordDBRepository.getInstance(getActivity());
        mWord = (WordTranslate) getArguments().getSerializable(ARGS_WORD_OBJECT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_word_detail_dialog,null);
        findViews(view);
        setAllListener();
        initUI();
        return view;
    }

    private void findViews(View view) {
        mEditTextPersian = view.findViewById(R.id.edit_text_persian);
        mEditTextEnglish = view.findViewById(R.id.edit_text_english);
        mEditTextFrance = view.findViewById(R.id.edit_text_france);
        mEditTextArab = view.findViewById(R.id.edit_text_arab);
        mButtonDelete = view.findViewById(R.id.btn_delete);
        mButtonEdit = view.findViewById(R.id.btn_edit);
        mButtonSave = view.findViewById(R.id.btn_save);
    }

    private void setAllListener(){
        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEnableEditTexts(true);
            }
        });
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditTextPersian.getText().length()== 0 && mEditTextEnglish.getText().length()== 0 &&mEditTextFrance.getText().length()== 0&&mEditTextArab.getText().length()== 0){
                    Toast.makeText(getActivity(), "all text filed is blank , please fill it.", Toast.LENGTH_SHORT).show();
                    return;
                }
                mWord.setPersian(mEditTextPersian.getText().toString());
                mWord.setEnglish(mEditTextEnglish.getText().toString());
                mWord.setFrance(mEditTextFrance.getText().toString());
                mWord.setArabian(mEditTextArab.getText().toString());
                mRepository.update(mWord);
                setResult(mWord);
                dismiss();
            }
        });
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRepository.delete(mWord);
                setResult();
                dismiss();
            }
        });
    }
    private void initUI(){
        mEditTextPersian.setText(mWord.getPersian());
        mEditTextEnglish.setText(mWord.getEnglish());
        mEditTextFrance.setText(mWord.getFrance());
        mEditTextArab.setText(mWord.getArabian());
        isEnableEditTexts(false);
    }

    private void isEnableEditTexts(Boolean b){
        mEditTextArab.setEnabled(b);
        mEditTextPersian.setEnabled(b);
        mEditTextFrance.setEnabled(b);
        mEditTextEnglish.setEnabled(b);
    }

    private void setResult(WordTranslate word){
        Fragment fragment = getTargetFragment();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESPONSE_WORD_OBJECT,word);
        fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
    }
    private void setResult(){
        Fragment fragment = getTargetFragment();
        Intent intent = new Intent();
        fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
    }
}