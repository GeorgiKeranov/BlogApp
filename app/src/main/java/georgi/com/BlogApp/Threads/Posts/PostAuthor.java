package georgi.com.BlogApp.Threads.Posts;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.User;

import static georgi.com.BlogApp.Configs.ServerURLs.DEFAULT_USER_IMG;
import static georgi.com.BlogApp.Configs.ServerURLs.POST_URL;
import static georgi.com.BlogApp.Configs.ServerURLs.USER_IMAGES_URL;

// Sending request to server with some id of post and
// server is returning the author of that post. Then this thread
// is setting the UI thread elements with this author.
public class PostAuthor extends AsyncTask<Long, Void, User> {

    // This interface is used to set String from that thread to UI thread.
    public interface Listener {
        void setUserUrl(String userUrl);
    }

    // The actual listener.
    private Listener listener;

    private Context context;

    private TextView authorName;
    private ImageView authorImage;

    public PostAuthor(Context context, TextView authorName, ImageView authorImage) {
        this.context = context;
        this.authorName = authorName;
        this.authorImage = authorImage;

        // Connecting the listener with the context of the UI thread.
        listener = (Listener) context;
    }

    @Override
    protected User doInBackground(Long... longs) {

        try {

            // Creating the request.
            // longs[0] : this is the id of the post from which that we need the author.
            HttpRequest httpRequest = new HttpRequest(POST_URL + longs[0] + "/author",
                    new PreferencesHelper(context).getCookie(), "GET");

            // Sending the request and getting the response into String.
            String response = httpRequest.sendTheRequest();

            Gson gson = new Gson();
            // Converting the JSON response to User object.
            return gson.fromJson(response, User.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(User user) {

        // If the user is null stop the method.
        if(user == null) return;

        authorName.setText(user.getFullName());

        // Setting the ImageView : authorImage from user's profile picture URL.
        Glide.with(context)
                .load(user.getProfPicUrl())
                .override(800, 800)
                .into(authorImage);

        // Using the method in the UI thread.
        listener.setUserUrl(user.getUserUrl());
    }
}
