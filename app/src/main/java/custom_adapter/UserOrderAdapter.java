package custom_adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pack1.models.UserOrder;
import com.pack1.quanlybangiaythethao.R;
import com.pack1.quanlybangiaythethao.Staticstuffs;

import java.util.List;

public class UserOrderAdapter extends BaseAdapter {
    private Context context;
    private List<UserOrder> userOrderList;
    private LayoutInflater inflater;

    public UserOrderAdapter(Context context, List<UserOrder> userOrderList) {
        this.context = context;
        this.userOrderList = userOrderList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return userOrderList.size();
    }

    @Override
    public Object getItem(int position) {
        return userOrderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return userOrderList.get(position).getOrderId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.order_item_layout, parent, false);
            holder = new ViewHolder();
            holder.productImage = convertView.findViewById(R.id.productImage);
            holder.productName = convertView.findViewById(R.id.productName);
            holder.orderStatus = convertView.findViewById(R.id.orderStatus);
            holder.orderDate = convertView.findViewById(R.id.orderDate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lấy thông tin đơn hàng hiện tại
        UserOrder order = userOrderList.get(position);

        // Cập nhật thông tin cho từng item
        holder.productName.setText(order.getProductName());
        holder.orderStatus.setText(order.getStatus());

        // Kiểm tra null và chuyển đổi ngày
        if (order.getDateOrder() != null) {
            holder.orderDate.setText(Staticstuffs.ConvertDatetoString(order.getDateOrder()));
        } else {
            holder.orderDate.setText("Ngày không xác định");
        }

        // Kiểm tra hình ảnh sản phẩm và cập nhật
        Bitmap productImage = order.getProductImage();
        if (productImage != null) {
            holder.productImage.setImageBitmap(productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.ic_launcher_background); // Hình ảnh mặc định nếu không có ảnh
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView orderStatus;
        TextView orderDate;
    }
}
