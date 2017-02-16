package georgi.com.BlogApp.Threads.Posts;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import georgi.com.BlogApp.Adapters.PostsAdapter;
import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.Comment;
import georgi.com.BlogApp.POJO.Post;


// This thread is sending request to server for the latest 5 posts
// and set them to the recyclerView.
public class Latest5Posts extends AsyncTask<String, Void, List<Post>>{

    private String TAG = getClass().getSimpleName();

    private Context context;

    private RecyclerView recyclerView;

    public Latest5Posts(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    protected List<Post> doInBackground(String... strings) {

        try {

            // Creating the request.
            // strings[0] is the url to send that request.
            HttpRequest httpRequest = new HttpRequest(strings[0],
                    new PreferencesHelper(context).getCookie(), "GET");

            // Sending the request and getting the response.
            String response = httpRequest.sendTheRequest();

            Gson gson = new Gson();
            // Converting the response from JSON object to array of posts.
            Post[] posts = gson.fromJson(response, Post[].class);

            // Converting array of posts to List of posts and returning it.
            return new ArrayList<>(Arrays.asList(posts));

        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Post> posts) {

        // Getting the adapter from the recyclerView
        PostsAdapter postsAdapter = (PostsAdapter) recyclerView.getAdapter();

        // Getting the posts from the postAdapter
        List<Post> oldPosts = postsAdapter.getPosts();

        // Deleting the old posts.
        oldPosts.clear();

        // Adding the new posts to the old posts reference.
        for(Post post : posts) {
            oldPosts.add(post);
        }

        // And notifying the adapter that data is changed.
        postsAdapter.notifyDataSetChanged();
    }
}
