# Git Commit Conventions — HSF302 Project

Quy ước commit message theo chuẩn **Conventional Commits 1.0.0** — chuẩn industry-wide được Angular, Vue, NestJS, GitHub CLI và nhiều project lớn dùng.

📚 **Tham khảo:** https://www.conventionalcommits.org/

---

## Mục lục

1. [Format tổng quát](#1-format-tổng-quát)
2. [Danh sách Type](#2-danh-sách-type)
3. [Quy tắc viết Subject](#3-quy-tắc-viết-subject)
4. [Template cho từng loại commit](#4-template-cho-từng-loại-commit)
5. [Ví dụ cụ thể cho HSF302](#5-ví-dụ-cụ-thể-cho-hsf302)
6. [Quy ước Branch name](#6-quy-ước-branch-name)
7. [Quy ước Pull Request](#7-quy-ước-pull-request)
8. [Setup tool tự động](#8-setup-tool-tự-động)
9. [Git aliases hữu ích](#9-git-aliases-hữu-ích)
10. [Cheat Sheet 1 trang](#10-cheat-sheet-1-trang)

---

## 1. Format tổng quát

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Cấu trúc chi tiết
| Phần | Bắt buộc | Mô tả |
|------|----------|-------|
| **type** | ✅ | Loại commit (feat, fix, docs…). Xem [danh sách Type](#2-danh-sách-type) |
| **scope** | ❌ | Phần code bị ảnh hưởng (entity, service, ci…) |
| **subject** | ✅ | Tóm tắt thay đổi (≤ 50 ký tự, imperative mood) |
| **body** | ❌ | Giải thích what và why, không phải how. Wrap 72 char |
| **footer** | ❌ | Reference issue, breaking change |

### Ví dụ đầy đủ

```
feat(repository): add findByEmail method to StudentRepository

Bổ sung method `Optional<Student> findByEmail(String email)` để
support feature login bằng email. Method dùng derived query của
Spring Data JPA, không cần viết JPQL.

Closes #42
```

---

## 2. Danh sách Type

| Type | Khi nào dùng | Ví dụ |
|------|-------------|-------|
| **feat** | Thêm tính năng mới cho user | `feat: add CRUD operations for Book entity` |
| **fix** | Sửa bug | `fix: prevent NullPointerException in StudentDAO.update()` |
| **docs** | Chỉ sửa documentation | `docs: update README with H2 setup instructions` |
| **style** | Format code (whitespace, semicolon, indent…) — không đổi logic | `style: reformat StudentServiceImpl with google-java-format` |
| **refactor** | Đổi cấu trúc code, KHÔNG thêm feature, KHÔNG sửa bug | `refactor: extract validateStudent into separate validator class` |
| **perf** | Cải thiện performance | `perf: add @EntityGraph to avoid N+1 query in findAll()` |
| **test** | Thêm/sửa test | `test: add unit tests for StudentServiceImpl validation` |
| **chore** | Thay đổi không ảnh hưởng src/test (build, deps…) | `chore: bump Spring Boot from 3.5.0 to 3.5.14` |
| **build** | Sửa build system, Maven, dependency | `build: add h2database test dependency to pom.xml` |
| **ci** | Sửa CI config (GitHub Actions…) | `ci: add Java 21 matrix to classroom.yml` |
| **revert** | Revert commit trước | `revert: feat(service): add caching to getById()` |

> 💡 **Nguyên tắc:** Khi không chắc, dùng `chore`. Nhưng cố gắng dùng đúng type.

---

## 3. Quy tắc viết Subject

### ✅ DO — Nên làm

| Quy tắc | Ví dụ tốt |
|--------|----------|
| Imperative mood ("add", "fix", "update" — như command) | `add login validation` |
| Viết thường (lower case) | `fix typo in error message` |
| Không có dấu chấm cuối | `update README` |
| ≤ 50 ký tự | `fix StudentDAO update method` |
| Trả lời câu: "If applied, this commit will _____" | _____ = `add unit tests` |

### ❌ DON'T — Không nên

| Sai | Đúng |
|-----|------|
| `Added new feature` (past tense) | `add new feature` |
| `Fixes bug in service` (3rd person) | `fix bug in service` |
| `update.` (có chấm) | `update` |
| `Update README.md with very long description…` (>50 ký tự) | `docs: update README with H2 setup` |
| `stuff / wip / fix` (vô nghĩa) | `fix: handle null email in StudentService.create()` |
| `Update files` (mơ hồ) | `refactor: extract DTO mapper from controller` |

---

## 4. Template cho từng loại commit

### 4.1 feat — Thêm tính năng

```
feat(<scope>): <add what feature>

<Why this feature is needed. What problem it solves.>

<Optional: Implementation notes.>

Closes #<issue-number>
```

**Ví dụ:**

```
feat(service): add searchByName method for Student

User cần tìm student theo tên (case-insensitive, partial match).
Method dùng JPQL với LOWER() + LIKE wildcard.

Closes #15
```

### 4.2 fix — Sửa bug

```
fix(<scope>): <what bug is fixed>

<Mô tả bug. Steps to reproduce nếu phức tạp.>
<Root cause.>
<Solution.>

Fixes #<issue-number>
```

**Ví dụ:**

```
fix(dao): prevent transaction leak in StudentDAO.update()

Khi update() throw exception giữa chừng, EntityManager chưa được
close → connection leak sau ~100 request.

Root cause: thiếu try-finally wrap em.close().
Fix: di chuyển em.close() vào finally block.

Fixes #23
```

### 4.3 docs — Documentation

```
docs(<scope>): <what doc is changed>

<Why this update is needed.>
```

**Ví dụ:**


```
docs(readme): add H2 in-memory setup instructions

Sinh viên báo confuse về persistence-unit "hsf302-chapter1-test".
Bổ sung section "Test Database" giải thích H2 tự config qua
persistence.xml, không cần cài MSSQL.
```

### 4.4 refactor — Đổi cấu trúc, không đổi behavior

```
refactor(<scope>): <what is refactored>

<Why refactor is needed (code smell, technical debt…).>
<What pattern is applied.>
```

**Ví dụ:**

```
refactor(dao): change StudentDAO from static EMF to instance field

Static EntityManagerFactory không cho phép inject H2 EMF khi test.
Refactor sang instance field + 2 constructor (default + injectable).

Behavior không đổi với production code.
```

### 4.5 test — Thêm/sửa test

```
test(<scope>): <what is tested>
```

**Ví dụ:**

```
test(service): add 17 unit tests for StudentServiceImpl

Cover validation cho null/blank fields + invalid id.
Dùng Mockito để mock StudentRepository → không cần DB.
```

### 4.6 chore / build / ci — Maintenance

```
<chore|build|ci>(<scope>): <what is changed>
```

**Ví dụ:**

```
build(deps): bump Hibernate from 6.5.1 to 6.5.2

Spring Boot 3.5.14 yêu cầu Hibernate ≥ 6.5.2.
Auto-update qua spring-boot-starter-parent.
```

```
ci(github): add autograding workflow for Chapter 1 exercise

Trigger trên push/PR. Chạy 6 test class tương ứng 6 TODO.
Tổng điểm 100 (15+10+25+10+25+15).
```

### 4.7 ⚠️ BREAKING CHANGE — Thay đổi không tương thích ngược

Khi commit thay đổi API public theo cách không backward-compatible, PHẢI có:

- `!` sau type/scope, **HOẶC**
- Footer `BREAKING CHANGE: <description>`

**Ví dụ:**

```
feat(repository)!: change findById to return Optional

BREAKING CHANGE: StudentRepository.findById() trả về Optional<Student>
thay vì Student (có thể null). Tất cả caller phải update:

  // Before
  Student s = repo.findById(1L);
  if (s != null) { ... }

  // After
  repo.findById(1L).ifPresent(s -> { ... });
5. Ví dụ cụ thể cho HSF302
Khi học / làm SlideNotes

docs(slides): add Chapter 01 JPA Mapping notes
docs(slides): fix typo in @OneToMany section of Chapter 04
docs(index): add JavaFX roadmap to INDEX.md
docs: reorganize SlideNotes into per-chapter files
Khi setup template assignment

feat(template): create chapter1-exercise1 student template
feat(template): add H2 in-memory persistence-unit for testing
build(pom): add h2database test scope dependency
ci(classroom): add autograding workflow for GitHub Classroom
docs(template): add README + INSTRUCTIONS for students
test(entity): add 9 unit tests for StudentEntity annotations
test(dao): add 10 unit tests for StudentDAO CRUD with H2
test(service): add 17 unit tests for StudentServiceImpl validation
Khi sinh viên làm bài (theo từng TODO)

# TODO #1 — Entity
feat(entity): implement Student entity with JPA annotations
feat(entity): implement Book entity with @ManyToOne relationship

# TODO #3 — DAO
feat(dao): implement save() method in StudentDAO
feat(dao): implement findById() and findAll() in StudentDAO
feat(dao): implement update() and delete() with transaction handling

# TODO #4 — Repository
feat(repository): implement StudentRepositoryImpl as DAO adapter

# TODO #5 — Service
feat(service): implement create/getById/getAll/update/deleteById
feat(service): add input validation for Student fields

# TODO #6 — Main
feat(app): implement CRUD demo in main method
Khi fix bug trong lúc làm bài

fix(dao): correct exception handling in StudentDAO.save()
fix(service): use exact error message "First name must not be blank"
fix(entity): add missing @JoinColumn for Book.student relation
fix(test): close EntityManager in StudentDAOTest @AfterAll
Khi refactor / cleanup

refactor(service): extract validateStudent helper method
refactor(dao): use try-with-resources for EntityManager
style: reformat all Java files with google-java-format
chore: remove unused imports across pojo package
chore(gitignore): add .DS_Store and Thumbs.db entries
Khi setup project mới (Chapter sau)

feat(chapter2): scaffold Spring Framework intro project
feat(chapter3): scaffold Spring Boot starter project
build: initialize Maven structure for Chapter 5 Spring MVC demo
docs(chapter6): add Thymeleaf integration guide
Khi nộp bài / milestone

chore: complete all TODOs for Chapter 1 Exercise 1
test: verify all 53 test methods pass locally
docs: finalize INSTRUCTIONS.md for student submission
6. Quy ước Branch name
Format

<type>/<short-description>
<type>/<issue-number>-<short-description>
Type cho branch (giống commit type)
Type	Mục đích	Ví dụ
feat/	Tính năng mới	feat/student-search-by-email
fix/	Bug fix	fix/transaction-leak-in-dao
docs/	Documentation	docs/update-readme-h2-setup
refactor/	Refactor code	refactor/extract-validator-class
test/	Thêm test	test/integration-crud-h2
chore/	Maintenance	chore/bump-spring-boot-3.5.14
hotfix/	Sửa khẩn cấp trên production	hotfix/fix-login-500-error
release/	Chuẩn bị release	release/v1.2.0
Quy tắc
✅ Dùng dấu gạch ngang (-), không gạch dưới hay space
✅ Viết thường
✅ Ngắn gọn (≤ 50 ký tự sau prefix)
❌ Tránh tên người, ngày tháng: feat/bichtra-2026-05-18-search
Ví dụ cho HSF302
Sinh viên làm bài:


git checkout -b feat/todo-1-student-entity
git checkout -b feat/todo-3-student-dao-crud
git checkout -b fix/todo-5-validation-message
Giảng viên maintain:


git checkout -b feat/add-chapter2-template
git checkout -b ci/upgrade-actions-to-v5
git checkout -b docs/student-guide-revision
7. Quy ước Pull Request
Title — giống commit subject

feat(service): add Student search by email
Body template

## Mục tiêu / Why

<Mô tả ngắn vấn đề cần giải quyết>

## Thay đổi gì / What

- Thêm method findByEmail(String) trong StudentRepository
- Update StudentService để expose qua searchByEmail()
- Thêm 3 unit test cho method mới

## Cách test

mvn test -Dtest=StudentRepositoryImplTest#findByEmail
mvn test -Dtest=StudentServiceImplTest#searchByEmail

## Screenshot (nếu có UI)

<đính kèm ảnh>

## Related issues

Closes #42
Refs #38

## Checklist

- [x] Code tuân thủ style guide
- [x] Thêm test mới (coverage > 80%)
- [x] Update documentation
- [x] Đã chạy mvn clean test local
- [ ] Đã peer review (chờ approve)
Quy tắc PR
✅ 1 PR = 1 mục đích duy nhất (đừng gom 5 feature vào 1 PR)
✅ ≤ 400 dòng diff (PR lớn hơn → tách nhỏ)
✅ Title viết theo Conventional Commits
✅ Body có "Why" + "What" + "How to test"
✅ Link đến issue nếu có
8. Setup tool tự động
8.1 Git Commit Template (built-in, không cần install)
Tạo file template:


cat > ~/.gitmessage << 'EOF'
# <type>(<scope>): <subject>      (max 50 chars)
# |<----  Preferably under 50 chars  ---->|

# Body — explain WHAT and WHY (not HOW). Wrap at 72.
# |<----   Try to limit each line to 72 characters   ---->|

# Footer
# Closes #issue-number
# BREAKING CHANGE: <description>

# Type can be:
#   feat     (new feature for the user)
#   fix      (bug fix for the user)
#   docs     (changes to documentation)
#   style    (formatting; missing semicolons; no production code change)
#   refactor (refactoring production code, no behavior change)
#   perf     (performance improvement)
#   test     (adding/correcting tests)
#   chore    (build process, auxiliary tools, libraries)
#   build    (build system, dependencies)
#   ci       (CI/CD config)
#   revert   (revert previous commit)
EOF

# Activate cho tất cả repo
git config --global commit.template ~/.gitmessage
Sau đó khi git commit (không có -m), editor mở với template sẵn.

8.2 Commitlint (kiểm tra format tự động)

# Cài Node.js trước, sau đó:
npm install --save-dev @commitlint/cli @commitlint/config-conventional

# Tạo config
echo "export default { extends: ['@commitlint/config-conventional'] };" > commitlint.config.js
8.3 Husky (git hook tự động chạy commitlint)

npm install --save-dev husky
npx husky init

# Tạo hook commit-msg
echo "npx --no-install commitlint --edit \$1" > .husky/commit-msg
chmod +x .husky/commit-msg
Bây giờ mọi commit sai format → bị reject ngay tại local trước khi push.

8.4 Commitizen (CLI tương tác chọn type)

npm install -g commitizen cz-conventional-changelog
echo '{"path":"cz-conventional-changelog"}' > ~/.czrc

# Dùng: git cz (thay vì git commit)
git cz
# → menu tương tác chọn type, scope, subject, body, footer
9. Git aliases hữu ích
Thêm vào ~/.gitconfig:


[alias]
    # Status compact
    s = status -s
    
    # Log đẹp
    l = log --oneline --decorate --graph --all
    lg = log --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit
    
    # Commit nhanh
    cm = commit -m
    
    # Amend (sửa commit cuối)
    amend = commit --amend --no-edit
    
    # Unstage
    unstage = reset HEAD --
    
    # Last commit
    last = log -1 HEAD --stat
    
    # Liệt kê branch theo thời gian
    br = branch --sort=-committerdate
    
    # Xoá branch đã merge
    cleanup = !git branch --merged | grep -v '\\*\\|main\\|master\\|develop' | xargs -n 1 git branch -d
Cách dùng:


git s                          # status ngắn
git lg                         # log đẹp
git cm "feat(dao): add save method"
git amend                      # sửa commit cuối, không đổi message
10. Cheat Sheet 1 trang

┌──────────────────────────────────────────────────────────────────┐
│           CONVENTIONAL COMMITS CHEAT SHEET                       │
├──────────────────────────────────────────────────────────────────┤
│                                                                  │
│  FORMAT:   <type>(<scope>): <subject>                            │
│                                                                  │
│  TYPES:                                                          │
│    feat      → New feature                                       │
│    fix       → Bug fix                                           │
│    docs      → Documentation only                                │
│    style     → Formatting, no logic change                       │
│    refactor  → Restructure code, no behavior change              │
│    perf      → Performance improvement                           │
│    test      → Add/modify tests                                  │
│    chore     → Maintenance, deps update                          │
│    build     → Build system, Maven, npm                          │
│    ci        → CI/CD config (GitHub Actions...)                  │
│    revert    → Revert previous commit                            │
│                                                                  │
│  RULES:                                                          │
│    ✓ Imperative mood: "add" not "added" / "adds"                 │
│    ✓ Lower case subject                                          │
│    ✓ No period at end                                            │
│    ✓ Max 50 chars in subject                                     │
│    ✓ Body wraps at 72 chars                                      │
│                                                                  │
│  BREAKING CHANGE:                                                │
│    feat(api)!: change findById return Optional                   │
│    Or in footer:                                                 │
│    BREAKING CHANGE: <description>                                │
│                                                                  │
│  EXAMPLES:                                                       │
│    feat(dao): add save method with transaction                   │
│    fix(service): use exact error message for blank name          │
│    docs(readme): add H2 setup instructions                       │
│    test(entity): add JPA annotation tests                        │
│    refactor: extract validateStudent helper                      │
│    chore: bump Spring Boot to 3.5.14                             │
│    ci(github): add autograding workflow                          │
│                                                                  │
│  BRANCH:   <type>/<short-desc>                                   │
│    feat/student-search-by-email                                  │
│    fix/transaction-leak-in-dao                                   │
│    docs/update-readme-h2-setup                                   │
│                                                                  │
└──────────────────────────────────────────────────