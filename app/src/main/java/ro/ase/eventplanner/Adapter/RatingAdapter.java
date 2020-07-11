/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 package ro.ase.eventplanner.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import ro.ase.eventplanner.Model.Rating;
import ro.ase.eventplanner.R;


public class RatingAdapter extends FirestoreAdapter<RatingAdapter.ViewHolder> {

    public RatingAdapter(Query query) {
        super(query);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_rating, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position).toObject(Rating.class));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        MaterialRatingBar ratingBar;
        TextView textView;
        TextView textDate;

        public ViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.rating_item_name);
            ratingBar = itemView.findViewById(R.id.rating_item_rating);
            textView = itemView.findViewById(R.id.rating_item_text);
            textDate = itemView.findViewById(R.id.rating_item_date);



        }

        public void bind(Rating rating) {
            nameView.setText(rating.getUserName());
            ratingBar.setRating((float) rating.getRating());
            textView.setText(rating.getText());
            Date date = rating.getTimestamp();
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Bucharest"));
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            textDate.setText(day + "/" + month + "/" + year);
        }
    }

}
