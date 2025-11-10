#!/bin/bash
set -e

ROOT_DIR="$(dirname "$(realpath "$0")")/.."
BIN_DIR="$ROOT_DIR/bin"

if [ ! -d "$BIN_DIR" ]; then
  echo "Не найден каталог bin/ — сначала выполните build.sh"
  exit 1
fi

java -cp "$BIN_DIR" app.Main
