package com.example.maruthiraja.retrivedatafire;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RatingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RatingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RatingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private RecyclerView itemlist;
    private DatabaseReference mdatabase;
    private StaggeredGridLayoutManager gridLayoutManager;
    private ProgressDialog progress;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RatingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RatingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RatingFragment newInstance(String param1, String param2) {
        RatingFragment fragment = new RatingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        return inflater.inflate(R.layout.fragment_rating, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setrecycler(view);
        itemlist.setFocusable(false);
        //itemlist.smoothScrollToPosition(view.getTop());
    }

    private void setrecycler(View view) {

        progress = new ProgressDialog(getContext());
        progress.setTitle("Loading Items");
        progress.setMessage("Loading...!!!");
        progress.show();
        //Toast.makeText(getContext(), "work", Toast.LENGTH_SHORT).show();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("shop_details");
        System.out.println(mdatabase);
        gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //gridLayoutManager.setReverseLayout(true);
        mdatabase.keepSynced(true);
        //Toast.makeText(this, "called", Toast.LENGTH_SHORT).show();
        itemlist = (RecyclerView) view.findViewById(R.id.ratingfragmentid);
        itemlist.setHasFixedSize(true);
        itemlist.setLayoutManager(gridLayoutManager);


        FirebaseRecyclerAdapter<Getdata,PriceFragment.Holderprice> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Getdata, PriceFragment.Holderprice>(
                Getdata.class,
                R.layout.itemrow,
                PriceFragment.Holderprice.class,
                mdatabase.orderByChild("rating")
        ) {

            @Override
            protected void populateViewHolder(PriceFragment.Holderprice viewHolder, Getdata model, int position) {
                System.out.println(model.getTitle());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getContext(),model.getImage());
                viewHolder.setRating(model.getRating());
                viewHolder.setQty(model.getQuantity());
                if (progress != null && progress.isShowing()) {
                    progress.dismiss();
                }
            }

            @Override
            public PriceFragment.Holderprice onCreateViewHolder(ViewGroup parent, int viewType) {
                PriceFragment.Holderprice viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new PriceFragment.Holderprice.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Toast.makeText(getApplicationContext(), "Item clicked at " + position, Toast.LENGTH_SHORT).show();
                        Intent selint = new Intent(getContext(),SelectedItem.class);
                        Intent intent = selint.putExtra("position", getRef(position).getKey());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(getContext(), "Item long clicked at " + position, Toast.LENGTH_SHORT).show();
                    }
                });
                return viewHolder;
            }

        };
        System.out.println("position top :"+view.getBottom());
        itemlist.setAdapter(firebaseRecyclerAdapter);

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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
