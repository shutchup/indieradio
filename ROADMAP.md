# Indieradio - Development Roadmap

**Project Timeline**: 15 Weeks
**Status**: Planning Complete → Ready for Development
**Last Updated**: January 2, 2026

---

## 📅 Timeline Overview

```
Week 1-3   │ Phase 1: Foundation & MVP Audio
Week 4-5   │ Phase 2: Modern Minimal Skin
Week 6-8   │ Phase 3: Vintage 80s Radio Skin
Week 9-11  │ Phase 4: Additional Skins & Features
Week 12-14 │ Phase 5: Polish, Testing & Optimization
Week 15+   │ Phase 6: Launch & Post-Launch
```

---

## Phase 1: Foundation & MVP Audio (Weeks 1-3)

**Goal**: Working app that can play radio stations

```
┌─────────────────────────────────────────────────────────────┐
│                      PHASE 1 ROADMAP                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Week 1: Project Setup                                      │
│  ┌───────────────────────────────────────────────────────┐ │
│  │ • Create Android project (Compose + Kotlin)           │ │
│  │ • Configure Hilt dependency injection                 │ │
│  │ • Set up Gradle dependencies                          │ │
│  │ • Create project structure (packages)                 │ │
│  │ • Set up Git repository                               │ │
│  │ • Define data models (Station, SkinTheme)             │ │
│  └───────────────────────────────────────────────────────┘ │
│                                                             │
│  Week 2: Radio API Integration                              │
│  ┌───────────────────────────────────────────────────────┐ │
│  │ • Implement Radio Browser API client (Retrofit)       │ │
│  │ • Create StationRepository                            │ │
│  │ • Location detection (GPS + IP fallback)              │ │
│  │ • Station search functionality                        │ │
│  │ • Caching strategy                                    │ │
│  │ • Unit tests for API client                           │ │
│  └───────────────────────────────────────────────────────┘ │
│                                                             │
│  Week 3: Audio Playback                                     │
│  ┌───────────────────────────────────────────────────────┐ │
│  │ • Set up Media3 ExoPlayer                             │ │
│  │ • Create MediaPlaybackService (foreground)            │ │
│  │ • Implement MediaSession (notifications)              │ │
│  │ • RadioPlayerViewModel                                │ │
│  │ • Play/pause/stop functionality                       │ │
│  │ • Network interruption handling                       │ │
│  │ • Background playback support                         │ │
│  └───────────────────────────────────────────────────────┘ │
│                                                             │
└─────────────────────────────────────────────────────────────┘

📦 Deliverable: App that searches and plays radio with system controls
```

**Key Milestones**:
- ✅ Radio Browser API successfully streaming stations
- ✅ Background playback working
- ✅ Notification controls functional
- ✅ Location-based station discovery working

---

## Phase 2: Modern Minimal Skin (Weeks 4-5)

**Goal**: Complete working radio app with polished modern UI

```
┌─────────────────────────────────────────────────────────────┐
│                      PHASE 2 ROADMAP                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Week 4: Modern UI Components                               │
│  ┌───────────────────────────────────────────────────────┐ │
│  │ • Design Modern Minimal skin (Figma)                  │ │
│  │ • Implement base SkinTheme architecture               │ │
│  │ • Create ModernMinimalSkin composable                 │ │
│  │ • Modern frequency slider                             │ │
│  │ • Modern volume controls                              │ │
│  │ • Play/pause/stop buttons                             │ │
│  │ • Station info display                                │ │
│  │ • Search screen                                       │ │
│  └───────────────────────────────────────────────────────┘ │
│                                                             │
│  Week 5: Modern Features & Polish                           │
│  ┌───────────────────────────────────────────────────────┐ │
│  │ • Waveform audio visualizer                           │ │
│  │ • Station list (infinite scroll)                      │ │
│  │ • Favorites functionality (Room DB)                   │ │
│  │ • Recently played history                             │ │
│  │ • Settings screen (basic)                             │ │
│  │ • Error states & loading indicators                   │ │
│  │ • Polish animations & transitions                     │ │
│  └───────────────────────────────────────────────────────┘ │
│                                                             │
└─────────────────────────────────────────────────────────────┘

📦 Deliverable: Fully functional radio app with modern UI
```

**Key Milestones**:
- ✅ Beautiful, polished modern UI
- ✅ Waveform visualizer working smoothly
- ✅ Favorites and history persisted to database
- ✅ Search and discovery working perfectly

---

## Phase 3: Vintage 80s Indian Radio Skin (Weeks 6-8)

**Goal**: Authentic vintage radio experience

```
┌─────────────────────────────────────────────────────────────┐
│                      PHASE 3 ROADMAP                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Week 6: Vintage UI Design                                  │
│  ┌───────────────────────────────────────────────────────┐ │
│  │ • Research 80s Indian radios (Murphy, Philips)        │ │
│  │ • High-fidelity Figma designs                         │ │
│  │ • Source/create vintage assets (textures)             │ │
│  │ • Find period-appropriate fonts                       │ │
│  │ • Design frequency dial mechanism                     │ │
│  │ • Design rotary knobs (volume, tuning)                │ │
│  └───────────────────────────────────────────────────────┘ │
│                                                             │
│  Week 7: Vintage Component Development                      │
│  ┌───────────────────────────────────────────────────────┐ │
│  │ • VintageFrequencyDial (rotatable)                    │ │
│  │ • VintageKnob (rotatable + haptic feedback)           │ │
│  │ • Vintage radio body (wood/metal textures)            │ │
│  │ • Authentic on/off toggle switch                      │ │
│  │ • LED/analog station display                          │ │
│  │ • Speaker grille (subtle animation)                   │ │
│  │ • Touch gestures for rotation                         │ │
│  └───────────────────────────────────────────────────────┘ │
│                                                             │
│  Week 8: Vintage Polish & Integration                       │
│  ┌───────────────────────────────────────────────────────┐ │
│  │ • Sound effects (dial click, switch)                  │ │
│  │ • Aging effects (scratches, wear)                     │ │
│  │ • Responsive layout (different screens)               │ │
│  │ • Skin switching mechanism                            │ │
│  │ • Smooth transitions (Modern ↔ Vintage)              │ │
│  │ • Save skin preference (DataStore)                    │ │
│  └───────────────────────────────────────────────────────┘ │
│                                                             │
└─────────────────────────────────────────────────────────────┘

📦 Deliverable: Two complete skins with smooth switching
```

**Key Milestones**:
- ✅ Authentic vintage aesthetic achieved
- ✅ Rotary controls feel natural and responsive
- ✅ Smooth skin switching (no audio interruption)
- ✅ Responsive on all screen sizes

---

## Phase 4: Additional Skins & Features (Weeks 9-11)

**Goal**: More skin variety and enhanced features

```
┌─────────────────────────────────────────────────────────────┐
│                      PHASE 4 ROADMAP                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Week 9: Retro Winamp-style Skin                            │
│  ┌───────────────────────────────────────────────────────┐ │
│  │ • Design Retro skin (90s aesthetics)                  │ │
│  │ • LED spectrum analyzer visualizer                    │ │
│  │ • Digital frequency display                           │ │
│  │ • Equalizer UI (visual/functional)                    │ │
│  │ • Playlist management UI                              │ │
│  │ • Implementation & testing                            │ │
│  └───────────────────────────────────────────────────────┘ │
│                                                             │
│  Week 10: Art Deco Skin (Optional)                          │
│  ┌───────────────────────────────────────────────────────┐ │
│  │ • Design Art Deco skin (1920s-30s)                    │ │
│  │ • Ornamental UI elements                              │ │
│  │ • Brass/gold color scheme                             │ │
│  │ • Geometric patterns                                  │ │
│  │ • Implementation                                      │ │
│  └───────────────────────────────────────────────────────┘ │
│                                                             │
│  Week 11: Enhanced Features                                 │
│  ┌───────────────────────────────────────────────────────┐ │
│  │ • Sleep timer                                         │ │
│  │ • Alarm/wake to radio                                 │ │
│  │ • Station recommendations                             │ │
│  │ • Share station functionality                         │ │
│  │ • Improved search (filters, sorting)                  │ │
│  │ • Genre browsing                                      │ │
│  │ • Country/language browsing                           │ │
│  └───────────────────────────────────────────────────────┘ │
│                                                             │
└─────────────────────────────────────────────────────────────┘

📦 Deliverable: 3-4 skins + enhanced functionality
```

**Key Milestones**:
- ✅ 3-4 distinct, polished skins available
- ✅ Sleep timer working reliably
- ✅ Enhanced discovery and browsing features
- ✅ Social sharing implemented

---

## Phase 5: Polish, Testing & Optimization (Weeks 12-14)

**Goal**: Production-ready app

```
┌─────────────────────────────────────────────────────────────┐
│                      PHASE 5 ROADMAP                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Week 12: Performance Optimization                          │
│  ┌───────────────────────────────────────────────────────┐ │
│  │ • Profile app performance (CPU, memory)               │ │
│  │ • Optimize image loading (WebP, caching)              │ │
│  │ • Reduce APK size (ProGuard, resources)               │ │
│  │ • Memory leak detection & fixes                       │ │
│  │ • Battery usage optimization                          │ │
│  │ • Network usage optimization                          │ │
│  │ • Achieve smooth 60fps on all skins                   │ │
│  └───────────────────────────────────────────────────────┘ │
│                                                             │
│  Week 13: Testing & Bug Fixes                               │
│  ┌───────────────────────────────────────────────────────┐ │
│  │ • Unit tests (ViewModels, Repositories)               │ │
│  │ • UI tests (Compose testing)                          │ │
│  │ • Integration tests                                   │ │
│  │ • Manual QA (multiple devices)                        │ │
│  │ • Edge case testing (offline, permissions)            │ │
│  │ • Accessibility testing (TalkBack)                    │ │
│  │ • Bug fixes & refinements                             │ │
│  └───────────────────────────────────────────────────────┘ │
│                                                             │
│  Week 14: Release Preparation                               │
│  ┌───────────────────────────────────────────────────────┐ │
│  │ • Finalize app icon & branding                        │ │
│  │ • Create Play Store screenshots                       │ │
│  │ • Write Play Store description                        │ │
│  │ • Create promotional video/GIF                        │ │
│  │ • Set up crash reporting (Crashlytics)                │ │
│  │ • Set up analytics (privacy-focused)                  │ │
│  │ • Privacy policy & licenses                           │ │
│  │ • Beta testing (internal/closed)                      │ │
│  └───────────────────────────────────────────────────────┘ │
│                                                             │
└─────────────────────────────────────────────────────────────┘

📦 Deliverable: Production-ready APK for Play Store
```

**Key Milestones**:
- ✅ App runs smoothly on all devices (mid-range 2022+)
- ✅ Crash-free rate > 98%
- ✅ Comprehensive test coverage
- ✅ Play Store assets ready

---

## Phase 6: Launch & Post-Launch (Week 15+)

**Goal**: Release to Play Store and iterate based on feedback

```
┌─────────────────────────────────────────────────────────────┐
│                      PHASE 6 ROADMAP                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Week 15: Launch                                            │
│  ┌───────────────────────────────────────────────────────┐ │
│  │ • Submit to Google Play Store                         │ │
│  │ • Create website/landing page                         │ │
│  │ • Social media announcement                           │ │
│  │ • Post on Reddit (r/Android, r/androidapps)           │ │
│  │ • Product Hunt launch                                 │ │
│  │ • Monitor crash reports                               │ │
│  │ • Respond to user feedback                            │ │
│  └───────────────────────────────────────────────────────┘ │
│                                                             │
│  Post-Launch Roadmap (Future)                               │
│  ┌───────────────────────────────────────────────────────┐ │
│  │ • Additional skins (community-created?)               │ │
│  │ • Custom skin creator/editor                          │ │
│  │ • Tablet-optimized layouts                            │ │
│  │ • Android Auto support                                │ │
│  │ • Wear OS companion app                               │ │
│  │ • Podcast integration                                 │ │
│  │ • Local audio file playback                           │ │
│  │ • Chromecast/Google Cast support                      │ │
│  │ • User-submitted stations                             │ │
│  │ • Social features (share listening)                   │ │
│  └───────────────────────────────────────────────────────┘ │
│                                                             │
└─────────────────────────────────────────────────────────────┘

📦 Deliverable: Live app on Google Play Store
```

**Key Milestones**:
- ✅ App live on Play Store
- ✅ First 1,000 downloads
- ✅ 4.0+ star rating
- ✅ Feedback loop established

---

## 📊 Progress Tracking

### Feature Completion Tracker

| Feature | Phase | Status |
|---------|-------|--------|
| Radio streaming | 1 | 🔲 Not Started |
| Location detection | 1 | 🔲 Not Started |
| Background playback | 1 | 🔲 Not Started |
| Notification controls | 1 | 🔲 Not Started |
| Modern Minimal skin | 2 | 🔲 Not Started |
| Waveform visualizer | 2 | 🔲 Not Started |
| Favorites | 2 | 🔲 Not Started |
| History | 2 | 🔲 Not Started |
| Search | 2 | 🔲 Not Started |
| Vintage 80s skin | 3 | 🔲 Not Started |
| Rotary controls | 3 | 🔲 Not Started |
| Skin switching | 3 | 🔲 Not Started |
| Retro Winamp skin | 4 | 🔲 Not Started |
| Spectrum analyzer | 4 | 🔲 Not Started |
| Sleep timer | 4 | 🔲 Not Started |
| Art Deco skin | 4 | 🔲 Optional |
| Performance optimization | 5 | 🔲 Not Started |
| Testing suite | 5 | 🔲 Not Started |
| Play Store release | 6 | 🔲 Not Started |

**Legend**:
- 🔲 Not Started
- 🟡 In Progress
- ✅ Completed
- ⚠️ Blocked
- 🔄 Under Review

---

## 🎯 Critical Path

These features MUST be completed in order (dependencies):

```
1. Radio API Integration
   └─> 2. Audio Playback
       └─> 3. Basic UI (Modern Skin)
           └─> 4. Skin System Architecture
               └─> 5. Vintage Skin
                   └─> 6. Additional Skins
                       └─> 7. Polish & Testing
                           └─> 8. Launch
```

**Blockers to Watch**:
- Radio Browser API reliability (test early)
- ExoPlayer codec support (test on multiple devices)
- Vintage UI responsiveness (prototype early)
- Skin switching performance (profile regularly)

---

## 📈 Success Metrics by Phase

### Phase 1 Success Criteria
- [ ] Can stream at least 10 different stations
- [ ] Background playback works for 30+ minutes
- [ ] Notification controls responsive
- [ ] No crashes during basic usage

### Phase 2 Success Criteria
- [ ] Waveform visualizer runs at 60fps
- [ ] Search returns results in < 1 second
- [ ] Favorites persist across app restarts
- [ ] UI feels polished and responsive

### Phase 3 Success Criteria
- [ ] Vintage controls feel natural (user testing)
- [ ] Skin switching < 500ms transition time
- [ ] No audio interruption during skin switch
- [ ] Vintage skin works on all screen sizes

### Phase 4 Success Criteria
- [ ] At least 3 skins available and polished
- [ ] Sleep timer reliable (no missed stops)
- [ ] Recommendations are relevant
- [ ] All features tested on multiple devices

### Phase 5 Success Criteria
- [ ] App startup < 2 seconds
- [ ] APK size < 20MB
- [ ] Test coverage > 70%
- [ ] Crash-free rate > 98%
- [ ] Accessibility score > 80%

### Phase 6 Success Criteria
- [ ] 1,000+ downloads in first month
- [ ] 4.0+ star rating
- [ ] < 5% negative reviews
- [ ] Daily active users > 100

---

## 🔄 Iteration & Feedback Loops

### Weekly Check-ins
- Review progress against roadmap
- Identify blockers
- Adjust timeline if needed
- Demo latest features

### Milestone Reviews
- End of each phase: comprehensive review
- Stakeholder approval before next phase
- User testing (Phases 2, 3, 4)
- Performance benchmarking

### User Testing Schedule
- **After Phase 2**: Test modern UI with 5-10 users
- **After Phase 3**: Test vintage skin with vintage radio enthusiasts
- **After Phase 4**: Test all skins with broader audience
- **Before Phase 6**: Closed beta (50-100 users)

---

## 🚨 Risk Mitigation Schedule

### Week 2: API Reliability Test
- Test Radio Browser API uptime
- Implement caching strategy
- Set up API health monitoring

### Week 3: Cross-Device Testing Setup
- Acquire test devices (or emulators)
- Set up device farm access
- Test ExoPlayer on different Android versions

### Week 7: Performance Baseline
- Profile current app performance
- Set performance budgets
- Identify optimization opportunities

### Week 12: Security Audit
- Review permissions usage
- Test network security
- Ensure no data leaks

---

## 📅 Gantt Chart (Simplified)

```
Phase/Task                 │ W1 W2 W3 W4 W5 W6 W7 W8 W9 W10 W11 W12 W13 W14 W15
───────────────────────────┼────────────────────────────────────────────────────
Phase 1: Foundation        │ ██ ██ ██
  Project Setup            │ ██
  API Integration          │    ██
  Audio Playback           │       ██
                           │
Phase 2: Modern Skin       │          ██ ██
  UI Components            │          ██
  Features & Polish        │             ██
                           │
Phase 3: Vintage Skin      │                ██ ██ ██
  Design                   │                ██
  Components               │                   ██
  Integration              │                      ██
                           │
Phase 4: More Skins        │                         ██ ██ ██
  Retro Skin               │                         ██
  Art Deco (Optional)      │                            ██
  Features                 │                               ██
                           │
Phase 5: Polish & Test     │                                  ██ ██ ██
  Optimization             │                                  ██
  Testing                  │                                     ██
  Release Prep             │                                        ██
                           │
Phase 6: Launch            │                                           ██ →
```

---

## 🎉 Celebration Milestones

Track and celebrate these achievements:

- ✅ First successful API call
- ✅ First radio station plays
- ✅ First skin switch works
- ✅ First user favorite saved
- ✅ First external user test
- ✅ First day without crashes
- ✅ APK under 20MB
- ✅ All tests passing
- ✅ Play Store submission
- ✅ First 100 downloads
- ✅ First 1,000 downloads
- ✅ First 4-star review
- ✅ First 10,000 downloads

---

## 📝 Notes & Adjustments

### Flexibility Built In
- Phases 4-5 can be parallelized if team size allows
- Art Deco skin (Week 10) is optional - can be skipped
- Post-launch features are prioritized based on user feedback

### Timeline Assumptions
- Single developer working full-time (40 hours/week)
- For larger team, phases can overlap
- For part-time work, multiply timeline by 2-3x

### When to Revisit Roadmap
- After Phase 1 (validate timeline)
- After Phase 3 (reassess scope)
- After Phase 5 (adjust launch date)
- Monthly (check against reality)

---

**End of Roadmap**

This roadmap is a living document. Update status as features are completed and adjust timeline based on actual progress. Good luck! 🚀
