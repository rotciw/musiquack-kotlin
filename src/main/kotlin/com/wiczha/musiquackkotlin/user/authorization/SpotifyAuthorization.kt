package com.wiczha.musiquackkotlin.user.authorization

import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.SpotifyHttpManager
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest

class SpotifyAuthorization {
    private val redirectUri = SpotifyHttpManager.
    makeUri("http://localhost:5173/auth/spotify/callback")

    fun getSpotifyBuilder(clientID: String, clientSecret: String): SpotifyApi
        = SpotifyApi.builder()
        .setClientId(clientID)
        .setClientSecret(clientSecret)
        .setRedirectUri(redirectUri)
        .build()

    fun authorizationCodeUriRequest(spotifyApi: SpotifyApi): AuthorizationCodeUriRequest
        = spotifyApi.authorizationCodeUri()
        .state("x4xkmn9pu3j6ukrs8n")
        .scope("user-read-email,user-read-private,playlist-read-private,playlist-read-collaborative,playlist-modify-private,playlist-modify-public")
        .show_dialog(true)
        .build()

    fun buildAuthorizationCode( spotifyApi: SpotifyApi, code: String ): AuthorizationCodeRequest
            = spotifyApi.authorizationCode(code).build()

    fun tokenAuthorization(accessToken: String?): SpotifyApi =
        SpotifyApi.builder().setAccessToken(accessToken).build()
}