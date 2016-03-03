package javis.wearsyncservice;


import android.content.Context;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridPagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PickerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;

    public PickerAdapter(Context ctx, FragmentManager fm) {
        super(fm);
        mContext = ctx;
    }

    static final int[][] BG_IMAGES = new int[][] {
            {
                    R.drawable.edlabovmin,
            },
    };

    /** A simple container for static data in each page */
    private static class Page {
        String title;
        String party;
        int pic_id;
        int cardGravity = Gravity.CENTER;
        boolean expansionEnabled = false;
        float expansionFactor = 1.0f;
        int expansionDirection = CardFragment.EXPAND_DOWN;

        public Page(String Title, String Party, int ID) {
            this.title = Title;
            this.party = Party;
            this.pic_id = ID;
        }
    }

    private final Page[][] PAGES = {
            {
                    new Page("Senator Laura Lakoff","REPUBLICAN",R.drawable.lauralakoff),
                    new Page("Senator Ronan Nash","DEMOCRAT",R.drawable.ronannash_wear),
                    new Page("Senator Ed Labov","REPUBLICAN", R.drawable.edlabov_wear),
                    new Page("Representative Hannah Whorf","DEMOCRAT", R.drawable.hannahwhorf_wear),
                    new Page("Senator Mark Hagen","DEMOCRAT", R.drawable.markhagen_wear)
                    //Same order as mobile list view
                    /*new Page("","REPUBLICAN Senator Laura Lakoff",R.drawable.lauralakoff),
                    new Page("","DEMOCRAT  Senator Ronan Nash",R.drawable.ronannashmin),
                    new Page("","REPUBLICAN Senator Ed Labov", R.drawable.edlabov_wear),
                    new Page("","DEMOCRAT  Representative Hannah Whorf", R.drawable.hannahwhorf),
                    new Page("","DEMOCRAT Senator Mark Hagen", R.drawable.markhagenmin)
                    //Same order as mobile list view*/
            },

    };

    @Override
    public CardFragment getFragment(int row, int col) {
        Page page = PAGES[row][col];
        String title = page.title;
        String party = page.party;
        int id = page.pic_id;
        //CardFragment fragment = CardFragment.create(party, title, id);
        //customCardFragment fragment = new customCardFragment();
        Custom2Fragment fragment = Custom2Fragment.newInstance(title, party,id);
        // Advanced settings
        fragment.setCardGravity(page.cardGravity);
        //fragment.setExpansionEnabled(page.expansionEnabled);
        fragment.setExpansionEnabled(false);
        fragment.setExpansionDirection(page.expansionDirection);
        fragment.setExpansionFactor(page.expansionFactor);

        return fragment;
    }


/*    @Override
    public ImageReference getBackground(int row, int column) {
        return ImageReference.forDrawable(BG_IMAGES[0][0]);
    }*/

/*    @Override
    public Drawable getBackgroundForRow(int row) {
        return mContext.getResources().getDrawable(
                (BG_IMAGES[row % BG_IMAGES.length]), null);
    }*/

/*    @Override
    public Drawable getBackgroundForRow(int row) {
        //return mContext.getResources().getDrawable(mData.get(row).getImageResource(),null);
        ColorDrawable cd = new ColorDrawable(0xFFFF6666);
        return cd;
    }*/

    // Obtain the background image for the specific page
/*    @Override
    public Drawable getBackgroundForPage(int row, int column) {
        if( row == 2 && column == 1) {
            // Place image at specified position
            return mContext.getResources().getDrawable(R.drawable.redhome, null);
        } else {
            // Default to background image for row
            return GridPagerAdapter.BACKGROUND_NONE;
        }
    }*/

    @Override
    public int getRowCount() {
        return PAGES.length;
    }

    @Override
    public int getColumnCount(int rowNum) {
        return PAGES[rowNum].length;
    }
}



