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

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.visa.r4r.poc.herospin.marvel.model.Character;

import com.visa.r4r.poc.herospin.R;
import com.visa.r4r.poc.herospin.marvel.model.MarvelImage;
import com.visa.r4r.poc.herospin.marvel.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CharacterDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CharacterDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterDetailsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static Character currentCharacter;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CharacterDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param character Parameter 1.
     * @return A new instance of fragment CharacterDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CharacterDetailsFragment newInstance(Character character) {
        CharacterDetailsFragment fragment = new CharacterDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, character.getId());
        args.putString(ARG_PARAM2, character.getName());
        currentCharacter = character;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_character_details, container, false);
        ImageView characterImage = (ImageView) fragmentView.findViewById(R.id.image);
        TextView characterTitle = (TextView) fragmentView.findViewById(R.id.tvTitle);
        TextView characterDescription = (TextView) fragmentView.findViewById(R.id.tvOverview);
        characterTitle.setText(currentCharacter.getName());
        characterDescription.setText(currentCharacter.getDescription());
        Picasso.with(getContext()).load(currentCharacter.getThumbnail().getImageUrl(MarvelImage.Size.LANDSCAPE_AMAZING))
                .fit().centerCrop().into(characterImage);
        View fabRandomMovie = fragmentView.findViewById(R.id.fab);
        fabRandomMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(currentCharacter.getName());
            }
        });

        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String characterName);
    }
}
