package me.mfathy.talent.video.data.mapper


/**
 * Mapper contract to convert and map data entities.
 */
interface EntityMapper<E, D> {
    fun mapToEntity(domain: D): E
    fun mapFromEntity(entity: E): D
}