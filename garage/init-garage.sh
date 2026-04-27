#!/bin/sh

# Stop script if something goes wrong
set -e

echo "--- 🚀 Starting Garage Server ---"
garage server &

echo "--- ⏳ Waiting for Garage RPC to respond... ---"
until nc -z localhost 3901; do
  sleep 1
done

# Wait until we get contact with CLI
until garage status > /dev/null 2>&1; do
  sleep 1
done

echo "--- 🆔 Fetching Node ID ---"
NODE_ID=$(garage node id | grep -oE '[0-9a-f]{64}' | head -n 1 | cut -c1-16)
if [ -z "$NODE_ID" ]; then
    echo "❌ Failed to extract Node ID"
    exit 1
fi
echo "✅ Rent Node ID: $NODE_ID"

echo "--- 🔍 Controlling Layout-status ---"
CURRENT_VERSION=$(garage layout show | grep -i "version:" | awk '{print $NF}' | tr -cd '0-9')

if [ -z "$CURRENT_VERSION" ] || [ "$CURRENT_VERSION" = "0" ]; then
    echo "🏗️ New installation detected. Configuring..."

    # 1. Configure Layout
    garage layout assign "$NODE_ID" --capacity 10G -z dc1
    garage layout apply --version 1

    # Wait for layout before creating S3-resource
    sleep 3

    # 2. Create API-key
    echo "--- 🔑 Create admin-key ---"
    garage key create my-admin-key || true
    KEY_ID=$(garage key list | grep "my-admin-key" | awk '{print $1}')
    echo "S3 Access Key ID: $KEY_ID"

    # 3. Create Bucket
    echo "--- 📦 Create bucket ---"
    garage bucket create ${S3_BUCKET_NAME} || true

    # 4. Connect
    garage bucket allow ${S3_BUCKET_NAME} --key "$KEY_ID" --read --write --owner || true

    echo "✅ Fist time configuration completed!"
else
    echo "✅ Garage is already configured (Version: $CURRENT_VERSION). Hoppar över initiering."
    KEY_ID=$(garage key list | grep "my-admin-key" | awk '{print $1}')
    echo "S3 Access Key ID: $KEY_ID"
fi

echo "--- 📊 Statuscheck ---"
garage status

echo "--- 🎉 Ready! ---"
wait
