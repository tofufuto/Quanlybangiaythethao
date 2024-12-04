package custom_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pack1.quanlybangiaythethao.R;
import com.pack1.quanlybangiaythethao.Review;
import com.pack1.quanlybangiaythethao.User;

import java.util.ArrayList;

public class EmployeesListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<User> employeeList;// list user là nhan vien
    private LayoutInflater inflater;

    public EmployeesListAdapter(Context context, ArrayList<User> employeeList, LayoutInflater inflater) {
        this.context = context;
        this.employeeList = employeeList;
        this.inflater = inflater.from(context);
    }

    @Override
    public int getCount() {
        return employeeList.size();
    }

    @Override
    public Object getItem(int i) {
        return employeeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.employee_list_item_layout,null);
        ImageView avatar = view.findViewById(R.id.imageView4);
        if(employeeList.get(i).getAvatar() == null) {
            avatar.setImageResource(R.drawable.baseline_face_24);
        }
        else {
            avatar.setImageBitmap(employeeList.get(i).getAvatar());
        }

        TextView employeeName = view.findViewById(R.id.textViewName);
        employeeName.setText(employeeList.get(i).getFname()+" "+employeeList.get(i).getLname());
        return view;
    }
}
