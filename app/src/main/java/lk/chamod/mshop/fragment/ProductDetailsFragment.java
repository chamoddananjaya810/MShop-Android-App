package lk.chamod.mshop.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.color.ColorContrast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.chamod.mshop.R;
import lk.chamod.mshop.activity.MainActivity;
import lk.chamod.mshop.activity.SignInActivity;
import lk.chamod.mshop.adpter.ProductSliderAdapter;
import lk.chamod.mshop.adpter.SectionAdapter;
import lk.chamod.mshop.databinding.FragmentProductDetailsBinding;
import lk.chamod.mshop.model.CartItem;
import lk.chamod.mshop.model.Product;


public class ProductDetailsFragment extends Fragment {

    private FragmentProductDetailsBinding binding;
    private String productId;

    private int quantity = 1;

    private int avbQuantity;

    private Map<String, ChipGroup> attributeGroup = new HashMap<>();


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


                                avbQuantity = product.getStockCount();

                                binding.productDetailsRating.setRating((float) product.getRating());

                                if (product.getAttribute() != null) {
                                    product.getAttribute().forEach(attribute -> {
                                        renderAttribute(attribute, binding.productDetailsAttributeContainer);
                                    });

                                }
                            }


                        }


                    }


                });

        binding.productDetailsBtnMinus.setOnClickListener(v -> {
            if (quantity > 1) {

                quantity--;
                binding.productDetailsQuntity.setText(String.valueOf(quantity));
            }
        });

        binding.productDetailsBtnPlus.setOnClickListener(v -> {


            if (quantity < avbQuantity) {

                quantity++;
                binding.productDetailsQuntity.setText(String.valueOf(quantity));
            }
        });


        loadTopASellProduct();


        binding.productDetailsBtnAddCart.setOnClickListener(v -> {


           FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();

            if (firebaseAuth.getCurrentUser()==null){

                Intent intent =  new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);


            }else{
                List<CartItem.Attribute> attributes= getFinalSelections();

           CartItem cartItem =new CartItem(productId,quantity,attributes);


                String uid=firebaseAuth.getCurrentUser().getUid();




                db.collection("users").document(uid).collection("cart")
                        .document()
                        .set(cartItem)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(),"Item added to cart!",Toast.LENGTH_SHORT).show();
                            }
                        });

            }





        });


    }

    private void loadTopASellProduct() {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .whereNotEqualTo("productId", productId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot qds) {
                        if (!qds.isEmpty()) {
                            List<Product> products = qds.toObjects(Product.class);

                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            binding.productDetailsTopSellSection.itemSectionContainer.setLayoutManager(layoutManager);

                            SectionAdapter adapter = new SectionAdapter(products, product -> {

                                Bundle bundle = new Bundle();
                                bundle.putString("productId", product.getProductId());


                                ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
                                productDetailsFragment.setArguments(bundle);

                                getParentFragmentManager().beginTransaction()
                                        // වැරදි: .replace(R.id.fragment_container, new ProductDetailsFragment())
                                        .replace(R.id.fragment_container, productDetailsFragment) // හරියටම මේ variable එක දාන්න
                                        .addToBackStack(null)
                                        .commit();
                            });


                            binding.productDetailsTopSellSection.itemSectionTitle.setText("Top Selling Product");
                            binding.productDetailsTopSellSection.itemSectionContainer.setAdapter(adapter);
                        }
                    }
                });
    }

    private void renderAttribute(Product.Attribute attribute, ViewGroup container) {
        if (getContext() == null) return;
        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(LinearLayout.VERTICAL);

        //Create Lable
        TextView lable = new TextView(getContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                100,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        lable.setLayoutParams(layoutParams);


        lable.setText(attribute.getName());


        row.addView(lable);


        ChipGroup group = new ChipGroup(getContext());
        attributeGroup.put(attribute.getName(), group);


        group.setSelectionRequired(true);
        group.setSingleSelection(true);

        if (attribute.getValues() != null) {
            attribute.getValues().forEach(value -> {
                Chip chip = new Chip(requireContext());
                chip.setCheckable(true);
                chip.setChipStrokeWidth(3f);


                if ("color".equals(attribute.getType())) {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(value)));

                } else {

                    chip.setText(value);
                }
                chip.setTag(value);
                group.addView(chip);
            });
        }

        row.addView(group);


        container.addView(row);

        attributeGroup.put(attribute.getName(), group);

    }


    private List<CartItem.Attribute> getFinalSelections() {


        List<CartItem.Attribute> attributes = new ArrayList<>();

        StringBuilder result = new StringBuilder("Selected: \n");


        for (Map.Entry<String, ChipGroup> entry : attributeGroup.entrySet()) {


            String attributeName = entry.getKey();
            ChipGroup chipGroup = entry.getValue();

            int checkedChipId = chipGroup.getCheckedChipId();

            if (checkedChipId != -1) {

                Chip chip = getView().findViewById(checkedChipId);
                String value = chip.getTag().toString();

                attributes.add(new CartItem.Attribute(attributeName, value));

            }


        }
        return attributes;


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