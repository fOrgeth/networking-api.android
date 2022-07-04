package com.roxiemobile.networkingapi.network.rest.routing;

import android.text.TextUtils;

import com.roxiemobile.androidcommons.data.Constants.Charsets;
import com.roxiemobile.androidcommons.diagnostics.Guard;
import com.roxiemobile.androidcommons.logging.Logger;
import com.roxiemobile.androidcommons.util.CollectionUtils;
import com.roxiemobile.networkingapi.network.http.util.LinkedMultiValueMap;
import com.roxiemobile.networkingapi.network.http.util.MultiValueMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class HttpRoute {

// MARK: - Construction

    private HttpRoute(@NotNull URI uri) {
        Guard.notNull(uri, "uri is null");

        // Init instance variables
        mUri = uri;
    }

// MARK: - Methods

    public static @NotNull HttpRoute buildRoute(@Nullable URI baseUri) {
        return buildRoute(baseUri, null);
    }

    public static @NotNull HttpRoute buildRoute(@Nullable URI baseUri, @Nullable String path) {
        return buildRoute(baseUri, path, null);
    }

    public static @NotNull HttpRoute buildRoute(@Nullable URI baseUri, @Nullable String path, @Nullable MultiValueMap<String, String> params) {
        String uriString = null;

        // Build new URI
        if (baseUri != null) {
            uriString = baseUri.toString();

            // Append path to URI
            if (path != null) {
                uriString += path.trim();
            }

            // Append query params to URI
            if (params != null && params.size() > 0) {
                uriString += "?" + buildQueryString(params, Charsets.UTF_8);
            }
        }

        // Build new HTTP route
        HttpRoute route = null;
        try {
            if (uriString != null) {
                route = new HttpRoute(new URI(uriString).normalize());
            }
        }
        catch (URISyntaxException e) {
            Logger.e(TAG, e);
        }

        // Validate result
        if (route == null) {
            throw new IllegalStateException("Could not create HTTP route for path ‘" + path + "’.");
        }

        // Done
        return route;
    }

    public @NotNull URI toURI() {
        return mUri;
    }

    public @NotNull String toString() {
        return mUri.toString();
    }

// MARK: - Private Methods

    @SuppressWarnings("SameParameterValue")
    private static @NotNull String buildQueryString(@NotNull MultiValueMap<String, String> params, @NotNull Charset charset) {
        List<String> components = new LinkedList<>();

        try {
            // Build query string components
            for (String key : params.keySet()) {
                components.addAll(buildQueryStringComponents(key, params.get(key), charset));
            }
        }
        catch (UnsupportedEncodingException e) {
            Logger.e(TAG, e);

            // Re-throw internal error
            throw new IllegalStateException("Could not build query string.", e);
        }

        // Done
        return TextUtils.join("&", components);
    }

    private static @NotNull List<String> buildQueryStringComponents(@NotNull String key, @NotNull List<String> values, @NotNull Charset charset)
            throws UnsupportedEncodingException {

        if (key == null || CollectionUtils.isEmpty(values) || charset == null) {
            throw new IllegalArgumentException();
        }

        List<String> components = new LinkedList<>();
        String charsetName = charset.name();
        String encodedValue = null;

        if (values.size() > 1) {
            for (String value : values) {
                encodedValue = URLEncoder.encode(key, charsetName) + "[]=" + URLEncoder.encode(value, charsetName);
                components.add(encodedValue);
            }
        }
        else {
            encodedValue = URLEncoder.encode(key, charsetName) + "=" + URLEncoder.encode(values.get(0), charsetName);
            components.add(encodedValue);
        }

        // Done
        return components;
    }

// MARK: - Inner Types

    public static final class QueryParams extends LinkedMultiValueMap<String, String> {

        public QueryParams() {
            super();
        }

        public QueryParams(int initialCapacity) {
            super(initialCapacity);
        }

        public QueryParams(@Nullable MultiValueMap<String, String> otherMap) {
            super(otherMap == null ? Collections.emptyMap() : otherMap);
        }
    }

// MARK: - Constants

    private static final @NotNull String TAG = HttpRoute.class.getSimpleName();

// MARK: - Variables

    private final @NotNull URI mUri;
}
