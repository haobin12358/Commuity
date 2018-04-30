# *- coding:utf8 *-
import sys
import os
sys.path.append(os.path.dirname(os.getcwd()))
from flask_restful import Resource
from config.requests import apis_wrong

class AWaters(Resource):
    def __init__(self):
        from control.CWaters import CWaters
        self.cwaters = CWaters()

    def get(self, waters):
        print "==================================================="
        print "api name is {0}".format(waters)
        print "==================================================="

        apis = {
            "my_water": "self.cwaters.my_water()"
        }

        if waters not in apis:
            return apis_wrong
        return eval(apis[waters])

    def post(self, waters):
        print "==================================================="
        print "api name is {0}".format(waters)
        print "==================================================="

        apis = {
            "update_wstatus": "self.cwaters.update_wstatus()"
        }

        if waters not in apis:
            return apis_wrong
        return eval(apis[waters])