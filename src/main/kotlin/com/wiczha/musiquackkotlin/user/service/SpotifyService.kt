package com.wiczha.musiquackkotlin.user.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import se.michaelthelin.spotify.model_objects.specification.Paging
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified
import se.michaelthelin.spotify.model_objects.specification.User
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest
import java.io.IOException

@Service
class SpotifyService {
    fun authorizationCodeUriSync( authorizationCodeUriRequest:
                                  AuthorizationCodeUriRequest
    ): String? {
        try {
            return authorizationCodeUriRequest.execute().toString()
        }catch (e: IOException){
            println("error " + e.localizedMessage)

        }catch (e: SpotifyWebApiException){
            println("Spotify web exception: " + e.localizedMessage)
        }
        return null
    }

    fun authorizationCodeSync(authorizationCodeRequest: AuthorizationCodeRequest,
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
        }
        return null
    }

    fun currentUserProfileAsync(accessToken: String?, spotifyApi: SpotifyApi): User? {
        val currentUserProfile = spotifyApi.currentUsersProfile.build()
        try {
            return currentUserProfile.execute()
        } catch (e: IOException) {
            println("Error" + e.localizedMessage)
        } catch (e: Throwable) {
            println("Spotify web exception " + e.localizedMessage)
        }
        return null
    }

    fun getListOfUserPlaylists(accessToken: String?, spotifyApi: SpotifyApi): Paging<PlaylistSimplified>? {
        val currentUserPlaylists = spotifyApi.listOfCurrentUsersPlaylists.build()
        try {
            return currentUserPlaylists.execute()
        } catch (e: IOException) {
            throw IOException("Error" + e.localizedMessage)
        } catch (e: Throwable) {
            throw SpotifyWebApiException("Spotify web exception " + e.localizedMessage)
        }
        return null
    }
}