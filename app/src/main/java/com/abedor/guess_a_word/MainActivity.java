package com.abedor.guess_a_word;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> words;
    String word;
    Button btn_submit; //Button that submits choice
    TextView ti_guess; //Text input where the guesses are
    TextView tv_hint; //A description of the chosen word
    TextView tv_cipher; //**** representation of a word
    private enum TO_TOAST { WORD_OK, WORD_BAD, LETTER_OK, LETTER_BAD, TOTAL_BAD }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_submit = findViewById(R.id.btn_guess);
        ti_guess = findViewById(R.id.ti_guess);
        tv_hint = findViewById(R.id.tv_hint);
        tv_cipher = findViewById(R.id.tv_cipher);
        words = new ArrayList<>();
        setWords();
        setWord();
        tv_cipher.setText(setCipher());
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(ti_guess.getText().toString());
            }
        });

    }

    //TODO: change to database access
    private void setWords(){
        words.add("flower");
        words.add("triethylborane");
        words.add("proxima");
        words.add("bird");
        words.add("flyer");
    }
    //sets current guess word
    private void setWord(){
        Random r = new Random();
        word = words.get(r.nextInt(words.size()));
    }
    //sets cipher for the first time
    private String setCipher(){
        String s = "";
        for(int i = 0; i < word.length(); i++){
            s += "*";
        }
        return s;
    }
    //sets cipher opening guessed letter at a given position
    @NonNull
    private String setCipher(int position, char c){
        StringBuilder s = new StringBuilder(tv_cipher.getText().toString());
        s.setCharAt(position, c);
        return s.toString();
    }

    private void submit(String s){
        if(s.length() > 0){
            //if text entered is a word
            if(s.length() > 1){
                //if the guess is correct
                if(ti_guess.getText().toString().equals(word)){
                    toaster(TO_TOAST.WORD_OK);
                    tv_cipher.setText(word);
                }
                //if the word guess is incorrect
                else{
                    toaster(TO_TOAST.WORD_BAD);
                }
            }
            //if text entered is a letter
            else{
                //if the letter guess is correct
                if(word.contains(s)){
                    char[] charword = word.toCharArray();
                    int pos = 0;
                    for (char c : charword
                         ) {
                        if(c == s.charAt(0)){
                            tv_cipher.setText(setCipher(pos, c));
                        }
                        pos++;
                    }
                    toaster(TO_TOAST.LETTER_OK);
                }
                //if the letter guess is incorrect
                else{
                    toaster(TO_TOAST.LETTER_BAD);
                }
            }
        }
        else toaster(TO_TOAST.TOTAL_BAD);
    }

    private void toaster(TO_TOAST toToast){
        try{
            switch(toToast){
                case WORD_OK:
                    Toast.makeText(MainActivity.this, R.string.result_word_ok, Toast.LENGTH_SHORT).show();
                    break;
                case WORD_BAD:
                    Toast.makeText(MainActivity.this, R.string.result_word_bad, Toast.LENGTH_SHORT).show();
                    break;
                case LETTER_OK:
                    Toast.makeText(MainActivity.this, R.string.result_letter_ok, Toast.LENGTH_SHORT).show();
                    break;
                case LETTER_BAD:
                    Toast.makeText(MainActivity.this, R.string.result_letter_bad, Toast.LENGTH_SHORT).show();
                    break;
                case TOTAL_BAD:
                    Toast.makeText(MainActivity.this, R.string.result_total_bad, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        catch (Exception e){
            Log.d("EXC",e.getMessage());
        }
    }

}
