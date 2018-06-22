#include <windows.h>
#include <UIAutomation.h>

#include "CustomMessages.h"
#include "Item.h"

class ButtonProvider;

class ButtonControl : public Item
{
public:
	ButtonControl(_In_ WCHAR* name, _In_ WCHAR* description);
	virtual ~ButtonControl();

	static ButtonControl* Create(_In_ HINSTANCE instance, _In_ HWND parentWindow, _In_ WCHAR* buttonName, _In_ WCHAR* buttonDescription);

	ButtonProvider* GetButtonProvider();
	void InvokeButton();

	bool HasFocus();

private:
	static LRESULT CALLBACK StaticButtonControlWndProc(_In_ HWND hwnd, _In_ UINT message, _In_ WPARAM wParam, _In_ LPARAM lParam);
	LRESULT CALLBACK ButtonControlWndProc(_In_ HWND hwnd, _In_ UINT message, _In_ WPARAM wParam, _In_ LPARAM lParam);


	static bool Initialize(_In_ HINSTANCE hInstance);
	static bool Initialized;

	void SetControlFocus(_In_ bool focused);

	bool m_focused;

	ButtonProvider* m_buttonProvider;

};