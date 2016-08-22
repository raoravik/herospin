/*
 * Copyright (c) 2016. Ravi Rao.
 *
 * This file is created as part of VISA POC and  Licensed under the Apache License, Version 2.0 (the "License");
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

package com.visa.r4r.poc.herospin.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.visa.r4r.poc.herospin.R;
import com.visa.r4r.poc.herospin.fragment.CharacterFragment.OnListFragmentInteractionListener;
import com.visa.r4r.poc.herospin.marvel.model.Character;
import com.visa.r4r.poc.herospin.marvel.model.MarvelImage;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Character} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CharacterRecyclerViewAdapter extends RecyclerView.Adapter<CharacterRecyclerViewAdapter.ViewHolder> {

    private final List<Character> mValues;
    private final OnListFragmentInteractionListener mListener;

    public CharacterRecyclerViewAdapter(List<Character> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_character, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Character character = mValues.get(position);
        holder.mItem = character;
        holder.nameTextView.setText(character.getName());
        Picasso.with(holder.itemView.getContext()).load(character.getThumbnail().getImageUrl(MarvelImage.Size.STANDARD_FANTASTIC))
                .fit().centerCrop().into(holder.photoImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView photoImageView;
        public final TextView nameTextView;

        public Character mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            photoImageView = (ImageView) view.findViewById(R.id.iv_super_hero_photo);
            nameTextView = (TextView) view.findViewById(R.id.tv_super_hero_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameTextView.getText() + "'";
        }
    }
}
