package com.example.translate.controller.fragment;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.translate.R;
import com.example.translate.model.WordTranslate;
import com.example.translate.repository.WordDBRepository;

public class AddWordDialogFragment extends DialogFragment {

    private WordDBRepository mRepository;
    private EditText mEditTextPersian,mEditTextEnglish,mEditTextFrance,mEditTextArab;
    private Button mButtonSave,mButtonCancel;

    public static AddWordDialogFragment newInstance() {
        AddWordDialogFragment fragment = new AddWordDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = WordDBRepository.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_word_dialog,null);
        findViews(view);
        setAllListener();
        return view;
    }


    private void findViews(View view) {
        mEditTextPersian = view.findViewById(R.id.edit_txt_persian);
        mEditTextEnglish = view.findViewById(R.id.edit_txt_english);
        mEditTextFrance = view.findViewById(R.id.edit_txt_france);
        mEditTextArab = view.findViewById(R.id.edit_txt_arabian);
        mButtonSave = view.findViewById(R.id.save_button);
        mButtonCancel = view.findViewById(R.id.cancels_button);
    }

    private void setAllListener() {
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEditTextPersian.getText().length()!= 0 || mEditTextEnglish.getText().length()!= 0 ||
                mEditTextFrance.getText().length()!= 0|| mEditTextArab.getText().length()!= 0){
                    WordTranslate word = new WordTranslate(
                            mEditTextPersian.getText().toString(),
                            mEditTextEnglish.getText().toString(),
                            mEditTextFrance.getText().toString(),
                            mEditTextArab.getText().toString());
                    mRepository.insert(word);
                    dismiss();
                }else {
                    Toast.makeText(getActivity(), "fill the blank text field", Toast.LENGTH_SHORT).show();
                    return;
                }
                dismiss();
            }
        });
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
