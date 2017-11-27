package com.eegeo.apisamples;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class SoftBackButtonActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
