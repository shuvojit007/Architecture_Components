package component.learnprogramming.com.arc_component.adapter;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import component.learnprogramming.com.arc_component.R;
import component.learnprogramming.com.arc_component.model.Note;

public class NoteAdapter extends ListAdapter<Note,NoteAdapter.ViewHolder> {

    private OnItemClickListener listener;
    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }


    private static final DiffUtil.ItemCallback<Note>DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(Note oldItem, Note newItem) {
            return oldItem.getId()==newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Note oldItem, Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle())&&
                    oldItem.getDescription().equals(newItem.getDescription())&&
                    oldItem.getPriority()==newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note currentNote = getItem(position);
        holder.title.setText(currentNote.getTitle());
        holder.piority.setText(String.valueOf(currentNote.getPriority()));
        holder.description.setText(currentNote.getDescription());
    }



    public Note getNoteAt(int position) {
        return getItem(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title,description,piority;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            piority = itemView.findViewById(R.id.piority);


            itemView.setOnClickListener(view -> {
                if (listener!=null&&getAdapterPosition()!=RecyclerView.NO_POSITION){
                    listener.onItemClick(getItem(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Note note);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
