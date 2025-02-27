# Channel Bus Key

This document explains the concept of channel bus keys and how they are used in the Kotlin Channel Event Bus implementation.

## What is a Channel Bus Key?

A channel bus key is a unique identifier used to route events to specific subscribers. It combines the event type with optional metadata to create targeted event delivery paths.

## Key Components

### 1. Event Type
```kotlin
interface EventBusKey<T> {
    val eventType: KClass<T>
}
```
- Represents the Kotlin class of the event
- Ensures type safety in event delivery
- Used for automatic subscriber type inference

### 2. Metadata
```kotlin
data class EventBusKeyMetadata(
    val tag: String? = null,
    val priority: Int = 0
)
```
- Additional information for event routing
- Can include tags for filtering
- May contain priority information

## Using Channel Bus Keys

### 1. Simple Type-Based Key
```kotlin
// Define an event
data class UserLoginEvent(val userId: String)

// Create a key
val loginKey = eventBus.key<UserLoginEvent>()

// Subscribe using the key
eventBus.subscribe(loginKey) { event ->
    println("User ${event.userId} logged in")
}
```

### 2. Tagged Keys
```kotlin
// Create a tagged key
val adminLoginKey = eventBus.key<UserLoginEvent>("admin")

// Subscribe to admin-only events
eventBus.subscribe(adminLoginKey) { event ->
    println("Admin ${event.userId} logged in")
}
```

### 3. Priority Keys
```kotlin
// Create a high-priority key
val highPriorityKey = eventBus.key<UserLoginEvent>(
    metadata = EventBusKeyMetadata(priority = 10)
)
```

## Key Benefits

1. **Type Safety**
   - Compile-time type checking
   - Prevents event type mismatches
   - IDE support for event handling

2. **Flexible Routing**
   - Multiple subscribers per event type
   - Filtered subscriptions using tags
   - Priority-based event handling

3. **Clean Architecture**
   - Decoupled components
   - Clear event flow paths
   - Easy to test and maintain

## Best Practices

1. **Key Organization**
   - Group related keys together
   - Use consistent naming conventions
   - Document key purposes and usage

2. **Type Safety**
   - Leverage Kotlin's type system
   - Avoid type casting in subscribers
   - Use sealed classes for event hierarchies

3. **Performance**
   - Reuse keys when possible
   - Avoid creating unnecessary keys
   - Clean up unused subscriptions

4. **Testing**
   - Mock keys for unit tests
   - Verify event routing
   - Test priority handling 