#!/bin/bash
set -e

PROJECT_ROOT="$(dirname "$(realpath "$0")")/.."
SRC_DIR="$PROJECT_ROOT/src"
OUT_DIR="$PROJECT_ROOT/bin"

echo "Очистка старой сборки..."
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"

echo "Поиск Java-файлов..."
find "$SRC_DIR" -name "*.java" > "$PROJECT_ROOT/sources.txt"

echo "Компиляция..."
javac -d "$OUT_DIR" @"$PROJECT_ROOT/sources.txt"

echo "=== Компиляция завершена ==="
echo "Запуск тестов..."
bash "$PROJECT_ROOT/scripts/test.sh"
