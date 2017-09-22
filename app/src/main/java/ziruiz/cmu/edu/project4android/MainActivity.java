package ziruiz.cmu.edu.project4android;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * The click listener will need a reference to this object, so that upon successfully finding a picture from Flickr, it
         * can callback to this object with the resulting picture Bitmap.  The "this" of the OnClick will be the OnClickListener, not
         * this InterestingPicture.
         */
        final MainActivity ma = this;

        /*
         * Find the "submit" button, and add a listener to it
         */
        Button submitButton = (Button)findViewById(R.id.submit);


        // Add a listener to the send button
        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View viewParam) {
                String searchTerm = ((EditText)findViewById(R.id.searchTerm)).getText().toString();
                GetWiki gp = new GetWiki();
                gp.search(searchTerm, ma); // Done asynchronously in another thread.  It calls ip.pictureReady() in this thread when complete.
            }
        });
    }

    /*
     * This is called by the GetWiki object when the wiki is ready.
     */
    public void wikiReady(String wiki) {
        System.out.println("here is wiki:!!!"+wiki);
        TextView searchView = (EditText)findViewById(R.id.searchTerm);
        String search = searchView.getText().toString();
        TextView comment = (TextView) findViewById(R.id.resultView);
        if (wiki != null) {
            comment.setText("Here is a wiki's snippet of a "+search+"\n\n"+wiki);
        } else {
            comment.setText("Sorry, I could not find a snippet of a "+search);
        }
        searchView.setText("");
    }
}
