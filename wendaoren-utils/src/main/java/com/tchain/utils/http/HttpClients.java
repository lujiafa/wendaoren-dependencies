package com.tchain.utils.http;

import com.tchain.utils.common.IntrospectorUtils;
import com.tchain.utils.common.JsonUtils;
import com.tchain.utils.common.MapUtils;
import com.tchain.utils.constant.SeparatorChar;
import com.tchain.utils.prop.HttpClientProperties;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import javax.net.ssl.SSLContext;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public final class HttpClients {

    static CloseableHttpClient httpClient = org.apache.hc.client5.http.impl.classic.HttpClients.createDefault();

    public HttpClients(HttpClientProperties httpClientProperties) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        // 创建连接池
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        // 设置最大连接数
        cm.setMaxTotal(httpClientProperties.getPool().getMaxTotal());
        // 设置每个路由的默认最大连接
        cm.setDefaultMaxPerRoute(httpClientProperties.getPool().getMaxPerRoute());

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(httpClientProperties.getRequest().getConnectTimeout()))
                .setResponseTimeout(Timeout.ofSeconds(httpClientProperties.getRequest().getResponseTimeout()))
                .build();
        SSLContext sslContext = SSLContextBuilder
                .create()
                .loadTrustMaterial(TrustAllStrategy.INSTANCE)
                .build();
        httpClient = HttpClientBuilder.create()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(requestConfig)
                .setProxy(StringUtils.hasLength(httpClientProperties.getProxy().getHostname()) && httpClientProperties.getProxy().getPort() > 0 ? new HttpHost(httpClientProperties.getProxy().getHostname(), httpClientProperties.getProxy().getPort()) : null)
                .build();

    }

    public static void main(String[] args) throws Exception {
        HttpResponseData response = get("https://www.baidu.com");
        System.out.println("----->" + response.getContent());
    }

    /**
     * POST请求
     * @param url 请求URL
     * @return 响应数据对象
     */
    public static HttpResponseData get(String url) {
        return get(url, null, (Map) null);
    }

    /**
     * POST请求
     * @param url 请求URL
     * @param clazz 响应数据待转换类型/返回类型
     * @return 响应数据转换对象
     */
    public static <T> T get(String url, Class<T> clazz) {
        return get(url, null, null, clazz);
    }

    /**
     * POST请求
     * @param url 请求URL
     * @param headerMap 请求头集合
     * @return 响应数据对象
     */
    public static HttpResponseData get(String url, Map<String, Object> headerMap) {
        return get(url, null, headerMap);
    }
    /**
     * POST请求
     * @param url 请求URL
     * @param headerMap 请求头集合
     * @param clazz 响应数据待转换类型/返回类型
     * @return 响应数据转换对象
     */
    public static <T> T get(String url, Map<String, Object> headerMap, Class<T> clazz) {
        return get(url, null, headerMap, clazz);
    }

    /**
     * POST请求
     * @param url 请求URL
     * @param paramMap 参数
     * @param headerMap 请求头集合
     * @param clazz 响应数据待转换类型/返回类型
     * @return 响应数据转换对象
     */
    public static <T> T get(String url, Map<String, String> paramMap, Map<String, Object> headerMap, Class<T> clazz) {
        HttpResponseData httpResponseData = get(url, paramMap, headerMap);
        return convertHttpResponseData(httpResponseData, clazz);
    }

    /**
     * POST请求
     * @param url 请求URL
     * @param paramMap 参数
     * @param headerMap 请求头集合
     * @return 响应数据对象
     */
    public static HttpResponseData get(String url, Map<String, String> paramMap, Map<String, Object> headerMap) {
        HttpGet httpGet = new HttpGet(url);
        ContentType contentType = ContentType.APPLICATION_FORM_URLENCODED;
        if (headerMap != null && headerMap.size() > 0) {
            headerMap.entrySet().forEach(p -> httpGet.setHeader(p.getKey(), p.getValue()));
            String contentTypeStr = (String) MapUtils.getIgnoreCase(headerMap, HttpHeaders.CONTENT_TYPE);
            if (contentTypeStr != null) {
                String[] customContentTypeSubs = contentTypeStr.split(SeparatorChar.SEMICOLON);
                if (customContentTypeSubs.length > 1) {
                    contentType = ContentType.create(customContentTypeSubs[0], customContentTypeSubs[1]);
                } else {
                    contentType = ContentType.create(customContentTypeSubs[0], StandardCharsets.UTF_8);
                }
                if (!ContentType.APPLICATION_FORM_URLENCODED.isSameMimeType(contentType)) {
                    throw new IllegalArgumentException("headerMap key \"Content-Type\" not support");
                }
            } else {
                httpGet.setHeader(org.apache.hc.core5.http.HttpHeaders.CONTENT_TYPE, contentType);
            }
        }
        List<NameValuePair> paramList = new ArrayList<>(paramMap.size());
        paramMap.entrySet().stream()
                .filter(p -> p.getValue() != null)
                .forEach(p -> paramList.add(new BasicNameValuePair(p.getKey(), String.valueOf(p.getValue()))));
        httpGet.setEntity(new UrlEncodedFormEntity((Iterable<? extends NameValuePair>) paramList.iterator(), contentType.getCharset()));
        return execute(httpGet);
    }


    /**
     * POST请求
     * @param url 请求URL
     * @return 响应数据对象
     */
    public static HttpResponseData post(String url) {
        return post(url, null, (Map) null);
    }
    /**
     * POST请求
     * @param url 请求URL
     * @param clazz 响应数据待转换类型/返回类型
     * @return 响应数据转换对象
     */
    public static <T> T post(String url, Class<T> clazz) {
        return post(url, null, null, clazz);
    }

    /**
     * POST请求
     * @param url 请求URL
     * @param headerMap 请求头集合。<br>
     *                  注：Content-Type仅支持"application/x-www-form-urlencoded"和"application/json"，且缺省默认为"application/json"
     * @return 响应数据对象
     */
    public static HttpResponseData post(String url, Map<String, Object> headerMap) {
        return post(url, null, headerMap);
    }

    /**
     * POST请求
     * @param url 请求URL
     * @param headerMap 请求头集合。<br>
     *                  注：Content-Type仅支持"application/x-www-form-urlencoded"和"application/json"，且缺省默认为"application/json"
     * @param clazz 响应数据待转换类型/返回类型
     * @return 响应数据转换对象
     */
    public static <T> T post(String url, Map<String, Object> headerMap, Class<T> clazz) {
        return post(url, null, headerMap, clazz);
    }

    /**
     * POST请求
     * @param url 请求URL
     * @param param 请求参数
     * @param headerMap 请求头集合。<br>
     *                  注：Content-Type仅支持"application/x-www-form-urlencoded"和"application/json"，且缺省默认为"application/json"
     * @param clazz 响应数据待转换类型/返回类型
     * @return 响应数据转换对象
     */
    public static <T> T post(String url, Object param, Map<String, Object> headerMap, Class<T> clazz) {
        HttpResponseData httpResponseData = post(url, param, headerMap);
        return convertHttpResponseData(httpResponseData, clazz);
    }

    /**
     * POST请求
     * @param url 请求URL
     * @param param 参数
     * @param headerMap 请求头集合。<br>
     *                  注：Content-Type仅支持"application/x-www-form-urlencoded"和"application/json"，且缺省默认为"application/json"
     * @return 响应数据对象
     */
    public static HttpResponseData post(String url, Object param, Map<String, Object> headerMap) {
        HttpPost httpPost = new HttpPost(url);

        ContentType contentType = ContentType.APPLICATION_JSON;
        if (headerMap != null && headerMap.size() > 0) {
            headerMap.entrySet().forEach(p -> httpPost.setHeader(p.getKey(), p.getValue()));
            String contentTypeStr = (String) MapUtils.getIgnoreCase(headerMap, HttpHeaders.CONTENT_TYPE);
            if (contentTypeStr != null) {
                String[] customContentTypeSubs = contentTypeStr.split(SeparatorChar.SEMICOLON);
                if (customContentTypeSubs.length > 1) {
                    contentType = ContentType.create(customContentTypeSubs[0], customContentTypeSubs[1]);
                } else {
                    contentType = ContentType.create(customContentTypeSubs[0], StandardCharsets.UTF_8);
                }
                if (!ContentType.APPLICATION_JSON.isSameMimeType(contentType)
                        && !ContentType.APPLICATION_FORM_URLENCODED.isSameMimeType(contentType)) {
                    throw new IllegalArgumentException("headerMap key \"Content-Type\" not support");
                }
            }
        }
        HttpEntity httpEntity = null;
        if (param instanceof CharSequence) {
            httpEntity = new StringEntity(param.toString(), contentType);
        } else {
            if (ContentType.APPLICATION_FORM_URLENCODED.isSameMimeType(contentType)) {
                Map<String, ?> paramMap = null;
                if (param instanceof Map) {
                    paramMap = (Map<String, ?>) param;
                } else {
                    paramMap = IntrospectorUtils.toMap(param);
                }
                List<NameValuePair> paramList = new ArrayList<>(paramMap.size());
                paramMap.entrySet().stream()
                        .filter(p -> p.getValue() != null)
                        .forEach(p -> paramList.add(new BasicNameValuePair(p.getKey(), String.valueOf(p.getValue()))));
                httpEntity = new UrlEncodedFormEntity((Iterable<? extends NameValuePair>) paramList.iterator(), contentType.getCharset());
            } else {
                httpEntity = new StringEntity(JsonUtils.toString(param), contentType);
            }
        }
        httpPost.setEntity(httpEntity);
        return execute(httpPost);
    }

    private static <T> T convertHttpResponseData(HttpResponseData httpResponseData, Class<T> clazz) {
        if (httpResponseData.getStatusCode() == HttpStatus.SC_OK) {
            if (CharSequence.class.isAssignableFrom(clazz)) {
                return (T) httpResponseData.getContent();
            }
            return JsonUtils.parseObject(httpResponseData.getContent(), clazz);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpResponseData.getHeaderMap().entrySet().forEach(p -> httpHeaders.put(p.getKey(), Arrays.asList(p.getValue())));
        throw HttpClientErrorException.create(org.springframework.http.HttpStatus.valueOf(httpResponseData.getStatusCode()),
                httpResponseData.getStatusText(),
                httpHeaders,
                httpResponseData.getContent() == null ? null : httpResponseData.getContent().getBytes(httpResponseData.getCharset()),
                httpResponseData.getCharset());
    }

    public static HttpResponseData execute(ClassicHttpRequest httpRequest) {
        try (CloseableHttpResponse response = httpClient.execute(httpRequest)) {
            return new HttpResponseData(response.getCode(),
                    response.getReasonPhrase(),
                    EntityUtils.toString(response.getEntity()),
                    response.getHeaders(),
                    response.getEntity().getContentEncoding());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class HttpResponseData {
        // 响应头信息
        private Map<String, String> headerMap;
        // 响应状态码
        private int statusCode;
        // 响应状态描述
        private String statusText;
        // 响应内容
        private String content;
        // 响应内容编码
        private Charset charset;

        private HttpResponseData(int statusCode, String statusText, String content, Header[] headers, String charset) {
            headerMap = new HashMap<>(headers.length);
            Arrays.stream(headers).forEach(p -> headerMap.put(p.getName(), p.getValue()));
            this.statusCode = statusCode;
            this.statusText = statusText;
            this.content = content;
            this.charset = charset == null ? StandardCharsets.UTF_8 : Charset.forName(charset);
        }

        public Map<String, String> getHeaderMap() {
            return headerMap;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getStatusText() {
            return statusText;
        }

        public String getContent() {
            return content;
        }

        public Charset getCharset() {
            return charset;
        }
    }

//    public static void getOkHttpClient throws IOException, NoSuchAlgorithmException, KeyManagementException {
//        X509TrustManager trustManager = new X509TrustManager() {
//            @Override
//            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}
//            @Override
//            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}
//            @Override
//            public X509Certificate[] getAcceptedIssuers() {
//                return new X509Certificate[0];
//            }
//        };
//
//        // 创建 SSLContext，并将自定义的 X509TrustManager 添加进去
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//        sslContext.init(null, new TrustManager[]{trustManager}, null);
//
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectionPool(new ConnectionPool(5,30, TimeUnit.SECONDS));
//        builder.proxy(Proxy.NO_PROXY);
//        builder.sslSocketFactory(sslContext.getSocketFactory(), trustManager);
//        OkHttpClient client = builder.build();
//
//        Request request = new Request.Builder().get().url("https://www.baidu.com").build();
//        Response response = client.newCall(request).execute();
//        System.out.println(response.code() + "  " + response.body().byteString());
//}
}