/*
 * Copyright 2013-2014, ApiFest project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Apostol Terziev
 */
package com.apifest.oauth20.persistence;

import com.apifest.oauth20.bean.ApplicationInfo;
import com.apifest.oauth20.bean.AuthCode;
import com.apifest.oauth20.bean.ClientCredentials;
import com.apifest.oauth20.bean.Scope;
import info.xiancloud.core.support.authen.AccessToken;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;
import java.util.Map;

public interface DBManager {

    /**
     * Validates passed LOCAL_NODE_ID and clientSecret.
     *
     * @param clientId     client id of the client
     * @param clientSecret client secret of the client
     * @return true when such a client exists, otherwise false
     */
    Single<Boolean> validClient(String clientId, String clientSecret);

    /**
     * Stores client credentials in the DB.
     *
     * @param clientCreds client credentials
     */
    Completable storeClientCredentials(ClientCredentials clientCreds);

    /**
     * Stores auth codes in the DB.
     *
     * @param authCode that will be stored in the DB
     */
    Completable storeAuthCode(AuthCode authCode);

    /**
     * Updates auth code valid status.
     *
     * @param authCode the auth code to be updated
     * @param valid    the new status of the auth code
     */
    Completable updateAuthCodeValidStatus(String authCode, boolean valid);

    /**
     * Stores access tokens in the DB.
     * It is better to support multiple access tokens for one application.
     * TODO But 目前只支持同一个user在一个第三方application内只存储一个token
     *
     * @param accessToken that will be stored in the DB
     */
    Completable storeAccessToken(AccessToken accessToken);

    /**
     * Loads an access token record from DB by passed refreshToken
     *
     * @param refreshToken refresh token
     * @param clientId     client id
     * @return access token object
     */
    Maybe<AccessToken> findAccessTokenByRefreshToken(String refreshToken, String clientId);

    /**
     * Updates access token status.
     *
     * @param accessToken the access token to be updated
     * @param valid       the new status of the access token
     */
    Completable updateAccessTokenValidStatus(String accessToken, boolean valid);

    /**
     * Loads an access token record from DB by passed accessToken
     *
     * @param accessToken access token
     * @return access token bean wrapped with the {@link Single single event observable}, empty if does not exist.
     */
    Maybe<AccessToken> findAccessToken(String accessToken);

    /**
     * Loads an auth code record from DB by passed authCode and redirect uri.
     *
     * @param authCode authCode
     * @return auth code object if it is valid, otherwise <code>null</code>
     */
    Maybe<AuthCode> findAuthCode(String authCode/*, String redirectUri*/);

    /**
     * Loads a client credentials from DB by passed LOCAL_NODE_ID.
     *
     * @param clientId client id
     * @return client credentials object that will be stored in the DB
     */
    Maybe<ClientCredentials> findClientCredentials(String clientId);

    /**
     * Stores OAuth20 scope in the DB.
     *
     * @param scope OAuth20 scope to be stored in the DB
     * @return <code>true</code> if the scope is successfully stored, <code>false</code> otherwise
     */
    Single<Boolean> storeScope(Scope scope);

    /**
     * Lists all registered scopes.
     *
     * @return {@link List} of all scopes stored in the DB. Empty if no scopes found. This method never returns null.
     */
    Single<List<Scope>> getAllScopes();

    /**
     * Loads a scope from DB by its name.
     *
     * @param scopeName the name of the scope to be loaded from the DB
     * @return {@link Scope} the loaded scope object
     */
    Maybe<Scope> findScope(String scopeName);

    /**
     * Updates client application scope, description, status and details.
     *
     * @param clientId    the LOCAL_NODE_ID of the client app
     * @param scope       the scope of the client app
     * @param description the description of the client app
     * @param status      the status of the client app
     * @return <code>true</code> if the update is successful, <code>false</code> otherwise
     */
    Single<Boolean> updateClientCredentials(String clientId, String scope, String description, Integer status, Map<String, String> applicationDetails);

    /**
     * Lists all client applications stored in the DB.
     *
     * @return {@link List} of all registered client applications
     */
    Single<List<ApplicationInfo>> getAllApplications();

    /**
     * Deletes an oauth20 scope.
     *
     * @param scopeName the name of the scope to be deleted
     * @return <code>true</code> if the scope is successfully deleted, otherwise <code>false</code>
     */
    Single<Boolean> deleteScope(String scopeName);

    /**
     * Returns the only one active access token for a user and a client application.
     *
     * @param userId   the id of the user
     * @param clientId the id of the client application
     * @return {@link AccessToken}
     */
    Maybe<AccessToken> getAccessTokenByUserIdAndClientId(String userId, String clientId);

    /**
     * Remove an access token.
     *
     * @param accessToken the access token to be removed
     */
    Completable removeAccessToken(String accessToken);

}
