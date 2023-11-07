package com.practicum.myplaylistmaker.domain.player.impl

import com.practicum.myplaylistmaker.domain.player.TracksInteractor
import com.practicum.myplaylistmaker.domain.player.TracksRepository
import com.practicum.myplaylistmaker.util.Resource
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            when(val resource = repository.searchTracks(expression)){
                is Resource.Success -> { consumer.consume(resource.data,null) }
                is Resource.Error -> {consumer.consume(null, resource.message)}
            }
        }
    }
}