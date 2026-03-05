package com.phricardo.eduapi.security;

import com.phricardo.eduapi.entity.AppUser;
import com.phricardo.eduapi.service.AppUserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final AppUserService appUserService;

  @Override
  public OAuth2User loadUser(final OAuth2UserRequest userRequest)
      throws OAuth2AuthenticationException {
    OAuth2User delegateUser = new DefaultOAuth2UserService().loadUser(userRequest);

    Map<String, Object> attributes = delegateUser.getAttributes();
    String email = (String) attributes.get("email");
    String nome = (String) attributes.getOrDefault("name", email);
    if (email == null || email.isBlank()) {
      throw new OAuth2AuthenticationException(
          new OAuth2Error("invalid_user_info"), "Email não retornado pelo Google");
    }

    AppUser appUser = appUserService.upsertGoogleUser(nome, email);

    Collection<GrantedAuthority> authorities = new ArrayList<>(delegateUser.getAuthorities());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + appUser.getTipo().name()));

    return new DefaultOAuth2User(authorities, attributes, "email");
  }
}
