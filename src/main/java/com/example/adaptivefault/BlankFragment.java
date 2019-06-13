package com.example.adaptivefault;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;

    private OnFragmentInteractionListener mListener;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
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
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        Log.d("recyclerView", recyclerView.toString());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Error> errors = Error.getFromLocal(getContext());
        errors.add(new Error("123","456"));
        Log.d("number", "" + errors.size());
        RecyclerListAdapter recyclerListAdapter = new RecyclerListAdapter(errors);
        RecyclerListAdapter.MyItemTouchCallback myItemTouchCallback = recyclerListAdapter.new MyItemTouchCallback(recyclerListAdapter);
        recyclerView.setAdapter(recyclerListAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(myItemTouchCallback);
        helper.attachToRecyclerView(recyclerView);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    class RecyclerListAdapter extends RecyclerView.Adapter {
        class MyItemTouchCallback extends ItemTouchHelper.Callback {

            private final RecyclerListAdapter adapter;

            public MyItemTouchCallback(RecyclerListAdapter adapter) {
                this.adapter = adapter;
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.LEFT,
                        ItemTouchHelper.START | ItemTouchHelper.END);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int startPosition = viewHolder.getAdapterPosition();
                int endPosition = target.getAdapterPosition();
                //the item to swap
                int index = startPosition;

                //drag direction
                int dir = startPosition - endPosition > 0 ? -1 : 1;

                while (index < endPosition) {
                    Collections.swap(adapter.getmItemLists(), index, index + dir);
                    index += dir;
                }

                recyclerView.getAdapter().notifyItemMoved(startPosition, endPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.END || direction == ItemTouchHelper.START) {
                    adapter.getmItemLists().remove(position);
                    adapter.notifyItemRemoved(position);
                }
            }

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView error;

            ViewHolder(View view) {
                super(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder customizeDialog =
                                new AlertDialog.Builder(getContext(), R.style.mdialog);
                        final View dialogView = LayoutInflater.from(getContext())
                                .inflate(R.layout.localsolution, null);
                        customizeDialog.setView(dialogView);
                        Dialog dialog = customizeDialog.create();
                        Window window = dialog.getWindow();
                        window.getDecorView().setPadding(50,50,50,50);
                        WindowManager.LayoutParams layoutParams = window.getAttributes();
                        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                        //layoutParams.horizontalMargin = 100;
                        //layoutParams.verticalMargin = 100;
                        window.setAttributes(layoutParams);
                        final TextView textView = dialogView.findViewById(R.id.solution);
                        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                    }
                });
                error = view.findViewById(R.id.error);
            }
        }

        final private List<Error> errors;

        RecyclerListAdapter(List<Error> errors) {
            this.errors = errors;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide, parent, false);
            RecyclerView.ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Log.d("position", "" + position);
            Log.d("size", "" + this.errors.size());
            if(this.errors.get(position)!=null) {
                ((ViewHolder) holder).error.setText(this.errors.get(position).toString());
            }
        }

        @Override
        public int getItemCount() {
            return this.errors.size();
        }

        public List getmItemLists() {
            return this.errors;
        }

    }
}
