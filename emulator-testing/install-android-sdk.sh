#!/bin/bash

################################################################################
# Match Mania - Android SDK Installer
# Automatically installs Android SDK on Linux, macOS, or Windows (WSL/Git Bash)
# This script makes testing completely self-contained - no manual setup needed!
################################################################################

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# SDK version info
CMDLINE_TOOLS_VERSION="11076708"  # Latest as of 2024
CMDLINE_TOOLS_BASE_URL="https://dl.google.com/android/repository"

# Print functions
print_header() {
    echo ""
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${CYAN}$1${NC}"
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo ""
}

print_info() {
    echo -e "${BLUE}ℹ${NC} $1"
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

# Detect platform
detect_platform() {
    case "$(uname -s)" in
        Linux*)
            echo "linux"
            ;;
        Darwin*)
            echo "mac"
            ;;
        MINGW*|MSYS*|CYGWIN*)
            echo "windows"
            ;;
        *)
            echo "unknown"
            ;;
    esac
}

# Get default SDK installation path for platform
get_default_sdk_path() {
    local platform=$1
    
    case "$platform" in
        linux)
            echo "$HOME/Android/Sdk"
            ;;
        mac)
            echo "$HOME/Library/Android/sdk"
            ;;
        windows)
            if [ -n "$LOCALAPPDATA" ]; then
                echo "$LOCALAPPDATA/Android/Sdk"
            else
                echo "$HOME/AppData/Local/Android/Sdk"
            fi
            ;;
        *)
            echo "$HOME/android-sdk"
            ;;
    esac
}

# Get command-line tools download URL
get_cmdline_tools_url() {
    local platform=$1
    
    case "$platform" in
        linux)
            echo "${CMDLINE_TOOLS_BASE_URL}/commandlinetools-linux-${CMDLINE_TOOLS_VERSION}_latest.zip"
            ;;
        mac)
            echo "${CMDLINE_TOOLS_BASE_URL}/commandlinetools-mac-${CMDLINE_TOOLS_VERSION}_latest.zip"
            ;;
        windows)
            echo "${CMDLINE_TOOLS_BASE_URL}/commandlinetools-win-${CMDLINE_TOOLS_VERSION}_latest.zip"
            ;;
        *)
            return 1
            ;;
    esac
}

# Check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check system requirements
check_requirements() {
    local platform=$1
    local missing_tools=()
    
    print_info "Checking system requirements..."
    
    # Check for required tools
    if ! command_exists curl && ! command_exists wget; then
        missing_tools+=("curl or wget")
    fi
    
    if ! command_exists unzip; then
        missing_tools+=("unzip")
    fi
    
    if ! command_exists java; then
        print_warning "Java (JDK) not found"
        print_info "Android SDK requires Java Development Kit (JDK) 8 or higher"
        missing_tools+=("java/jdk")
    fi
    
    if [ ${#missing_tools[@]} -gt 0 ]; then
        print_error "Missing required tools: ${missing_tools[*]}"
        echo ""
        echo "Please install the missing tools:"
        echo ""
        
        case "$platform" in
            linux)
                echo "  Ubuntu/Debian:"
                echo "    sudo apt update"
                echo "    sudo apt install -y curl unzip openjdk-11-jdk"
                echo ""
                echo "  Fedora:"
                echo "    sudo dnf install -y curl unzip java-11-openjdk-devel"
                echo ""
                echo "  Arch:"
                echo "    sudo pacman -S curl unzip jdk-openjdk"
                ;;
            mac)
                echo "  Using Homebrew:"
                echo "    brew install curl unzip"
                echo "    brew install --cask temurin"
                ;;
            windows)
                echo "  Using Git Bash/WSL:"
                echo "    Install from: https://www.oracle.com/java/technologies/downloads/"
                echo "    Install unzip: apt install unzip (WSL) or use Windows built-in"
                ;;
        esac
        echo ""
        return 1
    fi
    
    print_success "All required tools are installed"
    return 0
}

# Download file with progress
download_file() {
    local url=$1
    local output=$2
    
    if command_exists curl; then
        curl -L --progress-bar "$url" -o "$output"
    elif command_exists wget; then
        wget --show-progress -O "$output" "$url"
    else
        print_error "Neither curl nor wget found!"
        return 1
    fi
}

# Install Android SDK
install_sdk() {
    local platform=$1
    local install_path=$2
    local download_url=$(get_cmdline_tools_url "$platform")
    
    print_header "Installing Android SDK"
    
    print_info "Installation path: $install_path"
    print_info "Download URL: $download_url"
    
    # Create installation directory
    print_info "Creating installation directory..."
    mkdir -p "$install_path"
    
    # Download command-line tools
    local temp_zip="/tmp/android-cmdline-tools.zip"
    
    print_info "Downloading Android Command Line Tools (~150 MB)..."
    if ! download_file "$download_url" "$temp_zip"; then
        print_error "Failed to download Android SDK tools"
        return 1
    fi
    print_success "Download complete"
    
    # Extract command-line tools
    print_info "Extracting command-line tools..."
    local temp_extract="/tmp/cmdline-tools-extract"
    mkdir -p "$temp_extract"
    
    if ! unzip -q "$temp_zip" -d "$temp_extract"; then
        print_error "Failed to extract command-line tools"
        rm -f "$temp_zip"
        return 1
    fi
    
    # Move to correct location (cmdline-tools/latest)
    print_info "Installing to $install_path..."
    mkdir -p "$install_path/cmdline-tools"
    
    if [ -d "$temp_extract/cmdline-tools" ]; then
        mv "$temp_extract/cmdline-tools" "$install_path/cmdline-tools/latest"
    else
        # Some versions extract differently
        mv "$temp_extract"/* "$install_path/cmdline-tools/latest"
    fi
    
    # Cleanup
    rm -f "$temp_zip"
    rm -rf "$temp_extract"
    
    print_success "Android SDK command-line tools installed"
    
    # Set up environment
    export ANDROID_SDK_ROOT="$install_path"
    export ANDROID_HOME="$install_path"
    export PATH="$PATH:$install_path/cmdline-tools/latest/bin:$install_path/platform-tools:$install_path/emulator"
    
    print_success "SDK installation complete!"
    
    return 0
}

# Install essential SDK components
install_sdk_components() {
    print_header "Installing Essential SDK Components"
    
    print_info "This will install:"
    echo "  • Platform Tools (adb, fastboot)"
    echo "  • Build Tools (latest)"
    echo "  • Emulator"
    echo ""
    
    # Accept licenses
    print_info "Accepting SDK licenses..."
    yes | sdkmanager --licenses 2>/dev/null || true
    
    # Install components
    print_info "Installing platform-tools..."
    sdkmanager "platform-tools" 2>&1 | grep -v "Info:" || true
    
    print_info "Installing build-tools..."
    sdkmanager "build-tools;34.0.0" 2>&1 | grep -v "Info:" || true
    
    print_info "Installing emulator..."
    sdkmanager "emulator" 2>&1 | grep -v "Info:" || true
    
    print_success "Essential components installed"
}

# Add to shell profile
add_to_profile() {
    local sdk_path=$1
    local platform=$2
    
    print_header "Setting Up Environment Variables"
    
    # Detect shell
    local shell_profile=""
    if [ -n "$BASH_VERSION" ]; then
        if [ -f "$HOME/.bashrc" ]; then
            shell_profile="$HOME/.bashrc"
        elif [ -f "$HOME/.bash_profile" ]; then
            shell_profile="$HOME/.bash_profile"
        fi
    elif [ -n "$ZSH_VERSION" ]; then
        shell_profile="$HOME/.zshrc"
    fi
    
    if [ -z "$shell_profile" ]; then
        print_warning "Could not detect shell profile"
        print_info "Please add these lines to your shell profile manually:"
    else
        print_info "Adding environment variables to $shell_profile"
        
        # Check if already added
        if grep -q "ANDROID_SDK_ROOT" "$shell_profile" 2>/dev/null; then
            print_warning "Android SDK variables already in profile"
        else
            cat >> "$shell_profile" << EOF

# Android SDK (added by Match Mania test setup)
export ANDROID_SDK_ROOT="$sdk_path"
export ANDROID_HOME="\$ANDROID_SDK_ROOT"
export PATH="\$PATH:\$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:\$ANDROID_SDK_ROOT/platform-tools:\$ANDROID_SDK_ROOT/emulator"
EOF
            print_success "Environment variables added to $shell_profile"
        fi
    fi
    
    echo ""
    echo -e "${CYAN}Add these to your shell profile:${NC}"
    echo ""
    echo "  export ANDROID_SDK_ROOT=\"$sdk_path\""
    echo "  export ANDROID_HOME=\"\$ANDROID_SDK_ROOT\""
    echo "  export PATH=\"\$PATH:\$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:\$ANDROID_SDK_ROOT/platform-tools:\$ANDROID_SDK_ROOT/emulator\""
    echo ""
}

# Main installation flow
main() {
    print_header "Match Mania - Android SDK Installer"
    
    # Detect platform
    local platform=$(detect_platform)
    print_info "Detected platform: $platform"
    
    if [ "$platform" = "unknown" ]; then
        print_error "Unsupported platform: $(uname -s)"
        print_info "This script supports Linux, macOS, and Windows (WSL/Git Bash)"
        exit 1
    fi
    
    # Check requirements
    if ! check_requirements "$platform"; then
        exit 1
    fi
    
    # Get installation path
    local default_path=$(get_default_sdk_path "$platform")
    
    echo ""
    print_info "Android SDK will be installed to: $default_path"
    read -p "Use this location? (Y/n): " -n 1 -r
    echo ""
    
    local install_path="$default_path"
    if [[ $REPLY =~ ^[Nn]$ ]]; then
        read -p "Enter custom installation path: " install_path
        install_path="${install_path/#\~/$HOME}"  # Expand ~
    fi
    
    # Check if already exists
    if [ -d "$install_path/cmdline-tools" ]; then
        print_warning "Android SDK already exists at $install_path"
        read -p "Reinstall? (y/N): " -n 1 -r
        echo ""
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            print_info "Skipping installation"
            export ANDROID_SDK_ROOT="$install_path"
            export ANDROID_HOME="$install_path"
            export PATH="$PATH:$install_path/cmdline-tools/latest/bin:$install_path/platform-tools:$install_path/emulator"
            print_success "Using existing SDK at $install_path"
            
            # Still offer to install components
            echo ""
            read -p "Install/update essential components? (Y/n): " -n 1 -r
            echo ""
            if [[ ! $REPLY =~ ^[Nn]$ ]]; then
                install_sdk_components
            fi
            
            add_to_profile "$install_path" "$platform"
            exit 0
        fi
    fi
    
    # Confirm installation
    echo ""
    print_warning "This will download and install Android SDK (~150 MB + components)"
    read -p "Continue? (y/N): " -n 1 -r
    echo ""
    
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        print_error "Installation cancelled"
        exit 1
    fi
    
    # Install SDK
    if ! install_sdk "$platform" "$install_path"; then
        print_error "SDK installation failed"
        exit 1
    fi
    
    # Install components
    install_sdk_components
    
    # Add to profile
    add_to_profile "$install_path" "$platform"
    
    # Final message
    print_header "Installation Complete! 🎉"
    
    print_success "Android SDK installed successfully!"
    echo ""
    print_info "Next steps:"
    echo "  1. Restart your terminal or run: source ~/.bashrc (or ~/.zshrc)"
    echo "  2. Install system images: ./setup-system-images.sh"
    echo "  3. Run tests: ./run-comprehensive-tests.sh"
    echo ""
    
    print_warning "IMPORTANT: You must restart your terminal or reload your shell profile!"
    echo ""
}

# Run main
main "$@"
