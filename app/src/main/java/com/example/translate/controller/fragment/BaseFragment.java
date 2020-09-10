package com.example.translate.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.translate.database.TranslateDBSchema;
import com.example.translate.model.WordTranslate;
import com.example.translate.repository.WordDBRepository;
import com.example.translate.utils.StateOfTranslate;

import java.util.ArrayList;
import java.util.List;


public class BaseFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public static final int REQUEST_CODE_WORD_DETAIL_DIALOG = 1;
    public static final int REQUEST_CODE_ADD_WORD_DIALOG = 1;
    public static final String SAVE_INSTANCE_ORIGIN_STATE = "origin_state";
    public static final String SAVE_INSTANCE_DES_STATE = "des_state";
    public static final String SAVE_ENTERED_TEXT = "entered_text";
    private Button mButtonTranslate;
    private WordDBRepository mRepository;
    private StateOfTranslate mOriginState, mDesState;
    private TextView mResultWord;
    private EditText mEnterText;
    private Spinner mSpinnerDestination, mSpinnerOrigin;
    private ImageView mReplace;
    private RecyclerView mRecyclerView;
    private WordAdapter mWordAdapter;
    List<WordTranslate> mMeaningWord = new ArrayList<>();
    String mWhere;

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
        if (savedInstanceState==null)
            setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        findViews(view);
        if (savedInstanceState!=null){
            String origin = savedInstanceState.getString(SAVE_INSTANCE_ORIGIN_STATE);
            String des = savedInstanceState.getString(SAVE_INSTANCE_DES_STATE);
            mEnterText.setText(savedInstanceState.getString(SAVE_ENTERED_TEXT));
            switch (origin){
                case "PERSIAN" :
                    mOriginState=StateOfTranslate.PERSIAN;
                    mSpinnerOrigin.setSelection(0);
                    break;
                case "ENGLISH" :
                    mOriginState=StateOfTranslate.ENGLISH;
                    mSpinnerOrigin.setSelection(1);
                    break;
                case "FRANCE" :
                    mOriginState=StateOfTranslate.FRANCE;
                    mSpinnerOrigin.setSelection(2);
                    break;
                case "ARABIAN" :
                    mOriginState=StateOfTranslate.ARABIAN;
                    mSpinnerOrigin.setSelection(3);
                    break;
            }
            switch (des){
                case "PERSIAN" :
                    mDesState=StateOfTranslate.PERSIAN;
                    mSpinnerDestination.setSelection(0);
                    break;
                case "ENGLISH" :
                    mDesState=StateOfTranslate.ENGLISH;
                    mSpinnerDestination.setSelection(1);
                    break;
                case "FRANCE" :
                    mDesState=StateOfTranslate.FRANCE;
                    mSpinnerDestination.setSelection(2);
                    break;
                case "ARABIAN" :
                    mDesState=StateOfTranslate.ARABIAN;
                    mSpinnerDestination.setSelection(3);
                    break;
            }


        }else {
            updateSubtitle();
            setSpinnerOrigin(view);
            setSpinnerDestination(view);
        }
        allListener();


        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //updateSubtitle();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_INSTANCE_ORIGIN_STATE,mOriginState.toString());
        outState.putString(SAVE_INSTANCE_DES_STATE,mDesState.toString());
        outState.putString(SAVE_ENTERED_TEXT,mEnterText.getText().toString());
    }

    private void updateSubtitle() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        String numberOfCrimes = mRepository.getList().size() + "";

        activity.getSupportActionBar().setSubtitle(numberOfCrimes);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == REQUEST_CODE_WORD_DETAIL_DIALOG) {
            mMeaningWord = mRepository.findMeaning(mWhere);
            updateUI();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.translate_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_icon_menu:
                AddWordDialogFragment addWordDialogFragment = AddWordDialogFragment.newInstance();
                addWordDialogFragment.setTargetFragment(BaseFragment.this, REQUEST_CODE_ADD_WORD_DIALOG);
                addWordDialogFragment.show(getFragmentManager(), "addDialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void findViews(View view) {
        mEnterText = view.findViewById(R.id.editText_enter_text);
        mSpinnerDestination = view.findViewById(R.id.spinner_Destination);
        mSpinnerOrigin = view.findViewById(R.id.spinner_origin);
        mReplace = view.findViewById(R.id.image_view_replace);
        mResultWord = view.findViewById(R.id.textView_result);
        mRecyclerView = view.findViewById(R.id.recycler_word);
    }

    private void allListener() {

        mEnterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputWord = s.toString();
                switch (mOriginState) {
                    case PERSIAN:
                        mWhere = TranslateDBSchema.WordTable.COLS.PERSIAN + " LIKE '" + inputWord + "%'";
                        break;
                    case ENGLISH:
                        mWhere = TranslateDBSchema.WordTable.COLS.ENGLISH + " LIKE '" + inputWord + "%'";
                        break;
                    case FRANCE:
                        mWhere = TranslateDBSchema.WordTable.COLS.FRANCE + " LIKE '" + inputWord + "%'";
                        break;
                    case ARABIAN:
                        mWhere = TranslateDBSchema.WordTable.COLS.ARABIAN + " LIKE '" + inputWord + "%'";
                        break;
                }
                mMeaningWord = mRepository.findMeaning(mWhere);
                if (mMeaningWord.size() <= 0) {
                    Toast.makeText(getActivity(), "Not found ", Toast.LENGTH_SHORT).show();
                }
                updateUI();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mDesState) {
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
                switch (mOriginState) {
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

    private void updateUI() {
        if (mWordAdapter == null) {
            mWordAdapter = new WordAdapter(mMeaningWord);
            mRecyclerView.setAdapter(mWordAdapter);
        } else {
            mWordAdapter.setWords(mMeaningWord);
            mWordAdapter.notifyDataSetChanged();
        }
    }


    /**
     * find the destination spinner and create Array adaptor for getting the item in spinner
     * and set adapter to spinner object
     *
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
     *
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
     * @param parent   return the spinner that clicked
     * @param view
     * @param position return the position of the item clicked in parent
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner_origin) {
            if (parent.getItemAtPosition(position).equals("Persian"))
                mOriginState = StateOfTranslate.PERSIAN;
            if (parent.getItemAtPosition(position).equals("English"))
                mOriginState = StateOfTranslate.ENGLISH;
            if (parent.getItemAtPosition(position).equals("France"))
                mOriginState = StateOfTranslate.FRANCE;
            if (parent.getItemAtPosition(position).equals("Arabian"))
                mOriginState = StateOfTranslate.ARABIAN;
            mEnterText.setHint("enter the " + mOriginState.toString() + " word to translate");
        }
        if (parent.getId() == R.id.spinner_Destination) {
            if (parent.getItemAtPosition(position).equals("Persian"))
                mDesState = StateOfTranslate.PERSIAN;
            if (parent.getItemAtPosition(position).equals("English"))
                mDesState = StateOfTranslate.ENGLISH;
            if (parent.getItemAtPosition(position).equals("France"))
                mDesState = StateOfTranslate.FRANCE;
            if (parent.getItemAtPosition(position).equals("Arabian"))
                mDesState = StateOfTranslate.ARABIAN;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /**
     * word Holder
     */
    public class WordHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewInput;
        private TextView mTextViewOutput;
        private ImageView mImageViewShare;
        private WordTranslate mWordTranslate;

        public WordHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewInput = itemView.findViewById(R.id.input);
            mTextViewOutput = itemView.findViewById(R.id.output);
            mImageViewShare = itemView.findViewById(R.id.imageView_share);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WordDetailDialogFragment wordDetailDialogFragment = WordDetailDialogFragment.newInstance(mWordTranslate);
                    wordDetailDialogFragment.setTargetFragment(BaseFragment.this, REQUEST_CODE_WORD_DETAIL_DIALOG);
                    wordDetailDialogFragment.show(getFragmentManager(), "wordDetailDialog");
                }
            });
            mImageViewShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, getReportText(mWordTranslate));
                    sendIntent.setType("text/plain");
                    Intent share = Intent.createChooser(sendIntent, null);
                    startActivity(share);
                }
            });
        }

        public void wordBind(WordTranslate word) {
            mWordTranslate = word;
            switch (mOriginState) {
                case PERSIAN:
                    mTextViewInput.setText(word.getPersian());
                    break;
                case ENGLISH:
                    mTextViewInput.setText(word.getEnglish());
                    break;
                case FRANCE:
                    mTextViewInput.setText(word.getFrance());
                    break;
                case ARABIAN:
                    mTextViewInput.setText(word.getArabian());
                    break;
            }
            switch (mDesState) {
                case PERSIAN:
                    mTextViewOutput.setText(word.getPersian());
                    break;
                case ENGLISH:
                    mTextViewOutput.setText(word.getEnglish());
                    break;
                case FRANCE:
                    mTextViewOutput.setText(word.getFrance());
                    break;
                case ARABIAN:
                    mTextViewOutput.setText(word.getArabian());
                    break;
            }
        }
    }

    private String getReportText(WordTranslate word) {
        return getString(R.string.share_word, word.getPersian(), word.getEnglish(), word.getFrance(), word.getArabian());
    }

    /**
     * word Adapter
     */
    public class WordAdapter extends RecyclerView.Adapter<WordHolder> {

        private List<WordTranslate> mWords;

        public List<WordTranslate> getWords() {
            return mWords;
        }

        public void setWords(List<WordTranslate> words) {
            mWords = words;
        }

        public WordAdapter(List<WordTranslate> words) {
            mWords = words;
        }

        @NonNull
        @Override
        public WordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.translate_row, parent, false);
            WordHolder wordHolder = new WordHolder(view);
            return wordHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull WordHolder holder, int position) {
            WordTranslate wordTranslate = mWords.get(position);
            holder.wordBind(wordTranslate);
        }

        @Override
        public int getItemCount() {
            return mWords.size();
        }
    }
}