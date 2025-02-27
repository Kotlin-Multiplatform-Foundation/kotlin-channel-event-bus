# Use Cases

This document outlines common use cases and patterns for implementing the Kotlin Channel Event Bus in your applications.

## 1. User Interface Updates

### Real-time UI Updates
```kotlin
data class UiStateEvent(val state: UiState)

// Subscribe to UI updates
eventBus.subscribe<UiStateEvent> { event ->
    updateUiComponents(event.state)
}

// Publish UI changes
viewModel.onStateChange {
    eventBus.publish(UiStateEvent(newState))
}
```

## 2. Inter-Module Communication

### Cross-Module Events
```kotlin
// Module A
data class PaymentProcessedEvent(val orderId: String, val amount: Double)

// Module B (Order Management)
eventBus.subscribe<PaymentProcessedEvent> { event ->
    updateOrderStatus(event.orderId, "PAID")
}
```

## 3. Background Task Management

### Long-running Operations
```kotlin
data class BackgroundTaskEvent(
    val taskId: String,
    val status: TaskStatus,
    val progress: Float
)

// Progress updates
eventBus.subscribe<BackgroundTaskEvent> { event ->
    updateProgressBar(event.taskId, event.progress)
}
```

## 4. Authentication and Session Management

### User Session Events
```kotlin
sealed class SessionEvent {
    data class Login(val user: User) : SessionEvent()
    data class Logout(val reason: String) : SessionEvent()
    object SessionExpired : SessionEvent()
}

eventBus.subscribe<SessionEvent> { event ->
    when (event) {
        is SessionEvent.Login -> handleLogin(event.user)
        is SessionEvent.Logout -> handleLogout(event.reason)
        is SessionEvent.SessionExpired -> showSessionExpiredDialog()
    }
}
```

## 5. Data Synchronization

### Real-time Data Updates
```kotlin
data class DataSyncEvent<T>(
    val entity: T,
    val operation: SyncOperation
)

eventBus.subscribe<DataSyncEvent<UserProfile>> { event ->
    when (event.operation) {
        SyncOperation.CREATE -> createUser(event.entity)
        SyncOperation.UPDATE -> updateUser(event.entity)
        SyncOperation.DELETE -> deleteUser(event.entity)
    }
}
```

## 6. Error Handling

### Global Error Management
```kotlin
data class ErrorEvent(
    val error: Throwable,
    val severity: ErrorSeverity,
    val context: String
)

eventBus.subscribe<ErrorEvent> { event ->
    when (event.severity) {
        ErrorSeverity.CRITICAL -> showErrorDialog(event.error)
        ErrorSeverity.WARNING -> showToast(event.error.message)
        ErrorSeverity.INFO -> logError(event.error)
    }
}
```

## 7. Feature Flags and Configuration

### Dynamic Configuration Updates
```kotlin
data class ConfigurationEvent(
    val key: String,
    val value: Any,
    val source: ConfigSource
)

eventBus.subscribe<ConfigurationEvent> { event ->
    updateFeatureFlag(event.key, event.value)
}
```

## 8. Analytics and Logging

### Event Tracking
```kotlin
data class AnalyticsEvent(
    val name: String,
    val parameters: Map<String, Any>
)

eventBus.subscribe<AnalyticsEvent> { event ->
    analyticsTracker.logEvent(event.name, event.parameters)
}
```

## Best Practices for Implementation

1. **Event Design**
   - Keep events immutable
   - Use sealed classes for related events
   - Include necessary context in events

2. **Subscription Management**
   - Clean up subscriptions when no longer needed
   - Use appropriate scope for coroutines
   - Handle errors in subscribers

3. **Performance Considerations**
   - Use appropriate channel capacity
   - Batch events when possible
   - Monitor event processing time

4. **Testing**
   - Write unit tests for event handlers
   - Mock event bus in tests
   - Test error scenarios 