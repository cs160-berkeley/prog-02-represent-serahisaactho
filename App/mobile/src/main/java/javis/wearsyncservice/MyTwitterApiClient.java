package javis.wearsyncservice;

import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.TwitterApiClient;

import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

public class MyTwitterApiClient extends TwitterApiClient
{
    public MyTwitterApiClient(AppSession session)//(TwitterSession session)
    {
        super(session);
    }

    public UsersService getUsersService()
    {
        return getService(UsersService.class);
    }
}