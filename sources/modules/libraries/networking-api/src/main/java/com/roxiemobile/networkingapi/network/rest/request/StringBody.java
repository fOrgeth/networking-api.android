package com.roxiemobile.networkingapi.network.rest.request;

import com.roxiemobile.androidcommons.data.Constants.Charsets;
import com.roxiemobile.networkingapi.network.http.MediaType;
import com.roxiemobile.networkingapi.network.rest.HttpBody;

public class StringBody implements HttpBody
{
// MARK: - Construction

    public StringBody(String body) {
        mBody = body;
    }

// MARK: - Methods

    @Override
    public MediaType mediaType() {
        return MEDIA_TYPE;
    }

    @Override
    public byte[] body() {
        return (mBody != null) ? mBody.getBytes(Charsets.UTF_8) : null;
    }

// MARK: - Constants

    private final static MediaType MEDIA_TYPE =
            MediaType.valueOf(MediaType.TEXT_PLAIN_VALUE + "; charset=" + Charsets.UTF_8.name());

// MARK: - Variables

    private String mBody;
}
