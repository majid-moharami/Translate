package com.example.translate.controller.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.translate.R;
import com.example.translate.model.WordTranslate;
import com.example.translate.repository.WordDBRepository;
import com.example.translate.utils.StateOfTranslate;

import java.util.List;


public class BaseFragment extends Fragment implements  AdapterView.OnItemSelectedListener {

    private Button mButtonTranslate;
    private WordDBRepository mRepository;
    private List<WordTranslate> mWordList;
    private StateOfTranslate mOriginState , mDesState;
    private TextView mResultWord;
    private EditText mEnterText;
    private  Spinner mSpinnerDestination,mSpinnerOrigin;
    private ImageView mReplace;

    public static BaseFragment newInstance() {
        BaseFragment fragment = new BaseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = WordDBRepository.getInstance(getActivity());
        mWordList = mRepository.getList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        findViews(view);
        allListener();
        setSpinnerOrigin(view);
        setSpinnerDestination(view);

        return view;
    }

    private void findViews(View view){
        mButtonTranslate = view.findViewById(R.id.button_translate);
        mEnterText = view.findViewById(R.id.editText_enter_text);
        mSpinnerDestination = view.findViewById(R.id.spinner_Destination);
        mSpinnerOrigin  = view.findViewById(R.id.spinner_origin);
        mReplace  = view.findViewById(R.id.image_view_replace);
        mResultWord  = view.findViewById(R.id.textView_result);
    }

    private void allListener() {
        mButtonTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer  =  MediaPlayer.create(getActivity() , R.raw.multimedia_button_click_005);
                mediaPlayer.start();
                String inputWord = mEnterText.getText().toString();
                for (int i = 0; i < mWordList.size(); i++) {
                    String farsi = mWordList.get(i).getPersian();
                    String english = mWordList.get(i).getEnglish();
                    String france = mWordList.get(i).getFrance();
                    String arab = mWordList.get(i).getArabian();
                    if (inputWord.equals(farsi) || inputWord.equals(english) || inputWord.equals(france) || inputWord.equals(arab)){
                        switch (mDesState){
                            case PERSIAN:
                                mResultWord.setText(farsi);
                                break;
                            case ENGLISH:
                                mResultWord.setText(english);
                                break;
                            case FRANCE:
                                mResultWord.setText(france);
                                break;
                            case ARABIAN:
                                mResultWord.setText(arab);
                                break;
                        }
                        return;
                    }
                }
                Toast.makeText(getActivity(), "Not found", Toast.LENGTH_SHORT).show();
            }
        });

        mReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mDesState){
                    case PERSIAN:
                        mSpinnerOrigin.setSelection(0);
                        break;
                    case ENGLISH:
                        mSpinnerOrigin.setSelection(1);
                        break;
                    case FRANCE:
                        mSpinnerOrigin.setSelection(2);
                        break;
                    case ARABIAN:
                        mSpinnerOrigin.setSelection(3);
                        break;
                }
                switch (mOriginState){
                    case PERSIAN:
                        mSpinnerDestination.setSelection(1);
                        break;
                    case ENGLISH:
                        mSpinnerDestination.setSelection(0);
                        break;
                    case FRANCE:
                        mSpinnerDestination.setSelection(2);
                        break;
                    case ARABIAN:
                        mSpinnerDestination.setSelection(3);
                        break;
                }
            }
        });
    }


    /**
     * find the destination spinner and create Array adaptor for getting the item in spinner
     * and set adapter to spinner object
     * @param view
     */
    private void setSpinnerDestination(View view) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.language_array2, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDestination.setAdapter(adapter);
        mSpinnerDestination.setOnItemSelectedListener(this);
    }
    /**
     * find the origin spinner and create Array adaptor for getting the item in spinner
     * and set adapter to spinner object
     * @param view
     */
    private void setSpinnerOrigin(View view) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.language_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerOrigin.setAdapter(adapter);
        mSpinnerOrigin.setOnItemSelectedListener(this);
    }

    /**
     *
     * @param parent return the spinner that clicked
     * @param view
     * @param position return the position of the item clicked in parent
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner_origin){
            if (parent.getItemAtPosition(position).equals("Persian"))
                mOriginState= StateOfTranslate.PERSIAN;
            if (parent.getItemAtPosition(position).equals("English"))
                mOriginState= StateOfTranslate.ENGLISH;
            if (parent.getItemAtPosition(position).equals("France"))
                mOriginState= StateOfTranslate.FRANCE;
            if (parent.getItemAtPosition(position).equals("Arabian"))
                mOriginState= StateOfTranslate.ARABIAN;
            mEnterText.setHint("enter the "+mOriginState.toString()+" word to translate");
        }
        if (parent.getId() == R.id.spinner_Destination){
            if (parent.getItemAtPosition(position).equals("Persian"))
                mDesState= StateOfTranslate.PERSIAN;
            if (parent.getItemAtPosition(position).equals("English"))
                mDesState= StateOfTranslate.ENGLISH;
            if (parent.getItemAtPosition(position).equals("France"))
                mDesState= StateOfTranslate.FRANCE;
            if (parent.getItemAtPosition(position).equals("Arabian"))
                mDesState= StateOfTranslate.ARABIAN;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}