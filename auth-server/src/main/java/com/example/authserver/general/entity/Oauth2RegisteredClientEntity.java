package com.example.authserver.general.entity;

import jakarta.persistence.*;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * @Auther: 长安
 */
@Entity
@ToString
@Table(name = "OAUTH2_REGISTERED_CLIENT")
public class Oauth2RegisteredClientEntity {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @Column(name = "ID")
    private String id;
    @Basic
    @Column(name = "CLIENT_ID")
    private String clientId;
    @Basic
    @Column(name = "CLIENT_ID_ISSUED_AT")
    private Timestamp clientIdIssuedAt;
    @Basic
    @Column(name = "CLIENT_SECRET")
    private String clientSecret;
    @Basic
    @Column(name = "CLIENT_SECRET_EXPIRES_AT")
    private Timestamp clientSecretExpiresAt;
    @Basic
    @Column(name = "CLIENT_NAME")
    private String clientName;
    @Basic
    @Column(name = "CLIENT_AUTHENTICATION_METHODS")
    private String clientAuthenticationMethods;
    @Basic
    @Column(name = "AUTHORIZATION_GRANT_TYPES")
    private String authorizationGrantTypes;
    @Basic
    @Column(name = "REDIRECT_URIS")
    private String redirectUris;
    @Basic
    @Column(name = "SCOPES")
    private String scopes;
    @Basic
    @Column(name = "CLIENT_SETTINGS")
    private String clientSettings;
    @Basic
    @Column(name = "TOKEN_SETTINGS")
    private String tokenSettings;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Timestamp getClientIdIssuedAt() {
        return clientIdIssuedAt;
    }

    public void setClientIdIssuedAt(Timestamp clientIdIssuedAt) {
        this.clientIdIssuedAt = clientIdIssuedAt;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Timestamp getClientSecretExpiresAt() {
        return clientSecretExpiresAt;
    }

    public void setClientSecretExpiresAt(Timestamp clientSecretExpiresAt) {
        this.clientSecretExpiresAt = clientSecretExpiresAt;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAuthenticationMethods() {
        return clientAuthenticationMethods;
    }

    public void setClientAuthenticationMethods(String clientAuthenticationMethods) {
        this.clientAuthenticationMethods = clientAuthenticationMethods;
    }

    public String getAuthorizationGrantTypes() {
        return authorizationGrantTypes;
    }

    public void setAuthorizationGrantTypes(String authorizationGrantTypes) {
        this.authorizationGrantTypes = authorizationGrantTypes;
    }

    public String getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(String redirectUris) {
        this.redirectUris = redirectUris;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getClientSettings() {
        return clientSettings;
    }

    public void setClientSettings(String clientSettings) {
        this.clientSettings = clientSettings;
    }

    public String getTokenSettings() {
        return tokenSettings;
    }

    public void setTokenSettings(String tokenSettings) {
        this.tokenSettings = tokenSettings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Oauth2RegisteredClientEntity that = (Oauth2RegisteredClientEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (clientId != null ? !clientId.equals(that.clientId) : that.clientId != null) return false;
        if (clientIdIssuedAt != null ? !clientIdIssuedAt.equals(that.clientIdIssuedAt) : that.clientIdIssuedAt != null)
            return false;
        if (clientSecret != null ? !clientSecret.equals(that.clientSecret) : that.clientSecret != null) return false;
        if (clientSecretExpiresAt != null ? !clientSecretExpiresAt.equals(that.clientSecretExpiresAt) : that.clientSecretExpiresAt != null)
            return false;
        if (clientName != null ? !clientName.equals(that.clientName) : that.clientName != null) return false;
        if (clientAuthenticationMethods != null ? !clientAuthenticationMethods.equals(that.clientAuthenticationMethods) : that.clientAuthenticationMethods != null)
            return false;
        if (authorizationGrantTypes != null ? !authorizationGrantTypes.equals(that.authorizationGrantTypes) : that.authorizationGrantTypes != null)
            return false;
        if (redirectUris != null ? !redirectUris.equals(that.redirectUris) : that.redirectUris != null) return false;
        if (scopes != null ? !scopes.equals(that.scopes) : that.scopes != null) return false;
        if (clientSettings != null ? !clientSettings.equals(that.clientSettings) : that.clientSettings != null)
            return false;
        if (tokenSettings != null ? !tokenSettings.equals(that.tokenSettings) : that.tokenSettings != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (clientId != null ? clientId.hashCode() : 0);
        result = 31 * result + (clientIdIssuedAt != null ? clientIdIssuedAt.hashCode() : 0);
        result = 31 * result + (clientSecret != null ? clientSecret.hashCode() : 0);
        result = 31 * result + (clientSecretExpiresAt != null ? clientSecretExpiresAt.hashCode() : 0);
        result = 31 * result + (clientName != null ? clientName.hashCode() : 0);
        result = 31 * result + (clientAuthenticationMethods != null ? clientAuthenticationMethods.hashCode() : 0);
        result = 31 * result + (authorizationGrantTypes != null ? authorizationGrantTypes.hashCode() : 0);
        result = 31 * result + (redirectUris != null ? redirectUris.hashCode() : 0);
        result = 31 * result + (scopes != null ? scopes.hashCode() : 0);
        result = 31 * result + (clientSettings != null ? clientSettings.hashCode() : 0);
        result = 31 * result + (tokenSettings != null ? tokenSettings.hashCode() : 0);
        return result;
    }
}
