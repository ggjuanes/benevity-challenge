module.exports = {
    extends: ['@commitlint/config-conventional'],
    rules: {
        "type-enum": () => [2, "always", ["feat", "fix", "docs", "chore", "test", "refactor"]],
        "scope-case": () => [2, "always", "upper-case"],
        "subject-case": () => [0],
    }
}