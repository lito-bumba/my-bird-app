package api

import model.BirdImage

interface ImageApi {

    suspend fun getImages(): List<BirdImage>
}