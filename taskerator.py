import sublime
import sublime_plugin

import subprocess
import functools


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
        PETE.output.assign_syntax('Packages/Pete/Pete.tmLanguage')

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
            view.assign_syntax('Packages/Pete/Pete.tmLanguage')

    def addTask(self, s):
        global PETE
        PETE.tasks.run_command("append", {"characters": s + "\n"})
        PETE.tasks.run_command("save")
        self.showPanel()


class PeteProcessTasks(sublime_plugin.EventListener):

    def on_post_save_async(self, view):
        global PETE

        # TODO: switch this to using a settings attribute instead of object comparison
        if (view == PETE.tasks):
            taskRegion = sublime.Region(0, view.size())
            lines = view.lines(taskRegion)

            inputString = functools.reduce(lambda x,y: x + "\n" + view.substr(y), lines, "")

            try:
                sp = subprocess.Popen(["java", "-jar", sublime.packages_path() + "/Pete/Pete-assembly-0.1.jar"], 
                    stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
                output, err = sp.communicate(str.encode(inputString))
            except subprocess.CalledProcessError as e:
                output = inputString
                print("except")

            # TODO: find a better way to do delete
            outputRegion = sublime.Region(0, PETE.output.size())
            PETE.output.run_command("empty_view")

            PETE.output.run_command("append", {"characters": bytes.decode(output)})


# TODO: This feels so hacky. There has got to be a better way to call erase.
class EmptyViewCommand(sublime_plugin.TextCommand):

    def run(self, edit): 
        self.view.erase(edit, sublime.Region(0, self.view.size()))

