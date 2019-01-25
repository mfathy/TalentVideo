package me.mfathy.talent.video.executor

import io.reactivex.Scheduler
import me.mfathy.talent.video.domain.executor.ExecutionThread
import javax.inject.Inject

/**
 * Created by Mohammed Fathy on 20/01/2019.
 * dev.mfathy@gmail.com
 *
 * Rx Schedules
 */
class ExecutionSchedulers @Inject constructor(
    override val subscriber: Scheduler,
    override val observer: Scheduler
) : ExecutionThread