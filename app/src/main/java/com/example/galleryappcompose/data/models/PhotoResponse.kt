package com.example.galleryappcompose.data.models

data class PhotoResponse(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: List<Photo>?,
    val videos: List<Video>?,
    val prev_page: String,
    val total_results: Int
) {
    data class Photo(
        val src: Src?
    ) {
        data class Src(
            val large: String,
            val large2x: String,
            val medium: String,
            val original: String,
            val small: String,
            val tiny: String
        )
    }

    data class Video(
        val duration: Int,
        val image: String,
        val tags: List<Any>,
        val video_files: List<VideoFile>
    ) {

        data class VideoFile(
            val link: String
        )
    }
}