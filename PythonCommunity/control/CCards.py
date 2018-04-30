# *- coding:utf8 *-
import sys
import os
sys.path.append(os.path.dirname(os.getcwd()))
from flask import request
import json
from config.requests import param_miss, system_error

class CCards():
    def __init__(self):
        from service.SCards import SCards
        self.scards = SCards()
        from service.SUsers import SUsers
        self.susers = SUsers()

    def card_list(self):
        all_card = self.scards.get_all_card()
        data = []
        for row in all_card:
            data_item = {}
            data_item["Cid"] = row.Cid
            data_item["Uname"] = self.susers.get_uname_by_uid(row.Uid)
            data_item["Cname"] = row.Cname
            data_item["Cabo"] = row.Cabo
            data_item["Cstatus"] = row.Cstatus
            data_item["Ctime"] = row.Ctime
            data.append(data_item)

        from config.requests import response_ok
        response_ok["data"] = data
        return response_ok

    def card_abo(self):
        args = request.args.to_dict()
        print "======================args==========================="
        print args
        print "======================args==========================="

        if "Cid" not in args:
            return param_miss

        Cid = args["Cid"]

        card_abo_all = self.scards.get_card_abo_by_cid(Cid)
        data = {}
        data["Uname"] = self.susers.get_uname_by_uid(card_abo_all.Uid)
        data["Uid"] = card_abo_all.Uid
        data["Cname"] = card_abo_all.Cname
        data["Cabo"] = card_abo_all.Cabo
        data["Cstatus"] = card_abo_all.Cstatus
        data["Ctime"] = card_abo_all.Ctime

        from config.requests import response_ok
        response_ok["data"] = data
        return response_ok

    def new_card(self):
        args = request.args.to_dict()
        print "======================args==========================="
        print args
        print "======================args==========================="

        if "Uid" not in args:
            return param_miss

        Uid = args["Uid"]

        data = request.data
        print "======================data==========================="
        print data
        print "======================data==========================="
        data = json.loads(data)
        if "Cname" not in data or "Cabo" not in data:
            return param_miss

        Cname = data["Cname"]
        Cabo = data["Cabo"]
        add_card = self.scards.add_new_card(Uid, Cname, Cabo)

        if not add_card:
            return system_error
        from config.requests import response_ok
        return response_ok