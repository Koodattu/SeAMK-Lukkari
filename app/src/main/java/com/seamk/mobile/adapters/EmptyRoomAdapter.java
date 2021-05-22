package com.seamk.mobile.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seamk.mobile.R;
import com.seamk.mobile.objects.EmptyRoom;
import com.tolstykh.textviewrichdrawable.TextViewRichDrawable;

import java.util.List;

/**
 * Created by Juha Ala-Rantala on 5.4.2017.
 */

public class EmptyRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<EmptyRoom> items;
    private Context context;


    public EmptyRoomAdapter(Context context, List<EmptyRoom> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_empty_room,parent,false);
        return new EmptyRoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

        Object item = items.get(position);
        ((EmptyRoomViewHolder) viewHolder).bind((EmptyRoom) item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class EmptyRoomViewHolder extends RecyclerView.ViewHolder{

        public TextView room_code;
        public TextView building;
        public TextView type;
        public TextView status;

        public EmptyRoomViewHolder(View itemView){
            super(itemView);
            room_code = (TextViewRichDrawable) itemView.findViewById(R.id.room_code);
            building = (TextViewRichDrawable) itemView.findViewById(R.id.building);
            type = itemView.findViewById(R.id.type);
            status = (TextViewRichDrawable) itemView.findViewById(R.id.status);
        }
        public void bind(final EmptyRoom emptyRoom) {
            room_code.setText(emptyRoom.getRoomCode());
            building.setText(emptyRoom.getRoomBuilding());
            type.setText(emptyRoom.getRoomType());
            status.setText(emptyRoom.getRoomStatus());

        }
    }
}
