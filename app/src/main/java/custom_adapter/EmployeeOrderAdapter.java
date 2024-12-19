package custom_adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pack1.dao.ProductDao;
import com.pack1.dao.UserDao;
import com.pack1.dao.UserOrderDao;
import com.pack1.models.Product;
import com.pack1.models.User;
import com.pack1.models.UserOrder;
import com.pack1.quanlybangiaythethao.R;

import java.util.ArrayList;

public class EmployeeOrderAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<UserOrder> userOrders;
    private LayoutInflater inflater;

    public EmployeeOrderAdapter(Context context,ArrayList<UserOrder> userOrders,LayoutInflater inflater){
        this.context = context;
        this.userOrders = userOrders;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return userOrders.size();
    }

    @Override
    public Object getItem(int position) {
        return userOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.order_layout_employee,null);
        ProductDao productDao = new ProductDao(context);
        Product product = productDao.getProductById(userOrders.get(position).getProductId());
        Bitmap pImage = product.getPdImage();
        String pName = product.getName();
        String status = userOrders.get(position).getStatus();
        String quantity ="" + userOrders.get(position).getQuantity()+""+" Đôi";
        UserOrderDao userOrderDao = new UserOrderDao(context);
        String orderDate = userOrderDao.getOrderDateById(userOrders.get(position).getOrderId());

        Log.d("MYLOG",orderDate);

        float price = userOrders.get(position).getTotalPrice();
        ImageView imageView = view.findViewById(R.id.product_image);
        TextView pNameTv = view.findViewById(R.id.pName);
        TextView statusTv = view.findViewById(R.id.status);
        TextView quantityTv = view.findViewById(R.id.soluong);
        TextView priceTv = view.findViewById(R.id.totalprice);
        TextView orderDateTv = view.findViewById(R.id.orderDate);
        orderDateTv.setText(orderDate);
        imageView.setImageBitmap(pImage);
        pNameTv.setText(pName);
        priceTv.setText(""+String.format("%,d", (int) price) +" VNĐ");
        statusTv.setText(status);
        quantityTv.setText(quantity);

        UserDao userDao = new UserDao(context);
        User user = userDao.getUserById(userOrders.get(position).getUserId());
        TextView uName = view.findViewById(R.id.customername);
        uName.setText(user.getFname().toString() + " "+user.getLname().toString());
        TextView orderIDTV = view.findViewById(R.id.id);
        orderIDTV.setText(""+userOrders.get(position).getOrderId());
        return view;
    }
}

