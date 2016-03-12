package javis.wearsyncservice;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.User;

import retrofit.http.GET;
import retrofit.http.Query;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.User;

import java.util.concurrent.Callable;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Serah on 3/8/2016.
 */
public interface UsersService {

    @GET("/1.1/users/show.json")
    void show(@Query("user_id") Long userId,
              @Query("screen_name") String screenName,
              @Query("include_entities") Boolean includeEntities,
              Callback<User> cb);
}


