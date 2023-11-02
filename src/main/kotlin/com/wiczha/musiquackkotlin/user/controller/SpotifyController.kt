package com.wiczha.musiquackkotlin.user.controller

import com.wiczha.musiquackkotlin.user.authorization.SpotifyAuthorization
import com.wiczha.musiquackkotlin.user.controller.request.UserCreateRequest
import com.wiczha.musiquackkotlin.user.controller.request.UserUpdateRequest
import com.wiczha.musiquackkotlin.user.service.SpotifyService
import com.wiczha.musiquackkotlin.user.service.UserService
import com.wiczha.musiquackkotlin.user.service.impl.SpotifyServiceImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import se.michaelthelin.spotify.model_objects.miscellaneous.Device
import se.michaelthelin.spotify.model_objects.specification.*
import kotlin.random.Random

@RestController
@RequestMapping("v1/spotify")
class SpotifyController(
    private val userService: UserService,
    @Value("\${spotify.redirect.uri}") var spotifyCallbackUri: String
) {
    @Value("\${spotify.clientid}")
    lateinit var clientID: String

    @Value("\${spotify.clientsecret}")
    lateinit var clientSecret: String

    private var spotifyAuth = SpotifyAuthorization(spotifyCallbackUri)

    @RequestMapping("/callback/uri")
    fun authorizationUri(): String? = createSpotifyService().authorizationCodeUriSync(
        spotifyAuth.authorizationCodeUriRequest(
            spotifyAuth.getSpotifyBuilder(clientID, clientSecret)
        )
    )

    fun genRandomID(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..16)
            .map { _ -> Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    @RequestMapping("/token/{code}")
    fun authorizationToken(@PathVariable code: String): String {
        val sessionId = genRandomID()
        val codes = createSpotifyService()
            .authorizationCodeSync(
                spotifyAuth.buildAuthorizationCode(
                    spotifyAuth.getSpotifyBuilder(clientID, clientSecret), code
                ),
                spotifyAuth.getSpotifyBuilder(clientID, clientSecret)
            )
        userService.create(
            UserCreateRequest(
                userId = sessionId,
                username = sessionId,
                accessToken = codes?.get(0) ?: "",
                refreshToken = codes?.get(1) ?: "",
            )
        )
        return sessionId
    }

    @GetMapping("/refresh/{sessionId}")
    fun authCodeRefresh(@PathVariable sessionId: String): ResponseEntity<HttpStatus> {
        val requestUser = userService.findBySessionId(sessionId)
        val codes = createSpotifyService().authorizationCodeRefreshSync(
            spotifyAuth.refreshTokenAuthorization(requestUser.refreshToken, clientID, clientSecret)
        )
        userService.update(
            UserUpdateRequest(
                userId = sessionId,
                username = sessionId,
                accessToken = codes?.get(0) ?: "",
                refreshToken = requestUser.refreshToken,
            )
        )
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/user/{sessionId}")
    fun currentUserData(@PathVariable sessionId: String?): User? {
        val accessToken = sessionId?.let { userService.findBySessionId(it).accessToken }
        return createSpotifyService()
            .currentUserProfileAsync(spotifyAuth.tokenAuthorization(accessToken))
    }


    @GetMapping("/playlists/{sessionId}")
    fun currentUserPlaylists(@PathVariable sessionId: String?, @RequestParam offset: Int): Paging<PlaylistSimplified>? {
        val accessToken = sessionId?.let { userService.findBySessionId(it).accessToken }
        return createSpotifyService()
            .getListOfUserPlaylists(offset, spotifyAuth.tokenAuthorization(accessToken))
    }


    @GetMapping("/playlists/{sessionId}/{playlistId}")
    fun playlistItems(@PathVariable sessionId: String?, @PathVariable playlistId: String?): Paging<PlaylistTrack>? {
        val accessToken = sessionId?.let { userService.findBySessionId(it).accessToken }
        return createSpotifyService()
            .getPlaylistItems(
                playlistId, spotifyAuth.tokenAuthorization(accessToken)
            )
    }

    @GetMapping("/track/{sessionId}/{trackId}")
    fun track(@PathVariable sessionId: String?, @PathVariable trackId: String?): Track? {
        val accessToken = sessionId?.let { userService.findBySessionId(it).accessToken }
        return createSpotifyService()
            .getTrack(
                trackId, spotifyAuth.tokenAuthorization(accessToken)
            )
    }

    @GetMapping("/track/recommendations/{sessionId}/{trackId}")
    fun trackRecommendations(@PathVariable sessionId: String?, @PathVariable trackId: String?): Recommendations? {
        val accessToken = sessionId?.let { userService.findBySessionId(it).accessToken }
        return createSpotifyService()
            .getTrackRecommendations(
                trackId, spotifyAuth.tokenAuthorization(accessToken)
            )

    }

    @GetMapping("/search/{sessionId}/{queryString}")
    fun searchTracks(@PathVariable sessionId: String?, @PathVariable queryString: String?): Paging<Track>? {
        val accessToken = sessionId?.let { userService.findBySessionId(it).accessToken }
        return createSpotifyService().searchTracks(queryString, spotifyAuth.tokenAuthorization(accessToken))
    }


    @GetMapping("/play/{sessionId}/{uri}/{positionMs}/{deviceId}")
    fun playTrack(
        @PathVariable sessionId: String?,
        @PathVariable uri: String?,
        @PathVariable deviceId: String?,
        @PathVariable positionMs: Int?
    ): String? {
        val accessToken = sessionId?.let { userService.findBySessionId(it).accessToken }
        return createSpotifyService().playTrack(uri, positionMs, deviceId, spotifyAuth.tokenAuthorization(accessToken))
    }

    @GetMapping("/pause/{sessionId}")
    fun pauseTrack(@PathVariable sessionId: String?): String? {
        val accessToken = sessionId?.let { userService.findBySessionId(it).accessToken }
        return createSpotifyService().pauseTrack(spotifyAuth.tokenAuthorization(accessToken))
    }

    @GetMapping("/devices/{sessionId}")
    fun getDevices(@PathVariable sessionId: String?): Array<out Device>? {
        val accessToken = sessionId?.let { userService.findBySessionId(it).accessToken }
        return createSpotifyService().getDevices(spotifyAuth.tokenAuthorization(accessToken))
    }

    fun createSpotifyService(): SpotifyService = SpotifyServiceImpl()
}