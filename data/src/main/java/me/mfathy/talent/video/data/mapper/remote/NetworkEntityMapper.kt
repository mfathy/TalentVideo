package me.mfathy.talent.video.data.mapper.remote


/**
 * Mapper contract to convert and map data entities.
 */
interface NetworkEntityMapper<E, D> {
    fun mapToEntity(domain: D): E
}