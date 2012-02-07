// Copyright (c) 2010 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.


WebInspector.InspectorBackendStub = function()
{
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "populateScriptObjects", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "getSettings", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "storeLastActivePanel", "arguments": {"panelName": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "saveApplicationSettings", "arguments": {"settings": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "saveSessionSettings", "arguments": {"settings": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "enableSearchingForNode", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "disableSearchingForNode", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "enableMonitoringXHR", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "disableMonitoringXHR", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "enableResourceTracking", "arguments": {"always": "boolean"}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "disableResourceTracking", "arguments": {"always": "boolean"}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "getResourceContent", "arguments": {"identifier": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "reloadPage", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "startTimelineProfiler", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "stopTimelineProfiler", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Backend", "command": "enableDebugger", "arguments": {"always": "boolean"}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "disableDebugger", "arguments": {"always": "boolean"}}');
    this._registerDelegate('{"seq": 0, "domain": "Debug", "command": "setBreakpoint", "arguments": {"sourceID": "string","lineNumber": "number","enabled": "boolean","condition": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "Debug", "command": "removeBreakpoint", "arguments": {"sourceID": "string","lineNumber": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "Debug", "command": "activateBreakpoints", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Debug", "command": "deactivateBreakpoints", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Debug", "command": "pause", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Debug", "command": "resume", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Debug", "command": "stepOverStatement", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Debug", "command": "stepIntoStatement", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Debug", "command": "stepOutOfFunction", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Debug", "command": "setPauseOnExceptionsState", "arguments": {"pauseOnExceptionsState": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "Debug", "command": "editScriptSource", "arguments": {"sourceID": "string","newContent": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "Debug", "command": "getScriptSource", "arguments": {"sourceID": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "enableProfiler", "arguments": {"always": "boolean"}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "disableProfiler", "arguments": {"always": "boolean"}}');
    this._registerDelegate('{"seq": 0, "domain": "Profiler", "command": "startProfiling", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Profiler", "command": "stopProfiling", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Profiler", "command": "getProfileHeaders", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Profiler", "command": "getProfile", "arguments": {"uid": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "Profiler", "command": "removeProfile", "arguments": {"uid": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "Profiler", "command": "clearProfiles", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Backend", "command": "takeHeapSnapshot", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Backend", "command": "getProfilerLogLines", "arguments": {"inPosition": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "Backend", "command": "setInjectedScriptSource", "arguments": {"scriptSource": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "Backend", "command": "dispatchOnInjectedScript", "arguments": {"injectedScriptId": "number","methodName": "string","arguments": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "addScriptToEvaluateOnLoad", "arguments": {"scriptSource": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "removeAllScriptsToEvaluateOnLoad", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "getChildNodes", "arguments": {"nodeId": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "setAttribute", "arguments": {"elementId": "number","name": "string","value": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "removeAttribute", "arguments": {"elementId": "number","name": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "setTextNodeValue", "arguments": {"nodeId": "number","value": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "getEventListenersForNode", "arguments": {"nodeId": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "copyNode", "arguments": {"nodeId": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "removeNode", "arguments": {"nodeId": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "changeTagName", "arguments": {"nodeId": "number","newTagName": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "getOuterHTML", "arguments": {"nodeId": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "setOuterHTML", "arguments": {"nodeId": "number","outerHTML": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "addInspectedNode", "arguments": {"nodeId": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "performSearch", "arguments": {"query": "string","runSynchronously": "boolean"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "searchCanceled", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "pushNodeByPathToFrontend", "arguments": {"path": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "setDOMBreakpoint", "arguments": {"nodeId": "number","type": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "removeDOMBreakpoint", "arguments": {"nodeId": "number","type": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "clearConsoleMessages", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "highlightDOMNode", "arguments": {"nodeId": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "hideDOMNodeHighlight", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "openInInspectedWindow", "arguments": {"url": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "getStyles", "arguments": {"nodeId": "number","authOnly": "boolean"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "getAllStyles", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "getInlineStyle", "arguments": {"nodeId": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "getComputedStyle", "arguments": {"nodeId": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "getStyleSheet", "arguments": {"styleSheetId": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "getRuleRanges", "arguments": {"styleSheetId": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "applyStyleText", "arguments": {"styleId": "number","styleText": "string","propertyName": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "setStyleText", "arguments": {"styleId": "number","styleText": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "setStyleProperty", "arguments": {"styleId": "number","name": "string","value": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "toggleStyleEnabled", "arguments": {"styleId": "number","propertyName": "string","disabled": "boolean"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "setRuleSelector", "arguments": {"ruleId": "number","selector": "string","selectedNodeId": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "addRule", "arguments": {"selector": "string","selectedNodeId": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "DOM", "command": "getSupportedCSSProperties", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "getCookies", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "deleteCookie", "arguments": {"cookieName": "string","domain": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "ApplicationCache", "command": "getApplicationCaches", "arguments": {}}');
    this._registerDelegate('{"seq": 0, "domain": "Backend", "command": "releaseWrapperObjectGroup", "arguments": {"injectedScriptId": "number","objectGroup": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "didEvaluateForTestInFrontend", "arguments": {"testCallId": "number","jsonResult": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "Backend", "command": "getDatabaseTableNames", "arguments": {"databaseId": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "Backend", "command": "executeSQL", "arguments": {"databaseId": "number","query": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "getDOMStorageEntries", "arguments": {"storageId": "number"}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "setDOMStorageItem", "arguments": {"storageId": "number","key": "string","value": "string"}}');
    this._registerDelegate('{"seq": 0, "domain": "Controller", "command": "removeDOMStorageItem", "arguments": {"storageId": "number","key": "string"}}');
}

WebInspector.InspectorBackendStub.prototype = {
    _registerDelegate: function(commandInfo)
    {
        var commandObject = JSON.parse(commandInfo);
        this[commandObject.command] = this.sendMessageToBackend.bind(this, commandInfo);
    },

    sendMessageToBackend: function()
    {
        var args = Array.prototype.slice.call(arguments);
        var request = JSON.parse(args.shift());

        for (var key in request.arguments) {
            var value = args.shift();
            if (typeof value !== request.arguments[key])
                throw("Invalid type of property " + key + ". It should be '" + request.arguments[key] + "' but it is '" + typeof value + "'." );
            request.arguments[key] = value;
        }

        if (args.length === 1 && typeof args[0] === "function")
            request.seq = WebInspector.Callback.wrap(args[0]);

        var message = JSON.stringify(request);
        InspectorFrontendHost.sendMessageToBackend(message);
    }
}

InspectorBackend = new WebInspector.InspectorBackendStub();