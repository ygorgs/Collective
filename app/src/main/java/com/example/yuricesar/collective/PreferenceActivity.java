package com.example.yuricesar.collective;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;

import com.example.yuricesar.collective.data.Category;
import com.example.yuricesar.collective.data.PreferenceAuxiliar;


public class PreferenceActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        CheckBox location = (CheckBox) findViewById(R.id.checkbox_location);
        onCheckboxClicked(location);
        CheckBox music = (CheckBox) findViewById(R.id.checkbox_music);
        onCheckboxClicked(music);
        CheckBox movie = (CheckBox) findViewById(R.id.checkbox_movies);
        onCheckboxClicked(movie);
        CheckBox book = (CheckBox) findViewById(R.id.checkbox_books);
        onCheckboxClicked(book);
        CheckBox tv = (CheckBox) findViewById(R.id.checkbox_tv);
        onCheckboxClicked(tv);
        CheckBox game = (CheckBox) findViewById(R.id.checkbox_games);
        onCheckboxClicked(game);

        SeekBar s = (SeekBar)findViewById(R.id.seekbar_location);
        s.setOnSeekBarChangeListener(this);
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                PreferenceAuxiliar.getInstace().setDistancia(progress);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preference, menu);
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

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_location:
                if (checked) {
                    PreferenceAuxiliar.getInstace().addCategoria(Category.LOCATION);
                } else {
                    // Remove the meat
                }
                break;
            case R.id.checkbox_movies:
                if (checked) {
                    PreferenceAuxiliar.getInstace().addCategoria(Category.MOVIES);
                } else {
                    // I'm lactose intolerant
                }
                break;
            case R.id.checkbox_books:
                if (checked) {
                    PreferenceAuxiliar.getInstace().addCategoria(Category.BOOKS);
                } else {
                    // I'm lactose intolerant
                }
                break;
            case R.id.checkbox_games:
                if (checked) {
                    PreferenceAuxiliar.getInstace().addCategoria(Category.GAMES);
                } else {
                    // I'm lactose intolerant
                }
                break;
            case R.id.checkbox_tv:
                if (checked) {
                    PreferenceAuxiliar.getInstace().addCategoria(Category.TV);
                } else {
                    // I'm lactose intolerant
                }
                break;
            case R.id.checkbox_music:
                if (checked) {
                    PreferenceAuxiliar.getInstace().addCategoria(Category.MUSIC);
                } else {
                    // I'm lactose intolerant
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    //TODO barra pra pegar a distancia

}
