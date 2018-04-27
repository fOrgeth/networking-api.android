package com.roxiemobile.networkingapi.network.rest.request;

import com.roxiemobile.androidcommons.diagnostics.Guard;
import com.roxiemobile.networkingapi.network.http.CookieStore;
import com.roxiemobile.networkingapi.network.http.HttpHeaders;

import org.jetbrains.annotations.NotNull;

import java.net.URI;

public class BasicRequestEntity<T> implements RequestEntity<T>
{
// MARK: - Construction

    protected BasicRequestEntity(Builder<T> builder) {
        // Init instance variables
        mUri = builder.mUri;
        mHeaders = builder.mHeaders;
        mCookieStore = builder.mCookieStore;
        mBody = builder.mBody;
    }

// MARK: - Properties

    @Override
    public URI uri() {
        return mUri;
    }

    @Override
    public HttpHeaders headers() {
        return mHeaders;
    }

    @Override
    public CookieStore cookieStore() {
        return mCookieStore;
    }

    @Override
    public T body() {
        return mBody;
    }

// MARK: - Inner Types

    public static class Builder<T>
    {
        public Builder() {
            // Do nothing
        }

        public Builder(@NotNull RequestEntity<T> entity) {
            Guard.notNull(entity, "entity is null");

            // Init instance variables
            mUri = entity.uri();
            mHeaders = entity.headers();
            mCookieStore = entity.cookieStore();
            mBody = entity.body();
        }

        public <Ti> Builder(@NotNull RequestEntity<Ti> entity, T body) {
            Guard.notNull(entity, "entity is null");

            // Init instance variables
            mUri = entity.uri();
            mHeaders = entity.headers();
            mCookieStore = entity.cookieStore();
            mBody = body;
        }

        public Builder<T> uri(@NotNull URI uri) {
            mUri = uri;
            return this;
        }

        public Builder<T> headers(HttpHeaders headers) {
            mHeaders = headers;
            return this;
        }

        public Builder<T> cookieStore(CookieStore cookieStore) {
            mCookieStore = cookieStore;
            return this;
        }

        public Builder<T> body(T body) {
            mBody = body;
            return this;
        }

        public RequestEntity<T> build() {
            Guard.notNull(uri(), "url is null");
            return new BasicRequestEntity<>(this);
        }

        protected URI uri() {
            return mUri;
        }

        private URI mUri;
        private HttpHeaders mHeaders;
        private CookieStore mCookieStore;
        private T mBody;
    }

// MARK: - Variables

    private final URI mUri;

    private final HttpHeaders mHeaders;

    private final CookieStore mCookieStore;

    private final T mBody;
}
