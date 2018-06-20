#include <windows.h>
#include <UIAutomation.h>

#include "CustomMessages.h"
#include "Subtree.h"
#include "Item.h"

class TreeItemProvider;
class TreeControl;

class TreeItemControl : public Subtree, public Item
{
public:
	TreeItemControl(_In_ std::wstring menuItemName, _In_ std::wstring treeItemDescription, _In_ bool isSubtree, _In_ int uniqueId, _In_opt_ TreeItemControl* parentMenuItem, _In_ TreeControl* parentMenuBar);
	virtual ~TreeItemControl();

	TreeControl* GetParentTree();
	void SetParentMenuBar(_In_ TreeControl* menuBar);
	TreeItemControl* GetParentTreeItem();
	TreeItemProvider* GetTreeItemProvider();

	bool IsSubtree();
	Subtree* GetSubtree();

	int GetId();

	int GetTreeItemIndex();
	void SetTreeItemIndex(_In_ int index);

	bool HasFocus();

	void Expand();
	void Collapse();

private:

	// The id that uniquely identifies this item within an instance of a MenuBar or MenuItem collection.
	int m_uniqueId;

	// Where this MenuItem is located in the collection.
	int m_myIndex;

	bool m_isSubtree;

	// The provider for this MenuItem
	TreeItemProvider* m_pTreeItemProvider;
	
	// The Tree that this TreeItem belongs to. This won't always be
	// this TreeItem's direct parent but it is always an ancestor.
	TreeControl* m_pParentTree;

	// The TreeItem that this TreeItem is nested in. This can be null.
	// If this is null then the Tree is the direct parent of this control.
	TreeItemControl* m_pParentTreeItem;

	void SetControlFocus(_In_ bool focused);
};