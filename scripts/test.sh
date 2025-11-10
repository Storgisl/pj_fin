#!/bin/bash
set -e

PROJECT_ROOT="$(dirname "$(realpath "$0")")/.."
OUT_DIR="$PROJECT_ROOT/bin"

if [ ! -d "$OUT_DIR" ]; then
  echo "bin/ не найден — сначала выполните build.sh"
  exit 1
fi

TESTS=(
    "tests.TestAuthService"
    "tests.TestWalletService"
)

for test in "${TESTS[@]}"; do
    echo "Запуск $test..."
    java -cp "$OUT_DIR" "$test"
done

echo "=== Все тесты пройдены ==="

