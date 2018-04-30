# *- coding:utf8 *-
import sys
import os
sys.path.append(os.path.dirname(os.getcwd()))
from flask_restful import Resource
from config.requests import apis_wrong

class ACards(Resource):
    def __init__(self):
        from control.CCards import CCards
        self.ccards = CCards()

    def get(self, cards):
        print "==================================================="
        print "api name is {0}".format(cards)
        print "==================================================="

        apis = {
            "card_list": "self.ccards.card_list()",
            "card_abo": "self.ccards.card_abo()"
        }

        if cards not in apis:
            return apis_wrong
        return eval(apis[cards])

    def post(self, cards):
        print "==================================================="
        print "api name is {0}".format(cards)
        print "==================================================="

        apis = {
            "new_card": "self.ccards.new_card()"
        }

        if cards not in apis:
            return apis_wrong
        return eval(apis[cards])