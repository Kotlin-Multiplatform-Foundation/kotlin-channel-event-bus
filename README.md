# Kotlin Channel Event Bus

A lightweight and efficient event bus implementation using Kotlin Coroutines and Channels.

## Overview

This library provides a robust event bus implementation using Kotlin's Channel feature, allowing for efficient communication between different parts of your application while maintaining loose coupling.

## Features

- Coroutine-based implementation
- Type-safe event handling
- Configurable channel capacity
- Support for multiple event types
- Thread-safe operation

## Quick Start

```kotlin
// Example usage
val eventBus = ChannelEventBus()

// Subscribe to events
eventBus.subscribe<UserEvent> { event ->
    println("Received user event: $event")
}

// Publish events
eventBus.publish(UserEvent("user_logged_in"))
```

## Documentation

Detailed documentation is available in the [docs/](docs/) directory:

- [Core Concepts](docs/core-concepts.md)
- [Channel Bus Capacity](docs/channel-bus-capacity.md)
- [Channel Bus Key](docs/channel-bus-key.md)
- [Use Cases](docs/use-cases.md)

## Contributing

Contributions are welcome! Please read our [Contributing Guidelines](docs/contributing.md) before submitting changes.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
