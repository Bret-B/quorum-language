#include <iostream>
#include <string>
#include <strsafe.h>

#include "TextBoxControl.h"
#include "TextBoxTextAreaProvider.h"
#include "TextBoxProvider.h"

bool TextBoxControl::Initialized = false;

TextBoxControl::TextBoxControl(_In_reads_(lineCount) TextLine * lines, _In_ int lineCount, _In_ EndPoint caret) 
	: m_TextboxHWND(NULL), m_caretPosition(caret.line, caret.character), m_focused(false), m_pLines(lines), m_lineCount(lineCount), m_pTextboxName(L"Textbox")/*, m_Text(L"")*/, m_pTextBoxProvider(NULL)
{
	// Nothing to do here.
}

bool TextBoxControl::Initialize(_In_ HINSTANCE hInstance)
{
	WNDCLASSEXW wc;

	ZeroMemory(&wc, sizeof(wc));
	wc.cbSize = sizeof(wc);
	wc.style = CS_HREDRAW | CS_VREDRAW;
	wc.lpfnWndProc = StaticTextBoxControlWndProc;
	wc.hInstance = hInstance;
	wc.hCursor = LoadCursor(NULL, IDC_ARROW);
	wc.lpszClassName = L"QUORUM_TEXTBOX";

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

HWND TextBoxControl::Create(_In_ HINSTANCE instance, _In_ WCHAR* textboxName, _In_ WCHAR* textboxDescription, TextLine* quorumLines, _In_ EndPoint caret)
{

	if (!Initialized)
	{
		Initialized = Initialize(instance);
	}

	if (Initialized)
	{
		TextBoxControl * control = new TextBoxControl(quorumLines, _ARRAYSIZE(quorumLines), caret);

		control->m_TextboxHWND = CreateWindowExW(WS_EX_WINDOWEDGE,
			L"QUORUM_TEXTBOX",
			textboxName,
			WS_VISIBLE | WS_CHILD,
			-1,
			-1,
			1,
			1,
			GetMainWindowHandle(), // Parent window
			NULL,
			instance,
			static_cast<PVOID>(control)
		);

		if (control->m_TextboxHWND == 0)
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
			control->SetName(textboxName);

			if (UiaClientsAreListening())
			{
				control->GetTextBoxProvider();
			}

			return control->m_TextboxHWND;
		}
	}

	return 0; // Indicates failure to create window.

}

TextLine* TextBoxControl::GetLine(_In_ int line)
{
	if (line < 0 || line >= m_lineCount)
	{
		return NULL;
	}

	return &m_pLines[line];
}

//void TextBoxControl::SetLineText(_In_ int line, _In_ PCWSTR newText)
//{
//	if (line >= 0 || line < m_lineCount)
//	{
//		m_pLines[line].text = newText;
//	}
//
//}

int TextBoxControl::GetLineLength(_In_ int line)
{
	/*size_t strLength;
	if (FAILED(StringCchLengthW(m_pLines[line].text, 10000, &strLength)))
	{
		strLength = 0;
	}
	return static_cast<int>(strLength);*/
	return m_pLines[line].length;
}

int TextBoxControl::GetLineCount()
{
	return m_lineCount;
}

EndPoint TextBoxControl::GetTextboxEndpoint()
{
	EndPoint endOfText = { m_lineCount - 1, 0 };
	endOfText.character = GetLineLength(endOfText.line);
	return endOfText;
}

VARIANT TextBoxControl::GetAttributeAtPoint(_In_ EndPoint start, _In_ TEXTATTRIBUTEID attribute)
{
	VARIANT retval;
	VariantInit(&retval);

	// Many attributes are constant across the range, get them here
	if (attribute == UIA_AnimationStyleAttributeId)
	{
		retval.vt = VT_I4;
		retval.lVal = AnimationStyle_None;
	}
	else if (attribute == UIA_BackgroundColorAttributeId)
	{
		retval.vt = VT_I4;
		retval.lVal = GetSysColor(COLOR_WINDOW);
	}
	else if (attribute == UIA_BulletStyleAttributeId)
	{
		retval.vt = VT_I4;
		retval.lVal = BulletStyle_None;
	}
	else if (attribute == UIA_CapStyleAttributeId)
	{
		retval.vt = VT_I4;
		retval.lVal = CapStyle_None;
	}
	else if (attribute == UIA_CultureAttributeId)
	{
		retval.vt = VT_I4;
		retval.lVal = GetThreadLocale();
	}
	else if (attribute == UIA_HorizontalTextAlignmentAttributeId)
	{
		retval.vt = VT_I4;
		retval.lVal = HorizontalTextAlignment_Left;
	}
	else if (attribute == UIA_IndentationTrailingAttributeId)
	{
		retval.vt = VT_R8;
		retval.dblVal = 0.0;
	}
	else if (attribute == UIA_IsHiddenAttributeId)
	{
		retval.vt = VT_BOOL;
		retval.boolVal = VARIANT_FALSE;
	}
	else if (attribute == UIA_IsReadOnlyAttributeId)
	{
		// TODO: This should change depending on if the text from quorum is read-only.
		retval.vt = VT_BOOL;
		retval.boolVal = VARIANT_FALSE;
		//retval.boolVal = VARIANT_TRUE;
	}
	else if (attribute == UIA_IsSubscriptAttributeId)
	{
		retval.vt = VT_BOOL;
		retval.boolVal = VARIANT_FALSE;
	}
	else if (attribute == UIA_IsSuperscriptAttributeId)
	{
		retval.vt = VT_BOOL;
		retval.boolVal = VARIANT_FALSE;
	}
	else if (attribute == UIA_MarginLeadingAttributeId)
	{
		retval.vt = VT_R8;
		retval.dblVal = 0.0;
	}
	else if (attribute == UIA_MarginTrailingAttributeId)
	{
		retval.vt = VT_R8;
		retval.dblVal = 0.0;
	}
	else if (attribute == UIA_OutlineStylesAttributeId)
	{
		retval.vt = VT_I4;
		retval.lVal = OutlineStyles_None;
	}
	else if (attribute == UIA_OverlineColorAttributeId)
	{
		if (SUCCEEDED(UiaGetReservedNotSupportedValue(&retval.punkVal)))
		{
			retval.vt = VT_UNKNOWN;
		}
	}
	else if (attribute == UIA_OverlineStyleAttributeId)
	{
		retval.vt = VT_I4;
		retval.lVal = TextDecorationLineStyle_None;
	}
	else if (attribute == UIA_StrikethroughColorAttributeId)
	{
		if (SUCCEEDED(UiaGetReservedNotSupportedValue(&retval.punkVal)))
		{
			retval.vt = VT_UNKNOWN;
		}
	}
	else if (attribute == UIA_StrikethroughStyleAttributeId)
	{
		retval.vt = VT_I4;
		retval.lVal = TextDecorationLineStyle_None;
	}
	else if (attribute == UIA_TabsAttributeId)
	{
		if (SUCCEEDED(UiaGetReservedNotSupportedValue(&retval.punkVal)))
		{
			retval.vt = VT_UNKNOWN;
		}
	}
	else if (attribute == UIA_TextFlowDirectionsAttributeId)
	{
		retval.vt = VT_I4;
		retval.lVal = FlowDirections_RightToLeft;
	}
	else if (attribute == UIA_LinkAttributeId)
	{
		if (SUCCEEDED(UiaGetReservedNotSupportedValue(&retval.punkVal)))
		{
			retval.vt = VT_UNKNOWN;
		}
	}
	else if (attribute == UIA_IsActiveAttributeId)
	{
		retval.vt = VT_BOOL;
		retval.boolVal = m_focused ? VARIANT_TRUE : VARIANT_FALSE;
	}
	else if (attribute == UIA_SelectionActiveEndAttributeId)
	{
		retval.vt = VT_I4;
		retval.lVal = ActiveEnd_None;
	}
	else if (attribute == UIA_CaretPositionAttributeId)
	{
		retval.vt = VT_I4;
		if (m_caretPosition.character == 0)
		{
			retval.lVal = CaretPosition_BeginningOfLine;
		}
		else if (m_caretPosition.character == GetLineLength(m_caretPosition.line))
		{
			retval.lVal = CaretPosition_EndOfLine;
		}
		else
		{
			retval.lVal = CaretPosition_Unknown;
		}
	}
	else if (attribute == UIA_CaretBidiModeAttributeId)
	{
		retval.vt = VT_I4;
		retval.lVal = CaretBidiMode_LTR;
	}

	return retval;
}

bool TextBoxControl::StepCharacter(_In_ EndPoint start, _In_ bool forward, _Out_ EndPoint * end)
{
	*end = start;
	if (forward)
	{
		if (end->character >= GetLineLength(end->line))
		{
			if (end->line + 1 >= GetLineCount())
			{
				return false;
			}
			end->line++;
			end->character = 0;
		}
		else
		{
			end->character++;
		}
	}
	else
	{
		if (end->character <= 0)
		{
			if (end->line <= 0)
			{
				return false;
			}
			end->line--;
			end->character = GetLineLength(end->line);
		}
		else
		{
			end->character--;
		}
	}
	return true;
}

// This moves forward or backward by line. It targets the end of lines, so if
// in the middle of a line, it moves to the end, and if it's at the end of a line
// it moves to the end of the next line.

// When moving backwards it still targets the end of the line, moving to the end of
// the previous line, except when on the first line, where it will move to the
// beginning of the line, as there isn't a previous line.

// This is done so whether we walk forward or backwards, there is a consistent
// span given, from the end of one line, to the end of the next
bool TextBoxControl::StepLine(_In_ EndPoint start, _In_ bool forward, _Out_ EndPoint * end)
{
	*end = start;
	if (forward)
	{
		if (end->character >= GetLineLength(end->line))
		{
			if (end->line + 1 >= GetLineCount())
			{
				return false;
			}
			end->line++;
			end->character = GetLineLength(end->line);
		}
		else
		{
			end->character = GetLineLength(end->line);
		}
	}
	else
	{
		if (end->line <= 0)
		{
			if (end->character <= 0)
			{
				return false;
			}
			end->character = 0;
		}
		else
		{
			end->line--;
			end->character = GetLineLength(end->line);
		}
	}
	return true;
}

TextBoxProvider* TextBoxControl::GetTextBoxProvider()
{
	if (m_pTextBoxProvider == NULL)
	{
		m_pTextBoxProvider = new TextBoxProvider(this->m_TextboxHWND, this);
	}
	return m_pTextBoxProvider;
}

bool TextBoxControl::HasFocus()
{
	return m_focused;
}

EndPoint TextBoxControl::GetCaretPosition()
{
	return m_caretPosition;
}

WCHAR* TextBoxControl::GetName()
{
	return m_pTextboxName;
}

void TextBoxControl::SetName(_In_ WCHAR* name)
{
	m_pTextboxName = name;
}

//std::wstring TextBoxControl::GetText()
//{
//	return m_Text;
//}

LRESULT TextBoxControl::StaticTextBoxControlWndProc(_In_ HWND hwnd, _In_ UINT message, _In_ WPARAM wParam, _In_ LPARAM lParam)
{

	TextBoxControl * pThis = reinterpret_cast<TextBoxControl*>(GetWindowLongPtr(hwnd, GWLP_USERDATA));
	if (message == WM_NCCREATE)
	{
		CREATESTRUCT *createStruct = reinterpret_cast<CREATESTRUCT*>(lParam);
		pThis = reinterpret_cast<TextBoxControl*>(createStruct->lpCreateParams);
		SetWindowLongPtr(hwnd, GWLP_USERDATA, reinterpret_cast<LONG_PTR>(pThis));
	}

	if (message == WM_NCDESTROY)
	{
		pThis = NULL;
		SetWindowLongPtr(hwnd, GWLP_USERDATA, NULL);
	}

	if (pThis != NULL)
	{
		return pThis->TextBoxControlWndProc(hwnd, message, wParam, lParam);
	}

	return DefWindowProc(hwnd, message, wParam, lParam);
}


LRESULT CALLBACK TextBoxControl::TextBoxControlWndProc(_In_ HWND hwnd, _In_ UINT message, _In_ WPARAM wParam, _In_ LPARAM lParam)
{
	LRESULT lResult = 0;

	switch (message)
	{
	case WM_GETOBJECT:
	{
		// If the lParam matches the RootObjectId, send back the RawElementProvider
		if (static_cast<long>(lParam) == static_cast<long>(UiaRootObjectId))
		{
			// Register with UI Automation.
			IRawElementProviderSimple* provider = new TextBoxProvider(hwnd, this);
			if (provider != NULL)
			{
				lResult = UiaReturnRawElementProvider(hwnd, wParam, lParam, provider);
				provider->Release();
			}

			//lResult = UiaReturnRawElementProvider(hwnd, wParam, lParam, this->GetTextBoxProvider());
		}
		break;
	}
	case WM_DESTROY:
	{
		lResult = UiaReturnRawElementProvider(hwnd, 0, 0, NULL);
	}
	case WM_SETFOCUS:
	{
		SetControlFocus();
		break;
	}
	case WM_KILLFOCUS:
	{
		KillControlFocus();
		break;
	}
	case QUORUM_UPDATECARET:
	{

		// IQuorumAccessiblity passes the wstring by reference. So we cast lParam to a pointer to a wstring and then dereference it to assign it to the Textboxes m_Text wstring.
		//m_Text = *(std::wstring*)(lParam);

		UpdateCaret(/*(EndPoint*)lParam*/);
		break;
	}
	case QUORUM_SETNAME:
	{
		this->SetName((WCHAR*)lParam);
		break;
	}
	case QUORUM_SETTEXT:
	{
		// Set the text for the current text line.
		// Currently the textbox only maintains one textline at a time but
		// can and likely will need to be able to hold multiple lines.
		//this->m_pLines->text = (WCHAR*)lParam;
		break;
	}
	default:
		lResult = ForwardMessage(hwnd, message, wParam, lParam);
		break;
	}

	return lResult;
}

void TextBoxControl::SetControlFocus()
{
	NotifyFocusGained(this->m_TextboxHWND, this);
	m_focused = true;
}

void TextBoxControl::KillControlFocus()
{
	m_focused = false;
}

void TextBoxControl::UpdateCaret(/* _In_ EndPoint* caretPosition*/)
{
	//m_caretPosition = *caretPosition;
	NotifyCaretPositionChanged(this->m_TextboxHWND, this);
}