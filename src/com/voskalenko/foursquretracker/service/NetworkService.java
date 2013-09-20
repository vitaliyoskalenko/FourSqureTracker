package com.voskalenko.foursquretracker.service;

import com.googlecode.androidannotations.annotations.rest.Get;
import com.googlecode.androidannotations.annotations.rest.Post;
import com.googlecode.androidannotations.annotations.rest.Rest;
import com.voskalenko.foursquretracker.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Rest(converters = {MappingJackson2HttpMessageConverter.class})
public interface NetworkService {

    @Get("&client_id={clientId}&client_secret={clientSecret}&redirect_uri={callback}&code={verifyCode}")
    public ResponseEntity<Token> getAccessToken(String clientId, String clientSecret,
                                                String callback, String verifyCode) throws RestClientException;
    @Get("/v2/users/self/checkins?oauth_token={token}&v={version}")
    public CheckInsResponse getAllCheckIn(String token, String version) throws RestClientException;

    @Get("/v2/users/self?oauth_token={token}&v={version}")
    public UserResponse getUserProfile(String token, String version) throws RestClientException;

    @Get("/v2/venues/search?oauth_token={token}&ll={latitude},{longitude}&intent=checkin&radius={radius}&v={version}")
    public VenueResponse getNearestVenues(String token, double latitude, double longitude, int radius, String version) throws  RestClientException;

    @Post("/v2/checkins/add?oauth_token={token}&venueId={venueId}&shout={shout}&v={version}")
    public CheckInResponse addCheckIn(String token, String venueId, String shout, String version) throws RestClientException;

    public void setRootUrl(String url);

    public RestTemplate getRestTemplate();

}

	
