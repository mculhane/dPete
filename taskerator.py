import sublime
import sublime_plugin

import subprocess
import functools
import datetime
import uuid
import os
import sys

# add the parsedatetime plugin folder to our path
sys.path.append(os.path.join(os.path.dirname(__file__), "parsedatetime-1.4"))

import parsedatetime as pdt


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
        # self.showPanel()

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

    def setSyntax(self, trys, view):
        if (view.is_loading()):
            sublime.set_timeout(lambda: self.setSyntax(trys-1, view), 500)
        else:
            view.assign_syntax('Packages/Pete/Pete.tmLanguage')


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
                output = str.encode(inputString)
                print("except")

            # TODO: except case is not handled properly yet. Falls through with empty string
            if (output == b''): 
                output = str.encode(inputString)

            # TODO: find a better way to do delete
            outputRegion = sublime.Region(0, PETE.output.size())
            PETE.output.run_command("empty_view")

            PETE.output.run_command("append", {"characters": bytes.decode(output)})

            view.run_command("replace_dates")


# TODO: This feels so hacky. There has got to be a better way to call erase.
class EmptyViewCommand(sublime_plugin.TextCommand):

    def run(self, edit): 
        self.view.erase(edit, sublime.Region(0, self.view.size()))

class AddTaskCommand(sublime_plugin.TextCommand):
    def run(self, edit):
        uid = "%06x" % (uuid.uuid4().int & 0xffffff)
        contents = "#" + uid + " ${1:task name} @ ${2:start time} - ${3:end time} % ${4:repeat} \n$0"

        self.gotoEnd(edit)
        if (self.view.substr(self.view.size()-1) != '\n'):
            self.view.insert(edit, self.view.size(), "\n")
        self.view.run_command("insert_snippet", {"contents": contents})

    def gotoEnd(self, edit):
        pos = self.view.size()

        self.view.sel().clear()
        self.view.sel().add(sublime.Region(pos))

        self.view.show(pos)


class ReplaceDatesCommand(sublime_plugin.TextCommand):

    Calendar = pdt.Calendar()

    def run(self, edit):
        startDates = self.view.find_by_selector("entity.name.class.startdate.pete")
        endDates = self.view.find_by_selector("entity.name.class.enddate.pete")

        combined = startDates + endDates
        combined.sort(key=lambda x: x.a, reverse=True)

        # reverse iteration direction to prevent changes from messing up
        # region indexes later in the loop.
        for region in combined:
            s = self.view.substr(region)
            isoDate = self.replaceDate(s)
            self.view.replace(edit, region, isoDate)

    def replaceDate(self, s):
        try:
            isoDate = self.datetimeFromString(s).isoformat()
        except ValueError as e:
            sublime.error_message(e.__str__())
            return s

        return isoDate

    def datetimeFromString(self, s):
        
        result, what = self.Calendar.parse( s )

        dt = None

        # what was returned (see http://code-bear.com/code/parsedatetime/docs/)
        # 0 = failed to parse
        # 1 = date (with current time, as a struct_time)
        # 2 = time (with current date, as a struct_time)
        # 3 = datetime
        if what in (1,2):
            # result is struct_time
            dt = datetime.datetime( *result[:6] )
        elif what == 3:
            # result is a datetime
            dt = result

        if dt is None:
            # Failed to parse
            raise ValueError("Datetime '"+s+"' cannot be parsed.")

        return dt
