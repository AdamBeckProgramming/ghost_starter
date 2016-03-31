package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private SimpleDictionary simDictionary;
    private Random random = new Random();
    String totalString = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        onStart(null);

        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            simDictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView text = (TextView) findViewById(R.id.ghostText);
        // Do computer turn stuff then make it the user's turn again
        String s = " ";
        int length = text.length();
        if(length > 4 && simDictionary.isWord(text.getText().toString())){
            label.setText("Comp wins");
        }else if(length == 0){
            Log.d("ComputerTurn length 1", "Length 0");
            text.setText("");
            Log.d("ComputerTurn text 1", text.toString());
            // This causes the breakage whenever computer starts first for some reason
            // "Null pointer" error
            s = simDictionary.getAnyWordStartingWith("");
            Log.d("ComputerTurn s", s);
            text.append(s);
            Log.d("ComputerTurn", text.toString());

        }
        else{
            s = simDictionary.getAnyWordStartingWith(text.getText().toString());
            if(s == null){
                label.setText("Computer Challenges");
            }else{
                text.append(s.substring(length - 1, length));
            }

        }
        userTurn = true;
        label.setText(USER_TURN);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     *
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;

        // Game breaks whenever computer starts, doesn't break if I hit restart and computer starts
        /*TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");

        userTurn = true;
        return true;*/
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z){
            TextView text = (TextView) findViewById(R.id.ghostText);
            char keyChar = (char) event.getUnicodeChar();
            String s = text.getText().toString() + keyChar;
            if(simDictionary.isWord(s)) {
                TextView label = (TextView) findViewById(R.id.gameStatus);
                label.setText("Word!");
            }
            Log.d("Character to add", s);
            Log.d("Over all string", totalString);
            text.setText(s);
            userTurn = false;
            return true;
        } else{
            userTurn = false;
            return super.onKeyUp(keyCode,event);
        }
    }

    public void challengeWord(View view){
        TextView text = (TextView) findViewById(R.id.ghostText);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if(text.length() > 4 && simDictionary.isWord(text.getText().toString())){
            label.setText("User Wins");
        } else{
            Log.d("challengeWord", text.toString());
            String s = simDictionary.getAnyWordStartingWith(text.getText().toString());
            if(s != null){
                Log.d("challengeWord", s);
                label.setText("computer wins");
            }else{
                label.setText("User was right");
            }
        }
    }
}
