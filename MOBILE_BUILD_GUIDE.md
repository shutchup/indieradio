# Download APK Directly on Your Phone 📱

Since you're accessing this from your mobile device, here's how to build and download the APK without needing a computer!

---

## 🎯 Method 1: GitHub Actions (Recommended - Easiest!)

### **What is this?**
GitHub will build the APK for you in the cloud (free!) and you just download it on your phone.

### **How it works:**

#### **Option A: Automatic Build (when you push code)**

1. **The APK builds automatically** every time code is pushed to this repository
2. **Go to GitHub on your phone:**
   - Open: `https://github.com/YOUR_USERNAME/indieradio/actions`
   - Find the latest green checkmark ✅
   - Click on it
   - Scroll to **Artifacts** section
   - Click **indieradio-debug-apk** to download
   - Extract the ZIP file
   - Install the APK!

#### **Option B: Manual Build (trigger anytime)**

1. **On your phone, open GitHub:**
   - Go to: `https://github.com/YOUR_USERNAME/indieradio/actions`
   - Click on **"Build APK"** workflow
   - Click **"Run workflow"** button (top right)
   - Click green **"Run workflow"** button
   - Wait 3-5 minutes for build to complete ⏳

2. **Download the APK:**
   - Refresh the page
   - Click on the completed workflow (green checkmark ✅)
   - Scroll to **Artifacts** section at the bottom
   - Click **indieradio-debug-apk** - it downloads a ZIP file
   - Extract the ZIP to get `app-debug.apk`

3. **Install:**
   - Open your phone's **Downloads** folder
   - Extract the ZIP file (use any file manager)
   - Tap `app-debug.apk`
   - Allow "Install from Unknown Sources" if prompted
   - Tap **Install**
   - Done! 🎉

---

## 🔧 Method 2: Termux (Build on Android Phone)

You can actually build the APK **directly on your Android phone** using Termux!

### **Step 1: Install Termux**

Download from F-Droid (not Google Play - it's outdated):
- https://f-droid.org/packages/com.termux/

### **Step 2: Setup Termux**

Open Termux and run these commands:

```bash
# Update packages
pkg update && pkg upgrade -y

# Install required packages (takes ~10-15 minutes)
pkg install git openjdk-17 gradle -y

# Clone the repository
cd ~
git clone https://github.com/YOUR_USERNAME/indieradio.git
cd indieradio

# Build the APK (takes 10-20 minutes first time)
./gradlew assembleDebug
```

### **Step 3: Get the APK**

```bash
# Copy APK to your phone's storage
cp app/build/outputs/apk/debug/app-debug.apk /sdcard/Download/

# Now open your Files app and install from Downloads folder!
```

### **Pros & Cons:**

✅ **Pros:**
- Build directly on your phone
- No computer needed
- Full control over build process
- Can make code changes in Termux

❌ **Cons:**
- Initial setup takes time (~30 minutes)
- Uses ~2-3 GB of storage
- First build takes 15-20 minutes
- Can be slow on older phones

---

## 🌐 Method 3: Online Build Services

### **Option A: Replit (Free Online IDE)**

1. Go to **replit.com** on your phone
2. Sign up (free)
3. Import from GitHub: `YOUR_USERNAME/indieradio`
4. Add a `.replit` file with:
   ```toml
   run = "./gradlew assembleDebug"
   ```
5. Click **Run**
6. Download APK from `app/build/outputs/apk/debug/`

### **Option B: Gitpod (Free Cloud Dev Environment)**

1. Go to: `https://gitpod.io/#https://github.com/YOUR_USERNAME/indieradio`
2. Sign in with GitHub
3. Run in terminal: `./gradlew assembleDebug`
4. Download APK from file browser

---

## 📥 Quick Comparison

| Method | Setup Time | Build Time | Difficulty | Free? |
|--------|-----------|------------|------------|-------|
| **GitHub Actions** | 0 min | 3-5 min | ⭐ Easy | ✅ Yes |
| **Termux** | 30 min | 15-20 min | ⭐⭐⭐ Medium | ✅ Yes |
| **Replit** | 5 min | 10-15 min | ⭐⭐ Easy | ✅ Yes |
| **Gitpod** | 2 min | 5-10 min | ⭐⭐ Easy | ✅ Yes (50 hrs/month) |

---

## 🎯 My Recommendation for Mobile-Only Users

**Use GitHub Actions!** Here's why:

✅ **Zero setup** - just push code
✅ **Fastest** - 3-5 minute builds
✅ **Always works** - professional build servers
✅ **No storage needed** on your phone
✅ **Automatic** - builds on every push
✅ **Download link** - easy to share with others

---

## 📱 Step-by-Step for Mobile (GitHub Actions)

### **One-Time Setup:**

The workflow is already committed! It will trigger automatically.

### **Every Time You Want to Test:**

1. **On mobile browser, go to:**
   ```
   https://github.com/YOUR_USERNAME/indieradio/actions
   ```

2. **Trigger build (if not auto-triggered):**
   - Tap "Build APK" workflow
   - Tap "Run workflow" dropdown
   - Tap green "Run workflow" button

3. **Wait 3-5 minutes** (you'll see a yellow dot 🟡 → green check ✅)

4. **Download APK:**
   - Tap on the completed build
   - Scroll to "Artifacts"
   - Tap "indieradio-debug-apk"
   - ZIP file downloads

5. **Install:**
   - Files app → Downloads → Extract ZIP
   - Tap `app-debug.apk` → Install

---

## 🚀 Pro Tip: Direct Download Link

After the first build, you can bookmark this link for quick access:

```
https://github.com/YOUR_USERNAME/indieradio/actions/workflows/build-apk.yml
```

Or even shorter, go directly to latest run:

```
https://github.com/YOUR_USERNAME/indieradio/actions
```

---

## ⚡ Super Quick Mobile Workflow

```
Push code → GitHub builds automatically
           ↓
        (3-5 minutes)
           ↓
Download APK from GitHub Actions
           ↓
Install on phone
           ↓
Test! 🎉
```

---

## 🐛 Troubleshooting

### **"Actions not enabled"**
- Go to repo Settings → Actions → Enable Actions

### **"Can't download artifact"**
- Make sure you're logged into GitHub
- Try desktop mode in browser
- Or use GitHub mobile app

### **"Installation blocked"**
- Settings → Security → Allow installation from unknown sources
- Or Settings → Apps → Chrome/Browser → Install unknown apps → Allow

### **"App keeps crashing"**
- Check GitHub Actions logs for build errors
- Make sure your phone is Android 7.0+ (API 24+)

---

## 🎉 You're All Set!

With GitHub Actions, you can now:
- ✅ Build APKs without a computer
- ✅ Download directly to your phone
- ✅ Test immediately
- ✅ Iterate quickly
- ✅ Share APKs with others

**No computer needed, ever!** 📱🚀

---

## 📞 Need Help?

If the APK builds successfully, you'll see:
- ✅ Green checkmark in Actions tab
- 📦 Artifact available for download
- 📊 Build summary with APK size

If there's an error:
- ❌ Red X in Actions tab
- 📋 Click on it to see error logs
- Check the logs and fix the issue

---

**Happy building from your phone! 🎵📻**
