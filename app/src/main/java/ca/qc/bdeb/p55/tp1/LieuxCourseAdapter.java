package ca.qc.bdeb.p55.tp1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class LieuxCourseAdapter extends RecyclerView.Adapter<LieuxCourseAdapter.LieuxCourseViewHolder> {

    private List<LieuxCourse> lieuxCourse;
    private LieuxCourseListener lieuxCourseListener;

    public LieuxCourseAdapter(List<LieuxCourse> lieuxCourse, LieuxCourseListener lieuxCourseListener) {
        this.lieuxCourse = lieuxCourse;
        this.lieuxCourseListener = lieuxCourseListener;
    }

    @NonNull
    @Override
    public LieuxCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LieuxCourseViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_courses,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull LieuxCourseViewHolder holder, int position) {
    holder.bindLieuCourse(lieuxCourse.get(position));
    }

    @Override
    public int getItemCount() {
        return lieuxCourse.size();
    }

    public List<LieuxCourse> getSelectedLieuxCourse(){
        List<LieuxCourse> selectedLieuxCourse = new ArrayList<>();
        for (LieuxCourse lieuxCourse: lieuxCourse){
            if(lieuxCourse.isSelected){
                selectedLieuxCourse.add(lieuxCourse);
            }
        }
        return lieuxCourse;
    }

    class LieuxCourseViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout layoutLieuxCourse;
        View viewBackground;
        RoundedImageView imageLieuCourse;
        TextView lieuName, textCreatedBy, textStory;
        RatingBar ratingBar;
        ImageView imageSelected;

        public LieuxCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutLieuxCourse = itemView.findViewById(R.id.const_layout);
            viewBackground = itemView.findViewById(R.id.viewBackground);
            imageLieuCourse = itemView.findViewById(R.id.imageLieuCourse);
            lieuName = itemView.findViewById(R.id.nomLieu);
            textCreatedBy = itemView.findViewById(R.id.textCreateBy);
            textStory = itemView.findViewById(R.id.textStory);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            imageSelected = itemView.findViewById(R.id.imageSelected);

        }
        void bindLieuCourse(final LieuxCourse lieuxCourse){
            imageLieuCourse.setImageResource(lieuxCourse.image);
            lieuName.setText(lieuxCourse.name);
            textCreatedBy.setText(lieuxCourse.createdBy);
            textStory.setText(lieuxCourse.story);
            ratingBar.setRating(lieuxCourse.rating);
            if(lieuxCourse.isSelected){
                viewBackground.setBackgroundResource(R.drawable.lieu_course_selected_background);
                imageSelected.setVisibility(View.VISIBLE);
            }
            else{
                viewBackground.setBackgroundResource(R.drawable.lieu_course_background);
                imageSelected.setVisibility(View.GONE);
            }
            layoutLieuxCourse.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if (lieuxCourse.isSelected){
                        viewBackground.setBackgroundResource(R.drawable.lieu_course_background);
                        imageSelected.setVisibility(View.GONE);
                        lieuxCourse.isSelected = false;
                        if (getSelectedLieuxCourse().size()==0){
                            lieuxCourseListener.onLieuCourseAction(false);
                        }
                    }
                }
            })
            ;
        }
    }
}
