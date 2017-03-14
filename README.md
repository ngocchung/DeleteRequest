## Synopsis

An Android Volley sample project that implements DELETE request with body parameters.

## Code Example
```
        HttpStack httpStack;
        if (Build.VERSION.SDK_INT > 19){
            httpStack = new CustomHurlStack();
        } else if (Build.VERSION.SDK_INT >= 9 && Build.VERSION.SDK_INT <= 19)
        {
            httpStack = new OkHttpHurlStack();
        } else {
            httpStack = new HttpClientStack(AndroidHttpClient.newInstance("Android"));
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this, httpStack);
```
## Motivation

Please look at my answer at the following question http://stackoverflow.com/questions/33553559/delete-request-with-header-and-parametes-volley
