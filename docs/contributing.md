# Contributing Guidelines

Thank you for considering contributing to the Kotlin Channel Event Bus project! This document provides guidelines and instructions for contributing.

## Code of Conduct

This project adheres to a Code of Conduct that all contributors are expected to follow. Please read [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) before contributing.

## How to Contribute

### 1. Setting up the Development Environment

1. Fork the repository
2. Clone your fork:
   ```bash
   git clone https://github.com/your-username/kotlin-channel-event-bus.git
   ```
3. Add the upstream remote:
   ```bash
   git remote add upstream https://github.com/original-owner/kotlin-channel-event-bus.git
   ```

### 2. Making Changes

1. Create a new branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```
2. Make your changes
3. Write or update tests
4. Run the test suite:
   ```bash
   ./gradlew test
   ```
5. Format your code:
   ```bash
   ./gradlew ktlintFormat
   ```

### 3. Submitting Changes

1. Commit your changes:
   ```bash
   git commit -m "feat: add new feature"
   ```
   Follow [Conventional Commits](https://www.conventionalcommits.org/) format.

2. Push to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```

3. Create a Pull Request

## Pull Request Guidelines

1. **Title**: Use the format: `type: description`
   - Types: feat, fix, docs, style, refactor, test, chore

2. **Description**:
   - Clearly describe the changes
   - Reference any related issues
   - List breaking changes if any

3. **Checklist**:
   - [ ] Tests added/updated
   - [ ] Documentation updated
   - [ ] Code follows project style
   - [ ] All tests passing
   - [ ] No breaking changes (or documented if unavoidable)

## Code Style

- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Write clear comments and documentation
- Keep functions focused and concise
- Use appropriate visibility modifiers

## Testing

- Write unit tests for new features
- Update existing tests when modifying code
- Aim for high test coverage
- Include edge cases in tests

## Documentation

- Update README.md if needed
- Add/update documentation in docs/
- Include code examples where helpful
- Document breaking changes

## Review Process

1. Maintainers will review your PR
2. Address any requested changes
3. Once approved, maintainers will merge
4. Your contribution will be part of the next release

## Getting Help

- Create an issue for questions
- Join our community chat
- Read the documentation
- Check existing issues and PRs

## License

By contributing, you agree that your contributions will be licensed under the project's Apache License 2.0. 