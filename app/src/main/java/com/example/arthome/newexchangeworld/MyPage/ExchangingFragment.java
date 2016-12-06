package com.example.arthome.newexchangeworld.MyPage;


import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.BR;
import com.example.arthome.newexchangeworld.ExchangeAPI.RestClient;
import com.example.arthome.newexchangeworld.Models.CreateExchangeResponse;
import com.example.arthome.newexchangeworld.Models.ExchangeModel;
import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.RealmManager;
import com.example.arthome.newexchangeworld.User;
import com.example.arthome.newexchangeworld.databinding.FragmentExchangingBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arthome on 2016/4/10.
 */
public class ExchangingFragment extends Fragment {

    private ExchangingAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog progressDialog;
    private List<ExchangeModel> exchangeModels = new ArrayList<>();
    private User user;
    private FragmentExchangingBinding binding;
    private ExchangeModel selectedExchangeModel = null;

    public ExchangingFragment() {

    }

    public static ExchangingFragment newInstance() {
        ExchangingFragment fragment = new ExchangingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = RealmManager.INSTANCE.retrieveUser().get(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exchanging, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectedExchangeModel = null;
        binding.setVariable(BR.exchangeModel, selectedExchangeModel);
        binding.executePendingBindings();

        mAdapter = new ExchangingAdapter(exchangeModels);
        mAdapter.setExchangeListener(new ExchangingAdapter.ExchangeListener() {
            @Override
            public void layoutClicked(ExchangeModel exchangeModel) {
                selectedExchangeModel = exchangeModel;
                binding.setVariable(BR.exchangeModel, selectedExchangeModel);
                binding.setVariable(BR.agreed,exchangeModel.isOwner_agree());
                binding.setVariable(BR.owner_good, exchangeModel.getOwner_goods());
                binding.setVariable(BR.other_good, exchangeModel.getOther_goods());
                binding.executePendingBindings();
            }
        });
        binding.fragmentExchangingRecyclerView.setAdapter(mAdapter);


        mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        binding.fragmentExchangingRecyclerView.setLayoutManager(mLayoutManager);

        binding.fragmentExchangingRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                binding.fragmentExchangingRecyclerView.requestDisallowInterceptTouchEvent(true);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });

        binding.fragmentExchangingAcceptImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agreeExchange();
            }
        });

        binding.fragmentExchangingCancelImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropExchange();
            }
        });

        binding.fragmentExchangingRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadHistoryExchanges();
            }
        });

        progressDialog = new ProgressDialog(getActivity());
    }

    private void agreeExchange() {
        Call<CreateExchangeResponse> call = new RestClient().getExchangeService().agreeExchange(selectedExchangeModel.getEid(), user.getUid(), user.getExToken());
        call.enqueue(new Callback<CreateExchangeResponse>() {
            @Override
            public void onResponse(Call<CreateExchangeResponse> call, Response<CreateExchangeResponse> response) {
                if(response.code()==200){
                    if(response.body().getStatus().equals("initiated")){
                        binding.setAgreed(true);        //initiated代表只有owner同意
                        binding.executePendingBindings();
                    }else {
                        selectedExchangeModel = null;
                        binding.setExchangeModel(selectedExchangeModel);
                        binding.executePendingBindings();
                    }
                    Toast.makeText(getContext(),"同意交換 等對方同意",Toast.LENGTH_SHORT).show();
                    downloadHistoryExchanges();
                }else {
                    Toast.makeText(getContext(),"交換失敗 response code錯誤",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateExchangeResponse> call, Throwable t) {
                Toast.makeText(getContext(),"交換失敗 onFailure",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dropExchange() {
        Call<CreateExchangeResponse> call = new RestClient().getExchangeService().dropExchange(selectedExchangeModel.getEid(), user.getExToken());
        call.enqueue(new Callback<CreateExchangeResponse>() {
            @Override
            public void onResponse(Call<CreateExchangeResponse> call, Response<CreateExchangeResponse> response) {
                if(response.code()==200){
                    Toast.makeText(getContext(),"取消交換",Toast.LENGTH_SHORT).show();
                    selectedExchangeModel = null;
                    binding.setExchangeModel(selectedExchangeModel);
                    binding.executePendingBindings();
                    downloadHistoryExchanges();
                }else {
                    Toast.makeText(getContext(),"取消交換失敗 response code錯誤",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateExchangeResponse> call, Throwable t) {
                Toast.makeText(getContext(),"取消交換失敗 onFailure",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void downloadHistoryExchanges(){
        progressDialog = ProgressDialog.show(getContext(), "Loading", "Please wait", true);

        Call<List<ExchangeModel>> call = new RestClient().getExchangeService().historyExchange(user.getUid(), user.getExToken());
        call.enqueue(new Callback<List<ExchangeModel>>() {
            @Override
            public void onResponse(Call<List<ExchangeModel>> call, Response<List<ExchangeModel>> response) {
                if (response.code() == 200) {
                    exchangeModels.clear();
                    for(int i=0 ;i<response.body().size();i++){
                        if(response.body().get(i).getStatus().equals("initiated")) //排除已交換或取消的
                            exchangeModels.add(response.body().get(i));
                    }
                    mAdapter.notifyDataSetChanged();
                    binding.fragmentExchangingRefreshLayout.setRefreshing(false);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<ExchangeModel>> call, Throwable t) {
                Toast.makeText(getContext(), "下載物品失敗 onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        downloadHistoryExchanges();
    }
}
