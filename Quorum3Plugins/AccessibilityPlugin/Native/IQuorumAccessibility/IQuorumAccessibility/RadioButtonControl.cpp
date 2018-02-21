#include <string>
#include <windowsx.h>
#include <iostream>

#include "RadioButtonControl.h"
#include "RadioButtonProvider.h"

bool RadioButtonControl::Initialized = false;

/**** Button methods ***/

// RadioButtonControl: Constructor. Sets the default values for the button.
RadioButtonControl::RadioButtonControl() : m_buttonProvider(NULL), m_buttonName(L"Radio Button"), m_isOn(false)
{
	// Nothing to do here.
}

// ~RadioButtonControl: Release the reference to the RadioButtonProvider if there is one.
RadioButtonControl::~RadioButtonControl()
{
	if (m_buttonProvider != NULL)
	{
		m_buttonProvider->Release();
		m_buttonProvider = NULL;
	}
}

// GetButtonProvider: Gets the UI Automation provider for this control or creates one.
RadioButtonProvider* RadioButtonControl::GetButtonProvider(_In_ HWND hwnd)
{
	if (m_buttonProvider == NULL)
	{
		m_buttonProvider = new (std::nothrow) RadioButtonProvider(hwnd, this);
	}
	return m_buttonProvider;
}

// GetHWND: Get the HWND associated with this control.
HWND RadioButtonControl::GetHWND()
{
	return m_buttonControlHWND;
}

// InvokeButton: Handle button click or invoke.
void RadioButtonControl::InvokeButton(_In_ HWND hwnd)
{

	if (UiaClientsAreListening())
	{
		// Raise an event.
		UiaRaiseAutomationEvent(GetButtonProvider(hwnd), UIA_Invoke_InvokedEventId);
	}

}

// RegisterButtonControl: Registers the RadioButtonControl with Windows API so that it can used and later be registered with UI Automation
bool RadioButtonControl::Initialize(_In_ HINSTANCE hInstance)
{
	WNDCLASSEXW wc;

	ZeroMemory(&wc, sizeof(wc));
	wc.cbSize = sizeof(wc);
	wc.style = CS_HREDRAW | CS_VREDRAW;
	wc.lpfnWndProc = StaticRadioButtonControlWndProc;
	wc.hInstance = hInstance;
	wc.hCursor = LoadCursor(NULL, IDC_ARROW);
	wc.lpszClassName = L"QUORUM_RADIOBUTTON";

	if (RegisterClassExW(&wc) == 0)
	{
		// An error occured. Output this error so it can be seen from Quorum.
		DWORD errorMessageID = ::GetLastError();

		LPSTR messageBuffer = nullptr;
		size_t size = FormatMessageA(FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_FROM_SYSTEM | FORMAT_MESSAGE_IGNORE_INSERTS,
			NULL, errorMessageID, MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), (LPSTR)&messageBuffer, 0, NULL);

		std::string message(messageBuffer, size);
		std::cout << "RegisterButtonControl Error " << errorMessageID << ": " << message << std::endl;
		fflush(stdout);

		//Free the buffer.
		LocalFree(messageBuffer);

		return false;
	}

	return true;
}

HWND RadioButtonControl::Create(_In_ HWND parent, _In_ HINSTANCE instance, _In_ WCHAR* buttonName, _In_ WCHAR* buttonDescription)
{

	if (!Initialized)
	{
		Initialized = Initialize(instance);
	}

	if (Initialized)
	{
		RadioButtonControl * control = new RadioButtonControl();

		control->m_buttonControlHWND = CreateWindowExW(WS_EX_WINDOWEDGE,
			L"QUORUM_RADIOBUTTON",
			buttonName,
			WS_VISIBLE | WS_CHILD,
			-1,
			-1,
			1,
			1,
			parent, // Parent window
			NULL,
			instance,
			static_cast<PVOID>(control));

		if (control->m_buttonControlHWND == 0)
		{
			DWORD errorMessageID = ::GetLastError();

			LPSTR messageBuffer = nullptr;
			size_t size = FormatMessageA(FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_FROM_SYSTEM | FORMAT_MESSAGE_IGNORE_INSERTS,
				NULL, errorMessageID, MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), (LPSTR)&messageBuffer, 0, NULL);

			std::string message(messageBuffer, size);
			std::cout << "Native Code - CreateWindowExW Error " << errorMessageID << ": " << message;
			fflush(stdout);

			//Free the buffer.
			LocalFree(messageBuffer);
		}
		else
		{
			control->SetName(buttonName);
			return control->m_buttonControlHWND;
		}
	}

	return 0; // Indicates failure to create window.

}

WCHAR* RadioButtonControl::GetName()
{
	return m_buttonName;
}

void RadioButtonControl::SetName(_In_ WCHAR* name)
{
	m_buttonName = name;
}

void RadioButtonControl::SetFocus()
{
	m_buttonProvider->NotifyFocusGained();
}

void RadioButtonControl::SetState(_In_ bool controlState)
{
	m_isOn = controlState;
	if (m_isOn)
		m_buttonProvider->Select();

	if (UiaClientsAreListening())
	{
		UiaRaiseAutomationEvent(m_buttonProvider, UIA_AutomationPropertyChangedEventId);
	}
}

bool RadioButtonControl::GetState()
{
	return m_isOn;
}




LRESULT RadioButtonControl::StaticRadioButtonControlWndProc(_In_ HWND hwnd, _In_ UINT message, _In_ WPARAM wParam, _In_ LPARAM lParam)
{
	RadioButtonControl * pThis = reinterpret_cast<RadioButtonControl*>(GetWindowLongPtr(hwnd, GWLP_USERDATA));
	if (message == WM_NCCREATE)
	{
		CREATESTRUCT *createStruct = reinterpret_cast<CREATESTRUCT*>(lParam);
		pThis = reinterpret_cast<RadioButtonControl*>(createStruct->lpCreateParams);
		SetWindowLongPtr(hwnd, GWLP_USERDATA, reinterpret_cast<LONG_PTR>(pThis));
	}

	if (message == WM_NCDESTROY)
	{
		pThis = NULL;
		SetWindowLongPtr(hwnd, GWLP_USERDATA, NULL);
	}

	if (pThis != NULL)
	{
		return pThis->RadioButtonControlWndProc(hwnd, message, wParam, lParam);
	}

	return DefWindowProc(hwnd, message, wParam, lParam);
}

// Control window procedure.
LRESULT CALLBACK RadioButtonControl::RadioButtonControlWndProc(_In_ HWND hwnd, _In_ UINT message, _In_ WPARAM wParam, _In_ LPARAM lParam)
{

	switch (message)
	{
	case WM_GETOBJECT:
	{
		if (static_cast<long>(lParam) == static_cast<long>(UiaRootObjectId))
		{
			// Register with UI Automation.
			return UiaReturnRawElementProvider(hwnd, wParam, lParam, this->GetButtonProvider(this->m_buttonControlHWND));
		}

		return 0;
	}
	case CUSTOM_SETFOCUS:
	{
		this->SetFocus();
		return 0;
	}
	case CUSTOM_INVOKEBUTTON:
	{
		this->InvokeButton(hwnd);

		bool state = static_cast<bool>(wParam);

		this->SetState(state);

		return 0;
	}
	case CUSTOM_SETNAME:
	{
		this->SetName((WCHAR*)lParam);
	}

	break;
	}  // switch (message)

	return DefWindowProc(hwnd, message, wParam, lParam);
}
