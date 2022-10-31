package com.wiczha.musiquackkotlin.user.authorization

import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.SpotifyHttpManager
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest

class SpotifyAuthorization {
    private val spotifyCallbackUri: String = System.getenv("SPOTIFY_CALLBACK_URI")
    private val redirectUri = SpotifyHttpManager.
    makeUri(spotifyCallbackUri)

    fun getSpotifyBuilder(clientID: String, clientSecret: String): SpotifyApi
        = SpotifyApi.builder()
        .setClientId(clientID)
        .setClientSecret(clientSecret)
        .setRedirectUri(redirectUri)
        .build()

    fun authorizationCodeUriRequest(spotifyApi: SpotifyApi): AuthorizationCodeUriRequest
        = spotifyApi.authorizationCodeUri()
        //TODO: FIX STATE
        .state("x4xkmn9pu3jsdadsasa")
        .scope("user-read-email,user-read-private,playlist-read-private,playlist-read-collaborative,playlist-modify-private,playlist-modify-public,streaming,user-read-playback-state,user-modify-playback-state")
        .show_dialog(true)
        .build()

    fun buildAuthorizationCode( spotifyApi: SpotifyApi, code: String ): AuthorizationCodeRequest
            = spotifyApi.authorizationCode(code).build()

    fun tokenAuthorization(accessToken: String?): SpotifyApi =
        SpotifyApi.builder().setAccessToken(accessToken).build()

    fun refreshTokenAuthorization(refreshToken: String?, clientID: String, clientSecret: String): SpotifyApi
        = SpotifyApi.Builder().setClientId(clientID).setClientSecret(clientSecret).setRefreshToken(refreshToken).build()
}