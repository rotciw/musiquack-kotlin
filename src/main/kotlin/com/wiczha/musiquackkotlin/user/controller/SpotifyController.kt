package com.wiczha.musiquackkotlin.user.controller

import com.wiczha.musiquackkotlin.user.authorization.SpotifyAuthorization
import com.wiczha.musiquackkotlin.user.controller.request.UserCreateRequest
import com.wiczha.musiquackkotlin.user.service.SpotifyService
import com.wiczha.musiquackkotlin.user.service.UserService
import com.wiczha.musiquackkotlin.user.service.impl.SpotifyServiceImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import se.michaelthelin.spotify.model_objects.specification.*

@RestController
@RequestMapping("v1/spotify")
class SpotifyController(
    private val userService: UserService
) {
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

    @RequestMapping("/token/{code}/{email}")
    fun authorizationToken(@PathVariable code: String, @PathVariable email: String): String {
        val codes = createSpotifyService()
            .authorizationCodeSync(
                spotifyAuth.buildAuthorizationCode(
                    spotifyAuth.getSpotifyBuilder(clientID, clientSecret), code
                ),
                spotifyAuth.getSpotifyBuilder(clientID, clientSecret)
            )
        userService.create(
            UserCreateRequest(
                userId = email,
                username = email,
                accessToken = codes?.get(0) ?: "",
                refreshToken = codes?.get(1) ?: "",
            )
        )
        return codes?.get(0) ?: ""
    }

    @GetMapping("/refresh/{email}")
    fun authCodeRefresh(@PathVariable email: String): List<String>? {
        val requestUser = userService.findByUserId(email)
        return createSpotifyService().authorizationCodeRefreshSync(
            spotifyAuth.refreshTokenAuthorization(requestUser.refreshToken, clientID, clientSecret)
        )
    }

    @GetMapping("/user/{token}")
    fun currentUserData(@PathVariable token: String?): User?
        = createSpotifyService()
        .currentUserProfileAsync(
            token, spotifyAuth.tokenAuthorization(token)
        )

    @GetMapping("/playlists/{token}")
    fun currentUserPlaylists(@PathVariable token: String?): Paging<PlaylistSimplified>?
            = createSpotifyService()
        .getListOfUserPlaylists(
            token, spotifyAuth.tokenAuthorization(token)
        )

    @GetMapping("/playlists/{token}/{playlistId}")
    fun playlistItems(@PathVariable token: String?, @PathVariable playlistId: String?): Paging<PlaylistTrack>?
            = createSpotifyService()
        .getPlaylistItems(
            token, playlistId, spotifyAuth.tokenAuthorization(token)
        )

    @GetMapping("/track/{token}/{trackId}")
    fun track(@PathVariable token: String?, @PathVariable trackId: String?): Track?
            = createSpotifyService()
        .getTrack(
            token, trackId, spotifyAuth.tokenAuthorization(token)
        )

    @GetMapping("/track/recommendations/{token}/{trackId}")
    fun trackRecommendations(@PathVariable token: String?, @PathVariable trackId: String?): Recommendations?
            = createSpotifyService()
        .getTrackRecommendations(
            token, trackId, spotifyAuth.tokenAuthorization(token)
        )

    fun createSpotifyService(): SpotifyService = SpotifyServiceImpl()
}