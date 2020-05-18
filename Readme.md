Orihime-Mod
===

This is a little mod/addon for the software Orihime by @MT777. It adds the possibility 
to use keyboard shortcuts. Download the jar file [here](https://github.com/undertrox/orihimeMod/releases/download/v0.1.5/orihimeMod-0.1.5.jar)
(version 0.1.5)

How to add a keybind
--
1. If you just downloaded this software, start it once and close it again, so the config
file will be generated. After that, you will find a file called `orihimeKeybinds.cfg` in 
the same folder the jar file is in.
2. Open `orihimeKeybinds.cfg` with any Text editor (Microsoft Word is not a text editor) 
and make sure `orihimeKeybinds.showKeybindIdTooltips` is set to `true` (If you're using 
the latest version, it should be located at the very end of the file).
3. Start the software and put your mouse on top of the button that you want to create a 
keybind for. A Tooltip with the text `Keybind ID: <some text>` will appear.
4. In `orihimeKeybinds.cfg`, add a new line: 
```
<keybind id>=<key combination>
```
Here are some examples: 
```
orihimeKeybinds.button.46=ctrl+S
orihimeKeybinds.button.100=K
orihimeKeybinds.checkbox.42=ctrl+alt+A
orihimeKeybinds.button.10=shift+L
```
If you want to use special keys like Enter, Space or the arrow keys, you have to use 
`kc<keycode>` instead of the letter. You can find a list of all keycodes 
[here](https://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list/31637206#answer-31637206).

Here are some examples for that:

`ctrl+kc32` would be Ctrl+Space, `shift+kc10` is Shift+Enter, and `kc127` is 
the Del key.

Default keybinds
--
Basics:

|Action|Shortcut|
|------|---|
|Open|  Ctrl+O |
|Save| Ctrl+S|
|Help| H|
|Undo| Ctrl+Z|
|Redo| Ctrl+Shift+Z|

Folds:

|Action|Shortcut|
|------|---|
|Mountain Fold|  M |
|Valley Fold| V|
|Line| L|
|Extend Line | E |
|Angle Bisector| B|
|Rabbit Ear |Ctrl+B|
|Perpendicular Line| P|
|Reflection| R|
|Multiple Reflections|Ctrl+R|
|Flatfold Vertex|N|
|Grid fill| G|
|Mirror along a Line| Ctrl+G|
|Mirror| Ctrl+M|
|Change Linetype|C|
|Delete unused Vertices| Ctrl+V

Base Folding

|Action|Shortcut|
|------|---|
|Fold Base| F |
|Flip folded Base| Ctrl+Shift+F|
|Discard folded Base| Ctrl+F|

Miscellaneous

|Action|Shortcut|
|------|---|
|Selection Tool| S |
|Delete selected Lines| Del|
|Delete Lines| Ctrl+D|