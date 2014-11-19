import sublime
import sublime_plugin


class PeteControl(object):
    window = None
    tasks = None
    output = None
    edit = None

PETE = PeteControl()


class PeteStartCommand(sublime_plugin.TextCommand):

    def run(self, edit):
        global PETE

        PETE.edit = edit

        sublime.run_command("new_window")
        PETE.window = sublime.active_window()

        PETE.tasks = PETE.window.open_file("~/tasks.txt")

        PETE.window.set_layout({
            "cols": [0, 1],
            "rows": [0, 0.5, 1],
            "cells": [[0, 0, 1, 1], [0, 1, 1, 2]]
        })

        PETE.output = PETE.window.new_file()

        PETE.window.set_view_index(PETE.tasks, PETE.window.active_group(), 0)
        PETE.window.set_view_index(PETE.output, PETE.window.active_group()+1, 0)
        PETE.window.focus_view(PETE.tasks)

        self.showPanel()

    def addTask(self, s):
        global PETE
        PETE.tasks.run_command("pete_insert", {"task": s})
        self.showPanel()

    def showPanel(self):
        global PETE
        PETE.window.show_input_panel(
            "Add task:", "",
            self.addTask, None, self.showPanel)


class PeteInsertCommand(sublime_plugin.TextCommand):

    def run(self, edit, task=""):
        self.view.insert(edit, self.view.size(), task + "\n")


class PeteProcessTasks(sublime_plugin.EventListener):

    def on_post_save(self, view):
        global PETE

        if (view == PETE.tasks):
            region = sublime.Region(0, view.size())

            lines = view.lines(region)

            for l in lines:
                PETE.output.run_command(
                    "pete_insert",
                    {"task": view.substr(l)})
