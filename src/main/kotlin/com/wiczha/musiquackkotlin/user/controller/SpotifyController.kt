package com.wiczha.musiquackkotlin.user.controller

import com.wiczha.musiquackkotlin.user.authorization.SpotifyAuthorization
import com.wiczha.musiquackkotlin.user.service.SpotifyService
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import se.michaelthelin.spotify.model_objects.specification.User

@RestController
@RequestMapping("v1/spotify")
class SpotifyController {
    @Value("\${spotify.clientid}")
    lateinit var clientID: String

    @Value("\${spotify.clientsecret}")
    lateinit var clientSecret: String

    var spotifyAuth = SpotifyAuthorization()

    @RequestMapping("/callback/uri")
    fun authorizationUri(): String?
            = createSpotifyService().
    authorizationCodeUriSync(
        spotifyAuth.authorizationCodeUriRequest(
            spotifyAuth.getSpotifyBuilder(clientID,clientSecret)
        )
    )

    @RequestMapping("/token/{code}")
    fun authorizationToken(@PathVariable code: String): List<String>?
            = createSpotifyService()
        .authorizationCodeSync(
            spotifyAuth.buildAuthorizationCode(
                spotifyAuth.getSpotifyBuilder( clientID,clientSecret ), code
            ),
            spotifyAuth.getSpotifyBuilder( clientID, clientSecret )
        )

    @RequestMapping("/user/{token}")
    fun currentUserData(@PathVariable token: String?): User?
        = createSpotifyService()
        .currentUserProfileAsync(
            token, spotifyAuth.tokenAuthorization(token)
        )

    fun createSpotifyService(): SpotifyService = SpotifyService()
}