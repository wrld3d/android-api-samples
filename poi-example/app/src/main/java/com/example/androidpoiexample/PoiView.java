// Copyright eeGeo Ltd (2012-2015), All Rights Reserved

package com.example.androidpoiexample;

import java.io.InputStream;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.os.Handler;

public class PoiView implements View.OnClickListener
{
    protected MainActivity m_activity = null;
    private View m_view = null;
    private RelativeLayout m_uiRoot = null;

    private View m_searchResultPoiViewContainer = null;
    private View m_closeButton = null;
    private TextView m_titleView = null;
    private TextView m_subtitleView = null;
    private TextView m_addressView = null;
    private View m_detailsHeader = null;
    private TextView m_phoneView = null;
    private TextView m_webLinkView = null;
    private TextView m_humanReadableTagsView = null;
    private View m_humanReadableTagsHeader = null;
    private ImageView m_tagIcon = null;
    private TextView m_descriptionView = null;
    private View m_poiImageHeader = null;
    private ImageView m_poiImage = null;
    private View m_poiImageProgressBar = null;
    private View m_poiImageGradient = null;
    private ImageView m_facebookUrl = null;
    private ImageView m_twitterUrl = null;
    private ImageView m_email = null;
    private ImageView m_addressIcon = null;
    private ImageView m_phoneIcon = null;
    private ImageView m_webIcon = null;
    private ImageView m_tagsIcon = null;
    private ImageView m_descriptionIcon = null;
    private View m_poiImageViewContainer = null;
    private ScrollView m_contentContainer = null;
    private ImageView m_footerFade = null;
    private LinearLayout m_linearContentContainer = null;
    private WebView m_webView = null;
    private View m_webViewContainer = null;

    private boolean m_handlingClick = false;

    @SuppressLint("NewApi")
    public PoiView(MainActivity activity)
    {
        m_activity = activity;

        m_uiRoot = (RelativeLayout)m_activity.findViewById(R.id.ui_container);
        m_view = m_activity.getLayoutInflater().inflate(R.layout.search_result_poi_eegeo_layout, m_uiRoot, false);

        m_searchResultPoiViewContainer = m_view.findViewById(R.id.search_result_poi_view_container);
        m_closeButton = m_view.findViewById(R.id.search_result_poi_view_close_button);
        m_titleView = (TextView)m_view.findViewById(R.id.search_result_poi_view_title);
        m_subtitleView = (TextView)m_view.findViewById(R.id.search_result_poi_view_subtitle);
        m_addressView = (TextView)m_view.findViewById(R.id.search_result_poi_view_address);
        m_detailsHeader = (View)m_view.findViewById(R.id.search_result_poi_view_details_header);
        m_phoneView = (TextView)m_view.findViewById(R.id.search_result_poi_view_phone);
        m_webLinkView = (TextView)m_view.findViewById(R.id.search_result_poi_view_web_link);
        m_descriptionView = (TextView)m_view.findViewById(R.id.search_result_poi_view_descritption);
        m_poiImageHeader = (View)m_view.findViewById(R.id.search_result_poi_image_header);
        m_humanReadableTagsView = (TextView)m_view.findViewById(R.id.search_result_poi_view_tags);
        m_humanReadableTagsHeader = (View)m_view.findViewById(R.id.search_result_poi_view_tags_header);
        m_tagIcon = (ImageView)m_view.findViewById(R.id.search_result_poi_view_tag_icon);
        m_poiImage = (ImageView)m_view.findViewById(R.id.search_result_poi_view_image);
        m_poiImageGradient = m_view.findViewById(R.id.search_result_poi_view_image_gradient);
        m_poiImageProgressBar = m_view.findViewById(R.id.search_result_poi_view_image_progress);
        m_facebookUrl = (ImageView)m_view.findViewById(R.id.search_result_poi_view_facebook);
        m_twitterUrl = (ImageView)m_view.findViewById(R.id.search_result_poi_view_twitter);
        m_email = (ImageView)m_view.findViewById(R.id.search_result_poi_view_email);
        m_addressIcon = (ImageView)m_view.findViewById(R.id.search_result_poi_view_address_icon);
        m_phoneIcon = (ImageView)m_view.findViewById(R.id.search_result_poi_view_phone_icon);
        m_webIcon = (ImageView)m_view.findViewById(R.id.search_result_poi_view_web_link_icon);
        m_tagsIcon = (ImageView)m_view.findViewById(R.id.search_result_poi_view_tags_icon);
        m_descriptionIcon = (ImageView)m_view.findViewById(R.id.search_result_poi_view_description_icon);
        m_poiImageViewContainer = (View)m_view.findViewById(R.id.search_result_poi_view_image_container);
        m_contentContainer = (ScrollView)m_view.findViewById(R.id.content_container);
        m_footerFade = (ImageView)m_view.findViewById(R.id.footer_fade);
        m_linearContentContainer = (LinearLayout)m_view.findViewById(R.id.linear_content_container);
        m_webView = (WebView)m_view.findViewById(R.id.webview);
        m_webViewContainer = (View)m_view.findViewById(R.id.search_result_poi_view_webview_container);

        m_activity.recursiveDisableSplitMotionEvents((ViewGroup)m_view);

        m_view.setVisibility(View.GONE);
        m_uiRoot.addView(m_view);
        m_webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                view.loadUrl("file:///android_asset/page_not_found.html");
            }
        });

        m_closeButton.setOnClickListener(this);
        m_facebookUrl.setOnClickListener(this);
        m_twitterUrl.setOnClickListener(this);
        m_email.setOnClickListener(this);
    }

    public void displayPoiInfo(
            final String title,
            final String subtitle,
            final String address,
            final String description,
            final String phone,
            final String url,
            final String iconKey,
            final String[] humanReadableTags,
            final String imageUrl,
            final String facebook,
            final String twitter,
            final String email,
            final String customViewUrl,
            final int customViewHeight)
    {
        int containerHeight = m_searchResultPoiViewContainer.getHeight();
        int maxWidth = (int) (containerHeight * 0.5f);
        if(m_searchResultPoiViewContainer.getWidth() > maxWidth)
        {
            m_searchResultPoiViewContainer.getLayoutParams().width = maxWidth;
        }

        showTitleDetails(title, subtitle);

        showPOIImage(imageUrl);

        showPOIDetailsSection(address, phone, url, facebook, twitter, email);

        if(humanReadableTags.length > 0)
        {
            m_humanReadableTagsHeader.setVisibility(View.GONE);
            m_humanReadableTagsView.setVisibility(View.VISIBLE);
            m_tagsIcon.setVisibility(View.VISIBLE);

            String output;
            output = TextUtils.join(", ", humanReadableTags);
            m_humanReadableTagsView.setText(output);
        }
        else
        {
            m_humanReadableTagsHeader.setVisibility(View.GONE);
            m_humanReadableTagsView.setVisibility(View.GONE);
            m_tagsIcon.setVisibility(View.GONE);
        }

        if(!description.equals(""))
        {
            m_descriptionView.setVisibility(View.VISIBLE);
            m_descriptionIcon.setVisibility(View.VISIBLE);
            m_descriptionView.setText(description);
        }
        else
        {
            m_descriptionView.setVisibility(View.GONE);
            m_descriptionIcon.setVisibility(View.GONE);
        }

        int iconId = TagResources.getSmallIconForTag(m_activity, iconKey);
        m_tagIcon.setImageResource(iconId);

        m_closeButton.setEnabled(true);

        m_view.setVisibility(View.VISIBLE);
        m_view.requestFocus();

        m_handlingClick = false;

        showCustomView(customViewUrl, customViewHeight);
    }

    private void showTitleDetails(String title, String subtitle)
    {
        m_titleView.setText(title);
        m_subtitleView.setText(subtitle);

        if(subtitle.equals(""))
        {
            m_subtitleView.setVisibility(View.GONE);
        }
        else
        {
            m_subtitleView.setVisibility(View.VISIBLE);
        }

    }

    private void showPOIImage(String imageUrl)
    {
        m_poiImageProgressBar.setVisibility(View.GONE);
        m_poiImageGradient.setVisibility(View.GONE);

        if(!imageUrl.equals(""))
        {
            m_poiImageProgressBar.setVisibility(View.VISIBLE);
            updateImageData(imageUrl.toString());
            setPoiImageVisibility(View.VISIBLE);
        }
        else
        {
            setPoiImageVisibility(View.GONE);
        }
    }

    private void showPOIDetailsSection(final String address,
                                       final String phone,
                                       final String url,
                                       final String facebook,
                                       final String twitter,
                                       final String email)
    {
        if(!address.equals("") || !phone.equals("") || !facebook.equals("") || !twitter.equals("") || !email.equals(""))
        {
            m_detailsHeader.setVisibility(View.VISIBLE);
        }
        else
        {
            m_detailsHeader.setVisibility(View.GONE);
        }

        if(!address.equals(""))
        {
            m_addressView.setVisibility(View.VISIBLE);
            m_addressIcon.setVisibility(View.VISIBLE);
            String addressText = address.replace(", ", "\n");
            m_addressView.setText(addressText);
        }
        else
        {
            m_addressView.setVisibility(View.GONE);
            m_addressIcon.setVisibility(View.GONE);
        }

        if(!phone.equals(""))
        {
            m_phoneView.setVisibility(View.VISIBLE);
            m_phoneIcon.setVisibility(View.VISIBLE);
            m_phoneView.setText(phone.replace(" ", ""));

            // Autolink discards country code so add custom phone link
            final String phoneRegex = "[\\S]*";
            Linkify.addLinks(m_phoneView, Pattern.compile(phoneRegex), "Tel:");
        }
        else
        {
            m_phoneView.setVisibility(View.GONE);
            m_phoneIcon.setVisibility(View.GONE);
        }


        if(!url.equals(""))
        {
            m_webLinkView.setVisibility(View.VISIBLE);
            m_webIcon.setVisibility(View.VISIBLE);
            m_webLinkView.setText(url.replace("", ""));

            final String webRegex = "[\\S]*";
            Linkify.addLinks(m_webLinkView, Pattern.compile(webRegex), url);
        }
        else
        {
            m_webLinkView.setVisibility(View.GONE);
            m_webIcon.setVisibility(View.GONE);
        }

        showPOIImageDetail(facebook, m_facebookUrl);

        showPOIImageDetail(twitter, m_twitterUrl);

        showPOIImageDetail(email, m_email);
    }

    public void showPOIImageDetail(String detail, ImageView view)
    {
        if(!detail.equals(""))
        {
            view.setVisibility(View.VISIBLE);
            view.setTag(detail);
        }
        else
        {
            view.setVisibility(View.GONE);
        }
    }

    public void showCustomView(String url, int customViewHeight)
    {
        if(!url.equals(""))
        {
            m_webView.getSettings().setUseWideViewPort(true);
            m_webView.getSettings().setLoadWithOverviewMode(true);
            m_webView.loadUrl(url);

            if(customViewHeight != -1) {
                final int viewHeight = customViewHeight;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) m_webView.getLayoutParams();
                params.height = m_activity.dipAsPx(viewHeight);
                m_webView.setLayoutParams(params);
            }

            m_webView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            m_webView.zoomOut();
                        }
                    }, 300);

                }
            });

            m_poiImageViewContainer.setVisibility(View.GONE);
            m_webViewContainer.setVisibility(View.VISIBLE);
            m_poiImageHeader.setVisibility(View.VISIBLE);
        }
    }



    public void handleButtonLink(View view)
    {
        String url = (String)view.getTag();
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        m_activity.startActivity(intent);
        m_handlingClick = false;
    }

    public void openEmailLink(View view)
    {
        String url = (String)view.getTag();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { url });
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        m_activity.startActivity(Intent.createChooser(intent, ""));
        m_handlingClick = false;
    }

    public void onClick(View view)
    {
        if(m_handlingClick)
        {
            return;
        }
        m_handlingClick = true;

        if(view == m_closeButton)
        {
            handleCloseClicked();
        }
        else if(view == m_facebookUrl || view == m_twitterUrl)
        {
            handleButtonLink(view);
        }
        else if(view == m_email)
        {
            openEmailLink(view);
        }
    }

    public void dismissPoiInfo()
    {
        m_webViewContainer.setVisibility(View.GONE);
        m_view.setVisibility(View.GONE);
    }

    public void HandleFooterFadeInitialVisibility()
    {
        int childHeight = m_linearContentContainer.getHeight();
        boolean isScrollable = m_contentContainer.getHeight() < (childHeight + m_contentContainer.getPaddingTop() + m_contentContainer.getPaddingBottom());
        if(!isScrollable)
        {
            m_footerFade.setVisibility(View.GONE);
        }
        else
        {
            m_footerFade.setVisibility(View.VISIBLE);
        }
    }

    public void updateImageData(String urlString){
            new DownloadImageTask(m_poiImage)
                    .execute(urlString);
        HandleFooterFadeInitialVisibility();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            m_poiImage.setVisibility(View.VISIBLE);
            bmImage.setImageBitmap(result);
        }
    }
    private void setPoiImageVisibility(int view)
    {
        m_poiImageHeader.setVisibility(view);
        m_poiImageViewContainer.setVisibility(view);
    }

    private void handleCloseClicked()
    {
        m_view.setEnabled(false);
        dismissPoiInfo();
        m_view.setVisibility(View.GONE);
        m_poiImage.setVisibility(View.INVISIBLE);
    }
}
