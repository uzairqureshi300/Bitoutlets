package ourwallet.example.com.ourwallet.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ourwallet.example.com.ourwallet.Contacts;
import ourwallet.example.com.ourwallet.Models.Contacts_Model;
import ourwallet.example.com.ourwallet.R;

/**
 * Created by uzair on 09/05/2017.
 */

public class Contacts_recyclerView extends RecyclerView.Adapter<Contacts_recyclerView.MyViewHolder> {

    private List<Contacts_Model> horizontalList=new ArrayList<Contacts_Model>();
    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtView;
        public TextView phone;

        public MyViewHolder(View view) {
            super(view);
            txtView = (TextView) view.findViewById(R.id.txtView);
            phone = (TextView)view.findViewById(R.id.phone);
        }
    }


    public Contacts_recyclerView(Context context,List<Contacts_Model> horizontalList) {
        this.context=context;
        this.horizontalList = horizontalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_view_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txtView.setText(horizontalList.get(position).getFirst_name());
        holder.phone.setText(horizontalList.get(position).getPhone());
        holder.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,holder.txtView.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }
}