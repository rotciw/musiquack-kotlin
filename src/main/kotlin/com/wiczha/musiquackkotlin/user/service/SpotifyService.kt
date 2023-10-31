package com.wiczha.musiquackkotlin.user.service

import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.model_objects.specification.Paging
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack
import se.michaelthelin.spotify.model_objects.specification.Recommendations
import se.michaelthelin.spotify.model_objects.specification.Track
import se.michaelthelin.spotify.model_objects.specification.User
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest

interface SpotifyService {
    fun authorizationCodeUriSync( authorizationCodeUriRequest: AuthorizationCodeUriRequest): String?

    fun authorizationCodeSync(authorizationCodeRequest: AuthorizationCodeRequest, spotifyApi: SpotifyApi): List<String>?

    fun authorizationCodeRefreshSync(spotifyApi: SpotifyApi): List<String>?

    fun currentUserProfileAsync(spotifyApi: SpotifyApi): User?

    fun getListOfUserPlaylists(offset:Int, spotifyApi: SpotifyApi): Paging<PlaylistSimplified>?

    fun getPlaylistItems(playlistId: String?, spotifyApi: SpotifyApi): Paging<PlaylistTrack>?

    fun getTrack(trackId: String?, spotifyApi: SpotifyApi): Track?

    fun getTrackRecommendations(trackId: String?, spotifyApi: SpotifyApi): Recommendations?

    fun searchTracks(queryString: String?, spotifyApi: SpotifyApi): Paging<Track>?

    fun playTrack(uri: String?, positionMs: Int?, spotifyApi: SpotifyApi): String?

    fun pauseTrack(spotifyApi: SpotifyApi): String?
}