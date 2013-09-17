package com.voskalenko.foursquretracker.task;

import com.googlecode.androidannotations.annotations.rest.Get;
import com.googlecode.androidannotations.annotations.rest.Post;
import com.googlecode.androidannotations.annotations.rest.Rest;
import com.voskalenko.foursquretracker.model.Token;
import com.voskalenko.foursquretracker.model.CheckIn;
import com.voskalenko.foursquretracker.model.CheckInsResp;
import com.voskalenko.foursquretracker.model.User;
import com.voskalenko.foursquretracker.model.VenuesResp;
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
    public CheckInsResp getAllCheckIn(String token, String version) throws RestClientException;

    @Get("/v2/users/self?v={version}")
    public  ResponseEntity<User> getUserProfile(String version) throws RestClientException;

    @Get("/v2/venues/search?oauth_token={token}&ll={longitude}%2C{latitude}&intent=browse&radius={radius}&v={version}")
    public VenuesResp getNearestVenues(String token, double longitude, double latitude, int radius, String version) throws  RestClientException;

    @Post("/v2/checkins/{checkInId}/like")
    public ResponseEntity<CheckIn> likeCheckIn(String checkInId) throws RestClientException;


    public void setRootUrl(String url);

    public RestTemplate getRestTemplate();

}

	
