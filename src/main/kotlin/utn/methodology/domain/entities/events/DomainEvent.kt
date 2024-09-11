package utn.methodology.domain.entities.events

interface DomainEvent {
    abstract val eventName: String
}