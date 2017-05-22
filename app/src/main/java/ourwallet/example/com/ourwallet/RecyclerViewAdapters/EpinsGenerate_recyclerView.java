package ourwallet.example.com.ourwallet.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ourwallet.example.com.ourwallet.Constants;
import ourwallet.example.com.ourwallet.Models.Contacts_Model;
import ourwallet.example.com.ourwallet.Models.Epins_Model;
import ourwallet.example.com.ourwallet.R;

/**
 * Created by uzair on 09/05/2017.
 */

public class EpinsGenerate_recyclerView extends RecyclerView.Adapter<EpinsGenerate_recyclerView.MyViewHolder> implements View.OnClickListener {

    private List<String> horizontalList=new ArrayList<String>();
    private Context context;
    private  List<Epins_Model> epinslist=new ArrayList<Epins_Model>();
    @Override
    public void onClick(View view) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtView;


        public MyViewHolder(View view) {
            super(view);
            txtView = (TextView) view.findViewById(R.id.epins_text);
        }
    }

    public EpinsGenerate_recyclerView( List<Epins_Model> horizontalList) {

        this.epinslist = horizontalList;
    }

    public EpinsGenerate_recyclerView(Context context, List<String> horizontalList) {
        this.context=context;
        this.horizontalList = horizontalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.epins_view_items, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if(Constants.constrtuctor_value==1){
            holder.txtView.setText(horizontalList.get(position));

        }
       else if(Constants.constrtuctor_value==2) {
            holder.txtView.setText(epinslist.get(position).getPins());
        }
    }

    @Override
    public int getItemCount() {
        if(Constants.constrtuctor_value==1){
        return horizontalList.size();}
        else {
            return epinslist.size();
        }
}
}