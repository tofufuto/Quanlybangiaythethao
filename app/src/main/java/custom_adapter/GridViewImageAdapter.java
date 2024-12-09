package custom_adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pack1.quanlybangiaythethao.R;

import java.util.List;

public class GridViewImageAdapter extends BaseAdapter {

    private Context context;
    private List<Bitmap> images;
    private LayoutInflater inflater;

    public GridViewImageAdapter(Context context, List<Bitmap> images, LayoutInflater inflater)
    {
        this.context = context;
        this.images = images;
        this.inflater = inflater.from(context);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return images.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.add_product_images_selection_review,null);
        ImageView imageView = view.findViewById(R.id.imageViewPGVA);
        if(images.get(i) == null)
            imageView.setImageResource(R.drawable.ic_launcher_background);
        else
            imageView.setImageBitmap(images.get(i));
        return view;
    }
}
