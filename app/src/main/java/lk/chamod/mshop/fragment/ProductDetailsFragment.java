package lk.chamod.mshop.fragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import lk.chamod.mshop.R;
import lk.chamod.mshop.adpter.ProductSliderAdapter;
import lk.chamod.mshop.databinding.FragmentProductDetailsBinding;
import lk.chamod.mshop.model.Product;


public class ProductDetailsFragment extends Fragment {

    private FragmentProductDetailsBinding binding;
    private String productId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getString("productId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().findViewById(R.id.bottm_navigation_view).setVisibility(View.GONE);

        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });


        // Load Product Detials

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("products")
                .whereEqualTo("productId", productId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot qds) {
                        if (!qds.isEmpty()) {
                            Product product = qds.getDocuments().get(0).toObject(Product.class);

                            // Debug karala balanna mehema
                            if (product != null && product.getImages() != null) {
                                android.util.Log.d("ProductData", "Images: " + product.getImages().toString());

                                ProductSliderAdapter adapter = new ProductSliderAdapter(product.getImages());
                                binding.productImageSlider.setAdapter(adapter);

                                binding.dotsIndicator.attachTo(binding.productImageSlider);
                                binding.productDetailsTitle.setText(product.getTitle());
                                binding.productDetailsPrice.setText("LKR " + product.getPrice());
                                binding.productDetailsAvgQty.setText(String.valueOf(product.getStockCount()));


                                binding.productDetailsRating.setRating((float) product.getRating());
                            }


                        }


                    }
                });

    }

    @Override
    public void onStop() {
        super.onStop();

        getActivity().findViewById(R.id.bottm_navigation_view).setVisibility(View.VISIBLE);

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.bottm_navigation_view).setVisibility(View.GONE);
    }
}