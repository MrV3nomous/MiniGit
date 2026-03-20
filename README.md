<div align="center">

# MiniGit
**A robust, enterprise-grade local version control system built from scratch in Java.**

[![Java](https://img.shields.io/badge/Java-17+-blue?style=for-the-badge&logo=java)](#)
[![Testing](https://img.shields.io/badge/JUnit_5-9%2F9_Passing-brightgreen?style=for-the-badge&logo=junit5)](#)
[![Architecture](https://img.shields.io/badge/Architecture-Atomic_I%2FO-orange?style=for-the-badge)](#)
[![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)](#)

*Engineered for reliability, memory safety, and cross-platform consistency.*

</div>

---

## The Engineering Behind MiniGit

MiniGit is not just a command-line wrapper; it is a from-scratch implementation of Git's internal mechanics. Built to demonstrate deep understanding of system-level programming, file I/O safety, and data structure design.

### Core Architectural Highlights
* **Atomic File Operations:** Implements StandardCopyOption.ATOMIC_MOVE to guarantee that terminal crashes or power losses mid-commit never result in corrupted repository state.
* **Algorithmic Typo Correction:** Features a custom implementation of the Levenshtein Distance Algorithm to automatically detect and correct misspelled CLI commands.
* **Deterministic Hashing:** Utilizes strict TreeMap sorting for file indexing before SHA-1 digestion, ensuring identical codebase states strictly produce identical commit hashes across all JVMs.
* **Native Memory Safety:** Implements rigorous try-with-resources blocks and explicit .end() calls on Java's Inflater/Deflater classes to prevent native zlib C-library memory leaks.
* **Cross-Platform Pathing:** Normalizes OS-specific file separators, ensuring repository indexes remain perfectly intact when moving between Linux, Termux, and Windows environments.

---

## Quick Start

Run MiniGit globally on your system as a standalone executable.

### Download & Run
Grab the latest minigit.jar from the Releases tab.

```bash
java -jar minigit.jar help
```

*Pro-tip: Alias this in your .bashrc or .zshrc to use it natively!*

```bash
alias minigit="java -jar /path/to/minigit.jar"
```

### Build from Source

```bash
git clone https://github.com/MrV3nomous/MiniGit.git
cd MiniGit
```

# Compile source files

```bash
javac -d bin $(find src -name "*.java")
```

# Package the standalone executable

```bash
echo "Main-Class: minigit.Main" > MANIFEST.MF
jar cvfm minigit.jar MANIFEST.MF -C bin .
```

---

## Testing Architecture

MiniGit is backed by a rigorous JUnit 5 Test Suite designed to scrutinize edge cases, boundary limits, and system drift. 

**Test Coverage Includes:**
- Hash determinism and SHA-1 bounds checking.
- Zlib compression round-trips and empty array handling.
- E2E system smoke tests (Init -> Add -> Commit -> Branch).
- Path-traversal security rejections.

**Run the suite locally:**

# Download the JUnit standalone console

```bash
wget https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/junit-platform-console-standalone-1.10.2.jar
```

# Compile and Execute

```bash
javac -d bin -cp junit-platform-console-standalone-1.10.2.jar $(find src test -name "*.java")
java -jar junit-platform-console-standalone-1.10.2.jar -cp bin --select-package minigit.tests
```

---

## Command Reference

| Command | Action | Description |
| :--- | :--- | :--- |
| init | Initialize | Creates an empty .mgit repository structure. |
| add <file> | Stage | Hashes and compresses file contents into the staging index. |
| commit "<msg>" | Commit | Records staged changes permanently to the timeline. |
| status | Inspect | Displays modified, staged, and untracked working tree files. |
| log | History | Traverses the parent chain to display chronological commits. |
| branch <name> | Branch | Creates a new divergent timeline pointer. |
| checkout <hash> | Restore | Replaces the working directory with the specified state. |
| merge <branch> | Merge | Executes a fast-forward merge of branches. |
| diff | Compare | Calculates line-by-line differences of unstaged modifications. |
| pack | Compress | Condenses loose objects into a highly efficient packfile. |
| clone <src> <dest> | Clone | Safely duplicates an existing repository locally. |

---

<div align="center">
  
**Developed by Soumik Halder**
*MIT License © 2026*

</div>

