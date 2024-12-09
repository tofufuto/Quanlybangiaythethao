package custom_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pack1.models.Product;
import com.pack1.quanlybangiaythethao.R;

import java.util.List;


public class ProductGridViewAdapter extends BaseAdapter {

        private Context context;
        private List<Product> productList;
        private LayoutInflater inflater;


    public ProductGridViewAdapter(Context context,List<Product> productList, LayoutInflater inflater) {
        this.context = context;
        this.productList = productList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int i) {
        return productList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.product_gridview_item_layout,null);
        ImageView imageView = view.findViewById(R.id.imageView);
        if(productList.get(i).getPdImage() == null)
            imageView.setImageResource(R.drawable.ic_launcher_background);
        else
            imageView.setImageBitmap(productList.get(i).getPdImage());
        TextView textViewName = view.findViewById(R.id.pname);
        textViewName.setText(productList.get(i).getName());

        TextView textViewRating = view.findViewById(R.id.prating);
        textViewRating.setText(""+productList.get(i).getRating());

        TextView textViewPrice = view.findViewById(R.id.pprice);
        textViewPrice.setText((""+(int) productList.get(i).getPrice())+"VNĐ");
        return view;
    }
}
