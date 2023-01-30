package com.example.a2month_navigation_lesson1.ui.home;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2month_navigation_lesson1.R;
import com.example.a2month_navigation_lesson1.interfacess.OnItemClickListenerr;
import com.example.a2month_navigation_lesson1.models.Task;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private ArrayList<Task>list;
    private OnItemClickListenerr listenerr;

    public TaskAdapter(ArrayList<Task>list){
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_task,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
holder.bind(list.get(position));
/**if(position%2==0){
    holder.layout.setBackgroundColor(Color.GRAY);
}
else{
    holder.layout.setBackgroundColor(Color.WHITE);
}*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setListener(OnItemClickListenerr listener) {
        this.listenerr = listener;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
private TextView textTitle,textTitle1;
private View layout;

        public ViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            textTitle=itemView.findViewById(R.id.textTitle);
            textTitle1=itemView.findViewById(R.id.textTitle1);
            layout=itemView.findViewById(R.id.layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenerr.onClicklElement(getAdapterPosition());

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listenerr.onItemLongClick(getAdapterPosition());
                    return true;
                }
            });
        }

        public void bind(Task task) {
            textTitle.setText(task.getTitle());
            textTitle1.setText(task.getDesc());
        }
    }
}
