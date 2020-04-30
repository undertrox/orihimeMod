Orihime-Mod
===

This is a little mod/addon for the software Orihime by @MT777. It adds the possibility 
to use keyboard shortcuts.

How to add a keybind
--
1. If you just downloaded this software, start it once and close it again, so the config
file will be generated. After that, you will find a file called `orihimeKeybinds.cfg` in 
the same folder the jar file is in.
2. Open `orihimeKeybinds.cfg` with any Text editor (Microsoft Word is not a text editor) 
and make sure `orihimeKeybinds.showKeybindIdTooltips` is set to `true` (If you're using 
the latest version, it should be located at the very end of the file).
3. Start the software and put your mouse on top of the button that you want to create a 
keybind for. A Tooltip with the text `Keybind ID: <some number>` will appear.
4. In `orihimeKeybinds.cfg`, add a new line: 
```
orihimeKeybinds.button.<keybind id>=<key combination>
```
Here are some examples: 
```
orihimeKeybinds.button.46=ctrl+S
orihimeKeybinds.button.100=K
orihimeKeybinds.button.42=ctrl+alt+A
orihimeKeybinds.button.10=shift+L
```
If you want to use special keys like Enter, Space or the arrow keys, you have to use 
`kc<keycode>` instead of the letter. You can find a list of all keycodes 
[here](https://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list/31637206#answer-31637206).

Here are some examples for that:

`ctrl+kc32` would be Ctrl+Space, `shift+kc10` is Shift+Enter, and `kc127` is the Del key