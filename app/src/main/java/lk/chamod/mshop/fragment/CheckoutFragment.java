package lk.chamod.mshop.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lk.chamod.mshop.R;
import lk.chamod.mshop.databinding.FragmentCheckoutBinding;


public class CheckoutFragment extends Fragment {

private FragmentCheckoutBinding binding;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCheckoutBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
}