package com.quiz.prize.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.quiz.prize.Constant;
import com.quiz.prize.R;
import com.quiz.prize.helper.ApiConfig;
import com.quiz.prize.helper.AppController;
import com.quiz.prize.helper.Utils;
import com.quiz.prize.model.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class LearningChapterActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public ProgressBar progressBar;

    public TextView empty_msg;
    public RelativeLayout layout;
    public ArrayList<Category> categoryList;
    public SwipeRefreshLayout swipeRefreshLayout;
    public Snackbar snackbar;
    public Toolbar toolbar;
    public AlertDialog alertDialog;
    CategoryAdapter adapter;
    private ShimmerFrameLayout mShimmerViewContainer;

    String quizType;
    Activity activity;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DrawerActivity.Locallanguage(LearningChapterActivity.this);
        setContentView(R.layout.activity_category);
        activity = LearningChapterActivity.this;
        quizType = getIntent().getStringExtra(Constant.QUIZ_TYPE);
        layout = findViewById(R.id.layout);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        empty_msg = findViewById(R.id.txtblanklist);
        progressBar = findViewById(R.id.progressBar);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getData();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getData();
            swipeRefreshLayout.setRefreshing(false);
        });

        Utils.showBannerAds(LearningChapterActivity.this);
    }

    private void getData() {
        if (Utils.isNetworkAvailable(activity)) {
            getMainCategoryFromJson();
            invalidateOptionsMenu();

        } else {
            setSnackBar();
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
        }

    }


    public void setSnackBar() {
        snackbar = Snackbar
                .make(findViewById(android.R.id.content), getString(R.string.msg_no_internet), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.retry), view -> getData());

        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }


    @Override
    protected void onDestroy() {
        if (Utils.mAdView != null) {
            Utils.mAdView.destroy();
        }
        super.onDestroy();
    }

    /*
     * Get Quiz Category from Json
     */
    public void getMainCategoryFromJson() {
        // progressBar.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        params.put(Constant.Get_Learning, "1");
        params.put(Constant.category, Constant.CATE_ID);
        ApiConfig.RequestToVolley((result, response) -> {

            if (result) {
                try {
                    categoryList = new ArrayList<>();
                    System.out.println("ResponseOfChepter"+response);
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString(Constant.ERROR);

                    if (error.equalsIgnoreCase("false")) {
                        empty_msg.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Category category = new Category();
                            JSONObject object = jsonArray.getJSONObject(i);
                            category.setId(object.getString(Constant.ID));
                            category.setName(object.getString(Constant.Title));
                            category.setMessage(object.getString(Constant.Details));
                            category.setTtlQues(object.getString(Constant.NO_OF_CATE));
                            category.setVideo_id(object.getString(Constant.VIDEO_ID));
                            category.setPdfFile(object.getString(Constant.PDFFILE));


                            categoryList.add(category);
                        }

                        adapter = new CategoryAdapter(activity, categoryList);
                        recyclerView.setAdapter(adapter);
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);

                    } else {

                        empty_msg.setText(getString(R.string.no_category));
                        progressBar.setVisibility(View.GONE);
                        empty_msg.setVisibility(View.VISIBLE);
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        if (adapter != null) {
                            adapter = new CategoryAdapter(activity, categoryList);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params);


    }


    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
        Utils.CheckBgMusic(activity);
    }

    @Override
    public void onPause() {
        super.onPause();
        AppController.StopSound();
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        Context context = activity;
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);

        animation.setDuration(position * 50 + 500);
        viewToAnimate.startAnimation(animation);

    }


    public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        ArrayList<Category> dataList;
        public Context mContext;
        final int MENU_ITEM_VIEW_TYPE = 0;
        final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;

        public CategoryAdapter(Context context, ArrayList<Category> dataList) {
            this.dataList = dataList;
            this.mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {

                case MENU_ITEM_VIEW_TYPE:
                default:
                    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category, parent, false);
                    return new ItemRowHolder(v);
            }

        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, final int position) {
            DrawerActivity.Locallanguage(LearningChapterActivity.this);
            int viewType = getItemViewType(position);
            if (viewType == MENU_ITEM_VIEW_TYPE) {
                final ItemRowHolder holder = (ItemRowHolder) holder1;
                final Category category = dataList.get(position);
                holder.nameLyt.setBackgroundResource(Constant.colorBg[position % 6]);
                holder.imgCircles.setColorFilter(ContextCompat.getColor(activity, Constant.colors[position % 6]));
                holder.text.setText(category.getName());
                holder.noofque.setText(getString(R.string.que) + category.getTtlQues());
                holder.image.setDefaultImageResId(R.drawable.ic_launcher);
                holder.image.setImageUrl(category.getImage(), imageLoader);
                setAnimation(holder.text, position);
                holder.relativeLayout.setOnClickListener(v -> {
                    Constant.cate_name = category.getName();
                    Intent intent = new Intent(activity, LearningZoneActivity.class);
                    intent.putExtra("id", category.getId());
                    intent.putExtra("title", category.getName());
                    intent.putExtra("message", category.getMessage());
                    intent.putExtra("videoid",category.getVideo_id());
                    intent.putExtra("pdfile",category.getPdfFile());
                    startActivity(intent);
                });
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (categoryList.get(position).isAdsShow()) {
                return UNIFIED_NATIVE_AD_VIEW_TYPE;
            }
            return MENU_ITEM_VIEW_TYPE;
        }

        public class ItemRowHolder extends RecyclerView.ViewHolder {
            public NetworkImageView image;
            public TextView text, noofque;
            RelativeLayout relativeLayout, nameLyt;
            ImageView imgCircles;

            public ItemRowHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.cateImg);
                imgCircles = itemView.findViewById(R.id.imgCircles);
                text = itemView.findViewById(R.id.item_title);
                relativeLayout = itemView.findViewById(R.id.cat_layout);
                nameLyt = itemView.findViewById(R.id.nameLyt);
                noofque = itemView.findViewById(R.id.noofque);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cate_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (itemId == R.id.setting) {
            Utils.CheckVibrateOrSound(activity);
            Intent playQuiz = new Intent(activity, SettingActivity.class);
            startActivity(playQuiz);
            overridePendingTransition(R.anim.open_next, R.anim.close_next);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }





}