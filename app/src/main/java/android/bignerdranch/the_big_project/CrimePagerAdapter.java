package android.bignerdranch.the_big_project;





import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CrimePagerAdapter extends FragmentStateAdapter {
    private CrimesFragment unsolvedFragment;

    public CrimePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            if (unsolvedFragment == null)
                unsolvedFragment = new CrimesFragment();
            return unsolvedFragment;
        } else {


            return new DetailsFragment();
        }
    }

    public CrimesFragment getUnsolvedFragment() {
        return unsolvedFragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}




