package utn.methodology.domain.entities.contracts

import utn.methodology.domain.entities.events.DomainEvent

interface EventBus {
    fun publish(events: List<DomainEvent>)
}