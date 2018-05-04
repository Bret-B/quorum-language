#include "../IQuorumAccessibility/Header/plugins_quorum_Libraries_Interface_AccessibilityManager.h"
#include "../IQuorumAccessibility/Header/jni.h"

#include <Windows.h>
#include <UIAutomation.h>

#include "Item.h"
#include "PushButtonControl.h"
#include "RadioButtonControl.h"
#include "ToggleButtonControl.h"
#include "TextBoxControl.h"

// For Debug Output
#include <iostream>
#include <string>

// This is the handle to the main game window. It is set during initialization and must never be changed.
HWND GLFWParentWindow;

HWND GetMainWindowHandle()
{
	return GLFWParentWindow;
}

// CreateWideStringFromUTF8Win32: converts a const char* to a WCHAR*.
WCHAR* CreateWideStringFromUTF8Win32(const char* source)
{
	// newsize describes the length of the   
	// wchar_t string called wcstring in terms of the number   
	// of wide characters, not the number of bytes. 
	size_t newsize = strlen(source) + 1;

	// The following creates a buffer large enough to contain   
	// the exact number of characters in the original string  
	// in the new format.
	WCHAR* target = new wchar_t[newsize];

	// Convert char* string to a wchar_t* string.  
	size_t convertedChars = 0;
	mbstowcs_s(&convertedChars, target, newsize, source, _TRUNCATE);

	return target;

}

// DllMain: Entry point for dll. Nothing to do here.
BOOL WINAPI DllMain(HINSTANCE instance, DWORD reason, LPVOID reserved)
{
	return TRUE;
}


////////////////////////////////////////////////////////////////////////////
////////////				   JNI Functions
////////////
////////////////////////////////////////////////////////////////////////////

// NativeWin32InitializeAccessibility: Calls CoInitialize so that COM interface library functions are availible for use. This only ever needs to be called once. Never call this more than once.
//									   CoUninitialize must be called the same number of times as CoInitialize.
JNIEXPORT void JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32InitializeAccessibility(JNIEnv *env, jobject obj, jlong parentWindowHWND)
{
	HRESULT hr = CoInitializeEx(NULL, COINIT_MULTITHREADED); // COINIT_APARTMENTTHREADED COINIT_MULTITHREADED
	GLFWParentWindow = (HWND)parentWindowHWND;

	//DWORD threadID = GetCurrentThreadId();

	//std::cout << "NativeWin32InitializeAccessibility Thread ID: " << threadID << std::endl;
}

// NativeWin32ShutdownAccessibility: Closes the COM library gracefully.
// TODO: More work needs to be done in this function to ensure clean shutdown of the library.
JNIEXPORT void JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32ShutdownAccessibility(JNIEnv *env, jobject obj)
{
	CoUninitialize();
}

// TODO: REMOVE this method from here, the JNI header file, the Java AccessibilityManager, and the Quorum AccessibilityManager.
// NativePrint: Solely used to test whether a call from Java or Quorum can make it down to C++.
JNIEXPORT void JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativePrint(JNIEnv *env, jobject obj)
{
	std::cout << "Native C++ Print" << std::endl;

}

/* ==============================
*	This section of code contains the
*	JNI methods that will create an
*	instance of the appropriate accessible
*	object for UIA to retrieve info from.
// ============================== */
#pragma region Create Accessible Object

// NativeWin32CreateItem: This is the most generic accessible object that can be created. It only contains a name and a description.
JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32CreateItem(JNIEnv *env, jobject obj, jstring itemName, jstring description)
{

	const char *nativeItemName = env->GetStringUTFChars(itemName, 0);
	const char *nativeDescription = env->GetStringUTFChars(description, 0);

	WCHAR* wItemName = CreateWideStringFromUTF8Win32(nativeItemName);
	WCHAR* wDescription = CreateWideStringFromUTF8Win32(nativeDescription);

	HWND itemControlHandle;

	itemControlHandle = Item::Create(GetModuleHandle(NULL), wItemName, wDescription);

	env->ReleaseStringUTFChars(itemName, nativeItemName);
	env->ReleaseStringUTFChars(description, nativeDescription);

	return PtrToLong(itemControlHandle);

}

// NativeWin32CreatePushButton: Creates a window that contains the accessible information for a PushButton that was passed into this function.
//		Returns: jlong which is the HWND for the window. This is used to further interact with the button after creation. i.e., to rename the button later should the name be changed. Also, used to keep track of it in java.
JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32CreatePushButton(JNIEnv *env, jobject obj, jstring buttonName, jstring description)
{

	const char *nativeButtonName = env->GetStringUTFChars(buttonName, 0);
	const char *nativeDescription = env->GetStringUTFChars(description, 0);

	WCHAR* wButtonName = CreateWideStringFromUTF8Win32(nativeButtonName);
	WCHAR* wDescription = CreateWideStringFromUTF8Win32(nativeDescription);


	HWND pushbuttonControlHandle;

	pushbuttonControlHandle = PushButtonControl::Create(GetModuleHandle(NULL), wButtonName, wDescription);

	env->ReleaseStringUTFChars(buttonName, nativeButtonName);
	env->ReleaseStringUTFChars(description, nativeDescription);

	return PtrToLong(pushbuttonControlHandle);

}

// NativeWin32CreateToggleButton: 
JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32CreateToggleButton(JNIEnv *env, jobject obj, jstring togglebuttonName, jstring description)
{

	const char *nativeTogglebuttonName = env->GetStringUTFChars(togglebuttonName, 0);
	const char *nativeDescription = env->GetStringUTFChars(description, 0);

	WCHAR* wTogglebuttonName = CreateWideStringFromUTF8Win32(nativeTogglebuttonName);
	WCHAR* wDescription = CreateWideStringFromUTF8Win32(nativeDescription);

	HWND togglebuttonControlHandle;

	togglebuttonControlHandle = ToggleButtonControl::Create(GetModuleHandle(NULL), wTogglebuttonName, wDescription);

	env->ReleaseStringUTFChars(togglebuttonName, nativeTogglebuttonName);
	env->ReleaseStringUTFChars(description, nativeDescription);

	return PtrToLong(togglebuttonControlHandle);

}

// NativeWin32CreateRadioButton: 
JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32CreateRadioButton(JNIEnv *env, jobject obj, jstring itemName, jstring description)
{

	const char *nativeItemName = env->GetStringUTFChars(itemName, 0);
	const char *nativeDescription = env->GetStringUTFChars(description, 0);

	WCHAR* wRadiobuttonName = CreateWideStringFromUTF8Win32(nativeItemName);
	WCHAR* wDescription = CreateWideStringFromUTF8Win32(nativeDescription);

	HWND radiobuttonControlHandle;

	radiobuttonControlHandle = RadioButtonControl::Create(GetModuleHandle(NULL), wRadiobuttonName, wDescription);

	env->ReleaseStringUTFChars(itemName, nativeItemName);
	env->ReleaseStringUTFChars(description, nativeDescription);

	return PtrToLong(radiobuttonControlHandle);

}

// NativeWin32CreateTextBox: 
JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32CreateTextBox(JNIEnv *env, jobject obj, jstring textboxName, jstring description, jstring fullText, jint caretIndex)
{

	const char* nativeTextboxName = env->GetStringUTFChars(textboxName, 0);
	const char* nativeDescription = env->GetStringUTFChars(description, 0);
	const char* nativeFullText = env->GetStringUTFChars(fullText, 0);

	WCHAR* wTextboxName = CreateWideStringFromUTF8Win32(nativeTextboxName);
	WCHAR* wDescription = CreateWideStringFromUTF8Win32(nativeDescription);
	//WCHAR* wCurrentLineText = CreateWideStringFromUTF8Win32(nativeFullText);

	//EndPoint caret = EndPoint(caretLine, caretCharacter);
	
	//TextLine line[] = { wCurrentLineText };
	//TextLine line[] = { { L"Hello world!", 12 }, };

	//DWORD threadID = GetCurrentThreadId();

	//std::cout << "NativeWin32CreateTextBox Thread ID: " << threadID << std::endl;

	HWND textboxControlHandle = TextBoxControl::Create(GetModuleHandle(NULL), wTextboxName, wDescription, nativeFullText, (int)caretIndex);

	env->ReleaseStringUTFChars(textboxName, nativeTextboxName);
	env->ReleaseStringUTFChars(description, nativeDescription);
	env->ReleaseStringUTFChars(fullText, nativeFullText);

	return PtrToLong(textboxControlHandle);

}

#pragma endregion


/* ==============================
*	This section of code contains the
*   JNI methods that will send a message
*	to the control to raise UIA Events or update
*	a control's info to match the Quorum object.
// ============================== */
#pragma region Send A Message

// NativeWin32SetFocus: This function will send a message to the window procedure for the given jlongHWND to set focus. Each window procedure may handle that message differently but in general a window procedure will raise
//						a UI Automation Focus Changed event that triggers the screen reader to announce that the focus has changed. This function does not and should not change the keyboard focus to the given jlongHWND. If it does
//						then it will take keyboard control away from the main GLFW window that Quorum uses to get keyboard events from. There is no known way to give it back to the main GLFW window once the keyboard focus has been moved from it.
JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32SetFocus(JNIEnv *env, jobject obj, jlong jlongHWND)
{
	HWND control = (HWND)jlongHWND;


	//DWORD threadID = GetCurrentThreadId();

	//std::cout << "NativeWin32SetFocus Thread ID: " << threadID << std::endl;

	// Sends the appropriate messages to all windows.
	SetFocus(control);


	return PtrToLong(control);
}

// NativeWin32TextBoxTextSelectionChanged: This method will fire the appropriate UIA Event for when the text selection has changed. The selection can change as a result of the caret moving or text being added to the currentLineText.
// TODO: Update the currentLineText from what is given by Quorum. That way the line down here can stay in sync with Quorum.
JNIEXPORT void Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32TextBoxTextSelectionChanged(JNIEnv *env, jobject obj, jlong textboxHWND, jstring currentLineText, jint caretLine, jint caretCharacter)
{
	/*const char *nativeCurrentLineText = env->GetStringUTFChars(currentLineText, 0);
	WCHAR* wCurrentLineText = CreateWideStringFromUTF8Win32(nativeCurrentLineText);

	EndPoint caret;
	caret.line = (int)caretLine;
	caret.character = (int)caretCharacter;

	SendMessage((HWND)textboxHWND, QUORUM_UPDATECARET, 0, (LPARAM)&caret);

	env->ReleaseStringUTFChars(currentLineText, nativeCurrentLineText);*/
	std::cout << "NativeWin32TextBoxTextSelectionChanged is being reworked. Use NativeWin32UpdateCaretPosition instead." << std::endl;
}

// NativeWin32UpdateCaretPosition:
JNIEXPORT void Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32UpdateCaretPosition(JNIEnv *env, jobject obj, jlong textboxHWND, jstring fullText, jint caretIndex)
{
	HWND control = (HWND)textboxHWND;

	const char *nativeFullText = env->GetStringUTFChars(fullText, 0);

	SendMessage(control, QUORUM_UPDATECARET, (int)caretIndex, (LPARAM)nativeFullText);

	env->ReleaseStringUTFChars(fullText, nativeFullText);
}

// NativeWin32InvokeButton: 
JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32InvokeButton(JNIEnv *env, jobject obj, jlong jlongHWND)
{
	HWND control = (HWND)jlongHWND;

	SendMessage(control, QUORUM_INVOKEBUTTON, 0, 0);

	return PtrToLong(control);
}

// NativeWin32UpdateToggleStatus: 
JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32UpdateToggleStatus(JNIEnv *env, jobject obj, jlong jlongHWND, jboolean selected)
{
	HWND control = (HWND)jlongHWND;

	bool nativeSelected = (bool)selected;

	if (nativeSelected)
	{
		SendMessage(control, QUORUM_INVOKEBUTTON, true, 0);
		return true;
	}
	else
	{
		SendMessage(control, QUORUM_INVOKEBUTTON, false, 0);
		return true;
	}

	return true;

}

#pragma endregion


// ==============================
// TODO: Remove this section after debugging is finished
// This section is used to test the accessibility system
// in C++ with an empty GLFW window to rule out any issues that
// might arise from having it plugged into Quorum.
// ==============================
#pragma region Native DLL Exports

#define DLLEXPORT extern "C" __declspec(dllexport) 

DLLEXPORT HWND NativeWin32CreateTextBox(const char* nativeTextboxName, const char* nativeDescription, const char* fullText, int caretIndex)
{

	WCHAR* wTextboxName = CreateWideStringFromUTF8Win32(nativeTextboxName);
	WCHAR* wDescription = CreateWideStringFromUTF8Win32(nativeDescription);

	//EndPoint caret = EndPoint(caretLine, caretCharacter);
	//caret.character = caretCharacter;
	//caret.line = (int)caretLine;
	//TextLine line[] = { { L"Hello world!", 12 }, };

	HWND textboxControlHandle = TextBoxControl::Create(GetModuleHandle(NULL), wTextboxName, wDescription, fullText, caretIndex);

	return textboxControlHandle;

}

DLLEXPORT void NativeWin32SetFocus(HWND controlHWND)
{
	// Let windows handle focus changes.
	SetFocus(controlHWND);
}

DLLEXPORT void NativeWin32UpdateCaretPosition(HWND textboxHWND, const char * adjacentCharacter)
{
	std::wstring wAdjacentCharacter = CreateWideStringFromUTF8Win32(adjacentCharacter);

	// Just send a string for the screen reader to speak.
	SendMessage(textboxHWND, QUORUM_UPDATECARET, 0, (LPARAM)&wAdjacentCharacter);

	std::wcout << wAdjacentCharacter << std::endl;
}

DLLEXPORT HWND NativeWin32CreatePushButton(const char * nativeButtonName, const char * nativeDescription)
{

	WCHAR* wButtonName = CreateWideStringFromUTF8Win32(nativeButtonName);
	WCHAR* wDescription = CreateWideStringFromUTF8Win32(nativeDescription);

	HWND pushbuttonControlHandle;

	pushbuttonControlHandle = PushButtonControl::Create(GetModuleHandle(NULL), wButtonName, wDescription);

	return pushbuttonControlHandle;

}

DLLEXPORT void NativeWin32InitializeAccessibility(HWND parentWindow)
{
	HRESULT hr = CoInitializeEx(NULL, COINIT_MULTITHREADED); // COINIT_APARTMENTTHREADED COINIT_MULTITHREADED
	GLFWParentWindow = parentWindow;

	/*DWORD threadID = GetCurrentThreadId();

	std::cout << "NativeWin32InitializeAccessibility Thread ID: " << threadID << std::endl;*/
}


#pragma endregion