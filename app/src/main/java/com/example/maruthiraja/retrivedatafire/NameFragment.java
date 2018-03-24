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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NameFragment extends Fragment {
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

    public NameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NameFragment newInstance(String param1, String param2) {
        NameFragment fragment = new NameFragment();
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
        return inflater.inflate(R.layout.fragment_name, container, false);
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

    public void setrecycler(View view) {
        progress = new ProgressDialog(getContext());
        progress.setTitle("Loading Items");
        progress.setMessage("Loading...!!!");
        progress.show();
        //Toast.makeText(getContext(), "work", Toast.LENGTH_SHORT).show();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("shop_details");
        System.out.println(mdatabase);
        gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mdatabase.keepSynced(true);
        //Toast.makeText(this, "called", Toast.LENGTH_SHORT).show();
        itemlist = (RecyclerView) view.findViewById(R.id.namefragmentid);
        itemlist.setHasFixedSize(true);
        itemlist.setLayoutManager(gridLayoutManager);


        FirebaseRecyclerAdapter<Getdata,Holderprice> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Getdata, Holderprice>(
                Getdata.class,
                R.layout.itemrow,
                Holderprice.class,
                mdatabase.orderByChild("title")
        ) {

            @Override
            protected void populateViewHolder(Holderprice viewHolder, Getdata model, int position) {
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
            public Holderprice onCreateViewHolder(ViewGroup parent, int viewType) {
                Holderprice viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new Holderprice.ClickListener() {
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

        itemlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class Holderprice extends RecyclerView.ViewHolder
    {
        View mview;

        public Holderprice(View itemView) {
            super(itemView);
            mview = itemView;
            mview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // Toast.makeText(mview.getContext(), "you clicked me!!", Toast.LENGTH_SHORT).show();
                    mClickListener.onItemClick(view,getAdapterPosition());
                }
            });
            mview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(mview.getContext(), "you Long clicked me!!", Toast.LENGTH_SHORT).show();
                    mClickListener.onItemLongClick(view,getAdapterPosition());
                    return true;
                }
            });
        }

        private Holderprice.ClickListener mClickListener;

        public void setQty(String qty) {
            TextView textdesc = (TextView) mview.findViewById(R.id.countid);
            textdesc.setText(qty);
        }


        public interface ClickListener{
            public void onItemClick(View view, int position);
            public void onItemLongClick(View view, int position);
        }

        public void setOnClickListener(Holderprice.ClickListener clickListener){
            mClickListener = clickListener;
        }

        public void setTitle(String title)
        {
            TextView textTitle = (TextView) mview.findViewById(R.id.itemtitle);
            textTitle.setText(title);
        }
        public void setDescription(String desc)
        {
            TextView textdesc = (TextView) mview.findViewById(R.id.itemdescription);
            textdesc.setText(desc);
        }
        public void setPrice(String price)
        {
            TextView textprice = (TextView) mview.findViewById(R.id.itemprice);
            textprice.setText(price);
        }
        public void setImage(Context ctx,String image)
        {
            ImageView img = (ImageView) mview.findViewById(R.id.postimage);
            Picasso.with(ctx).load(image).fit().centerCrop().error(R.drawable.ic_broken_image_black_24dp)
                    .placeholder(R.drawable.ic_image_black_24dp).into(img);
            //  Picasso.with(ctx).load(image).into(img);
        }
        public void setRating(String rating)
        {
            RatingBar ratingBar = (RatingBar) mview.findViewById(R.id.ratingBar);
            ratingBar.setFocusable(false);
            ratingBar.setRating(Float.parseFloat(rating));
            System.out.println(rating);
        }
        public void setReview()
        {

        }
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
