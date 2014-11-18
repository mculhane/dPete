import sublime, sublime_plugin


class PeteStartCommand(sublime_plugin.TextCommand):

    window = None
    tasks = None
    output = None
    edit = None

    def run(self, edit):
        self.edit = edit

        sublime.run_command("new_window")
        self.window = sublime.active_window()

        self.tasks = self.window.open_file("~/tasks.txt")

        self.window.set_layout({
            "cols": [0,1], 
            "rows": [0,0.5,1], 
            "cells": [[0,0,1,1], [0,1,1,2]]
            })

        self.output = self.window.new_file()

        self.window.set_view_index(self.tasks, self.window.active_group(), 0)
        self.window.set_view_index(self.output, self.window.active_group()+1, 0)

        self.showPanel()

    
    def addTask(self, s):
        self.tasks.run_command("pete_insert", {"task": s})
        self.showPanel()

    def showPanel(self):
        self.window.show_input_panel("Add task:", 
            "", 
            self.addTask, None, self.showPanel)

    class PeteProcessTasks(sublime_plugin.EventListener):

        def on_post_save_async(view):
            pass

    class PeteInsertCommand(sublime_plugin.TextCommand):

        def run(self, edit, task=""):
            self.view.insert(edit, self.view.size(), task + "\n")




