package com.mislead.ikanxue.app.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mislead.ikanxue.app.model.ImageDiskCache;

/**
 * VolleyHelper
 *
 * @author Zhaoyy
 *         2015/6/29
 *         DESC:
 **/
public class VolleyHelper {

  private static String TAG = "VolleyHelper";

  private static RequestQueue queue = null;

  public static void init(Context context) {
    queue = Volley.newRequestQueue(context);
  }

  /**
   * requestStringGet
   *
   * @param url request url
   * @param listener handle request listener
   */
  public static void requestStringGet(String url, RespinseListener<String> listener) {
    StringRequest request = new StringRequest(Request.Method.GET, url, listener, listener);
    request.setShouldCache(true);
    request.setTag(url);
    queue.add(request);
  }

  /**
   * requestImageWithoutCache
   * if success, you can get the bitmap you requested.
   * if you want cache the bitmap, use {@link #requestImageWithCache(String, ImageView,
   * ImageLoader.ImageCache, int, int, int, int)} or {@link #requestImageWithCacheSimple(String,
   * ImageLoader.ImageCache, ImageLoader.ImageListener)}
   *
   * @param url request url
   * @param maxWidth bitmap maximum width
   * @param maxHeight bitmap maximum height
   * @param config bitmap code type ARGB_8888,RGB_565,ALPHA_8
   * @param listener handler request listener
   */
  public static void requestImageWithoutCache(String url, int maxWidth, int maxHeight,
      Bitmap.Config config, RespinseListener<Bitmap> listener) {
    ImageRequest request = new ImageRequest(url, listener, maxWidth, maxHeight, config, listener);
    request.setShouldCache(true);
    request.setTag(url);
    queue.add(request);
  }

  public static void requestImageViewWithoutCache(String url, RespinseListener<Bitmap> listener) {
    requestImageViewWithoutCache(url, Bitmap.Config.RGB_565, listener);
  }

  public static void requestImageViewWithoutCache(String url, Bitmap.Config config,
      RespinseListener<Bitmap> listener) {
    requestImageWithoutCache(url, 0, 0, config, listener);
  }

  /**
   * requestImageWithCache
   * you can set default image & error image on the view.
   *
   * if not needed set default image ,you can use {@link #requestImageWithCacheSimple(String,
   * ImageLoader.ImageCache, ImageLoader.ImageListener)}
   *
   * @param url request url
   * @param imageView imageView to diaplay image
   * @param cache image cacher
   * @param defaultImageResId default image
   * @param errorImageResId error image
   * @param maxWidth maximum image width
   * @param maxHeight maximum image height
   */
  public static void requestImageWithCache(String url, ImageView imageView,
      ImageLoader.ImageCache cache, int defaultImageResId, int errorImageResId, int maxWidth,
      int maxHeight) {
    ImageLoader imageLoader = new ImageLoader(queue, cache);
    imageLoader.get(url,
        ImageLoader.getImageListener(imageView, defaultImageResId, errorImageResId), maxWidth,
        maxHeight);
  }

  public static void requestImageWithCache(String url, ImageView imageView,
      ImageLoader.ImageCache cache, int defaultImageResId, int errorImageResId) {
    requestImageWithCache(url, imageView, cache, defaultImageResId, errorImageResId, 0, 0);
  }

  /**
   * requestImageWithCacheSimple
   * request a image, cache it,and deal it in listener.
   * if you don't need deal, use {@link #requestAndCacheImage(String, ImageLoader.ImageCache)}
   * @param url       request url
   * @param cache     image cache
   * @param listener  image request listener
   */
  public static void requestImageWithCacheSimple(String url, ImageLoader.ImageCache cache,
      ImageLoader.ImageListener listener) {
    ImageLoader imageLoader = new ImageLoader(queue, cache);
    imageLoader.get(url, listener);
  }

  /**
   * requestAndCacheImage
   * just cahce a image for the next use.
   * if you want to deal it immediately, use {@link #requestImageWithCacheSimple(String,
   * ImageLoader.ImageCache, ImageLoader.ImageListener)}
   * @param url
   * @param cache
   */
  public static void requestAndCacheImage(String url, ImageLoader.ImageCache cache) {
    requestImageWithCacheSimple(url, cache, new SimpleImageListener());
  }

  /**
   * getCacheKey
   * get cache key about image url, used for getImage from cahcer;
   * @param url       image url
   * @param maxWidth  cache maximum width
   * @param maxHeight cache maximum height
   * @return
   */
  public static String getCacheKey(String url, int maxWidth, int maxHeight) {
    return (new StringBuilder(url.length() + 12)).append("#W").append(maxWidth).append("#H")
        .append(maxHeight).append(url).toString();
  }

  public static String getCacheKey(String url) {
    return getCacheKey(url, 0, 0);
  }

  public static interface RespinseListener<T> extends Response.Listener<T>, Response.ErrorListener {

  }

  static class SimpleImageListener implements ImageLoader.ImageListener {

    @Override public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {

    }

    @Override public void onErrorResponse(VolleyError volleyError) {

    }
  }
}
