package custom_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pack1.quanlybangiaythethao.R;
import com.pack1.models.Review;
import com.pack1.dao.UserDao;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ReviewDisplayAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Review> reviews;
    private LayoutInflater inflater;

    public ReviewDisplayAdapter(Context context, ArrayList<Review> reviews, LayoutInflater inflater) {
        this.context = context;
        this.reviews = reviews;
        this.inflater = inflater.from(context);
    }

    @Override
    public int getCount() {
        if(!reviews.isEmpty())
            return reviews.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return reviews.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.review_display_layout,null);
        TextView reviewText = view.findViewById(R.id.review);
        reviewText.setText(reviews.get(i).getReview());

        UserDao userDao = new UserDao(context);
        String name = userDao.getUserById(reviews.get(i).getUserId()).getFname()+" "+userDao.getUserById(reviews.get(i).getUserId()).getLname();
        TextView uname = view.findViewById(R.id.username);
        uname.setText(name);

        ImageView ava = view.findViewById(R.id.avatar);
        if(userDao.getUserById(reviews.get(i).getUserId()).getAvatar() != null)
            ava.setImageBitmap(userDao.getUserById(reviews.get(i).getUserId()).getAvatar());
        else
            ava.setImageResource(R.drawable.ic_launcher_background);
        TextView rating = view.findViewById(R.id.rating);

        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        rating.setText(""+decimalFormat.format( reviews.get(i).getRating()));
        return view;
    }
}
