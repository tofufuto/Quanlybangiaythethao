package custom_adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pack1.models.UserOrder;
import com.pack1.quanlybangiaythethao.R;

import java.util.ArrayList;

public class OrderAdapter extends ArrayAdapter<UserOrder> {

    private Context context;
    private ArrayList<UserOrder> orders;

    public OrderAdapter(Context context, ArrayList<UserOrder> orders) {
        super(context, 0, orders); // Gọi constructor của ArrayAdapter
        this.context = context;
        this.orders = orders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false); // Layout cho mỗi item
        }

        // Lấy đơn hàng tại vị trí hiện tại
        UserOrder order = orders.get(position);

        // Liên kết các view trong item với dữ liệu đơn hàng
        ImageView imgProduct = convertView.findViewById(R.id.imgProduct);
        TextView tvProductName = convertView.findViewById(R.id.tvProductName);
        TextView tvOrderStatus = convertView.findViewById(R.id.tvOrderStatus);
        TextView tvTotalPrice = convertView.findViewById(R.id.tvTotalPrice);

        // Gán dữ liệu cho các TextView
        tvProductName.setText(order.getProductName()); // Cập nhật tên sản phẩm từ đối tượng đơn hàng
        tvOrderStatus.setText("Trạng thái: " + order.getStatus()); // Cập nhật trạng thái đơn hàng
        tvTotalPrice.setText("Tổng tiền: " + order.getTotalPrice() + " VND"); // Cập nhật giá tiền


        imgProduct.setImageResource(R.drawable.pt1); // Thay bằng ảnh thực tế nếu có

        return convertView;
    }
}
