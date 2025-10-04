# Security Policy

## Supported Versions

We release patches for security vulnerabilities for the following versions:

| Version | Supported          |
| ------- | ------------------ |
| 2.1.x   | :white_check_mark: |
| 2.0.x   | :white_check_mark: |
| 1.4.x   | :x:                |
| < 1.4   | :x:                |

## Reporting a Vulnerability

The Match Mania team takes security bugs seriously. We appreciate your efforts to responsibly disclose your findings and will make every effort to acknowledge your contributions.

### How to Report

**Please DO NOT report security vulnerabilities through public GitHub issues.**

Instead, please report them via email to: **ssfdre38@gmail.com**

Include the following information:
- Type of issue (e.g., buffer overflow, SQL injection, cross-site scripting, etc.)
- Full paths of source file(s) related to the manifestation of the issue
- The location of the affected source code (tag/branch/commit or direct URL)
- Any special configuration required to reproduce the issue
- Step-by-step instructions to reproduce the issue
- Proof-of-concept or exploit code (if possible)
- Impact of the issue, including how an attacker might exploit it

### What to Expect

After you submit a report, you can expect:

1. **Acknowledgment**: We'll acknowledge receipt of your vulnerability report within 48 hours
2. **Initial Assessment**: We'll provide an initial assessment of the report within 5 business days
3. **Status Updates**: We'll keep you informed about our progress
4. **Resolution**: We'll work to release a fix as quickly as possible, depending on complexity
5. **Credit**: With your permission, we'll credit you in the security advisory

### Response Timeline

- **Critical vulnerabilities**: Patched within 7 days
- **High severity**: Patched within 30 days
- **Medium severity**: Patched within 90 days
- **Low severity**: Patched in next regular release

## Security Measures in Match Mania

### Current Security Features

1. **No Network Communication**: App operates entirely offline, eliminating many attack vectors
2. **No Sensitive Data Storage**: No passwords, personal info, or payment data stored
3. **Minimal Permissions**: Only requests essential Android permissions
4. **Secure Random**: Uses `SecureRandom` for card shuffling (cryptographically secure)
5. **No Third-Party Services**: No external APIs or analytics that could leak data
6. **Local Data Only**: All game data stored locally in app private storage

### What We Don't Collect

Match Mania does NOT collect, transmit, or store:
- Personal identifying information
- Location data
- Contact information
- Device identifiers
- Usage analytics
- Crash reports (unless manually submitted)

## Known Limitations

### Out of Scope

The following are explicitly out of scope for security reports:

1. **Theoretical vulnerabilities**: Without proof of exploitability
2. **Social engineering**: Attacks requiring user interaction beyond normal app use
3. **Physical device access**: Vulnerabilities requiring physical access to device
4. **Rooted devices**: Issues only present on rooted/jailbroken devices
5. **Outdated OS versions**: Security issues in Android OS itself
6. **Game balance**: AI behavior, card distribution, or gameplay fairness (use [game balance template](https://github.com/ssfdre38/match-mania/issues/new?template=game_balance.md) instead)

### Third-Party Dependencies

Match Mania uses minimal third-party dependencies. Security vulnerabilities in:
- Android SDK components
- Gradle build system
- Java standard library

Should be reported to the respective upstream projects.

## Best Practices for Users

To keep your Match Mania installation secure:

1. **Download from Official Sources**: Only download APKs from [GitHub Releases](https://github.com/ssfdre38/match-mania/releases)
2. **Verify Signatures**: Check that APK signatures match official releases
3. **Keep Updated**: Install updates promptly when released
4. **Report Issues**: If something seems wrong, report it
5. **Check Permissions**: Verify app requests only necessary permissions

## Disclosure Policy

When we receive a security bug report, we will:

1. Confirm the problem and determine affected versions
2. Audit code to find any similar problems
3. Prepare fixes for all supported releases
4. Release patches as quickly as possible

### Public Disclosure

- We aim to disclose vulnerabilities within 90 days of receiving the report
- We'll coordinate disclosure timing with the reporter
- Security advisories will be published on GitHub
- Fixes will be included in release notes (unless disclosure timing requires delay)

## Security Acknowledgments

We'd like to thank the following individuals for responsibly disclosing security issues:

*(No reports received yet)*

If you've reported a security issue and would like to be acknowledged (or not), please let us know in your report.

## Security Updates

Security updates are released as:
- **Patch releases** for supported versions (e.g., 2.1.2)
- **Hotfix releases** for critical vulnerabilities
- Documented in [CHANGELOG.md](CHANGELOG.md) and release notes

Subscribe to:
- [GitHub Releases](https://github.com/ssfdre38/match-mania/releases) for notifications
- [Security Advisories](https://github.com/ssfdre38/match-mania/security/advisories) for security-specific updates

## Questions?

Have questions about this security policy?
- Email: ssfdre38@gmail.com
- GitHub Discussions: https://github.com/ssfdre38/match-mania/discussions

## Legal

By reporting security vulnerabilities to us, you agree that:
- You will not publicly disclose the vulnerability without our consent
- You will not exploit the vulnerability beyond what is necessary to demonstrate it
- You will not violate any laws or regulations in your research

We commit to:
- Not pursue legal action against researchers who follow this policy
- Work with you to understand and resolve the issue quickly
- Recognize your contribution (if desired)

---

**Thank you for helping keep Match Mania and its users safe!** 🔒

**Last Updated**: January 2025
