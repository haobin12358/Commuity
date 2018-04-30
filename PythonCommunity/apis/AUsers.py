# *- coding:utf8 *-
import sys
import os
sys.path.append(os.path.dirname(os.getcwd()))
from flask_restful import Resource
from config.requests import apis_wrong

class AUsers(Resource):
    def __init__(self):
        from control.CUsers import CUsers
        self.cusers = CUsers()

    def get(self, users):
        print "==================================================="
        print "api name is {0}".format(users)
        print "==================================================="

        apis = {
            "user_info": "self.cusers.user_info()"
        }

        if users not in apis:
            return apis_wrong
        return eval(apis[users])

    def post(self, users):
        print "==================================================="
        print "api name is {0}".format(users)
        print "==================================================="

        apis = {
            "register": "self.cusers.register()",
            "login": "self.cusers.login()",
            "update_info": "self.cusers.update_info()"
        }

        if users not in apis:
            return apis_wrong
        return eval(apis[users])