#!/usr/bin/env bash
# Naveedify F-Droid Submission Helper
set -e

APP_ID="com.aistudio.naveedify.nvdkmp"
VERSION="1.0"
RECIPE_SRC="fdroid/${APP_ID}.yml"

echo "=== Naveedify F-Droid Publication Helper ==="
echo "Application ID: $APP_ID"
echo "Version: $VERSION"

if [ ! -f "$RECIPE_SRC" ]; then
    echo "Error: Recipe file $RECIPE_SRC not found!"
    exit 1
fi

echo "1. Checking git status..."
if [ -d ".git" ]; then
    echo "Creating git release tag v$VERSION..."
    git tag -a "v$VERSION" -m "Release v$VERSION for F-Droid" || true
    echo "Pushing tag to origin..."
    git push origin "v$VERSION" || true
else
    echo "Note: Not inside an active git clone. Please push this repository to GitHub/GitLab first."
fi

echo ""
echo "2. F-Droid Metadata Recipe is ready at: $RECIPE_SRC"
echo "--------------------------------------------------------"
cat "$RECIPE_SRC"
echo "--------------------------------------------------------"
echo ""
echo "3. To submit to F-Droid directly from your terminal with GitLab CLI (glab):"
echo "   glab auth login"
echo "   glab mr create --repo fdroid/fdroiddata \\"
echo "       --title \"New app: Naveedify ($APP_ID)\" \\"
echo "       --description \"Adding Naveedify, a Spotify-style FOSS music player.\" \\"
echo "       --target-branch master"
echo ""
echo "Done! Recipe and Fastlane metadata are ready in the repo."
