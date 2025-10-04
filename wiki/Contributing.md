# Contributing to Match Mania

Thank you for your interest in contributing to Match Mania! This document provides guidelines and instructions for contributing to the project.

## Code of Conduct

By participating in this project, you agree to maintain a respectful and inclusive environment for all contributors.

### Our Standards

- Use welcoming and inclusive language
- Be respectful of differing viewpoints and experiences
- Gracefully accept constructive criticism
- Focus on what is best for the community
- Show empathy towards other community members

## How to Contribute

### Reporting Bugs

Before creating a bug report:
1. Check the [existing issues](https://github.com/ssfdre38/match-mania/issues) to avoid duplicates
2. Ensure you're using the latest version
3. Collect relevant information (device, Android version, steps to reproduce)

To submit a bug report:
1. Use the [Bug Report template](https://github.com/ssfdre38/match-mania/issues/new?template=bug_report.md)
2. Provide a clear and descriptive title
3. Include steps to reproduce the issue
4. Describe expected vs actual behavior
5. Add screenshots if applicable
6. Include device and version information

### Suggesting Features

Before suggesting a feature:
1. Check if it's already been suggested
2. Consider if it fits the project's scope and goals
3. Think about how it would benefit most users

To suggest a feature:
1. Use the [Feature Request template](https://github.com/ssfdre38/match-mania/issues/new?template=feature_request.md)
2. Clearly describe the proposed feature
3. Explain the use case and benefits
4. Consider including mockups or examples
5. Discuss implementation complexity if possible

### Contributing Code

#### First Time Contributors

Look for issues labeled:
- `good first issue` - Good for newcomers
- `help wanted` - Need community help
- `documentation` - Documentation improvements

#### Development Setup

1. **Fork the Repository**
   ```bash
   # Fork via GitHub UI, then clone your fork
   git clone https://github.com/YOUR_USERNAME/match-mania.git
   cd match-mania
   ```

2. **Set Up Development Environment**
   - Install Android Studio (latest stable version)
   - Install JDK 11 or higher
   - Install Android SDK (API 21-33)
   - Open project in Android Studio
   - Sync Gradle dependencies

3. **Create a Branch**
   ```bash
   git checkout -b feature/your-feature-name
   # or
   git checkout -b bugfix/issue-number-description
   ```

#### Coding Standards

**Java/Android Conventions**:
- Follow standard Java naming conventions
- Use meaningful variable and method names
- Keep methods focused and concise
- Add comments for complex logic
- Use proper indentation (4 spaces)

**Code Organization**:
- Place UI components in appropriate packages
- Keep game logic separate from UI code
- Use proper access modifiers
- Follow existing project structure

**Testing**:
- Test on multiple Android versions if possible
- Test in both portrait and landscape orientations
- Verify with different rule configurations
- Ensure AI players function correctly

#### Making Changes

1. **Write Good Commit Messages**
   ```
   Short summary (50 chars or less)
   
   More detailed explanation if necessary. Wrap at 72 characters.
   
   - Bullet points are okay
   - Reference issues: Fixes #123
   ```

2. **Keep Commits Focused**
   - One logical change per commit
   - Don't mix refactoring with feature additions
   - Separate formatting changes from logic changes

3. **Test Your Changes**
   ```bash
   # Build the project
   ./gradlew assembleDebug
   
   # Run tests
   ./gradlew test
   
   # Install and test on device/emulator
   ./gradlew installDebug
   ```

#### Submitting a Pull Request

1. **Update Your Fork**
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

2. **Push Your Changes**
   ```bash
   git push origin feature/your-feature-name
   ```

3. **Create Pull Request**
   - Go to your fork on GitHub
   - Click "New Pull Request"
   - Fill out the PR template completely
   - Link related issues
   - Add screenshots for UI changes
   - Request review from maintainers

4. **PR Review Process**
   - Maintainers will review your code
   - Address feedback and requested changes
   - Push updates to the same branch
   - Be patient and respectful

### Contributing Documentation

Documentation contributions are highly valued:

- Fix typos and grammar
- Improve clarity and examples
- Add missing documentation
- Update outdated information
- Create tutorials and guides

Documentation is in:
- `README.md` - Main project readme
- `wiki/` folder - Wiki articles
- Code comments - Inline documentation
- `docs/` folder - Additional docs

## Project Structure

```
match-mania/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/matchmania/
│   │   │   │   ├── activities/      # UI screens
│   │   │   │   ├── models/          # Data models
│   │   │   │   ├── views/           # Custom views
│   │   │   │   └── utils/           # Helper classes
│   │   │   ├── res/                 # Resources
│   │   │   └── AndroidManifest.xml
│   │   └── test/                    # Unit tests
│   └── build.gradle                 # App build config
├── .github/                         # GitHub configs
│   ├── workflows/                   # CI/CD workflows
│   └── ISSUE_TEMPLATE/              # Issue templates
├── wiki/                            # Wiki articles
└── README.md
```

## Style Guide

### Java Style
- Class names: `PascalCase`
- Methods: `camelCase`
- Constants: `UPPER_SNAKE_CASE`
- Variables: `camelCase`
- Packages: `lowercase`

### XML Style
- Layout files: `activity_name.xml` or `fragment_name.xml`
- IDs: `camelCase` with prefix (e.g., `btnStart`, `tvPlayerName`)
- Strings: descriptive keys in snake_case

### Git Commit Messages
- Use present tense ("Add feature" not "Added feature")
- Use imperative mood ("Move cursor to..." not "Moves cursor to...")
- Limit first line to 72 characters
- Reference issues and PRs liberally

## Release Process

Releases are managed by maintainers:

1. Version bump in `build.gradle`
2. Update `CHANGELOG.md`
3. Create release branch
4. Build and test release candidate
5. Create GitHub release with APKs
6. Update documentation

## Recognition

Contributors will be:
- Listed in release notes
- Credited in the About section
- Mentioned in significant contributions

## Questions?

- Check the [FAQ](FAQ)
- Browse [existing issues](https://github.com/ssfdre38/match-mania/issues)
- Start a [discussion](https://github.com/ssfdre38/match-mania/discussions)
- Contact the maintainer: ssfdre38@gmail.com

## License

By contributing, you agree that your contributions will be licensed under the Apache License 2.0, the same license as the project.

---

**Thank you for contributing to Match Mania!** 🎮🎉

**Related**: [Building from Source](Building-from-Source) | [Architecture](Architecture)
