#!/bin/sh

# Stoppa skriptet om något går fel
set -e

echo "--- 🚀 Startar Garage Server ---"
garage server &

echo "--- ⏳ Väntar på att Garage RPC ska svara... ---"
until nc -z localhost 3901; do
  sleep 1
done

# Vänta tills vi faktiskt kan få kontakt med CLI
until garage status > /dev/null 2>&1; do
  sleep 1
done

echo "--- 🆔 Hämtar Node ID ---"
NODE_ID=$(garage node id | grep -oE '[0-9a-f]{64}' | head -n 1 | cut -c1-16)
echo "✅ Rent Node ID: $NODE_ID"

echo "--- 🔍 Kontrollerar Layout-status ---"
# Hämta versionen och rensa bort allt utom siffror
CURRENT_VERSION=$(garage layout show | grep -i "version:" | awk '{print $NF}' | tr -cd '0-9')

if [ -z "$CURRENT_VERSION" ] || [ "$CURRENT_VERSION" = "0" ]; then
    echo "🏗️ Ny installation detekterad. Konfigurerar allt..."

    # 1. Konfigurera Layout
    garage layout assign "$NODE_ID" --capacity 10G -z dc1
    garage layout apply --version 1

    # Vänta på att layouten sätter sig innan vi skapar S3-resurser
    sleep 3

    # 2. Skapa API-nyckel
    echo "--- 🔑 Skapar admin-nyckel ---"
    garage key create my-admin-key || true
    KEY_ID=$(garage key list | grep "my-admin-key" | awk '{print $1}')
    echo "S3 Access Key ID: $KEY_ID"

    # 3. Skapa Bucket
    echo "--- 📦 Skapar bucket ---"
    garage bucket create my-bucket || true

    # 4. Koppla ihop dem
    garage bucket allow my-bucket --key "$KEY_ID" --read --write --owner || true

    echo "✅ Förstagångskonfiguration slutförd!"
else
    echo "✅ Garage är redan konfigurerat (Version: $CURRENT_VERSION). Hoppar över initiering."
    # Om du vill se ditt Key ID även vid omstart kan du köra:
    KEY_ID=$(garage key list | grep "my-admin-key" | awk '{print $1}')
    echo "S3 Access Key ID: $KEY_ID"
fi

echo "--- 📊 Statuskontroll ---"
garage status

echo "--- 🎉 Ready! ---"
wait
