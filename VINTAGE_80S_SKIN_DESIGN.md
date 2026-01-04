# Vintage 80s Indian Radio - Design Specification

## Overview
Authentic recreation of 1980s All India Radio transistor sets, inspired by iconic brands like **Murphy**, **Philips**, and **National Panasonic** that were popular in Indian households.

---

## 🎨 Visual Design

### Color Palette
```
Primary Colors:
├── Wood Brown: #4A3728 (Radio body)
├── Brass Gold: #B8860B (Dial rim, knobs)
├── Cream White: #F5F5DC (Frequency display background)
├── Deep Red: #8B0000 (LED indicator when on)
└── Charcoal: #2C2C2C (Speaker grille)

Accent Colors:
├── Fabric Beige: #D4C5B9 (Speaker cloth texture)
├── Metal Silver: #C0C0C0 (Antenna, switches)
└── Yellow Glow: #FFD700 (Backlit dial numbers)
```

### Layout Structure

```
┌─────────────────────────────────────────────────┐
│  ┌─────────────────────────────────────────┐   │  ← Wood texture border
│  │                                         │   │
│  │    ┌─────────────────────────────┐     │   │
│  │    │  MURPHY RADIO               │     │   │  ← Brand nameplate
│  │    └─────────────────────────────┘     │   │
│  │                                         │   │
│  │         ┌───────────────────┐          │   │
│  │         │  FM  96.5  MHz    │          │   │  ← LED/Analog display
│  │         │  [●] Rock FM      │          │   │
│  │         └───────────────────┘          │   │
│  │                                         │   │
│  │    ┌──────────────────────────────┐   │   │
│  │    │    🔘  Frequency Dial 🔘     │   │   │  ← Rotatable dial
│  │    │       FM  88 - 108 MHz       │   │   │
│  │    │     ◀──────●──────▶          │   │   │
│  │    └──────────────────────────────┘   │   │
│  │                                         │   │
│  │    ┌─────┐              ┌─────┐        │   │
│  │    │ 🔊  │              │ ⚡  │        │   │  ← Volume & Power knobs
│  │    │VOL  │              │ PWR │        │   │
│  │    └─────┘              └─────┘        │   │
│  │                                         │   │
│  │    ┌─────────────────────────────┐    │   │
│  │    │ ░░░░░░░░░░░░░░░░░░░░░░░░░░ │    │   │  ← Speaker grille
│  │    │ ░░░░░░░ SPEAKER ░░░░░░░░░░ │    │   │
│  │    │ ░░░░░░░░░░░░░░░░░░░░░░░░░░ │    │   │
│  │    └─────────────────────────────┘    │   │
│  │                                         │   │
│  └─────────────────────────────────────────┘   │
└─────────────────────────────────────────────────┘
```

---

## 🎛️ Interactive Components

### 1. Frequency Dial
**Visual Design:**
- Large circular dial (200dp diameter on phone)
- Brass/gold rim with tick marks
- Numbers: 88, 92, 96, 100, 104, 108 MHz
- Red pointer at current frequency
- Subtle aging texture (scratches)

**Interaction:**
- **Drag gesture:** Swipe left/right to rotate dial
- **Haptic feedback:** Tiny vibration every 0.1 MHz
- **Sound effect:** Mechanical click sound
- **Rotation range:** -90° to +90° (180° total arc)
- **Station mapping:** Stations mapped to frequency positions

**State:**
```kotlin
@Composable
fun VintageFrequencyDial(
    currentFrequency: Float, // 88.0 - 108.0 MHz
    onFrequencyChange: (Float) -> Unit,
    stations: List<Station> // Mapped to frequencies
)
```

### 2. Volume Knob
**Visual Design:**
- Smaller circular knob (80dp diameter)
- Ribbed metal texture
- Dot indicator for current position
- "VOL" label below

**Interaction:**
- **Drag gesture:** Swipe in circular motion
- **Haptic feedback:** Click at each volume step
- **Range:** 0-15 (15 discrete steps)
- **Visual rotation:** 0° (min) to 270° (max)

### 3. Power Switch
**Visual Design:**
- Toggle switch (ON/OFF)
- Red LED indicator when ON
- Metallic chrome finish

**Interaction:**
- **Tap to toggle**
- **Animation:** Switch flips with spring effect
- **Sound:** Classic "click" sound
- **LED glow:** Fades in/out over 300ms

### 4. Station Display
**Visual Design:**
- Cream/yellow backlit panel
- LCD-style font (or LED segment style)
- Two lines:
  - Line 1: "FM 96.5 MHz"
  - Line 2: "[●] Rock FM Radio" (● = playing indicator)

**Dynamic Content:**
- Updates when frequency dial changes
- Shows current station name
- Pulsing red dot when playing

### 5. Speaker Grille
**Visual Design:**
- Fabric mesh texture (beige/brown)
- Subtle wave animation when playing
- "SPEAKER" text embossed

**Animation:**
- Subtle fabric movement (parallax effect)
- Audio-reactive: Slight vibration on loud sounds

---

## 🎭 Textures & Materials

### Wood Texture
- **Type:** Walnut or teak grain
- **Application:** Radio body background
- **Details:** Subtle grain lines, slight glossiness

### Metal (Brass/Chrome)
- **Type:** Brushed metal
- **Application:** Dial rims, knobs, switches
- **Details:** Radial brushing pattern, slight oxidation

### Fabric (Speaker)
- **Type:** Woven mesh
- **Application:** Speaker grille
- **Details:** Crosshatch pattern, slight dust/aging

### Aging Effects
- **Scratches:** Very subtle on metal parts
- **Dust:** Light accumulation in corners
- **Yellowing:** Slightly yellowed plastic/display
- **Wear:** Polished areas on frequently touched knobs

---

## 🎨 Typography

```kotlin
val VintageDisplayFont = FontFamily(
    Font(R.font.seven_segment, FontWeight.Normal) // LED style
)

val VintageLabelFont = FontFamily(
    Font(R.font.helvetica_condensed, FontWeight.Bold)
)

Display Text:
- Size: 24sp (frequency)
- Color: #FF4500 (LED red) or #FFD700 (backlit yellow)
- Letter spacing: 0.15em

Labels:
- Size: 12sp
- Color: #FFFFFF embossed
- All caps
```

---

## 🔊 Sound Design

### Sound Effects
1. **Dial Rotation:** Mechanical ratchet click (50ms)
2. **Volume Knob:** Softer click (30ms)
3. **Power Switch:** Satisfying "clunk" (100ms)
4. **Station Lock:** Bell-like "ding" when station found
5. **Background:** Optional subtle static when tuning

**Audio Files:**
```
res/raw/
├── vintage_dial_click.mp3
├── vintage_knob_click.mp3
├── vintage_power_on.mp3
├── vintage_power_off.mp3
└── vintage_station_lock.mp3
```

---

## 📱 Responsive Design

### Phone Portrait (Compact)
- Full screen vintage radio
- Dial: 200dp diameter
- Controls: Standard size

### Phone Landscape (Medium)
- Radio on left 60%
- Station list on right 40%

### Tablet (Expanded)
- Radio in center
- Station list on sides
- Larger dial (300dp)

---

## ✨ Animations & Transitions

### Dial Rotation
```kotlin
animateFloatAsState(
    targetValue = currentRotation,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
)
```

### Power On Sequence
1. Power switch flips (100ms)
2. LED glows (fade in 200ms)
3. Display backlight turns on (300ms)
4. Station info appears (fade in 200ms)
5. Speaker grille activates (subtle)

### Frequency Tuning
1. Dial rotates smoothly
2. Frequency numbers update
3. Static sound plays
4. When station found: "Lock" ding + haptic

---

## 🎯 Key Features

### Authentic Feel
- ✅ Feels like using a real radio from the 80s
- ✅ Physical metaphors (dials, knobs, switches)
- ✅ Tactile feedback (haptics)
- ✅ Sound effects for every interaction

### Modern Convenience
- ✅ Quick station access (tap display to search)
- ✅ Favorites accessible via long-press dial
- ✅ Digital precision with analog feel
- ✅ Smooth gestures (no lag)

### Attention to Detail
- ✅ Aging/wear effects
- ✅ Realistic lighting (shadows, highlights)
- ✅ Authentic typography
- ✅ Period-appropriate branding

---

## 🔄 Station-to-Frequency Mapping

Since we don't have real FM frequencies for internet stations, we'll create a **virtual frequency mapping**:

```kotlin
fun mapStationsToFrequencies(stations: List<Station>): Map<Float, Station> {
    val frequencies = (88.0f..108.0f step 0.2f).toList()
    return stations.shuffled()
        .take(frequencies.size)
        .mapIndexed { index, station ->
            frequencies[index] to station
        }
        .toMap()
}
```

**User Experience:**
- Rotating dial scans through stations
- Each station has a "home frequency"
- Haptic + sound when station is found
- Visual feedback on display

---

## 🚀 Implementation Priority

**Week 1: Core Components**
1. ✅ Skinning system architecture
2. ✅ VintageFrequencyDial (basic rotation)
3. ✅ VintageKnob (volume control)
4. ✅ Power switch
5. ✅ Station display

**Week 2: Polish & Effects**
1. ✅ Textures (wood, metal, fabric)
2. ✅ Sound effects
3. ✅ Haptic feedback
4. ✅ Animations
5. ✅ Responsive layouts

**Week 3: Integration**
1. ✅ Skin switching mechanism
2. ✅ Save preference
3. ✅ Smooth transitions
4. ✅ Testing on real devices
5. ✅ Final polish

---

## 📝 Notes for Implementation

- Use **Canvas API** for custom dial drawing
- **Gesture detectors** for rotation (atan2 math)
- **Coil** for loading textures efficiently
- **Material3 elevation** for depth/shadows
- **Remember previous state** on skin switch
- **Preload assets** to avoid lag

---

## 🎨 Design References

### Historical Radios (Inspiration)
1. **Murphy Radio A104** - Classic Indian radio (1980s)
2. **Philips 22RL287** - Transistor radio popular in India
3. **National Panasonic RF-2200** - Iconic design
4. **All India Radio Vividh Bharati** - Station aesthetic

### Modern Implementations
- Winamp (for skinning concept)
- Analog.fm (vintage aesthetic)
- Old Time Radio (player design)

---

This design creates an **authentic, nostalgic experience** while maintaining modern usability. The player becomes the **hero** of the app - something users will want to show off and interact with for the joy of it, not just utility.
