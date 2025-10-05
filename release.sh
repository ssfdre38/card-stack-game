#!/bin/bash

################################################################################
# Match Mania Release Script
# Automates building and deploying new versions to website and GitHub
################################################################################

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
PROJECT_DIR="/home/ubuntu/match-mania"
WEBSITE_DIR="/var/www/matchmaina.ssfdre38.xyz/html"
WIKI_REPO="/home/ubuntu/match-mania-wiki"
BUILD_GRADLE="$PROJECT_DIR/app/build.gradle"
CHANGELOG_FILE="$PROJECT_DIR/CHANGELOG.md"

# Function to print colored messages
print_header() {
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${GREEN}$1${NC}"
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ ${NC}$1"
}

print_success() {
    echo -e "${GREEN}✓${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}⚠${NC} $1"
}

print_error() {
    echo -e "${RED}✗${NC} $1"
}

# Function to get current version from build.gradle
get_current_version() {
    local version_name=$(grep 'versionName' "$BUILD_GRADLE" | sed 's/.*versionName "\(.*\)"/\1/')
    local version_code=$(grep 'versionCode' "$BUILD_GRADLE" | head -1 | sed 's/.*versionCode \(.*\)/\1/')
    echo "$version_name|$version_code"
}

# Function to update version in build.gradle
update_version() {
    local new_version=$1
    local new_code=$2
    
    sed -i "s/versionCode [0-9]*/versionCode $new_code/" "$BUILD_GRADLE"
    sed -i "s/versionName \"[^\"]*\"/versionName \"$new_version\"/" "$BUILD_GRADLE"
    
    print_success "Updated build.gradle: v$new_version (code: $new_code)"
}

# Function to extract changelog for specific version
extract_changelog() {
    local version=$1
    local in_section=0
    local changelog=""
    
    while IFS= read -r line; do
        if [[ $line == "## [$version]"* ]] || [[ $line == "## $version"* ]] || [[ $line == "## v$version"* ]]; then
            in_section=1
            continue
        fi
        if [[ $in_section -eq 1 ]]; then
            if [[ $line == "## "* ]]; then
                break
            fi
            changelog+="$line"$'\n'
        fi
    done < "$CHANGELOG_FILE"
    
    echo "$changelog"
}

# Function to update website API JSON
update_website_api() {
    local version=$1
    local apk_size=$2
    local changelog=$3
    local release_date=$(date +%Y-%m-%d)
    
    # Get version code from build.gradle
    local version_code=$(grep 'versionCode' "$BUILD_GRADLE" | head -1 | sed 's/.*versionCode \(.*\)/\1/' | tr -d ' ')
    
    # Escape changelog for JSON
    local changelog_json=$(echo "$changelog" | python3 -c 'import json,sys; print(json.dumps(sys.stdin.read().strip()))')
    
    # Format file size
    local file_size="${apk_size}.0"
    
    sudo bash -c "cat > $WEBSITE_DIR/api/latest-version.json" << EOF
{
  "version": "v$version",
  "name": "Match Mania v$version",
  "changelog": $changelog_json,
  "download_url": "https://matchmaina.ssfdre38.xyz/downloads/MatchMania-release-v$version.apk",
  "github_url": "https://github.com/ssfdre38/match-mania/releases/tag/v$version",
  "website_url": "https://matchmaina.ssfdre38.xyz",
  "versionCode": $version_code,
  "releaseDate": "$release_date",
  "fileSize": "${file_size} MB",
  "minAndroidVersion": "7.0",
  "minApiLevel": 24
}
EOF
    
    # Set proper permissions
    sudo chown www-data:www-data "$WEBSITE_DIR/api/latest-version.json"
    sudo chmod 644 "$WEBSITE_DIR/api/latest-version.json"
    
    print_success "Updated website API JSON"
}

# Function to update website HTML
update_website_html() {
    local old_version=$1
    local new_version=$2
    
    # Update index.html
    sudo sed -i "s/v$old_version/v$new_version/g" "$WEBSITE_DIR/index.html"
    
    print_success "Updated website HTML"
}

# Function to update wiki files
update_wiki() {
    local version=$1
    local wiki_dir=$2
    
    # Update Home.md
    sed -i "s/Current Version.*: v[0-9.]*/Current Version: v$version/" "$wiki_dir/Home.md"
    sed -i "s/\*\*Current Version\*\*.*: v[0-9.]*/\*\*Current Version**: v$version/" "$wiki_dir/Home.md"
    
    # Update Getting-Started.md
    sed -i "s/v[0-9.]*\\.apk/v$version.apk/g" "$wiki_dir/Getting-Started.md"
    sed -i "s/version [0-9.]*/version $version/g" "$wiki_dir/Getting-Started.md"
    
    # Update FAQ.md
    sed -i "s/version is \*\*v[0-9.]*/version is **v$version/" "$wiki_dir/FAQ.md"
    sed -i "s/Current Version\*\*.*: v[0-9.]*/Current Version**: v$version/" "$wiki_dir/FAQ.md"
    
    print_success "Updated wiki files in $wiki_dir"
}

# Function to regenerate website wiki HTML
regenerate_website_wiki() {
    print_info "Regenerating website wiki HTML..."
    
    if [ -f "/tmp/convert_wiki.py" ]; then
        sudo python3 /tmp/convert_wiki.py
        sudo python3 /tmp/fix_tablet_layout.py 2>/dev/null || true
        sudo mv "$WEBSITE_DIR/wiki/Home.html" "$WEBSITE_DIR/wiki/index.html" 2>/dev/null || true
        print_success "Website wiki HTML regenerated"
    else
        print_warning "Wiki converter not found, skipping HTML regeneration"
    fi
}

# Function to build APK
build_apk() {
    print_info "Building release APK..."
    cd "$PROJECT_DIR"
    ./gradlew clean assembleRelease --no-daemon > /tmp/gradle_build.log 2>&1
    
    if [ $? -eq 0 ]; then
        print_success "APK built successfully"
        return 0
    else
        print_error "Build failed! Check /tmp/gradle_build.log"
        tail -50 /tmp/gradle_build.log
        return 1
    fi
}

# Function to deploy to website
deploy_to_website() {
    local version=$1
    local apk_path=$2
    
    print_info "Deploying to website..."
    
    # Copy APK
    sudo cp "$apk_path" "$WEBSITE_DIR/downloads/MatchMania-release-v$version.apk"
    sudo chmod 644 "$WEBSITE_DIR/downloads/MatchMania-release-v$version.apk"
    
    # Get APK size in MB
    local apk_size=$(du -m "$apk_path" | cut -f1)
    apk_size=$(echo "scale=1; $apk_size / 1" | bc)
    
    print_success "APK deployed to website (${apk_size}MB)"
    echo "$apk_size"
}

# Function to create GitHub release
create_github_release() {
    local version=$1
    local apk_path=$2
    local changelog=$3
    
    print_info "Creating GitHub release..."
    
    cd "$PROJECT_DIR"
    
    # Format changelog for GitHub release
    local release_notes="## What's New in v$version

$changelog

---

### 📥 Downloads

**Primary (Recommended):** Download from the official website for the latest version with automatic updates:
- **🌐 [Download from Website](https://matchmaina.ssfdre38.xyz#downloads)** ← Recommended
- Includes automatic OTA update system
- Direct download from our servers

**Alternative:** Download from GitHub (fallback):
- Download the APK from the Assets section below
- Manual updates only

---

### 📱 Installation

1. Download the APK file (website recommended)
2. Enable 'Install from Unknown Sources' in your Android settings
3. Install the APK
4. Enjoy Match Mania!

### 🔄 Automatic Updates

When you download from the website, the app will automatically check for updates and download them in the background. You'll get a notification when an update is ready to install!

---

**Website**: https://matchmaina.ssfdre38.xyz  
**Full Changelog**: https://matchmaina.ssfdre38.xyz#changelog  
**Documentation**: https://matchmaina.ssfdre38.xyz/wiki/"
    
    gh release create "v$version" \
        --title "Match Mania v$version" \
        --notes "$release_notes" \
        "$apk_path#MatchMania-release-v$version.apk"
    
    print_success "GitHub release created"
}

# Function to commit and push changes
commit_and_push() {
    local version=$1
    local repo_dir=$2
    local repo_name=$3
    
    cd "$repo_dir"
    
    git add -A
    
    if git diff --staged --quiet; then
        print_info "No changes to commit in $repo_name"
        return
    fi
    
    git commit -m "Release v$version

- Updated version to $version
- Updated documentation and wiki
- Updated website references
- Auto-generated by release script"
    
    if [ "$repo_name" == "main" ]; then
        git tag "v$version"
        git push origin master
        git push origin "v$version"
    else
        git push origin master
    fi
    
    print_success "Changes committed and pushed to $repo_name"
}

################################################################################
# Main Release Process
################################################################################

print_header "🚀 Match Mania Release Script"

# Check if running from correct directory
if [ ! -f "$BUILD_GRADLE" ]; then
    print_error "build.gradle not found! Run this script from the project directory"
    exit 1
fi

# Get current version
current_info=$(get_current_version)
current_version=$(echo "$current_info" | cut -d'|' -f1)
current_code=$(echo "$current_info" | cut -d'|' -f2)

echo ""
print_info "Current version: v$current_version (code: $current_code)"
echo ""

# Ask for new version
read -p "Enter new version number (e.g., 2.3.5): " new_version
read -p "Enter new version code (e.g., 23): " new_code

echo ""
print_warning "This will release v$new_version (code: $new_code)"
read -p "Continue? (y/N): " confirm

if [ "$confirm" != "y" ] && [ "$confirm" != "Y" ]; then
    print_error "Release cancelled"
    exit 1
fi

print_header "📝 Step 1: Checking Changelog"

# Check if changelog exists for this version
if [ -f "$CHANGELOG_FILE" ]; then
    changelog=$(extract_changelog "$new_version")
    if [ -z "$changelog" ]; then
        print_warning "No changelog found for v$new_version in CHANGELOG.md"
        print_info "Please add a changelog entry and try again"
        exit 1
    fi
    print_success "Changelog found for v$new_version"
    echo "$changelog"
else
    print_warning "CHANGELOG.md not found"
    changelog="Version $new_version released"
fi

print_header "📝 Step 2: Updating Version"

# Update version in build.gradle
update_version "$new_version" "$new_code"

print_header "🔨 Step 3: Building APK"

# Build APK
if ! build_apk; then
    print_error "Build failed! Aborting release"
    exit 1
fi

apk_path="$PROJECT_DIR/app/build/outputs/apk/release/app-release.apk"

if [ ! -f "$apk_path" ]; then
    print_error "APK not found at $apk_path"
    exit 1
fi

print_header "🌐 Step 4: Deploying to Website"

# Deploy to website
apk_size=$(deploy_to_website "$new_version" "$apk_path")

# Update website API JSON
update_website_api "$new_version" "$apk_size" "$changelog"

# Update website HTML
update_website_html "$current_version" "$new_version"

print_header "📚 Step 5: Updating Wikis"

# Update main repo wiki files
if [ -d "$PROJECT_DIR/wiki" ]; then
    update_wiki "$new_version" "$PROJECT_DIR/wiki"
    regenerate_website_wiki
fi

# Update GitHub wiki repo
if [ -d "$WIKI_REPO" ]; then
    update_wiki "$new_version" "$WIKI_REPO"
    commit_and_push "$new_version" "$WIKI_REPO" "wiki"
else
    print_warning "GitHub wiki repo not found at $WIKI_REPO"
fi

print_header "📦 Step 6: Creating GitHub Release"

# Commit main repo changes
commit_and_push "$new_version" "$PROJECT_DIR" "main"

# Create GitHub release
create_github_release "$new_version" "$apk_path" "$changelog"

print_header "✅ Release Complete!"

echo ""
print_success "Match Mania v$new_version has been released!"
echo ""
echo "📍 Download Locations:"
echo "   • Website: https://matchmaina.ssfdre38.xyz"
echo "   • GitHub:  https://github.com/ssfdre38/match-mania/releases/tag/v$new_version"
echo ""
echo "📚 Documentation:"
echo "   • Website Wiki: https://matchmaina.ssfdre38.xyz/wiki/"
echo "   • GitHub Wiki:  https://github.com/ssfdre38/match-mania/wiki"
echo ""
echo "🔗 API Endpoint:"
echo "   • https://matchmaina.ssfdre38.xyz/api/latest-version.json"
echo ""
print_success "All systems updated and synchronized! 🎉"
echo ""
