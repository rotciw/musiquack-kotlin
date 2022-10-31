package com.wiczha.musiquackkotlin.user.service.impl

import com.wiczha.musiquackkotlin.user.controller.request.UserCreateRequest
import com.wiczha.musiquackkotlin.user.service.SpotifyService
import org.apache.http.HttpException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.SpotifyHttpManager
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import se.michaelthelin.spotify.model_objects.specification.Paging
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack
import se.michaelthelin.spotify.model_objects.specification.Recommendations
import se.michaelthelin.spotify.model_objects.specification.Track
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest
import se.michaelthelin.spotify.model_objects.specification.User as SpotifyUser
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest
import java.io.IOException
import com.wiczha.musiquackkotlin.user.service.UserService as UserService

@Service
class SpotifyServiceImpl: SpotifyService {
    override fun authorizationCodeUriSync(authorizationCodeUriRequest:
                                  AuthorizationCodeUriRequest
    ): String? {
        try {
            return authorizationCodeUriRequest.execute().toString()
        }catch (e: IOException){
            println("error " + e.localizedMessage)

        }catch (e: SpotifyWebApiException){
            println("Spotify web exception: " + e.localizedMessage)
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.localizedMessage, e)
        }
        return null
    }

    override fun authorizationCodeSync(authorizationCodeRequest: AuthorizationCodeRequest,
                                       spotifyApi: SpotifyApi
    ): List<String>? {
        try {
            val authorizationCodeCredentials = authorizationCodeRequest.execute()
            spotifyApi.accessToken = authorizationCodeCredentials.accessToken
            spotifyApi.refreshToken = authorizationCodeCredentials.refreshToken
            return listOf<String>(spotifyApi.accessToken, spotifyApi.refreshToken)

        } catch (e: IOException) {
            println("error " + e.localizedMessage)

        } catch (e: SpotifyWebApiException) {
            println("Spotify web exception: " + e.localizedMessage)
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.localizedMessage, e)
        }
        return null
    }

    override fun authorizationCodeRefreshSync(spotifyApi: SpotifyApi): List<String>? {
        try {
            val authorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh().build()
            val authRefreshCredentials = authorizationCodeRefreshRequest.execute()
            spotifyApi.accessToken = authRefreshCredentials.accessToken
            return listOf<String>(spotifyApi.accessToken, authRefreshCredentials.expiresIn.toString())

        } catch (e: IOException) {
            println("error " + e.localizedMessage)

        } catch (e: SpotifyWebApiException) {
            println("Spotify web exception: " + e.localizedMessage)
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.localizedMessage, e)
        }
        return null
    }

    override fun currentUserProfileAsync(accessToken: String?, spotifyApi: SpotifyApi): SpotifyUser? {
        val currentUserProfile = spotifyApi.currentUsersProfile.build()
        try {
            return currentUserProfile.execute()
        } catch (e: IOException) {
            println("Error" + e.localizedMessage)
        } catch (e: SpotifyWebApiException) {
            println("Spotify web exception: " + e.localizedMessage)
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.localizedMessage, e)
        }
        return null
    }

    override fun getListOfUserPlaylists(accessToken: String?, spotifyApi: SpotifyApi): Paging<PlaylistSimplified>? {
        val currentUserPlaylists = spotifyApi.listOfCurrentUsersPlaylists.build()
        try {
            return currentUserPlaylists.execute()
        } catch (e: IOException) {
            throw IOException("Error" + e.localizedMessage)
        } catch (e: SpotifyWebApiException) {
            println("Spotify web exception: " + e.localizedMessage)
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.localizedMessage, e)
        }
    }

    override fun getPlaylistItems(accessToken: String?, playlistId: String?, spotifyApi: SpotifyApi): Paging<PlaylistTrack>? {
        val playlistItem = spotifyApi.getPlaylistsItems(playlistId).build()
        try {
            return playlistItem.execute()
        } catch (e: IOException) {
            throw IOException("Error" + e.localizedMessage)
        } catch (e: SpotifyWebApiException) {
            println("Spotify web exception: " + e.localizedMessage)
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.localizedMessage, e)
        }
    }

    override fun getTrack(accessToken: String?, trackId: String?, spotifyApi: SpotifyApi): Track? {
        val track = spotifyApi.getTrack(trackId).build()
        try {
            return track.execute()
        } catch (e: IOException) {
            throw IOException("Error" + e.localizedMessage)
        } catch (e: Throwable) {
            println("Spotify web exception: " + e.localizedMessage)
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.localizedMessage, e)
        }
    }

    override fun getTrackRecommendations(accessToken: String?, trackId: String?, spotifyApi: SpotifyApi): Recommendations? {
        val trackRecommendations = spotifyApi.recommendations.seed_tracks(trackId).limit(50).min_popularity(27).build()
        try {
//            TODO("Sort by popularity")
//            val executedTrackRecommendations = trackRecommendations.execute()
//            val stringList = executedTrackRecommendations.tracks.map { trackId }.joinToString()
//            print(stringList)
//            val getMultipleTracks = spotifyApi.getSeveralTracks(stringList).build()
            return trackRecommendations.execute()
        } catch (e: IOException) {
            throw IOException("Error" + e.localizedMessage)
        } catch (e: SpotifyWebApiException) {
            println("Spotify web exception: " + e.localizedMessage)
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, e.localizedMessage, e)
        }
    }
}