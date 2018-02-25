package com.abedor.guess_a_word;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> words;
    ArrayList<String> hints;
    String word; //current word to guess
    Button btn_submit; //Button that submits choice
    TextView ti_guess; //Text input where the guesses are
    TextView tv_hint; //A description of the chosen word
    TextView tv_cipher; //**** representation of a word
    AlertDialog.Builder builder;
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
        hints = new ArrayList<>();
        setWords();
        setWord();
        tv_cipher.setText(setCipher());
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(ti_guess.getText().toString());
            }
        });
        builder = new AlertDialog.Builder(this).setTitle(R.string.menu_about).setMessage(R.string.about).
                setNegativeButton(R.string.dialog_close,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

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
        hints.add("A reproductive structure of a certain kind of plants.");
        hints.add("Toxic chemical. Widely used in rocketry and aircraft engineering, but is not a fuel.");
        hints.add("Second closest to the Earth star.");
        hints.add("A certain group of animals, vast majority of which are capable of sustaining themselves in the air.");
        hints.add("The first ever motorized, heavier-than-air piece of wood and cloth that lifter freely from the ground.");
    }
    //sets current guess word
    private void setWord(){
        Random r = new Random();
        int pos = r.nextInt(words.size());
        word = words.get(pos);
        tv_hint.setText(hints.get(pos));
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
                    if(tv_cipher.getText().toString().contains("*"))
                    toaster(TO_TOAST.LETTER_OK);
                    else toaster(TO_TOAST.WORD_OK);

                }
                //if the letter guess is incorrect
                else{
                    toaster(TO_TOAST.LETTER_BAD);
                }
            }
        }
        else toaster(TO_TOAST.TOTAL_BAD);
        ti_guess.setText(null);
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

    private void restart(){
        finish();
        startActivity(getIntent());
    }
    //Creates the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_about:
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.menu_restart:
                restart();
                break;
        }
        return true;
    }


}
