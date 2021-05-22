package com.seamk.mobile.objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.seamk.mobile.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Room extends AbstractItem<Room, Room.ViewHolder> {

    private String roomId;
    private String roomCode;
    private String roomFullName;
    private String roomName;
    private String roomDesc;
    private String parentBuilding;

    public Room(String roomCode, String roomFullName, String parentBuilding) {
        this.roomCode = roomCode;
        this.roomFullName = roomFullName;
        this.parentBuilding = parentBuilding;

        String[] splitRoom;
        splitRoom = roomFullName.split(" \\(");
        if (splitRoom.length == 2){
            splitRoom[1] = splitRoom[1].substring(0, splitRoom[1].length() - 1);
            this.roomName = splitRoom[0];
            this.roomDesc =  splitRoom[1];
        } else {
            this.roomName = splitRoom[0];
            this.roomDesc = "";
        }
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getRoomFullName() {
        return roomFullName;
    }

    public void setRoomFullName(String roomFullName) {
        this.roomFullName = roomFullName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomDesc() {
        return roomDesc;
    }

    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }

    public String getParentBuilding() {
        return parentBuilding;
    }

    public void setParentBuilding(String parentBuilding) {
        this.parentBuilding = parentBuilding;
    }

    @Override
    public int getType() {
        return R.id.fa_room_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.card_classroom;
    }

    @Override
    public void bindView(@NonNull ViewHolder viewHolder, @NonNull List<Object> payloads) {
        super.bindView(viewHolder, payloads);
        viewHolder.roomCode.setText(roomName);
        viewHolder.roomName.setText(roomDesc);
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);

        holder.roomCode.setText(null);
        holder.roomName.setText(null);
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(@NonNull View view) {
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_classroom_name) TextView roomCode;
        @BindView(R.id.card_classroom_desc) TextView roomName;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}