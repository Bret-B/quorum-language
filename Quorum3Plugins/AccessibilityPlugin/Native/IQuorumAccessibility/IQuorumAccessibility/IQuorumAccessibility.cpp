#include "../IQuorumAccessibility/Header/plugins_quorum_Libraries_Interface_AccessibilityManager.h"
#include "../IQuorumAccessibility/Header/jni.h"

#include <Windows.h>
#include <UIAutomation.h>

#include "ItemControl.h"
#include "ButtonControl.h"
#include "RadioButtonControl.h"
#include "CheckBoxControl.h"
#include "TextBoxControl.h"
#include "MenuBarControl.h"
#include "MenuItemControl.h"
#include "TreeControl.h"
#include "TreeItemControl.h"

// For Debug Output
#include <iostream>
#include <string>

// This is the handle to the main game window. It is set during initialization and must never be changed.
HWND GLFWParentWindow = NULL;
JavaVM* jvm = NULL;

HWND GetMainWindowHandle()
{
	return GLFWParentWindow;
}

JavaVM* GetJVM()
{
	return jvm;
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
	UNREFERENCED_PARAMETER(instance);
	UNREFERENCED_PARAMETER(reason);
	UNREFERENCED_PARAMETER(reserved);
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
	UNREFERENCED_PARAMETER(obj);
	CoInitializeEx(NULL, COINIT_MULTITHREADED); // COINIT_APARTMENTTHREADED COINIT_MULTITHREADED
	GLFWParentWindow = (HWND)parentWindowHWND;
	env->GetJavaVM(&jvm);
}

// NativeWin32ShutdownAccessibility: Closes the COM library gracefully.
// TODO: More work needs to be done in this function to ensure clean shutdown of the library.
JNIEXPORT void JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32ShutdownAccessibility(JNIEnv *env, jobject obj)
{
	UNREFERENCED_PARAMETER(env);
	UNREFERENCED_PARAMETER(obj);
	CoUninitialize();
}

// TODO: REMOVE this method from here, the JNI header file, the Java AccessibilityManager, and the Quorum AccessibilityManager.
// NativePrint: Solely used to test whether a call from Java or Quorum can make it down to C++.
JNIEXPORT void JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativePrint(JNIEnv *env, jobject obj)
{
	UNREFERENCED_PARAMETER(env);
	UNREFERENCED_PARAMETER(obj);
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
	UNREFERENCED_PARAMETER(obj);
	const char *nativeItemName = env->GetStringUTFChars(itemName, 0);
	const char *nativeDescription = env->GetStringUTFChars(description, 0);

	WCHAR* wItemName = CreateWideStringFromUTF8Win32(nativeItemName);
	WCHAR* wDescription = CreateWideStringFromUTF8Win32(nativeDescription);

	// For now the parent window for this control is the main game window. Once Quourum has able to create additional windows
	// then GetMainWindowHandle() will need to be replaced by which open window the accessible object is being created for.
	ItemControl* pItemControl = ItemControl::Create(GetModuleHandle(NULL), GetMainWindowHandle(), wItemName, wDescription);

	env->ReleaseStringUTFChars(itemName, nativeItemName);
	env->ReleaseStringUTFChars(description, nativeDescription);

	return PtrToLong(pItemControl);

}

// NativeWin32CreateButton: Creates a window that contains the accessible information for a PushButton that was passed into this function.
//		Returns: jlong which is the HWND for the window. This is used to further interact with the button after creation. i.e., to rename the button later should the name be changed. Also, used to keep track of it in java.
JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32CreateButton(JNIEnv *env, jobject obj, jstring buttonName, jstring description)
{
	UNREFERENCED_PARAMETER(obj);
	const char *nativeButtonName = env->GetStringUTFChars(buttonName, 0);
	const char *nativeDescription = env->GetStringUTFChars(description, 0);

	WCHAR* wButtonName = CreateWideStringFromUTF8Win32(nativeButtonName);
	WCHAR* wDescription = CreateWideStringFromUTF8Win32(nativeDescription);

	ButtonControl* pButtonControl;

	// For now the parent window for this control is the main game window. Once Quourum has able to create additional windows
	// then GetMainWindowHandle() will need to be replaced by which open window the accessible object is being created for.
	pButtonControl = ButtonControl::Create(GetModuleHandle(NULL), GetMainWindowHandle(), wButtonName, wDescription);

	env->ReleaseStringUTFChars(buttonName, nativeButtonName);
	env->ReleaseStringUTFChars(description, nativeDescription);

	return PtrToLong(pButtonControl);

}

// NativeWin32CreateCheckBox: 
JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32CreateCheckBox(JNIEnv *env, jobject obj, jstring togglebuttonName, jstring description)
{
	UNREFERENCED_PARAMETER(obj);
	const char *nativeTogglebuttonName = env->GetStringUTFChars(togglebuttonName, 0);
	const char *nativeDescription = env->GetStringUTFChars(description, 0);

	WCHAR* wTogglebuttonName = CreateWideStringFromUTF8Win32(nativeTogglebuttonName);
	WCHAR* wDescription = CreateWideStringFromUTF8Win32(nativeDescription);

	// For now the parent window for this control is the main game window. Once Quourum has able to create additional windows
	// then GetMainWindowHandle() will need to be replaced by which open window the accessible object is being created for.
	CheckBoxControl* pCheckBoxControl = CheckBoxControl::Create(GetModuleHandle(NULL), GetMainWindowHandle(), wTogglebuttonName, wDescription);

	env->ReleaseStringUTFChars(togglebuttonName, nativeTogglebuttonName);
	env->ReleaseStringUTFChars(description, nativeDescription);

	return PtrToLong(pCheckBoxControl);

}

// NativeWin32CreateRadioButton: 
JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32CreateRadioButton(JNIEnv *env, jobject obj, jstring itemName, jstring description)
{
	UNREFERENCED_PARAMETER(obj);
	const char *nativeItemName = env->GetStringUTFChars(itemName, 0);
	const char *nativeDescription = env->GetStringUTFChars(description, 0);

	WCHAR* wRadiobuttonName = CreateWideStringFromUTF8Win32(nativeItemName);
	WCHAR* wDescription = CreateWideStringFromUTF8Win32(nativeDescription);

	// For now the parent window for this control is the main game window. Once Quourum has able to create additional windows
	// then GetMainWindowHandle() will need to be replaced by which open window the accessible object is being created for.
	RadioButtonControl* pRadiobuttonControl = RadioButtonControl::Create(GetModuleHandle(NULL), GetMainWindowHandle(), wRadiobuttonName, wDescription);

	env->ReleaseStringUTFChars(itemName, nativeItemName);
	env->ReleaseStringUTFChars(description, nativeDescription);

	return PtrToLong(pRadiobuttonControl);

}

// NativeWin32CreateTextBox: 
JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32CreateTextBox(JNIEnv *env, jobject obj, jstring textboxName, jstring description, jstring fullText, jint caretIndex, jobject self)
{
	UNREFERENCED_PARAMETER(obj);
	const char* nativeTextboxName = env->GetStringUTFChars(textboxName, 0);
	const char* nativeDescription = env->GetStringUTFChars(description, 0);
	const char* nativeFullText = env->GetStringUTFChars(fullText, 0);

	WCHAR* wTextboxName = CreateWideStringFromUTF8Win32(nativeTextboxName);
	WCHAR* wDescription = CreateWideStringFromUTF8Win32(nativeDescription);
	WCHAR* wFullText = CreateWideStringFromUTF8Win32(nativeFullText);

	jclass textboxClass = env->GetObjectClass(self);

	env->NewGlobalRef(textboxClass);

	// For now the parent window for this control is the main game window. Once Quourum has able to create additional windows
	// then GetMainWindowHandle() will need to be replaced by which open window the accessible object is being created for.
	TextBoxControl* pTextboxControl = TextBoxControl::Create(GetModuleHandle(NULL), GetMainWindowHandle(), wTextboxName, wDescription, wFullText, Range(caretIndex,caretIndex), static_cast<jobject>(textboxClass));

	env->ReleaseStringUTFChars(textboxName, nativeTextboxName);
	env->ReleaseStringUTFChars(description, nativeDescription);
	env->ReleaseStringUTFChars(fullText, nativeFullText);

	return PtrToLong(pTextboxControl);

}

JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32CreateMenuBar(JNIEnv * env, jobject obj, jstring menuBarName)
{
	const char* nativeMenuBarName = env->GetStringUTFChars(menuBarName, 0);
	WCHAR* wMenuBarName = CreateWideStringFromUTF8Win32(nativeMenuBarName);

	// For now the parent window for this control is the main game window. Once Quourum has able to create additional windows
	// then GetMainWindowHandle() will need to be replaced by which open window the accessible object is being created for.
	MenuBarControl* pMenuBarControl = MenuBarControl::Create(GetModuleHandle(NULL), GetMainWindowHandle(), wMenuBarName);

	env->ReleaseStringUTFChars(menuBarName, nativeMenuBarName);

	return PtrToLong(pMenuBarControl);
}

JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32CreateMenuItem(JNIEnv * env, jobject obj, jstring menuItemName, jstring menuShortcut, jboolean isMenu, jlong parentMenu, jlong parentMenuBar)
{
	const char* nativeMenuItemName = env->GetStringUTFChars(menuItemName, 0);
	const char* nativeMenuShortcut = env->GetStringUTFChars(menuShortcut, 0);

	WCHAR* wMenuItemName = CreateWideStringFromUTF8Win32(nativeMenuItemName);
	WCHAR* wMenuShortcut = CreateWideStringFromUTF8Win32(nativeMenuShortcut);

	MenuBarControl* pMenuBar = static_cast<MenuBarControl*>(LongToPtr((long)parentMenuBar));
	
	MenuItemControl* parentMenuItem = NULL;
	
	if ((long)parentMenu != NULL)
		parentMenuItem = static_cast<MenuItemControl*>(LongToPtr((long)parentMenu));
	
	MenuItemControl* menuItemControl = new MenuItemControl(wMenuItemName, wMenuShortcut, (bool)isMenu, pMenuBar->CreateUniqueId(), parentMenuItem, pMenuBar);

	SendMessage(pMenuBar->GetHWND(), QUORUM_ADDMENUITEM, 0, (LPARAM)menuItemControl);

	env->ReleaseStringUTFChars(menuItemName, nativeMenuItemName);
	env->ReleaseStringUTFChars(menuShortcut, nativeMenuShortcut);

	return PtrToLong(menuItemControl);
}

JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32CreateTree(JNIEnv * env, jobject obj, jstring treeName)
{
	const char* nativeTreeName = env->GetStringUTFChars(treeName, 0);
	WCHAR* wTreeName = CreateWideStringFromUTF8Win32(nativeTreeName);

	// For now the parent window for this control is the main game window. Once Quourum has able to create additional windows
	// then GetMainWindowHandle() will need to be replaced by which open window the accessible object is being created for.
	TreeControl* pTreeControl = TreeControl::Create(GetModuleHandle(NULL), GetMainWindowHandle(), wTreeName);

	env->ReleaseStringUTFChars(treeName, nativeTreeName);

	return PtrToLong(pTreeControl);
}

JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32CreateTreeItem(JNIEnv * env, jobject obj, jstring treeItemName, jstring treeItemDescription, jboolean isSubtree, jboolean isExpanded, jlong parentSubtree, jlong parentTree)
{
	const char* nativeTreeItemName = env->GetStringUTFChars(treeItemName, 0);
	const char* nativeTreeItemDescription = env->GetStringUTFChars(treeItemDescription, 0);

	WCHAR* wTreeItemName = CreateWideStringFromUTF8Win32(nativeTreeItemName);
	WCHAR* wTreeItemDescription = CreateWideStringFromUTF8Win32(nativeTreeItemDescription);

	TreeControl* pTree = static_cast<TreeControl*>(LongToPtr((long)parentTree));

	TreeItemControl* parentTreeItem = NULL;

	if ((long)parentSubtree != NULL)
		parentTreeItem = static_cast<TreeItemControl*>(LongToPtr((long)parentSubtree));

	TreeItemControl* treeItemControl = new TreeItemControl(wTreeItemName, wTreeItemDescription, (bool)isSubtree, (bool)isExpanded, pTree->CreateUniqueId(), parentTreeItem, pTree);

	SendMessage(pTree->GetHWND(), QUORUM_ADDTREEITEM, 0, (LPARAM)treeItemControl);

	env->ReleaseStringUTFChars(treeItemName, nativeTreeItemName);
	env->ReleaseStringUTFChars(treeItemDescription, nativeTreeItemDescription);

	return PtrToLong(treeItemControl);
}

#pragma endregion

/* ==============================
*	This section of code contains the
*	JNI methods that will remove an
*	instance of the appropriate accessible
*	object for the UIA hierarchy.
// ============================== */
#pragma region Remove Accessible Object


JNIEXPORT bool JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32Remove(JNIEnv * env, jobject obj, jlong item)
{
	Item* itemToRemove = static_cast<Item*>(LongToPtr((long)item));
		
	DestroyWindow(itemToRemove->GetHWND());

	// The pointer is still in use until DestroyWindow has returned.
	// So delete the pointer here.
	delete itemToRemove;

	return true;
}

JNIEXPORT bool JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32RemoveMenuItem(JNIEnv * env, jobject obj, jlong menuItem)
{
	
	MenuItemControl* menuItemToRemove = static_cast<MenuItemControl*>(LongToPtr((long)menuItem));

	HWND hwnd = menuItemToRemove->GetParentMenuBar()->GetHWND();

	SendMessage(hwnd, QUORUM_REMOVEMENUITEM, 0, (LPARAM)menuItemToRemove);

	return true;

}

JNIEXPORT bool JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32RemoveTreeItem(JNIEnv * env, jobject obj, jlong treeItem)
{

	TreeItemControl* treeItemToRemove = static_cast<TreeItemControl*>(LongToPtr((long)treeItem));

	HWND hwnd = treeItemToRemove->GetParentTree()->GetHWND();

	SendMessage(hwnd, QUORUM_REMOVETREEITEM, 0, (LPARAM)treeItemToRemove);

	return true;

}


#pragma endregion

/* ==============================
*	This section of code contains the
*   JNI methods that will send a message
*	to the control to raise UIA Events or update
*	a control's info to match the Quorum object.
// ============================== */
#pragma region Send A Message

// NativeWin32SetFocus: This function will send a message to the window procedure for the control to change focus to that control. This function can only be called
//						on controls that have an HWND. MenuItems, for example, don't have an HWND and receive focus through selection.
JNIEXPORT long JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32SetFocus(JNIEnv *env, jobject obj, jlong control)
{
	UNREFERENCED_PARAMETER(env);
	UNREFERENCED_PARAMETER(obj);

	Item* pControl = static_cast<Item*>(LongToPtr((long)control));
	
	if (pControl != NULL && pControl->GetHWND() != NULL)
	{
		// Sends the appropriate messages to all windows.
		HWND prevFocus = SetFocus(pControl->GetHWND());
		return PtrToLong(prevFocus);
	}
	
	return NULL;
}

// NativeWin32TextBoxTextSelectionChanged: This method will fire the appropriate UIA Event for when the text selection has changed. The selection can change as a result of the caret moving or text being added to the currentLineText.
// TODO: Update the currentLineText from what is given by Quorum. That way the line down here can stay in sync with Quorum.
JNIEXPORT void JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32TextBoxTextSelectionChanged(JNIEnv *env, jobject obj, jlong textbox, jstring currentLineText, jint startIndex, jint endIndex)
{

	const char* nativeCurrentLineText = env->GetStringUTFChars(currentLineText, 0);
	WCHAR* wText = CreateWideStringFromUTF8Win32(nativeCurrentLineText);

	TextBoxControl* pTextBox = static_cast<TextBoxControl*>(LongToPtr((long)textbox));
	Range indices((int)startIndex, (int)endIndex);

	SendMessage(pTextBox->GetHWND(), QUORUM_SETTEXT, 0, (LPARAM)wText);
	SendMessage(pTextBox->GetHWND(), QUORUM_UPDATESELECTION, 0, (LPARAM)&indices);

	env->ReleaseStringUTFChars(currentLineText, nativeCurrentLineText);
}

// NativeWin32UpdateCaretPosition:
JNIEXPORT void JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32UpdateCaretPosition(JNIEnv *env, jobject obj, jlong textboxHWND, jstring fullText, jint caretIndex)
{
	UNREFERENCED_PARAMETER(env);
	UNREFERENCED_PARAMETER(obj);
	UNREFERENCED_PARAMETER(textboxHWND);
	UNREFERENCED_PARAMETER(fullText);
	UNREFERENCED_PARAMETER(caretIndex);

	/*HWND control = (HWND)textboxHWND;

	const char *nativeFullText = env->GetStringUTFChars(fullText, 0);

	SendMessage(control, QUORUM_UPDATECARET, (int)caretIndex, (LPARAM)nativeFullText);

	env->ReleaseStringUTFChars(fullText, nativeFullText);*/
	std::cout << "Textbox must be reworked. This function does nothing for now." << std::endl;
}

// NativeWin32InvokeButton: 
JNIEXPORT bool JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32InvokeButton(JNIEnv *env, jobject obj, jlong control)
{
	UNREFERENCED_PARAMETER(env);
	UNREFERENCED_PARAMETER(obj);
	
	Item* pControl = static_cast<Item*>(LongToPtr((long)control));

	if (pControl != NULL && pControl->GetHWND() != NULL)
	{
		SendMessage(pControl->GetHWND(), QUORUM_INVOKEBUTTON, 0, 0);
		return true;
	}
	
	return false;
}

// NativeWin32UpdateToggleStatus: 
JNIEXPORT bool JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32UpdateToggleStatus(JNIEnv *env, jobject obj, jlong control, jboolean selected)
{
	UNREFERENCED_PARAMETER(env);
	UNREFERENCED_PARAMETER(obj);

	Item* pControl = static_cast<Item*>(LongToPtr((long)control));

	bool nativeSelected = (bool)selected;

	if (pControl != NULL && pControl->GetHWND() != NULL)
	{
		if (nativeSelected)
			SendMessage(pControl->GetHWND(), QUORUM_INVOKEBUTTON, true, 0);
		else
			SendMessage(pControl->GetHWND(), QUORUM_INVOKEBUTTON, false, 0);
		return true;
	}
	
	return false;
}

JNIEXPORT bool JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32SelectMenuItem(JNIEnv * env, jobject obj, jlong selectedMenuItem)
{
	UNREFERENCED_PARAMETER(env);
	UNREFERENCED_PARAMETER(obj);
	
	MenuItemControl* pMenuItem = static_cast<MenuItemControl*>(LongToPtr((long)selectedMenuItem));
	MenuBarControl* pMenuBar = pMenuItem->GetParentMenuBar();

	if (!pMenuBar->HasFocus())
		SetFocus(pMenuBar->GetHWND());

	SendMessage(pMenuBar->GetHWND(), QUORUM_SELECTMENUITEM, 0, (LPARAM)pMenuItem);

	return true;
}

JNIEXPORT bool JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32DeselectMenuItem(JNIEnv * env, jobject obj, jlong deselectedMenuItem)
{
	UNREFERENCED_PARAMETER(env);
	UNREFERENCED_PARAMETER(obj);

	MenuItemControl* pMenuItem = static_cast<MenuItemControl*>(LongToPtr((long)deselectedMenuItem));

	pMenuItem->GetParentMenuBar()->SetSelectedMenuItem(nullptr);
	return true;
}

JNIEXPORT bool JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32SelectTreeItem(JNIEnv * env, jobject obj, jlong selectedTreeItem)
{
	UNREFERENCED_PARAMETER(env);
	UNREFERENCED_PARAMETER(obj);

	TreeItemControl* pTreeItem = static_cast<TreeItemControl*>(LongToPtr((long)selectedTreeItem));
	TreeControl* pTree = pTreeItem->GetParentTree();

	if (!pTree->HasFocus())
		SetFocus(pTree->GetHWND());

	SendMessage(pTree->GetHWND(), QUORUM_SELECTTREEITEM, 0, (LPARAM)pTreeItem);

	return true;
}

JNIEXPORT bool JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32MenuExpanded(JNIEnv *env, jobject obj, jlong menuItem)
{
	MenuItemControl* pMenuItem = static_cast<MenuItemControl*>(LongToPtr((long)menuItem));

	if (pMenuItem != NULL)
		pMenuItem->Expand();
	else
		return false;

	return true;
}

JNIEXPORT bool JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32MenuCollapsed(JNIEnv *env, jobject obj, jlong menuItem)
{
	MenuItemControl* pMenuItem = static_cast<MenuItemControl*>(LongToPtr((long)menuItem));

	if (pMenuItem != NULL)
		pMenuItem->Collapse();
	else
		return false;

	return true;
}

JNIEXPORT bool JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32SubtreeExpanded(JNIEnv *env, jobject obj, jlong treeItem)
{
	TreeItemControl* pTreeItem = static_cast<TreeItemControl*>(LongToPtr((long)treeItem));

	if (pTreeItem != NULL)
		pTreeItem->Expand();
	else
		return false;

	return true;
}

JNIEXPORT bool JNICALL Java_plugins_quorum_Libraries_Interface_AccessibilityManager_NativeWin32SubtreeCollapsed(JNIEnv *env, jobject obj, jlong treeItem)
{
	TreeItemControl* pTreeItem = static_cast<TreeItemControl*>(LongToPtr((long)treeItem));

	if (pTreeItem != NULL)
		pTreeItem->Collapse();
	else
		return false;

	return true;
}

#pragma endregion