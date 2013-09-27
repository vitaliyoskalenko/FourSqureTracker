/*
 * @(#)NetworkService.java  1.0 2013/09/11
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker.service;

/**
 * Interface provides interact with network for sending Get or Post queries
 *
 * @author Vitaly Oskalenko
 * @version 1.0 11 Sep 2013
 */

import com.googlecode.androidannotations.annotations.rest.Get;
import com.googlecode.androidannotations.annotations.rest.Post;
import com.googlecode.androidannotations.annotations.rest.Rest;
import com.voskalenko.foursquretracker.model.CheckInResp;
import com.voskalenko.foursquretracker.model.CheckInsResp;
import com.voskalenko.foursquretracker.model.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;

@Rest(converters = {MappingJackson2HttpMessageConverter.class})
public interface NetworkService {

    @Get("&client_id={clientId}&client_secret={clientSecret}&redirect_uri={callback}&code={verifyCode}")
    public ResponseEntity<Token> getAccessToken(String clientId, String clientSecret,
                                                String callback, String verifyCode) throws RestClientException;
    @Get("/v2/users/self/checkins?oauth_token={token}&v={version}")
    public CheckInsResp getAllCheckIn(String token, String version) throws RestClientException;

    @Post("/v2/checkins/add?oauth_token={token}&venueId={venueId}&shout={shout}&v={version}")
    public CheckInResp addCheckIn(String token, String venueId, String shout, String version) throws RestClientException;

    public void setRootUrl(String url);
}

	
