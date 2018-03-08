class TextBoxControl;

class TextBoxTextAreaProvider : public ITextProvider,
								public IRawElementProviderSimple,
								public IRawElementProviderFragment
{
	public:

		TextBoxTextAreaProvider(_In_ HWND hwnd, _In_ TextBoxControl *control);
		virtual ~TextBoxTextAreaProvider();

		// IUnknown methods
		IFACEMETHODIMP_(ULONG) AddRef();
		IFACEMETHODIMP_(ULONG) Release();
		IFACEMETHODIMP QueryInterface(_In_ REFIID riid, _Outptr_ void** ppInterface);

		// IRawElementProviderSimple methods
		IFACEMETHODIMP get_ProviderOptions(_Out_ ProviderOptions * pRetVal);
		IFACEMETHODIMP GetPatternProvider(PATTERNID patternId, _Outptr_result_maybenull_ IUnknown ** pRetVal);
		IFACEMETHODIMP GetPropertyValue(PROPERTYID propertyId, _Out_ VARIANT * pRetVal);
		IFACEMETHODIMP get_HostRawElementProvider(_Outptr_result_maybenull_ IRawElementProviderSimple ** pRetVal);

		// IRawElementProviderFragment methods
		IFACEMETHODIMP Navigate(NavigateDirection direction, _Outptr_result_maybenull_ IRawElementProviderFragment ** pRetVal);
		IFACEMETHODIMP GetRuntimeId(_Outptr_result_maybenull_ SAFEARRAY ** pRetVal);
		IFACEMETHODIMP get_BoundingRectangle(_Out_ UiaRect * pRetVal);
		IFACEMETHODIMP GetEmbeddedFragmentRoots(_Outptr_result_maybenull_ SAFEARRAY ** pRetVal);
		IFACEMETHODIMP SetFocus();
		IFACEMETHODIMP get_FragmentRoot(_Outptr_result_maybenull_ IRawElementProviderFragmentRoot ** pRetVal);

		// TextProvider methods
		IFACEMETHODIMP GetSelection(_Outptr_result_maybenull_ SAFEARRAY** retVal);
		IFACEMETHODIMP GetVisibleRanges(_Outptr_result_maybenull_ SAFEARRAY ** retVal);
		IFACEMETHODIMP RangeFromChild(_In_opt_ IRawElementProviderSimple * childElement, _Outptr_result_maybenull_ ITextRangeProvider ** retVal);
		IFACEMETHODIMP RangeFromPoint(UiaPoint screenLocation, _Outptr_result_maybenull_ ITextRangeProvider ** retVal);
		IFACEMETHODIMP get_DocumentRange(_Outptr_result_maybenull_ ITextRangeProvider ** retVal);
		IFACEMETHODIMP get_SupportedTextSelection(_Out_ SupportedTextSelection * retVal);


	private:
		ULONG m_refCount;

		HWND m_TextBoxControlHWND;
		TextBoxControl* m_pTextBoxControl;
};

void NotifyFocusGained(_In_ HWND hwnd, _In_ TextBoxControl *control);
void NotifyCaretPositionChanged(_In_ HWND hwnd, _In_ TextBoxControl *control);