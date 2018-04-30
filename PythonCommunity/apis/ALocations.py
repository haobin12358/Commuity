# *- coding:utf8 *-
import sys
import os
sys.path.append(os.path.dirname(os.getcwd()))
from flask_restful import Resource
from config.requests import apis_wrong

class ALocations(Resource):
    def __init__(self):
        from control.CLocations import CLocations
        self.clocations = CLocations()

    def get(self, locations):
        print "==================================================="
        print "api name is {0}".format(locations)
        print "==================================================="

        apis = {
            "location_list": "self.clocations.location_list()",
            "my_location": "self.clocations.my_location()"
        }

        if locations not in apis:
            return apis_wrong
        return eval(apis[locations])

    def post(self, locations):
        print "==================================================="
        print "api name is {0}".format(locations)
        print "==================================================="

        apis = {
            "update_lstatus": "self.clocations.update_lstatus()"
        }

        if locations not in apis:
            return apis_wrong
        return eval(apis[locations])