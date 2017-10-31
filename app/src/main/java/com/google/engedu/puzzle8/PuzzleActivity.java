package com.google.engedu.puzzle8;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;


public class PuzzleActivity extends ActionBarActivity {
    static private String TAG = "PuzzleActivity Log";
    private PuzzleBoardView mBoardView;
    private RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        container = (RelativeLayout) findViewById(R.id.puzzle_container);

        mBoardView = new PuzzleBoardView(this);


        mBoardView.initialize(
                BitmapFactory.decodeResource(getResources(),
                        R.drawable.googleplus), mBoardView);


        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);

        container.addView(mBoardView, params);



        Log.d(TAG, "onCreate succeeded.");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_puzzle, menu);
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

    //Handler for the 'Take photo' button
    public void dispatchTakePictureIntent(View view) {
        Intent intent = new Intent(android.provider.
                MediaStore.ACTION_IMAGE_CAPTURE);
        // Launch activity and wait for response
        startActivityForResult(intent, 0);
    }

    //The image can be obtained from onActivityResult
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bp = (Bitmap) data.getExtras().get("data");
        //mImageView.setImageBitmap(bp);
        mBoardView.initialize(bp, container.
                findViewWithTag(mBoardView));

    }

    public void shuffleImage(View view) {
        mBoardView.shuffle();
    }

    public void solve(View view) {
      mBoardView.solve();

    }
}
