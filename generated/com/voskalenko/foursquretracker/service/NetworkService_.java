//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package com.voskalenko.foursquretracker.service;

import java.util.HashMap;
import com.voskalenko.foursquretracker.model.CheckInResponse;
import com.voskalenko.foursquretracker.model.CheckInsResponse;
import com.voskalenko.foursquretracker.model.Token;
import com.voskalenko.foursquretracker.model.UserResponse;
import com.voskalenko.foursquretracker.model.VenueResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class NetworkService_
    implements NetworkService
{

    private RestTemplate restTemplate;
    private String rootUrl;

    public NetworkService_() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        rootUrl = "";
    }

    @Override
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    @Override
    public void setRootUrl(String url) {
        this.rootUrl = url;
    }

    @Override
    public CheckInsResponse getAllCheckIn(String token, String version) {
        HashMap<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("token", token);
        urlVariables.put("version", version);
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(httpHeaders);
        return restTemplate.exchange(rootUrl.concat("/v2/users/self/checkins?oauth_token={token}&v={version}"), HttpMethod.GET, requestEntity, CheckInsResponse.class, urlVariables).getBody();
    }

    @Override
    public VenueResponse getNearestVenues(String token, double latitude, double longitude, int radius, String version) {
        HashMap<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("token", token);
        urlVariables.put("latitude", latitude);
        urlVariables.put("longitude", longitude);
        urlVariables.put("radius", radius);
        urlVariables.put("version", version);
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(httpHeaders);
        return restTemplate.exchange(rootUrl.concat("/v2/venues/search?oauth_token={token}&ll={latitude},{longitude}&intent=checkin&radius={radius}&v={version}"), HttpMethod.GET, requestEntity, VenueResponse.class, urlVariables).getBody();
    }

    @Override
    public ResponseEntity<Token> getAccessToken(String clientId, String clientSecret, String callback, String verifyCode) {
        HashMap<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("clientId", clientId);
        urlVariables.put("clientSecret", clientSecret);
        urlVariables.put("callback", callback);
        urlVariables.put("verifyCode", verifyCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(httpHeaders);
        return restTemplate.exchange(rootUrl.concat("&client_id={clientId}&client_secret={clientSecret}&redirect_uri={callback}&code={verifyCode}"), HttpMethod.GET, requestEntity, Token.class, urlVariables);
    }

    @Override
    public UserResponse getUserProfile(String token, String version) {
        HashMap<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("token", token);
        urlVariables.put("version", version);
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(httpHeaders);
        return restTemplate.exchange(rootUrl.concat("/v2/users/self?oauth_token={token}&v={version}"), HttpMethod.GET, requestEntity, UserResponse.class, urlVariables).getBody();
    }

    @Override
    public CheckInResponse addCheckIn(String token, String venueId, String shout, String version) {
        HashMap<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("token", token);
        urlVariables.put("venueId", venueId);
        urlVariables.put("shout", shout);
        urlVariables.put("version", version);
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(httpHeaders);
        return restTemplate.exchange(rootUrl.concat("/v2/checkins/add?oauth_token={token}&venueId={venueId}&shout={shout}&v={version}"), HttpMethod.POST, requestEntity, CheckInResponse.class, urlVariables).getBody();
    }

}
