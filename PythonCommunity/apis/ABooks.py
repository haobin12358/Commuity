# *- coding:utf8 *-
import sys
import os
sys.path.append(os.path.dirname(os.getcwd()))
from flask_restful import Resource
from config.requests import apis_wrong

class ABooks(Resource):
    def __init__(self):
        from control.CBooks import CBooks
        self.cbooks = CBooks()

    def get(self, books):
        print "==================================================="
        print "api name is {0}".format(books)
        print "==================================================="

        apis = {
            "book_list": "self.cbooks.book_list()",
            "book_abo": "self.cbooks.book_abo()",
            "my_book": "self.cbooks.my_book()"
        }

        if books not in apis:
            return apis_wrong
        return eval(apis[books])

    def post(self, books):
        print "==================================================="
        print "api name is {0}".format(books)
        print "==================================================="

        apis = {
            "update_status": "self.cbooks.update_status()"
        }

        if books not in apis:
            return apis_wrong
        return eval(apis[books])