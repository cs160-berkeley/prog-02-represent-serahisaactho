package javis.wearsyncservice;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.wearable.view.CardFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.Result;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Custom2Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Custom2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Custom2Fragment extends CardFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;

    private OnFragmentInteractionListener mListener;

    public Custom2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Custom2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Custom2Fragment newInstance(String param1, String param2, int param3,
                                              int param4) {
        Custom2Fragment fragment = new Custom2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, String.valueOf(param3));
        args.putString(ARG_PARAM4, String.valueOf(param4));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View w = inflater.inflate(R.layout.fragment_custom2, container, false);
        TextView title = (TextView)w.findViewById(R.id.Title);
        TextView party = (TextView)w.findViewById(R.id.Party);
        //ImageView img = (ImageView)w.findViewById(R.id.Img);
        title.setText(mParam1);
        party.setText(mParam2);
        if (mParam2.equals("DEMOCRAT"))
        {
            party.setTextColor(Color.BLUE);
        }
        else if (mParam2.equals("REPUBLICAN"))
        {
            party.setTextColor(Color.RED);
        }
        //img.setImageResource(Integer.parseInt(mParam3));

        //TWITTER IMAGE STUFF
       /* TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        String output = "";
        TwitterCore.getInstance().logInGuest(new com.twitter.sdk.android.core.Callback<AppSession>() {
            @Override
            public void success(Result appSessionResult) {
                AppSession guestAppSession = (AppSession) appSessionResult.data;
                //TwitterSession guestAppSession = (TwitterSession) appSessionResult.data;
                    new MyTwitterApiClient(guestAppSession).getUsersService().show(null, curr_twitter_id, true, new com.twitter.sdk.android.core.Callback<User>() {
                        @Override
                        public void success(Result<User> result) {
                            output =  (result.data.profileImageUrlHttps.replace("_normal", "_mini"));
                        }

                        @Override
                        public void failure(TwitterException exception) {

                        }
                    });
                }

            public void failure(TwitterException exception) {
                //Do something on failure
                System.out.println("BAZOOKA FAILURE in guest auth");
            }

        });*/

        //TWITTER IMAGE STUFF
        return w;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
