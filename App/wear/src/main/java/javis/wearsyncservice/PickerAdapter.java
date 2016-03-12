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
    public String[] name_title;
    public String[] party;
    private final Page[][] PAGES;

    public PickerAdapter(Context ctx, FragmentManager fm, String[] name_title, String[] party) {
        super(fm);
        mContext = ctx;
        this.name_title = name_title;
        this.party = party;
        PAGES = new Page[1][name_title.length];
        for (int i=0;i<name_title.length;i++)
        {
            PAGES[0][i] = new Page(name_title[i], party[i], R.drawable.edlabov_wear, i);
        }
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
        int pos;
        int cardGravity = Gravity.CENTER;
        boolean expansionEnabled = false;
        float expansionFactor = 1.0f;
        int expansionDirection = CardFragment.EXPAND_DOWN;

        public Page(String Title, String Party, int ID, int pos) {
            this.title = Title;
            this.party = Party;
            this.pic_id = ID;
            this.pos = pos;
        }
    }

    /*private final Page[][] PAGES = {
            {
                    new Page("Senator Laura Lakoff","REPUBLICAN",R.drawable.lauralakoff),
                    new Page("Senator Ronan Nash","DEMOCRAT",R.drawable.ronannash_wear),
                    new Page("Senator Ed Labov","REPUBLICAN", R.drawable.edlabov_wear),
                    new Page("Representative Hannah Whorf","DEMOCRAT", R.drawable.hannahwhorf_wear),
                    new Page("Senator Mark Hagen","DEMOCRAT", R.drawable.markhagen_wear)
            },

    };*/


    @Override
    public CardFragment getFragment(int row, int col) {
        Page page = PAGES[row][col];
        String title = page.title;
        String party = page.party;
        int id = page.pic_id;
        int position = page.pos;
        //CardFragment fragment = CardFragment.create(party, title, id);
        //customCardFragment fragment = new customCardFragment();
        Custom2Fragment fragment = Custom2Fragment.newInstance(title, party,id, position);
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



