/* DO NOT EDIT THIS FILE - it is machine generated */
#include "../Header/jni.h"
/* Header for class plugins_quorum_Libraries_Interface_AccessibilityManager */

#ifndef _Included_plugins_quorum_Libraries_Interface_AccessibilityManager
#define _Included_plugins_quorum_Libraries_Interface_AccessibilityManager
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     plugins_quorum_Libraries_Interface_AccessibilityManager
 * Method:    print
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32InitializeAccessibility(JNIEnv *, jobject);

JNIEXPORT void JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32ShutdownAccessibility(JNIEnv *, jobject);

JNIEXPORT void JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativePrint(JNIEnv *, jobject);

JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32CreateItem(JNIEnv *, jobject, jlong, jstring, jstring);

JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32CreatePushButton(JNIEnv *, jobject, jlong, jstring, jstring);

JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32CreateToggleButton(JNIEnv *, jobject, jlong, jstring, jstring);

JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32CreateRadioButton(JNIEnv *, jobject, jlong, jstring, jstring);

JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32CreateTextBox(JNIEnv *, jobject, jlong, jstring, jstring, jstring, jint, jint);

JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32SetFocus(JNIEnv *, jobject, jlong);

JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32InvokeButton(JNIEnv *, jobject, jlong);

JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32UpdateToggleStatus(JNIEnv *, jobject, jlong, jboolean);

JNIEXPORT void JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32TextBoxTextSelectionChanged(JNIEnv *, jobject, jlong, jstring, jint, jint);

JNIEXPORT void JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32UpdateCaretPosition(JNIEnv *, jobject, jlong, jstring);

#ifdef __cplusplus
}
#endif
#endif
