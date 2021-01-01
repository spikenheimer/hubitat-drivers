/**
 ***
 ***   Hubitat port attempt - Spike Kwaan 2020/2021
 ***	originally from https://github.com/jasonxh/SmartThings-jasonxh
 ***
 *  Copyright 2017 Jason Xia
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
    definition (name: "Leviton Decora Z-Wave Plus Dimmer", namespace: "jasonxh", author: "Jason Xia", ocfDeviceType: "oic.d.light") {
        capability "Actuator"
        capability "Configuration"
        capability "Health Check"
        capability "Indicator"
        capability "Light"
        capability "Polling"
        capability "Refresh"
        capability "Sensor"
        capability "Switch"
        capability "Switch Level"

        attribute "loadType", "enum", ["incandescent", "led", "cfl"]
        attribute "presetLevel", "number"
        attribute "minLevel", "number"
        attribute "maxLevel", "number"
        attribute "fadeOnTime", "number"
        attribute "fadeOffTime", "number"
        attribute "levelIndicatorTimeout", "number"
        attribute "firmwareVersion", "string"

        command "low"
        command "medium"
        command "high"
        command "levelUp"
        command "levelDown"

        fingerprint mfr:"001D", prod:"3201", model:"0001", deviceJoinName: "Leviton Decora Z-Wave Plus 600W Dimmer"
        fingerprint mfr:"001D", prod:"3301", model:"0001", deviceJoinName: "Leviton Decora Z-Wave Plus 1000W Dimmer"
    }

    simulator {
        // TODO - the simulator is not working yet
        status "on":  "command: 2003, payload: FF"
        status "off": "command: 2003, payload: 00"
        status "09%": "command: 2003, payload: 09"
        status "10%": "command: 2003, payload: 0A"
        status "33%": "command: 2003, payload: 21"
        status "66%": "command: 2003, payload: 42"
        status "99%": "command: 2003, payload: 63"

        reply "2001FF,delay 5000,2602": "command: 2603, payload: FF"
        reply "200100,delay 5000,2602": "command: 2603, payload: 00"
        reply "200119,delay 5000,2602": "command: 2603, payload: 19"
        reply "200132,delay 5000,2602": "command: 2603, payload: 32"
        reply "20014B,delay 5000,2602": "command: 2603, payload: 4B"
        reply "200163,delay 5000,2602": "command: 2603, payload: 63"
    }

    tiles(scale: 2) {
        multiAttributeTile(name: "switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
            tileAttribute("device.switch", key: "PRIMARY_CONTROL") {
                attributeState "on", label: '${name}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00a0dc", nextState: "turningOff"
                attributeState "off", label: '${name}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState: "turningOn"
                attributeState "turningOn", label: '${name}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00a0dc", nextState: "turningOff"
                attributeState "turningOff", label: '${name}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState: "turningOn"
            }
            tileAttribute("device.level", key: "VALUE_CONTROL") {
                attributeState "VALUE_UP", action:"levelUp"
                attributeState "VALUE_DOWN", action:"levelDown"
            }
            tileAttribute("device.level", key: "SLIDER_CONTROL") {
                attributeState "level", label: '${currentValue} %', action: "switch level.setLevel"
            }
        }

        standardTile("low", "device.level", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
            state "default", label: 'LOW', action: "low", icon: "st.Lighting.light14"
        }

        standardTile("medium", "device.level", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
            state "default", label: 'MED', action: "medium", icon: "st.Lighting.light13"
        }

        standardTile("high", "device.level", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
            state "default", label: 'HIGH', action: "high", icon: "st.Lighting.light11"
        }

        standardTile("refresh", "device.switch", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
            state "default", label: '', action: "refresh.refresh", icon: "st.secondary.refresh"
        }

        valueTile("firmwareVersion", "device.firmwareVersion", width: 4, height: 2, decoration: "flat") {
            state "firmwareVersion", label: 'Firmware Version\n${currentValue}'
        }

        valueTile("loadType", "device.loadType", width: 3, height: 1, decoration: "flat") {
            state "loadType", label: 'Load Type\n${currentValue}'
        }

        valueTile("indicatorStatus", "device.indicatorStatus", width: 3, height: 1, decoration: "flat") {
            state "indicatorStatus", label: 'Indicator Status\n${currentValue}'
        }

        valueTile("presetLevel", "device.presetLevel", width: 3, height: 1, decoration: "flat") {
            state "presetLevel", label: 'Preset Level\n${currentValue}'
        }

        valueTile("minLevel", "device.minLevel", width: 3, height: 1, decoration: "flat") {
            state "minLevel", label: 'Min Level\n${currentValue}'
        }

        valueTile("maxLevel", "device.maxLevel", width: 3, height: 1, decoration: "flat") {
            state "maxLevel", label: 'Max Level\n${currentValue}'
        }

        valueTile("fadeOnTime", "device.fadeOnTime", width: 3, height: 1, decoration: "flat") {
            state "fadeOnTime", label: 'Fade-on Time\n${currentValue}'
        }

        valueTile("fadeOffTime", "device.fadeOffTime", width: 3, height: 1, decoration: "flat") {
            state "fadeOffTime", label: 'Fade-off Time\n${currentValue}'
        }

        valueTile("levelIndicatorTimeout", "device.levelIndicatorTimeout", width: 3, height: 1, decoration: "flat") {
            state "levelIndicatorTimeout", label: 'Level Indicator Timeout\n${currentValue}'
        }

        main("switch")
        details(["switch",
                 "low", "medium", "high",
                 "refresh", "firmwareVersion",
                 "loadType", "indicatorStatus",
                 "minLevel", "maxLevel",
                 "fadeOnTime", "fadeOffTime",
                 "presetLevel", "levelIndicatorTimeout"])
    }

    preferences {
        input name: "levelIncrement", type: "number", title: "<b>In-App Level Increment</b>",
                description: "1 - 100 (default $defaultLevelIncrement)", range: "1..100", defaultValue: defaultLevelIncrement,
                displayDuringSetup: false, required: false

        input type: "paragraph", element: "paragraph", title: "<b>Device Preferences</b>",
                description: "The following preferences are configuring the device behaviors. " +
                        "<br>All of them are optional. Leave a preference empty to skip configuring it."

        input name: "loadType", type: "enum", title: "<b>Load type</b>",
                options: ["Incandescent (default)", "LED", "CFL"],
                displayDuringSetup: false, required: false
        input name: "indicatorStatus", type: "enum", title: "<b>Indicator LED is lit</b>",
                options: ["When switch is off (default)", "When switch is on", "Never"],
                displayDuringSetup: false, required: false
        input name: "presetLevel", type: "number", title: "<b>Light turns on to level</b>",
                description: "0 to 100 (default 0) <br> "+"0 = last dim level (default)<br> 1 - 100 = fixed level", range: "0..100",
                displayDuringSetup: false, required: false
        input name: "minLevel", type: "number", title: "<b>Minimum light level</b>",
                description: "0 to 100 (default 10)", range: "0..100",
                displayDuringSetup: false, required: false
        input name: "maxLevel", type: "number", title: "<b>Maximum light level</b>",
                description: "0 to 100 (default 100)", range: "0..100",
                displayDuringSetup: false, required: false
        input name: "fadeOnTime", type: "number", title: "<b>Fade-on time</b>",
                description: "0 to 253 (default 2)"+"0 = instant on<br> 1 - 127 = 1 - 127 seconds (default 2)<br> 128 - 253 = 1 - 126 minutes" , range: "0..253",
                displayDuringSetup: false, required: false
        input name: "fadeOffTime", type: "number", title: "<b>Fade-off time</b>",
                description: "0 to 253 (default 2)<br> "+"0 = instant off<br> 1 - 127 = 1 - 127 seconds (default 2)<br> 128 - 253 = 1 - 126 minutes", range: "0..253",
                displayDuringSetup: false, required: false
        input name: "levelIndicatorTimeout", type: "number", title: "<b>Dim level indicator timeout</b>",
                description: "0 to 255 (default 3)<br> "+"0 = dim level indicator off<br> 1 - 254 = timeout in seconds (default 3)<br> 255 = dim level indicator always on", range: "0..255",
                displayDuringSetup: false, required: false
		input name: "debuglog", type: "bool", title: "<b>Enable debug logging</b>", defaultValue: false

    }
}

def installed() {
	log.info "installed..."
    initialize()
    response(refresh())
}

def updated() {
    if (state.lastUpdatedAt != null && state.lastUpdatedAt >= now() - 1000) {
        if (debuglog) log.debug "ignoring double updated"
        return
    }
    if (debuglog) log.debug "updated..."
    state.lastUpdatedAt = now()

    initialize()
    response(configure())
}

def configure() {
    def commands = []
    if (loadType != null) {
        commands.addAll(setLoadType(loadType))
    }
    if (indicatorStatus != null) {
        commands.addAll(setIndicatorStatus(indicatorStatus))
    }
    if (presetLevel != null) {
        commands.addAll(setPresetLevel(presetLevel as short))
    }
    if (minLevel != null) {
        commands.addAll(setMinLevel(minLevel as short))
    }
    if (maxLevel != null) {
        commands.addAll(setMaxLevel(maxLevel as short))
    }
    if (fadeOnTime != null) {
        commands.addAll(setFadeOnTime(fadeOnTime as short))
    }
    if (fadeOffTime != null) {
        commands.addAll(setFadeOffTime(fadeOffTime as short))
    }
    if (levelIndicatorTimeout != null) {
        commands.addAll(setLevelIndicatorTimeout(levelIndicatorTimeout as short))
    }
    if (debuglog) log.debug "Configuring with commands $commands"
    commands
}

def parse(String description) {
    def result = null
    def cmd = zwave.parse(description, [0x20: 1, 0x25:1, 0x26: 1, 0x70: 1, 0x72: 2])
    if (cmd) {
        result = zwaveEvent(cmd)
        if (debuglog) log.debug "Parsed $cmd to $result"
    } else {
        log.warn "Non-parsed event: $description"
    }
    result
}

def on() {
    def fadeOnTime = device.currentValue("fadeOnTime")
    def presetLevel = device.currentValue("presetLevel")

    short duration = fadeOnTime == null ? 255 : fadeOnTime
    short level = presetLevel == null || presetLevel == 0 ? 0xFF : toZwaveLevel(presetLevel as short)
    delayBetween([
            zwave.switchMultilevelV2.switchMultilevelSet(value: level, dimmingDuration: duration).format(),
            zwave.switchMultilevelV1.switchMultilevelGet().format()
    ], durationToSeconds(duration) * 1000 + commandDelayMs)
}

def off() {
    def fadeOffTime = device.currentValue("fadeOffTime")

    short duration = fadeOffTime == null ? 255 : fadeOffTime
    delayBetween([
            zwave.switchMultilevelV2.switchMultilevelSet(value: 0x00, dimmingDuration: duration).format(),
            zwave.switchMultilevelV1.switchMultilevelGet().format()
    ], durationToSeconds(duration) * 1000 + commandDelayMs)
}

def setLevel(value, durationSeconds = null) {
    if (debuglog) log.debug "setLevel >> value: $value, durationSeconds: $durationSeconds"
    short level = toDisplayLevel(value as short)
    short dimmingDuration = durationSeconds == null ? 255 : secondsToDuration(durationSeconds as int)

    sendEvent(name: "level", value: level, unit: "%")
    sendEvent(name: "switch", value: level > 0 ? "on" : "off")
    delayBetween([
            zwave.switchMultilevelV2.switchMultilevelSet(value: toZwaveLevel(level), dimmingDuration: dimmingDuration).format(),
            zwave.switchMultilevelV1.switchMultilevelGet().format()
    ], durationToSeconds(dimmingDuration) * 1000 + commandDelayMs)
}

def poll() {
	log.debug "in poll()"
    delayBetween(statusCommands, commandDelayMs)
}

def ping() {
	log.debug "in ping()"
    poll()
}

def refresh() {
    def commands = statusCommands
    commands << zwave.versionV1.versionGet().format()

    if (getDataValue("MSR") == null) {
        commands << zwave.manufacturerSpecificV1.manufacturerSpecificGet().format()
    }
    for (i in 1..8) {
        commands << zwave.configurationV1.configurationGet(parameterNumber: i).format()
    }
    if (debuglog) log.debug "Refreshing with commands $commands"
    delayBetween(commands, commandDelayMs)
}

def indicatorNever() {
    sendEvent(name: "indicatorStatus", value: "never")
    configurationCommand(7, 0)
}

def indicatorWhenOff() {
    sendEvent(name: "indicatorStatus", value: "when off")
    configurationCommand(7, 255)
}

def indicatorWhenOn() {
    sendEvent(name: "indicatorStatus", value: "when on")
    configurationCommand(7, 254)
}

def low() {
    setLevel(10)
}

def medium() {
    setLevel(50)
}

def high() {
    setLevel(100)
}

def levelUp() {
    setLevel(device.currentValue("level") + (levelIncrement ?: defaultLevelIncrement))
}

def levelDown() {
    setLevel(device.currentValue("level") - (levelIncrement ?: defaultLevelIncrement))
}


private static int getCommandDelayMs() { 1000 }
private static int getDefaultLevelIncrement() { 10 }

private initialize() {
    // Device-Watch simply pings if no device events received for 32min(checkInterval)
    sendEvent(name: "checkInterval", value: 2 * 15 * 60 + 2 * 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID, offlinePingable: "1"])
}

private zwaveEvent(hubitat.zwave.commands.basicv1.BasicReport cmd) {
    dimmerEvent(cmd.value)
}

private zwaveEvent(hubitat.zwave.commands.switchmultilevelv1.SwitchMultilevelReport cmd) {
    dimmerEvent(cmd.value)
}

private zwaveEvent(hubitat.zwave.commands.switchmultilevelv1.SwitchMultilevelStopLevelChange cmd) {
    response(zwave.switchMultilevelV1.switchMultilevelGet().format())
}

private zwaveEvent(hubitat.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
    if (cmd.value == 0) {
        switchEvent(false)
    } else if (cmd.value == 255) {
        switchEvent(true)
    } else {
        if (debuglog) log.debug "Bad switch value $cmd.value"
    }
}

private zwaveEvent(hubitat.zwave.commands.configurationv1.ConfigurationReport cmd) {
    def result = null
    switch (cmd.parameterNumber) {
        case 1:
            result = createEvent(name: "fadeOnTime", value: cmd.configurationValue[0])
            break
        case 2:
            result = createEvent(name: "fadeOffTime", value: cmd.configurationValue[0])
            break
        case 3:
            result = createEvent(name: "minLevel", value: cmd.configurationValue[0])
            break
        case 4:
            result = createEvent(name: "maxLevel", value: cmd.configurationValue[0])
            break
        case 5:
            result = createEvent(name: "presetLevel", value: cmd.configurationValue[0])
            break
        case 6:
            result = createEvent(name: "levelIndicatorTimeout", value: cmd.configurationValue[0])
            break
        case 7:
            def value = null
            switch (cmd.configurationValue[0]) {
                case 0: value = "never"; break
                case 254: value = "when on"; break
                case 255: value = "when off"; break
            }
            result = createEvent(name: "indicatorStatus", value: value)
            break
        case 8:
            def value = null
            switch (cmd.configurationValue[0]) {
                case 0: value = "incandescent"; break
                case 1: value = "led"; break
                case 2: value = "cfl"; break
            }
            result = createEvent(name: "loadType", value: value)
            break
    }
    result
}

private zwaveEvent(hubitat.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
    if (debuglog) log.debug "manufacturerId:   $cmd.manufacturerId"
    if (debuglog) log.debug "manufacturerName: $cmd.manufacturerName"
    if (debuglog) log.debug "productId:        $cmd.productId"
    if (debuglog) log.debug "productTypeId:    $cmd.productTypeId"
    def msr = String.format("%04X-%04X-%04X", cmd.manufacturerId, cmd.productTypeId, cmd.productId)
    updateDataValue("MSR", msr)
    updateDataValue("manufacturer", cmd.manufacturerName)
    createEvent([descriptionText: "$device.displayName MSR: $msr", isStateChange: false])
}

private zwaveEvent(hubitat.zwave.commands.hailv1.Hail cmd) {
    createEvent(name: "hail", value: "hail", descriptionText: "Switch button was pressed", displayed: false)
}

private zwaveEvent(hubitat.zwave.commands.versionv1.VersionReport cmd) {
    createEvent(name: "firmwareVersion", value: "${cmd.applicationVersion}.${cmd.applicationSubVersion}", displayed: false)
}

private zwaveEvent(hubitat.zwave.Command cmd) {
    log.warn "Unhandled zwave command $cmd"
}

private dimmerEvent(short level) {
    def result = null
    if (level == 0) {
        result = [createEvent(name: "level", value: 0, unit: "%"), switchEvent(false)]
    } else if (level >= 1 && level <= 100) {
        result = [createEvent(name: "level", value: toDisplayLevel(level), unit: "%")]
        if (!isNewFirmware && device.currentValue("switch") != "on") {
            // Don't blindly trust level. Explicitly request on/off status.
            result << response(zwave.switchBinaryV1.switchBinaryGet().format())
        } else {
            result << switchEvent(true)
        }
    } else {
        log.warn "Bad dimming level $level"
    }
    result
}

private switchEvent(boolean on) {
    createEvent(name: "switch", value: on ? "on" : "off")
}

private getStatusCommands() {
    def cmds = []
    if (!isNewFirmware) {
        // Even though SwitchBinary is not advertised by this device, it seems to be the only way to assess its true
        // on/off status.
        cmds << zwave.switchBinaryV1.switchBinaryGet().format()
    }
    cmds << zwave.switchMultilevelV1.switchMultilevelGet().format()
    cmds
}

private short toDisplayLevel(short level) {
    level = Math.max(0, Math.min(100, level))
    (level == (short) 99) ? 100 : level
}

private short toZwaveLevel(short level) {
    Math.max(0, Math.min(99, level))
}

private int durationToSeconds(short duration) {
    if (duration >= 0 && duration <= 127) {
        duration
    } else if (duration >= 128 && duration <= 254) {
        (duration - 127) * 60
    } else if (duration == 255) {
        2   // factory default
    } else {
        log.error "Bad duration $duration"
        0
    }
}

private short secondsToDuration(int seconds) {
    if (seconds >= 0 && seconds <= 127) {
        seconds
    } else if (seconds >= 128 && seconds <= 127 * 60) {
        127 + Math.round(seconds / 60)
    } else {
        log.error "Bad seconds $seconds"
        255
    }
}

private configurationCommand(param, value) {
    param = param as short
    value = value as short
    delayBetween([
            zwave.configurationV1.configurationSet(parameterNumber: param, configurationValue: [value]).format(),
            zwave.configurationV1.configurationGet(parameterNumber: param).format()
    ], commandDelayMs)
}

private setFadeOnTime(short time) {
    sendEvent(name: "fadeOnTime", value: time)
    configurationCommand(1, time)
}

private setFadeOffTime(short time) {
    sendEvent(name: "fadeOffTime", value: time)
    configurationCommand(2, time)
}

private setMinLevel(short level) {
    sendEvent(name: "minLevel", value: level)
    configurationCommand(3, level)
}

private setMaxLevel(short level) {
    sendEvent(name: "maxLevel", value: level)
    configurationCommand(4, level)
}

private setPresetLevel(short level) {
    sendEvent(name: "presetLevel", value: level)
    configurationCommand(5, level)
}

private setLevelIndicatorTimeout(short timeout) {
    sendEvent(name: "levelIndicatorTimeout", value: timeout)
    configurationCommand(6, timeout)
}

private setLoadType(String loadType) {
    switch (loadType) {
        case "Incandescent (default)":
            sendEvent(name: "loadType", value: "incandescent")
            return configurationCommand(8, 0)
        case "LED":
            sendEvent(name: "loadType", value: "led")
            return configurationCommand(8, 1)
        case "CFL":
            sendEvent(name: "loadType", value: "cfl")
            return configurationCommand(8, 2)
    }
}

private setIndicatorStatus(String status) {
    switch (indicatorStatus) {
        case "When switch is off (default)":    return indicatorWhenOff()
        case "When switch is on":               return indicatorWhenOn()
        case "Never":                           return indicatorNever()
    }
}

private boolean getIsNewFirmware() { device.currentValue("firmwareVersion") ==~ /1\.2\d/ }
