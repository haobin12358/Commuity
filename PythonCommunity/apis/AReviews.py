# *- coding:utf8 *-
import sys
import os
sys.path.append(os.path.dirname(os.getcwd()))
from flask_restful import Resource
from config.requests import apis_wrong

class AReviews(Resource):
    def __init__(self):
        from control.CReviews import CReviews
        self.creviews = CReviews()

    def get(self, reviews):
        print "==================================================="
        print "api name is {0}".format(reviews)
        print "==================================================="

        apis = {
            "reviews_list": "self.creviews.reviews_list()"
        }

        if reviews not in apis:
            return apis_wrong
        return eval(apis[reviews])

    def post(self, reviews):
        print "==================================================="
        print "api name is {0}".format(reviews)
        print "==================================================="

        apis = {
            "add_review": "self.creviews.add_review()"
        }

        if reviews not in apis:
            return apis_wrong
        return eval(apis[reviews])