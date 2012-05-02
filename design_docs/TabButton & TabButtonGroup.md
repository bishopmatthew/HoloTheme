# TabButton & TabButtonGroup

## TabButton
	- support both icon-based and text based tabs
		- basic background using the Rect trick to get an under or overline
	- tabs can appear at the top or the bottom
	- integrate with Tab, TabPager, TabAdapter, etc.

### Layout
	- ImageTab
		- FrameLayout
		- ImageView w/ duplicateParentState set to true
	- TextTab
		- FrameLayout
		- FontText w/ duplicateParentState set to true
	- Text&ImageTab?
		- 	
	- If no icon or text is set via xml, just have the tab background show up