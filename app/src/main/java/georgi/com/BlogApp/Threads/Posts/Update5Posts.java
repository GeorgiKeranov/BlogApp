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
import georgi.com.BlogApp.Adapters.YourPostsAdapter;
import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.Post;

import static georgi.com.BlogApp.Configs.ServerURLs.AUTH_USER_UPDATE_5POSTS_URL;


// This thread is updating with 5 new posts the RecyclerView.
public class Update5Posts extends AsyncTask<String, Void, List<Post>> {

    private Context context;
    private RecyclerView postsRecyclerView;

    // This String is used to determinate what kind of adapter to use in PostExecute method.
    private String whatAdapter;

    public Update5Posts(Context context, RecyclerView postsRecyclerView) {
        this.context = context;
        this.postsRecyclerView = postsRecyclerView;
    }


    @Override
    protected List<Post> doInBackground(String... strings) {

        // Setting the whatAdapter value.
        if(strings[0].equals(AUTH_USER_UPDATE_5POSTS_URL)) whatAdapter = "YourPostsAdapter";
        else whatAdapter = "PostsAdapter";

        try {

            // Creating the request.
            HttpRequest httpRequest = new HttpRequest(strings[0], // string[0] - url for the request.
                            new PreferencesHelper(context).getCookie(), "GET");

            // Adding the id of the last downloaded post.
            httpRequest.addStringField("postsBeforeId", strings[1]);

            // Sending the request and getting the response from the server.
            String response = httpRequest.sendTheRequest();

            // Converting the response from JSON object to array of posts.
            Post[] posts = new Gson().fromJson(response, Post[].class);

            // Converting the array of posts to list of posts.
            return new ArrayList<>(Arrays.asList(posts));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Post> posts) {

        // Checks the value of whatAdapter.

        if(whatAdapter.equals("PostsAdapter")) {
            // Getting the adapter from recyclerView.
            PostsAdapter postsAdapter = (PostsAdapter) postsRecyclerView.getAdapter();
            // Getting the oldPosts in the adapter.
            List<Post> oldPosts = postsAdapter.getPosts();

            // Adding the new Post objects to the old list.
            for(Post post : posts) {
                oldPosts.add(post);
            }

            // Notifying the postsAdapter that data is changed.
            postsAdapter.notifyDataSetChanged();
        }

        else if(whatAdapter.equals("YourPostsAdapter")) {
            // Getting the adapter from recyclerView.
            YourPostsAdapter yourPostsAdapter = (YourPostsAdapter) postsRecyclerView.getAdapter();
            // Getting the oldPosts in the adapter.
            List<Post> oldPosts = yourPostsAdapter.getPosts();

            // Adding the new Post objects to the old list.
            for(Post post : posts) {
                oldPosts.add(post);
            }

            // Notifying the postsAdapter that data is changed.
            yourPostsAdapter.notifyDataSetChanged();
        }
    }

}
