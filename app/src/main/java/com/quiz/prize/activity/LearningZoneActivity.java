package com.quiz.prize.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.quiz.prize.Constant;
import com.quiz.prize.R;
import com.quiz.prize.helper.Utils;

public class LearningZoneActivity extends YouTubeBaseActivity {

    public ProgressBar prgLoading;
    public WebView mWebView,webView;
    public String id,video_id;
    public Toolbar toolbar;
    TextView tvStartGame;
    FloatingActionButton btnPDFViewer;

    ImageView imgBack;
    TextView txtTitle;

    Activity activity;

    private String pdfUrl = "";

    String youTubeUrl = "https://www.youtube.com/embed/47yJ2XCRLZs";



    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_learningzone);

        activity= new LearningZoneActivity();
        id = getIntent().getStringExtra("id");
        video_id=getIntent().getStringExtra("videoid");

        pdfUrl=getIntent().getStringExtra("pdfile");
        prgLoading = findViewById(R.id.prgLoading);
        tvStartGame = findViewById(R.id.tvStartGame);
        tvStartGame.setVisibility(View.VISIBLE);
        mWebView = findViewById(R.id.webView1);
        webView =findViewById(R.id.webView);

        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> onBackPressed());

     //   ytPlayer = (YouTubePlayerView) findViewById(R.id.ytPlayer);
        txtTitle=findViewById(R.id.txtTitle);
        txtTitle.setText(Constant.cate_name);
        btnPDFViewer=findViewById(R.id.btnPDFViewer);

        String regexYoutUbe = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
        if (youTubeUrl.matches(regexYoutUbe)) {

            //setting web client
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });
            //web settings for JavaScript Mode
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webView.loadData("<html><body><iframe id=\"player\" type=\"text/html\" width=\"380\" height=\"360\"\n" +
                    "  src=https://www.youtube.com/embed/"+video_id+"?controls=0&showinfo=0"+"  frameborder=\"0\"></iframe></body></html>", "text/html", "utf-8");


        } else {
            Toast.makeText(LearningZoneActivity.this, "This is other video",
                    Toast.LENGTH_SHORT).show();
        }

        System.out.println("valueofPDF"+pdfUrl);
        if(pdfUrl.equals(Constant.DOMAIN_URL+"pdf_files/")){
            btnPDFViewer.setVisibility(View.GONE);
        }else {
            btnPDFViewer.setVisibility(View.VISIBLE);
        }
        btnPDFViewer.setOnClickListener(view -> startActivity(PdfRenderActivity.newIntent(LearningZoneActivity.this, pdfUrl)));

        if(video_id.equals("")){
            webView.setVisibility(View.GONE);
        }else {
            webView.setVisibility(View.VISIBLE);
        }
        try {

            mWebView.setClickable(true);
            mWebView.setFocusableInTouchMode(true);
            mWebView.getSettings().setJavaScriptEnabled(true);
            // getSupportActionBar().setTitle(Constant.cate_name);
            /*GetPrivacyAndTerms();*/
            if (Utils.isNetworkAvailable(this)) {
                if (!prgLoading.isShown()) {
                    prgLoading.setVisibility(View.VISIBLE);
                }
                mWebView.setVerticalScrollBarEnabled(true);
                mWebView.loadDataWithBaseURL("", getIntent().getStringExtra("message"), "text/html", "UTF-8", "");
                mWebView.setBackgroundColor(getResources().getColor(R.color.bg_color));
            } else {
                //setSnackBar();
            }
            prgLoading.setVisibility(View.GONE);
            tvStartGame.setOnClickListener(v -> {

                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                intent.putExtra("fromQue", "learning");
                intent.putExtra("learning_id", id);
                startActivity(intent);

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();

    }
}