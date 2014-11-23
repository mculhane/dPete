import sublime
import sublime_plugin


class PeteControl(object):
    window = None
    tasks = None
    output = None

PETE = PeteControl()

class PeteStartCommand(sublime_plugin.ApplicationCommand):

    def run(self):
        global PETE

        self.create_layout()


    def create_layout(self):
        """
        Create and layout a new window
        """
        global PETE

        sublime.run_command("new_window")
        PETE.window = sublime.active_window()

        PETE.window.set_layout({
            "cols": [0, 1],
            "rows": [0, 0.5, 1],
            "cells": [[0, 0, 1, 1], [0, 1, 1, 2]]
        })

        self.setup_output_buffer()
        self.setup_tasks_buffer()

        PETE.window.focus_view(PETE.tasks)
        self.showPanel()

    def setup_output_buffer(self):
        """
        Set up the output buffer
        """
        global PETE

        PETE.output = PETE.window.new_file()
        PETE.output.set_name("Next Tasks")
        PETE.output.set_scratch(True)                   # Never report dirty for output window
        PETE.output.assign_syntax('Packages/Pete.tmLanguage')

        # For unclear reasons, the view index must be set before creating the next view. 
        # Otherwise, this view is not displayed properly initially.
        # There goes 3 hours of my life...
        PETE.window.set_view_index(PETE.output, 1, 0)

    def setup_tasks_buffer(self):
        """
        Set up the tasks file buffer
        """
        global PETE
        
        PETE.tasks = PETE.window.open_file("~/tasks.txt")
        self.setSyntax(3, PETE.tasks)                   # Wait for the view to load before apply settings
        PETE.window.set_view_index(PETE.tasks, 0, 0)

    def showPanel(self):
        global PETE
        PETE.window.show_input_panel(
            "Add task:", "",
            self.addTask, None, self.showPanel)

    def setSyntax(self, trys, view):
        if (view.is_loading()):
            sublime.set_timeout(lambda: self.setSyntax(trys-1, view), 500)
        else:
            view.assign_syntax('Packages/Pete.tmLanguage')

    def addTask(self, s):
        global PETE
        PETE.tasks.run_command("append", {"characters": s + "\n"})
        PETE.tasks.run_command("save")
        self.showPanel()


class PeteProcessTasks(sublime_plugin.EventListener):

    def on_post_save_async(self, view):
        global PETE

        if (view == PETE.tasks):
            region = sublime.Region(0, view.size())

            lines = view.lines(region)

            for l in lines:
                PETE.output.run_command("append", {"characters": view.substr(l) + "\n"})
