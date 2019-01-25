package me.mfathy.talent.video.domain.executor

import io.reactivex.Scheduler

/**
 * Created by Mohammed Fathy on 20/01/2019.
 * dev.mfathy@gmail.com
 *
 * ExecutionThread is a Scheduler thread contract.
 */
interface ExecutionThread {
    val subscriber: Scheduler
    val observer: Scheduler
}