package me.mfathy.talent.video.test

import java.util.concurrent.ThreadLocalRandom

/**
 * Created by Mohammed Fathy on 11/01/2019.
 * dev.mfathy@gmail.com
 */
object TestFactory {
    fun randomInt(): Int = ThreadLocalRandom.current().nextInt(0, 5)

    fun randomDouble(): Double = ThreadLocalRandom.current().nextDouble(0.0, 5.0)

    fun randomString(): String = java.util.UUID.randomUUID().toString()

    fun randomBoolean(): Boolean = ThreadLocalRandom.current().nextBoolean()


}