# COPYRIGHT & TRADEMARK COMPLIANCE REMINDER

## ALWAYS REMEMBER: Respect Copyright and Trademarks

This document serves as a permanent reminder to maintain proper copyright and trademark compliance in all development work.

---

## Core Principles

### 1. ALWAYS ATTRIBUTE Third-Party Software

When using any third-party software, tools, libraries, or services:

✅ **DO:**
- Include copyright notices for the original authors
- Reference the applicable licenses
- Provide links to license terms
- Document what components are being used
- Update COPYRIGHT.md when adding new dependencies

❌ **DON'T:**
- Use software without proper attribution
- Ignore license requirements
- Remove or modify existing copyright notices
- Claim ownership of others' work

### 2. ALWAYS RESPECT Trademarks

When mentioning trademarks (especially Android™, Google, etc.):

✅ **DO:**
- Use proper trademark symbols (™, ®)
- State "X is a trademark of Y"
- Maintain distinction between our product and trademarked products
- Include trademark notices in documentation

❌ **DON'T:**
- Imply endorsement from trademark owners
- Use trademarks in a way that causes confusion
- Modify or combine trademarks inappropriately
- Use trademarks as your own

### 3. ALWAYS INCLUDE Disclaimers

For tools and systems we create:

✅ **DO:**
- Clarify independence from other companies
- State proper use of third-party tools
- Note compliance with licenses
- Make it clear what is ours vs. third-party

❌ **DON'T:**
- Claim affiliation without permission
- Imply endorsement from tool creators
- Misrepresent relationships with companies

---

## Specific Software We Use

### Android SDK & Tools (Google LLC)

**Copyright:** © Google LLC  
**License:** Android Software Development Kit License Agreement  
**Terms:** https://developer.android.com/studio/terms

**Components:**
- Android Emulator
- Android Debug Bridge (adb)
- Android Virtual Device Manager (avdmanager)
- SDK Manager (sdkmanager)
- Platform Tools
- Build Tools
- System Images

**ALWAYS INCLUDE:**
- Copyright notice: "© Google LLC"
- Trademark notice: "Android is a trademark of Google LLC"
- Link to license terms
- Disclaimer of non-affiliation

**NEVER:**
- Claim ownership of Android SDK
- Remove Google copyright notices
- Imply endorsement by Google

---

### Android Open Source Project (AOSP)

**Copyright:** © Google LLC and The Android Open Source Project  
**License:** Apache License 2.0  
**Terms:** https://source.android.com/license

**ALWAYS INCLUDE:**
- AOSP copyright notice
- Apache 2.0 license reference
- Link to source.android.com

---

### GNU Tools (Free Software Foundation)

**Copyright:** © Free Software Foundation, Inc.  
**License:** GNU General Public License v3

**Tools:** Bash, grep, sed, awk, coreutils

**ALWAYS INCLUDE:**
- FSF copyright notice
- GPL license reference
- Acknowledge open source nature

---

### Python (Python Software Foundation)

**Copyright:** © Python Software Foundation  
**License:** Python Software Foundation License

**ALWAYS INCLUDE:**
- PSF copyright notice
- License reference
- Acknowledgment in documentation

---

## Where Copyright Info Must Appear

### 1. Main Copyright Document
**Location:** `emulator-testing/COPYRIGHT.md`  
**Update:** When adding new dependencies

### 2. Website Pages
**Location:** Testing page, footer  
**Include:** Copyright section with attributions

### 3. README Files
**Location:** Bottom of README.md files  
**Include:** Copyright section with links

### 4. Source Code (when applicable)
**Location:** Header comments in scripts  
**Include:** Brief copyright notice

---

## Checklist for New Dependencies

When adding ANY new software, library, or tool:

- [ ] Identify the copyright holder
- [ ] Determine the license type
- [ ] Add entry to COPYRIGHT.md
- [ ] Update README.md if needed
- [ ] Add attribution to website if user-facing
- [ ] Include required notices per license
- [ ] Add trademark notice if applicable
- [ ] Document in this reminder if significant

---

## License Types We Commonly Encounter

### Permissive Licenses (Generally Easy)
- **MIT License:** Requires copyright notice and license text
- **Apache 2.0:** Requires copyright notice, license text, and NOTICE file
- **BSD License:** Requires copyright notice

### Copyleft Licenses (More Restrictions)
- **GNU GPL:** Requires source code disclosure for derivatives
- **GNU LGPL:** Allows dynamic linking without disclosure
- **AGPL:** Requires source for network-accessible software

### Proprietary Licenses
- **Android SDK:** Must follow Google's terms
- **Commercial Software:** Must follow vendor terms

---

## Red Flags - When to Stop and Check

⚠️ **STOP and verify copyright compliance when:**

1. Adding a new library or tool
2. Using code from external sources
3. Integrating third-party APIs
4. Using company logos or trademarks
5. Redistributing software components
6. Creating derivative works
7. Mentioning other companies/products
8. Using SDK or development tools

---

## Common Mistakes to Avoid

❌ **Mistake 1:** "It's open source, I can do anything"
- ✅ Reality: Open source has licenses with requirements

❌ **Mistake 2:** "No copyright notice = public domain"
- ✅ Reality: Copyright exists automatically, always check

❌ **Mistake 3:** "Small project, doesn't matter"
- ✅ Reality: Size doesn't affect legal obligations

❌ **Mistake 4:** "Everyone uses it this way"
- ✅ Reality: Widespread violation doesn't make it legal

❌ **Mistake 5:** "I'll add attribution later"
- ✅ Reality: Do it immediately to avoid forgetting

---

## Quick Reference Template

When documenting new software:

```markdown
### [Software Name]

**Copyright:** © [Copyright Holder]  
**License:** [License Type]  
**Components Used:** [List what we use]  
**Website:** [Link to official site]  
**License Terms:** [Link to license]

**Attribution Required:**
- [Specific requirements from license]

**Trademark Notices:**
- [Any trademark notices needed]
```

---

## Emergency Contact

If you're unsure about copyright/trademark compliance:

1. **Check COPYRIGHT.md first**
2. **Review the specific license terms**
3. **Search for official attribution requirements**
4. **When in doubt, ask or over-attribute**

Better to over-attribute than under-attribute!

---

## Resources

### Match Mania Copyright Docs
- `emulator-testing/COPYRIGHT.md`
- Website testing page (copyright section)
- This reminder document

### External Resources
- Android SDK Terms: https://developer.android.com/studio/terms
- AOSP License: https://source.android.com/license
- GNU Licenses: https://www.gnu.org/licenses/
- OSI License List: https://opensource.org/licenses
- Choose a License: https://choosealicense.com

---

## Commitment

**We commit to:**
- ✅ Always respecting copyright holders
- ✅ Properly attributing all third-party software
- ✅ Following license requirements
- ✅ Maintaining accurate copyright documentation
- ✅ Respecting trademarks
- ✅ Being transparent about dependencies
- ✅ Updating documentation when things change

**We will never:**
- ❌ Use software without proper attribution
- ❌ Ignore license requirements
- ❌ Claim ownership of others' work
- ❌ Misuse trademarks
- ❌ Imply false affiliations

---

## Summary

**Golden Rules:**

1. **Attribution is mandatory, not optional**
2. **Read and follow license terms**
3. **Respect trademarks**
4. **Document everything**
5. **When in doubt, over-attribute**
6. **Update documentation when dependencies change**
7. **Be transparent about what we use**

**Remember:** Copyright and trademark compliance is not just legal obligation—it's respect for the creators and contributors who made their work available to us.

---

## Last Updated

**Date:** October 5, 2025  
**Status:** Active Reminder  
**Review:** Annually or when adding major dependencies

---

**This is a living document. Keep it updated as our dependencies and usage evolve.**

© 2025 Daniel Elliott. This reminder document is part of the Match Mania project and is subject to the same MIT License.
