Orihime-Mod
===

This is a little mod/addon for the software Orihime by @MT777. It adds the possibility 
to use keyboard shortcuts and a lot of other features. Download the jar file [here](https://github.com/undertrox/orihimeMod/releases/download/v1.0.0/orihimeMod-1.0.0.jar)
(version 1.0.0).

I am not actively working on this project anymore, however [OriEdita](https://oriedita.github.io/) has all the features of orihimeMod and even more, and it is still being developed.

Features
---

### Keybinds

This was the original purpose of this mod, but over time, a lot of other features were added as well.

### Autosave

By default, every 5 minutes, a copy of what you are currently working on will be saved to the `orihimeMod-Autosave` folder in both .orh and .cp format.
Files in the autosave folder that are older than 1 day are automatically deleted.

The autosave interval and the age at which files get deleted can be configured using `orihimeMod.autoSave.interval` and  `orihimeMod.autoSave.maxAge`

### Tooltips

Every button in the software has a short description that gets shown when hovered over.

### Self intersection Highlighting

If a CP is locally flatfoldable, but still cannot be folded, the sections of the CP that would have to intersect to fold it are highlighted in red

### Text on Crease Patterns

You can add Text on CPs. it will be saved in a separate file as to not break compatibility with oripa and different versions of orihime(Mod)

### Various Quality-of-Life Improvements

For example:
 - Asking whether to save before closing
 - More intuitive saving behaviour for new users
 - easier access to exporting in different formats
 - better behavior when selecting
 - warning when saving as .orh didn't work
 - ...


How to add a keybind 
--

### Version 0.1.5 and newer

1. Right click on any button or checkbox
2. Click add Keybind
3. Input the Keybind and click save

### Version 0.1.4 and older
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
