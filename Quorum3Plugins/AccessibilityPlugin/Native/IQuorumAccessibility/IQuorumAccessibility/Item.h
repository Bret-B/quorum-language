#include <windows.h>
#include <UIAutomation.h>
#include <string>

#include "Resources.h"
#include "../IQuorumAccessibility/Header/jni.h"

#ifndef Item_HEADER
#define Item_HEADER 

class Item
{
public:
	Item(JNIEnv* env, std::wstring controlName, std::wstring controlDescription, jobject jItem);
	virtual void SetControlFocus(_In_ bool Focused);
	virtual bool HasFocus();

	HWND GetHWND();

	void SetName(_In_ std::wstring name);
	const WCHAR* GetName();

	void SetDescription(_In_ std::wstring description);
	const WCHAR* GetDescription();

	jobject GetMe();

protected:
	std::wstring m_ControlName;
	std::wstring m_ControlDescription;
	HWND   m_ControlHWND;

	jobject javaItem;
};

#endif


