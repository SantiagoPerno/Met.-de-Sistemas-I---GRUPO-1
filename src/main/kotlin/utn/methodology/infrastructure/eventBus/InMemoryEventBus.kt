package utn.methodology.infrastructure.eventBus

import utn.methodology.domain.entities.contracts.EventBus
import utn.methodology.domain.entities.events.DomainEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class InMemoryEventBus : EventBus {

    private val subscribers = mutableMapOf<Class<out DomainEvent>, MutableList<suspend (DomainEvent) -> Unit>>()
    private val scope = CoroutineScope(Dispatchers.Default)
    private val channel = Channel<DomainEvent>()

    init {
        // Iniciar una coroutine para procesar eventos
        scope.launch {
            for (event in channel) {
                subscribers[event::class.java]?.forEach { handler ->
                    launch {
                        handler(event)
                    }
                }
            }
        }
    }

    override fun publish(events: List<DomainEvent>) = runBlocking {
        events.forEach {
            channel.send(it)
        }
    }
}