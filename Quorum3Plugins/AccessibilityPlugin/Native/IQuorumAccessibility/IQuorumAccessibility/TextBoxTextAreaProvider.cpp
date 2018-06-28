#include <UIAutomation.h>
#include "../IQuorumAccessibility/Header/jni.h"

#include "TextBoxTextAreaProvider.h"
#include "TextBoxControl.h"
#include "TextBoxTextRange.h"
#include "TextBoxProvider.h"

#include <iostream>

void NotifyCaretPositionChanged(_In_ HWND hwnd, _In_ TextBoxControl *control)
{
	TextBoxTextAreaProvider *eventControl = new TextBoxTextAreaProvider(hwnd, control);
	if (eventControl != NULL)
	{
		UiaRaiseAutomationEvent(eventControl, UIA_Text_TextSelectionChangedEventId);
		UiaRaiseAutomationEvent(eventControl, UIA_AutomationFocusChangedEventId);
		eventControl->Release();
	}
	else
	{
		// This is an error. Should probably be dealt with somehow.
	}
}

void NotifyFocusGained(_In_ HWND hwnd, _In_ TextBoxControl *control)
{
	TextBoxTextAreaProvider *eventControl = new TextBoxTextAreaProvider(hwnd, control);
	if (eventControl != NULL)
	{
		UiaRaiseAutomationEvent(eventControl, UIA_AutomationFocusChangedEventId);
		eventControl->Release();
	}
	else
	{
		// This is an error. Should probably be dealt with somehow.
	}
}

TextBoxTextAreaProvider::TextBoxTextAreaProvider(_In_ HWND hwnd, _In_ TextBoxControl *control) : m_refCount(1), m_TextBoxControlHWND(hwnd), m_pTextBoxControl(control)
{
	// Nothing to do here.
}

TextBoxTextAreaProvider::~TextBoxTextAreaProvider()
{
}

// =========== IUnknown implementation.

IFACEMETHODIMP_(ULONG) TextBoxTextAreaProvider::AddRef()
{
	return InterlockedIncrement(&m_refCount);
}

IFACEMETHODIMP_(ULONG) TextBoxTextAreaProvider::Release()
{
	long val = InterlockedDecrement(&m_refCount);
	if (val == 0)
	{
		delete this;
	}
	return val;
}

IFACEMETHODIMP TextBoxTextAreaProvider::QueryInterface(_In_ REFIID riid, _Outptr_ void ** ppInterface)
{
	if (riid == __uuidof(IUnknown))
	{
		*ppInterface = static_cast<IRawElementProviderSimple*>(this);
	}
	else if (riid == __uuidof(IRawElementProviderSimple))
	{
		*ppInterface = static_cast<IRawElementProviderSimple*>(this);
	}
	else if (riid == __uuidof(IRawElementProviderFragment))
	{
		*ppInterface = static_cast<IRawElementProviderFragment*>(this);
	}
	else if (riid == __uuidof(ITextProvider))
	{
		*ppInterface = static_cast<ITextProvider*>(this);
	}
	else
	{
		*ppInterface = NULL;
		return E_NOINTERFACE;
	}

	(static_cast<IUnknown*>(*ppInterface))->AddRef();
	return S_OK;
}

// =========== IRawElementProviderSimple implementation

IFACEMETHODIMP TextBoxTextAreaProvider::get_ProviderOptions(_Out_ ProviderOptions *pRetVal)
{
	if (!IsWindow(m_TextBoxControlHWND))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	*pRetVal = ProviderOptions_ServerSideProvider | ProviderOptions_UseComThreading;
	return S_OK;
}

IFACEMETHODIMP TextBoxTextAreaProvider::GetPatternProvider(PATTERNID patternId, _Outptr_result_maybenull_ IUnknown ** pRetVal)
{
	if (!IsWindow(m_TextBoxControlHWND))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	*pRetVal = NULL;

	if (patternId == UIA_TextPatternId)
	{
		*pRetVal = static_cast<ITextProvider *>(this);
		(*pRetVal)->AddRef();
	}

	return S_OK;
}

IFACEMETHODIMP TextBoxTextAreaProvider::GetPropertyValue(PROPERTYID propertyId, _Out_ VARIANT * pRetVal)
{
	if (!IsWindow(m_TextBoxControlHWND))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	if (propertyId == UIA_LocalizedControlTypePropertyId)
	{
		pRetVal->vt = VT_BSTR;
		pRetVal->bstrVal = SysAllocString(L"textbox");
	}
	else if (propertyId == UIA_NamePropertyId)
	{
		pRetVal->vt = VT_BSTR;
		pRetVal->bstrVal = SysAllocString(m_pTextBoxControl->GetName());
	}
	else if (propertyId == UIA_HelpTextPropertyId)
	{
		pRetVal->vt = VT_BSTR;
		pRetVal->bstrVal = SysAllocString(m_pTextBoxControl->GetDescription());
	}
	else if (propertyId == UIA_ControlTypePropertyId)
	{
		pRetVal->vt = VT_I4;
		pRetVal->lVal = UIA_EditControlTypeId;
		//pRetVal->lVal = UIA_DocumentControlTypeId;
	}
	else if (propertyId == UIA_IsEnabledPropertyId)
	{
		// This tells the screen reader whether or not the control can be interacted with.
		// Hardcoded to true but this property could be dynamic depending on the needs of the Quorum GUI.
		pRetVal->vt = VT_BOOL;
		pRetVal->boolVal = VARIANT_TRUE;
	}
	else if (propertyId == UIA_IsKeyboardFocusablePropertyId)
	{
		// Tells the screen reader that this control is capable of getting keyboard focus.
		// This isn't enough for the screen reader to announce the control's existence to the user when it gains focus in Quorum.
		// UIA_HasKeyboardFocusPropertyId is responsible for whether or not the screen reader announces that this control gained focus.
		pRetVal->vt = VT_BOOL;
		pRetVal->boolVal = VARIANT_TRUE;
	}
	else if (propertyId == UIA_HasKeyboardFocusPropertyId)
	{
		// This tells the screen reader whether or not this control has Keyboard focus. Normally, only one control/window is allowed to have keyboard focus at a time
		// but by lying and having every instance of this control report that it has keyboard focus then we don't have to mantain what has focus on the native level.
		pRetVal->vt = VT_BOOL;
		pRetVal->boolVal = VARIANT_TRUE;
	}
	else if (propertyId == UIA_IsPasswordPropertyId)
	{
		/*
		Identifies the IsPassword property, which is a Boolean value that indicates whether
		the automation element contains protected content or a password.

		When the IsPassword property is TRUE and the element has the keyboard focus, a client
		application should disable keyboard echoing or keyboard input feedback that may expose
		the user's protected information. Attempting to access the Value property of the protected
		element (edit control) may cause an error to occur.

		For now, this will be hardcoded as FALSE but in the future this can be maintained in Quorum and
		updated down here when textboxes contain protected info.
		*/

		pRetVal->vt = VT_BOOL;
		pRetVal->boolVal = VARIANT_FALSE;
	}
	//else if (propertyId == UIA_LabeledByPropertyId)
	//{
	/*
	If there is a static text label associated with this control, then this property must expose
	a reference to this control. If the TextBox is a subcomponent of another control,
	it will not have a LabeledBy property set. E.g., it's contained within a tab.
	*/

	//}
	else
	{
		pRetVal->vt = VT_EMPTY;
		// UI Automation will attempt to get the property from the host window provider.
		// If the property is found then it will have the UI Automation defaults listed in the Microsoft Developer's Network documentation.
		// More often than not the default values are responsible for a control not functioning properly with a screen reader.
	}

	return S_OK;
}

IFACEMETHODIMP TextBoxTextAreaProvider::get_HostRawElementProvider(_Outptr_result_maybenull_ IRawElementProviderSimple ** pRetVal)
{
	if (!IsWindow(m_TextBoxControlHWND))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	*pRetVal = NULL;
	return S_OK;
}

// =========== IRawElementProviderFragment implementation

IFACEMETHODIMP TextBoxTextAreaProvider::Navigate(NavigateDirection direction, _Outptr_result_maybenull_ IRawElementProviderFragment ** pRetVal)
{
	if (!IsWindow(m_TextBoxControlHWND))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	*pRetVal = NULL;

	HRESULT hr = S_OK;
	if (direction == NavigateDirection_Parent)
	{
		*pRetVal = new TextBoxProvider(m_TextBoxControlHWND, m_pTextBoxControl);
		if (*pRetVal == NULL)
		{
			hr = E_OUTOFMEMORY;
		}
	}

	// For the other directions (NextSibling, firstchild, lastchild, previous) the default of NULL is correct
	return hr;
}

IFACEMETHODIMP TextBoxTextAreaProvider::GetRuntimeId(_Outptr_result_maybenull_ SAFEARRAY ** pRetVal)
{
	if (!IsWindow(m_TextBoxControlHWND))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	// AppendRuntimeId is a magic Number that tells UIAutomation to Append its own Runtime ID(From the HWND)
	int rId[] = { UiaAppendRuntimeId, -1 };

	HRESULT hr = S_OK;
	*pRetVal = BuildIntSafeArray(rId, 2);
	if (*pRetVal == NULL)
	{
		hr = E_OUTOFMEMORY;
	}

	return hr;
}

IFACEMETHODIMP TextBoxTextAreaProvider::get_BoundingRectangle(_Out_ UiaRect * pRetVal)
{
	if (!IsWindow(m_TextBoxControlHWND))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	// For now we aren't painting a rectangle for the provider
	// In the future this could be implemented so the screen reader cursor follows along
	// with what is being said.
	pRetVal->left = 0;
	pRetVal->top = 0;
	pRetVal->width = 0;
	pRetVal->height = 0;
	return S_OK;
}

IFACEMETHODIMP TextBoxTextAreaProvider::GetEmbeddedFragmentRoots(_Outptr_result_maybenull_ SAFEARRAY ** pRetVal)
{
	if (!IsWindow(m_TextBoxControlHWND))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	*pRetVal = NULL;
	return S_OK;
}

IFACEMETHODIMP TextBoxTextAreaProvider::SetFocus()
{
	if (!IsWindow(m_TextBoxControlHWND))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	return S_OK;
}

IFACEMETHODIMP TextBoxTextAreaProvider::get_FragmentRoot(_Outptr_result_maybenull_ IRawElementProviderFragmentRoot ** pRetVal)
{
	if (!IsWindow(m_TextBoxControlHWND))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	*pRetVal = new TextBoxProvider(m_TextBoxControlHWND, m_pTextBoxControl);
	return (*pRetVal == NULL) ? E_OUTOFMEMORY : S_OK;
}


// =========== ITextProvider implementation

// get_SupportedTextSelection: Retrieves a value that specifies the type of text selection that is supported by the control.
IFACEMETHODIMP TextBoxTextAreaProvider::get_SupportedTextSelection(_Out_ SupportedTextSelection * pRetVal)
{
	if (!IsWindow(m_TextBoxControlHWND))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	*pRetVal = SupportedTextSelection_Single;
	return S_OK;
}

// GetSelection: Retrieves a collection of text ranges that represents the currently selected text in a text-based control.
//				 For this control, the selection will either be a single text range or a degenerate text range.
//				 Note: A degenerate text range is an empty text range. Which means its EndPoint begin and end are equal.
IFACEMETHODIMP TextBoxTextAreaProvider::GetSelection(_Outptr_result_maybenull_ SAFEARRAY ** pRetVal)
{
	if (!IsWindow(m_TextBoxControlHWND))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	Range caretRange = m_pTextBoxControl->GetSelectionRange();


	ITextRangeProvider *selectionRangeProvider = new TextBoxTextRange(m_TextBoxControlHWND, m_pTextBoxControl, caretRange);
	HRESULT hr = S_OK;
	if (selectionRangeProvider == NULL)
	{
		hr = E_OUTOFMEMORY;
	}
	else
	{
		*pRetVal = SafeArrayCreateVector(VT_UNKNOWN, 0, 1);
		if (*pRetVal == NULL)
		{
			hr = E_OUTOFMEMORY;
		}
		else
		{
			long index = 0;
			hr = SafeArrayPutElement(*pRetVal, &index, selectionRangeProvider);
			if (FAILED(hr))
			{
				SafeArrayDestroy(*pRetVal);
				*pRetVal = NULL;
			}
		}
		selectionRangeProvider->Release();
	}

	return hr;
}

// GetVisibleRanges: Retrieves an array of disjoint text ranges from a text-based control where each text range represents a contiguous span of visible text.
IFACEMETHODIMP TextBoxTextAreaProvider::GetVisibleRanges(_Outptr_result_maybenull_ SAFEARRAY ** pRetVal)
{
	if (!IsWindow(m_TextBoxControlHWND))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	// Not Implemented yet.
	*pRetVal = NULL;
	return S_OK;
}

// RangeFromChild: Retrieves a text range enclosing a child element such as an image, hyperlink, or other embedded object. 
IFACEMETHODIMP TextBoxTextAreaProvider::RangeFromChild(_In_opt_ IRawElementProviderSimple * childElement, _Outptr_result_maybenull_ ITextRangeProvider ** pRetVal)
{
	UNREFERENCED_PARAMETER(childElement);
	if (!IsWindow(m_TextBoxControlHWND))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	// There are no children of this text control
	*pRetVal = NULL;
	return S_OK;
}

// RangeFromPoint: Returns the degenerate (empty) text range nearest to the specified screen coordinates.
IFACEMETHODIMP TextBoxTextAreaProvider::RangeFromPoint(UiaPoint point, _Outptr_result_maybenull_ ITextRangeProvider ** pRetVal)
{
	if (!IsWindow(m_TextBoxControlHWND))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	/*
	This implementation will always report the closest range from a given screen coordinate point as before the
	first character on the first line of text. This will stop narrator from saying "No item in view" instead of
	the textbox's actual name. However, this comes with the trade off that a mouse click at any arbitrary location
	within the textbox won't ever result in the character to the right of the caret being read aloud.

	If we wanted to get rid of that trade off then that'd be a fairly tricky implementation because we'd need to solve
	the problem that this function is supposed to solve in Quorum, pass that down along with a mouse click event, and then
	this function would look for it in the textbox control down here instead of solving the problem itself.

	TODO: Implement this in Quorum and pass down the required info when mouse clicking is implemented in the Quorum Textbox.
	*/
	UNREFERENCED_PARAMETER(point); // This will never be used. Instead we get the point from Quorum.
	Range closestRange = { { 0 },{ 0 } };
	*pRetVal = new TextBoxTextRange(m_TextBoxControlHWND, m_pTextBoxControl, closestRange);
	return (*pRetVal == NULL) ? E_OUTOFMEMORY : S_OK;

}

// get_DocumentRange: Retrieves a text range that encloses the main text of a document.
//		NOTE: When this function is implemented it is the reason that a screen reader will automatically
//			  read all text contained within the textbox. 
IFACEMETHODIMP TextBoxTextAreaProvider::get_DocumentRange(_Outptr_result_maybenull_ ITextRangeProvider ** pRetVal)
{
	if (!IsWindow(m_TextBoxControlHWND))
	{
		return UIA_E_ELEMENTNOTAVAILABLE;
	}

	// Get the full text range that encompasses the document. From the first character on the first line
	// all the way to the last character on the last line.
	Range fullDocumentRange = { { 0 }, m_pTextBoxControl->GetTextboxEndpoint() };

	*pRetVal = new TextBoxTextRange(m_TextBoxControlHWND, m_pTextBoxControl, fullDocumentRange);
	return (*pRetVal == NULL) ? E_OUTOFMEMORY : S_OK;

}

